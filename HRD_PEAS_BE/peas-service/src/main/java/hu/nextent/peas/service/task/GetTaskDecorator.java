package hu.nextent.peas.service.task;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import hu.nextent.peas.jpa.entity.Evaluation;
import hu.nextent.peas.jpa.entity.EvaluationStatusEnum;
import hu.nextent.peas.model.TaskModel;
import lombok.val;

@Service
@Transactional
public class GetTaskDecorator 
extends AbstarctTaskDecorator 
{

    public ResponseEntity<TaskModel> getTask(Long taskId) {
    	validateGetTask(taskId);
		ResponseEntity<TaskModel> taskModelResponseEntity = getTaskModel(taskId);
		val task = taskRepository.getOne(taskId);
		if (task.getOwner().equals(getCurrentUser())) {
			taskModelResponseEntity.getBody().getEvaluators().stream().forEach(taskEvaluationModel -> taskEvaluationModel.setScore(null));
		}
		List<Evaluation> myEvaluationList = evaluationRepository.findAllByEvaluatorAndTaskAndStatus(getCurrentUser(), task, EvaluationStatusEnum.EVALUATING);
		if (myEvaluationList != null && myEvaluationList.size() > 0) {
			taskModelResponseEntity.getBody().setEvaluationId(myEvaluationList.get(0).getId());
		}
    	return taskModelResponseEntity;
    }
    
    private void validateGetTask(Long taskId) {
    	checkExists(taskId);
    	val task = taskRepository.getOne(taskId);
    	checkActive(task);
    	checkCompany(task);
    	checkCurrentUserView(task);
	} 

}
