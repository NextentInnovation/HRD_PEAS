package hu.nextent.peas.service.task;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import hu.nextent.peas.jpa.dao.DatabaseInfoRepository;
import hu.nextent.peas.model.TaskCreateModel;
import hu.nextent.peas.model.TaskItemPageModel;
import hu.nextent.peas.model.TaskModel;
import hu.nextent.peas.model.TaskQueryParameterModel;
import hu.nextent.peas.service.TaskService;
import lombok.val;

@Service
@Transactional
public class TaskServiceImp
implements TaskService
{
	@Autowired private CopyTaskDecorator copyTaskDecorator;
	@Autowired private DeleteTaskDecorator deleteTaskDecorator;
	@Autowired private GetTaskDecorator getTaskDecorator;
	@Autowired private TaskQueryDecorator taskQueryDecorator;
	@Autowired private StartEvaluationDecorator startEvaluationDecorator;
	@Autowired private CreateAndModifyTaskDecorator createAndModifyTaskDecorator;
	@Autowired private DatabaseInfoRepository databaseInfoRepository;

	@Override
	public ResponseEntity<TaskModel> copyTask(Long taskId) {
		val ret = copyTaskDecorator.copyTask(taskId);
		databaseInfoRepository.flush();
		return ret;
	}

	@Override
	public ResponseEntity<TaskModel> createTask(TaskCreateModel body) {
		val ret = createAndModifyTaskDecorator.createTask(body);
		databaseInfoRepository.flush();
		return ret;
	}

	@Override
	public ResponseEntity<Void> deleteTask(Long taskId) {
		val ret = deleteTaskDecorator.deleteTask(taskId);
		databaseInfoRepository.flush();
		return ret;
	}

	@Override
	public ResponseEntity<TaskModel> getTask(Long taskId) {
		val ret = getTaskDecorator.getTask(taskId);
		databaseInfoRepository.flush();
		return ret;
	}

	@Override
	public ResponseEntity<TaskModel> modifyTask(TaskModel body, Long taskId) {
		val ret = createAndModifyTaskDecorator.modifyEditableTask(body, taskId);
		databaseInfoRepository.flush();
		return ret;
	}

	@Override
	public ResponseEntity<TaskItemPageModel> queryTask(TaskQueryParameterModel body) {
		val ret = taskQueryDecorator.queryTask(body);
		databaseInfoRepository.flush();
		return ret;
	}

	@Override
	public ResponseEntity<Void> startEvaluation(Long taskId) {
		val ret = startEvaluationDecorator.startEvaluation(taskId);
		databaseInfoRepository.flush();
		return ret;
	}

}
