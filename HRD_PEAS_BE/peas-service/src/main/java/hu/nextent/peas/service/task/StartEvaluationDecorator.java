package hu.nextent.peas.service.task;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.Collection;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import hu.nextent.peas.constant.TodoLabelConstant;
import hu.nextent.peas.exceptions.ExceptionList;
import hu.nextent.peas.jpa.entity.Evaluation;
import hu.nextent.peas.jpa.entity.EvaluationStatusEnum;
import hu.nextent.peas.jpa.entity.Notification;
import hu.nextent.peas.jpa.entity.NotificationStatusEnum;
import hu.nextent.peas.jpa.entity.NotificationTypeEnum;
import hu.nextent.peas.jpa.entity.ReferenceTypeEnum;
import hu.nextent.peas.jpa.entity.Task;
import hu.nextent.peas.jpa.entity.TaskStatusEnum;
import hu.nextent.peas.jpa.entity.TaskTypeEnum;
import hu.nextent.peas.jpa.entity.TaskXFactor;
import hu.nextent.peas.jpa.entity.ToDo;
import hu.nextent.peas.jpa.entity.ToDoStatusEnum;
import hu.nextent.peas.jpa.entity.ToDoTypeEnum;
import lombok.val;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@Transactional
public class StartEvaluationDecorator 
extends AbstarctTaskDecorator 
{

	public ResponseEntity<Void> startEvaluation(Long taskId) {
		log.debug("startEvaluation, taskId: {}", taskId);
    	checkExists(taskId);
    	val task = taskRepository.getOne(taskId);
    	validateBaseData(task);
    	validateBeforeStartEvaulation(task);
    	addTaskOwnerUserIfNotHave(task);
    	setValuesAndChangeStatus(task);
    	changeEvalautionStatusAndAddDeadline(task);
    	// ToDo-k létrehozása az értékelésekhez
    	makeToDoAndNotification(task); 
    	// Lezárom a TASK_DEADLINE notifikációkat
    	closeTaskNotifications(task);
    	return ResponseEntity.ok().build();
    }
    

	private void validateBaseData(Task task) {
		log.debug("validateBaseData, taskId: {}", task.getId());
    	checkActive(task);
    	checkCompany(task);
    	checkStatus(task, TaskStatusEnum.PARAMETERIZATION);
    	checkType(task, TaskTypeEnum.NORMAL);
    	checkCurrentUserEdit(task);
	} 
    
    private void validateBeforeStartEvaulation(Task task) {
		log.debug("validateBeforeStartEvaulation, taskId: {}", task.getId());
		if (TaskTypeEnum.TEMPLATE.equals(task.getTaskType())) {
			log.debug("validateBeforeStartEvaulation, taskId: {}, exit, because TEMPLATE", task.getId());
			return;
		}
		
		val factorRange = getFactorRange();
		val minReq = getFactorRequiredMin();
		val companyVirtueRange = getCompanyVirtueRange();
		val leaderVirtueRange = getLeaderVirtueRange();
		val userRange = getUserRange();
		
		//databaseInfoRepository.refresh(task);
		
		int factorCount = task.getTaskXFactors().size();
		if (!factorRange.contains(factorCount)) {
			throw ExceptionList.task_factor_limit_violation(task.getId(), factorCount, factorRange.getMinimum(), factorRange.getMaximum());
		}
	
		int requiredCount = Long.valueOf(task.getTaskXFactors().stream().filter(TaskXFactor::getRequired).count()).intValue();
		if (requiredCount < minReq) {
			throw ExceptionList.task_factor_required_limit_violation(task.getId(), requiredCount, minReq);
		}
		
		int companyVirtueCount = task.getTaskXCompanyVirtues().size();
		if (!companyVirtueRange.contains(companyVirtueCount)) {
			throw ExceptionList.task_company_virtue_limit_violation(task.getId(), companyVirtueCount, companyVirtueRange.getMinimum(), companyVirtueRange.getMaximum());
		}
		int leaderVirtueCount = task.getTaskXLeaderVirtues().size();
		if (!leaderVirtueRange.contains(leaderVirtueCount)) {
			throw ExceptionList.task_leader_virtue_limit_violation(task.getId(), leaderVirtueCount, leaderVirtueRange.getMinimum(), leaderVirtueRange.getMaximum());
		}
		
		val cntBeforeUserCnt = task.getEvaluations().size();
		Long userCount = task.getEvaluations()
				.stream()
				.filter(p -> !p.getEvaluator().getId().equals(task.getOwner().getId())) // Minusz saját felhazsnáló ha van
				.count();
		
		userCount ++; // Plusz saját felhasználó
		log.debug(
				"validateBeforeStartEvaulation, taskId: {}, evaulator Cnt: {}, fixed evaulator Cnt: {}, userRange: {}"
				, task.getId(), cntBeforeUserCnt, userCount, userRange
				);
		
		if (!userRange.contains(userCount.intValue())) {
	    	throw ExceptionList.task_evaluator_limit_violation(task.getId(), userCount.intValue(), userRange.getMinimum(), userRange.getMaximum());
		}
		
	}


	/**
     * Ha nincs Kurrens felhazsnáló hozzá, akkor hozzáadja
     * @param task
     */
    private void addTaskOwnerUserIfNotHave(Task task) {
    	boolean existsCurrentUser = 
    			task.getEvaluations().stream().anyMatch(p -> p.getEvaluator().getId().equals(task.getOwner().getId()));
    	if (!existsCurrentUser) {
    		log.debug("addTaskOwnerUserIfNotHave. taskId: {}, add task owner evaulator", task.getId());
    		val evaluation =
					Evaluation.builder()
						.task(task)
						.company(task.getCompany())
						.status(EvaluationStatusEnum.BEFORE_EVALUATING)
						.evaluator(task.getOwner())
						.build();
			evaluationRepository.save(evaluation);
			task.getEvaluations().add(evaluation);
			taskRepository.save(task);
			refreshEvaluaterCount(task);
    	}
    }
    
    private OffsetDateTime getEvaluationDeadline() {
    	OffsetDateTime deadline = OffsetDateTime.now(ZoneOffset.UTC);
    	deadline = deadline.plusDays(getEvaluationExiredDay());
    	return deadline;
    }
    
    /**
     * Értékelések státusz váltás és deadline számolás
     * @param task
     */
    private void changeEvalautionStatusAndAddDeadline(Task task) {
    	log.debug("changeEvalautionStatusAndAddDeadline. taskId: {}", task.getId());
    	OffsetDateTime deadline = getEvaluationDeadline();
    	for(Evaluation evaluation: task.getEvaluations()) {
        	evaluation.setStatus(EvaluationStatusEnum.EVALUATING);
        	evaluation.setDeadline(deadline);
        	evaluation.setEvaluatedStartDate(task.getStartDate());
        	evaluationRepository.save(evaluation);
    	}
    }
    
    /**
     * Alap értékek beállítása
     * Plusz státusz váltás PARAMETERIZATION -> UNDER_EVALUATION-ra
     * @param task
     */
    private void setValuesAndChangeStatus(Task task) {
    	log.debug("setValuesAndChangeStatus. taskId: {}", task.getId());
    	int userCount = task.getEvaluations().size();
    	task.setStatus(TaskStatusEnum.UNDER_EVALUATION);
    	task.setEvaluaterCount(userCount);
    	task.setStartDate(OffsetDateTime.now(ZoneOffset.UTC));
    	task.setEndDate(getEvaluationDeadline());
    	taskRepository.save(task);
    }
    
    
    /**
     * Értékelésekhez rendelt Teendők létrehozása
     * @param task
     */
    private void makeToDoAndNotification(Task task) {
    	log.debug("makeToDoAndNotification. taskId: {}", task.getId());
    	for(Evaluation evaluation: task.getEvaluations()) {
    		createTodoToToEvaluation(evaluation);
    	}
    	toDoRepository.flush();
		factoryServiceNotification.createEvaluationStart(task);
    }
    
    private ToDo createTodoToToEvaluation(Evaluation evaluation) {
		val todo = 
				ToDo.builder()
					.toDoType(ToDoTypeEnum.EVALUATION)
					.status(ToDoStatusEnum.OPEN)
					.messageCode(TodoLabelConstant.TODO_SUMMON_EVALUATOR)
					.deadline(evaluation.getDeadline())
					.done(null)
					.toUser(evaluation.getEvaluator())
					.referenceType(ReferenceTypeEnum.EVALUATION)
					.evaluation(evaluation)
					.task(evaluation.getTask())
					.company(evaluation.getCompany())
					.build();
		toDoRepository.save(todo);
		return todo;
	}

    
    private void closeTaskNotifications(Task task) {
		Collection<Notification> notifications = 
				notificationRepository.findAllByTaskAndNotificationType(
						task, 
						NotificationTypeEnum.TASK_DEADLINE
				);
		if (!notifications.isEmpty()) {
			notifications
				.stream()
				.forEach(n -> n.setStatus(NotificationStatusEnum.EXPIRED));
			notificationRepository.saveAll(notifications);

		}

		
	}
	
    
}
