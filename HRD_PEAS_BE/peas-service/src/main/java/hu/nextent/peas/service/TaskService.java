package hu.nextent.peas.service;

import org.springframework.http.ResponseEntity;

import hu.nextent.peas.model.TaskCreateModel;
import hu.nextent.peas.model.TaskItemPageModel;
import hu.nextent.peas.model.TaskModel;
import hu.nextent.peas.model.TaskQueryParameterModel;

public interface TaskService {

	public ResponseEntity<TaskModel> copyTask(Long taskId);
	
	public ResponseEntity<TaskModel> createTask(TaskCreateModel body);
	
	public ResponseEntity<Void> deleteTask(Long taskId);
	
	public ResponseEntity<TaskModel> getTask(Long taskId);
	
	public ResponseEntity<TaskModel> modifyTask(TaskModel body, Long taskId);
	
	public ResponseEntity<TaskItemPageModel> queryTask(TaskQueryParameterModel body);
	
	public ResponseEntity<Void> startEvaluation(Long taskId);

}
