package hu.nextent.peas.service.task;

import java.util.stream.Collectors;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import hu.nextent.peas.jpa.entity.Evaluation;
import hu.nextent.peas.jpa.entity.EvaluationStatusEnum;
import hu.nextent.peas.jpa.entity.Task;
import hu.nextent.peas.jpa.entity.TaskStatusEnum;
import hu.nextent.peas.jpa.entity.TaskTypeEnum;
import hu.nextent.peas.jpa.entity.TaskXCompanyVirtue;
import hu.nextent.peas.jpa.entity.TaskXFactor;
import hu.nextent.peas.jpa.entity.TaskXLeaderVirtue;
import hu.nextent.peas.model.TaskModel;
import lombok.val;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@Transactional
public class CopyTaskDecorator 
extends AbstarctTaskDecorator {

	public ResponseEntity<TaskModel> copyTask(Long taskId) {
		log.debug("copyTask, taskId: {}", taskId);
		checkExists(taskId);
		val sourceTask = taskRepository.getOne(taskId);
		validateCopyTask(sourceTask);
		val targetTask = copy(sourceTask);
		return getTaskModel(targetTask);
    }
	
	private void validateCopyTask(Task task) {
		log.debug("validateCopyTask, taskId: {}", task.getId());
		// checkActive(task);
		checkCompany(task);
		checkType(task, TaskTypeEnum.NORMAL, TaskTypeEnum.TEMPLATE);
		checkCurrentUserView(task);
	}
	
	private Task copy(Task sourceTask) {
		log.debug("copy, taskId: {}", sourceTask.getId());
		Task targetTask = 
				Task.builder()
					.status(TaskStatusEnum.PARAMETERIZATION)
					.taskType(TaskTypeEnum.NORMAL)
					.name(sourceTask.getName())
					.description(sourceTask.getDescription())
					.owner(getCurrentUser())
					.difficulty(sourceTask.getDifficulty())
					.company(getCurrentCompany())
					.build()
					;
		
		log.debug("create new task from taskId: {}, name: {}", sourceTask.getId(), sourceTask.getName());

		taskRepository.save(targetTask);
		// Deadline másolás nem történik itt meg

		// Copy CompanyVirtues
		targetTask.setTaskXCompanyVirtues(
			sourceTask.getTaskXCompanyVirtues()
				.stream()
				.map(TaskXCompanyVirtue::getCompanyVirtue)
				.map(companyVirtue -> new TaskXCompanyVirtue(targetTask, companyVirtue))
				.peek(x -> taskXCompanyVirtueRepository.save(x))
				.collect(Collectors.toList())
				);
		
		log.debug("copy company virtues, old taskId: {}, cnt: {}", sourceTask.getId(), targetTask.getTaskXCompanyVirtues().size());

		
		// Copy LeaderVirtues, Only Leaders
		val leader = getCurrentLeader();
		if (leader != null) {
			targetTask.setTaskXLeaderVirtues(
				sourceTask.getTaskXLeaderVirtues()
					.stream()
					.map(TaskXLeaderVirtue::getLeaderVirtue)
					.filter(p -> p.getOwner().equals(leader))
					.map(leaderVirtue -> new TaskXLeaderVirtue(targetTask, leaderVirtue))
					.peek(x -> taskXLeaderVirtueRepository.save(x))
					.collect(Collectors.toList())
					);
			log.debug("copy leader virtues, old taskId: {}, cnt: {}", sourceTask.getId(), targetTask.getTaskXLeaderVirtues().size());
		}


		// Factor
		targetTask.setTaskXFactors(
			sourceTask.getTaskXFactors().stream()
				.map(p -> new TaskXFactor(targetTask, p.getFactor(), p.getRequired()))
				.peek(x -> taskXFactorRepository.save(x))
				.collect(Collectors.toList())
			);
		
		log.debug("copy factors, old taskId: {}, cnt: {}", sourceTask.getId(), targetTask.getTaskXFactors().size());
		
		// Evaulators
		if (sourceTask.getOwner().getId().equals(getCurrentUser().getId())) {
			// Saját Task, ha igen, akkor másolom az értékelőket
			int cnt = 0;
			for(Evaluation sourceEvaluation: sourceTask.getEvaluations()) {
				if (sourceEvaluation.getEvaluator().getActive()) {
					Evaluation targetEvaluation = Evaluation.builder()
							.status(EvaluationStatusEnum.BEFORE_EVALUATING)
							.company(getCurrentCompany())
							.evaluator(sourceEvaluation.getEvaluator())
							.task(targetTask)
							.build();
					evaluationRepository.save(targetEvaluation);
					targetTask.getEvaluations().add(targetEvaluation);
					cnt++;
				}
			}
			log.debug("copy evaluation, old taskId: {}, cnt: {}", sourceTask.getId(), cnt);
		}
		
		taskRepository.save(targetTask);
		taskRepository.flush();
		return targetTask;
	}
}
