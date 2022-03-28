package hu.nextent.peas.service.task;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import hu.nextent.peas.jpa.entity.Evaluation;
import hu.nextent.peas.jpa.entity.EvaluationStatusEnum;
import hu.nextent.peas.jpa.entity.Notification;
import hu.nextent.peas.jpa.entity.NotificationStatusEnum;
import hu.nextent.peas.jpa.entity.NotificationTypeEnum;
import hu.nextent.peas.jpa.entity.Task;
import hu.nextent.peas.jpa.entity.TaskStatusEnum;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@Transactional
public class DeleteTaskDecorator extends AbstarctTaskDecorator {

	public ResponseEntity<Void> deleteTask(Long taskId) {
    	log.debug(String.format("deleteTask: taskId %s", taskId));
    	checkExists(taskId);
    	Task task = taskRepository.getOne(taskId);
    	validateDeleteTask(task);
    	task.setStatus(TaskStatusEnum.DELETED);
    	taskRepository.save(task);
    	deleteEvaluation(task);
    	deleteToDo(task);
    	deleteNotification(task);
    	// TODO Todo / Notification / Evaulation Delete
    	log.debug(String.format("taskId %s deleted", taskId));
    	return ResponseEntity.ok().build();
    }

	private void validateDeleteTask(Task task) {
    	checkActive(task);
    	checkStatus(task, TaskStatusEnum.PARAMETERIZATION);
    	checkCurrentUserEdit(task);
	}

    private void deleteEvaluation(Task task) {
    	for(Evaluation eval: task.getEvaluations()) {
    		eval.setStatus(EvaluationStatusEnum.DELETED);
    		evaluationRepository.save(eval);
    	}
		
	}

	private void deleteToDo(Task task) {
		// Itt nem kell még semmit se csinálni
	}

	private void deleteNotification(Task task) {
		List<Notification> notifications = notificationRepository.findAllByTaskAndNotificationType(task, NotificationTypeEnum.TASK_DEADLINE);
		for(Notification notif : notifications) {
			notif.setStatus(NotificationStatusEnum.CLOSE);
		}
		notificationRepository.saveAll(notifications);
	}

}
