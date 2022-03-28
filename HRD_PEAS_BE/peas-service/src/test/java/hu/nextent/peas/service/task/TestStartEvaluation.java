package hu.nextent.peas.service.task;

import java.util.List;

import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;
import org.springframework.transaction.annotation.Transactional;

import hu.nextent.peas.TestConstant;
import hu.nextent.peas.exceptions.BaseResponseException;
import hu.nextent.peas.jpa.entity.Evaluation;
import hu.nextent.peas.jpa.entity.EvaluationStatusEnum;
import hu.nextent.peas.jpa.entity.Notification;
import hu.nextent.peas.jpa.entity.NotificationStatusEnum;
import hu.nextent.peas.jpa.entity.NotificationTypeEnum;
import hu.nextent.peas.jpa.entity.ReferenceTypeEnum;
import hu.nextent.peas.jpa.entity.Task;
import hu.nextent.peas.jpa.entity.TaskStatusEnum;
import hu.nextent.peas.jpa.entity.ToDo;
import hu.nextent.peas.jpa.entity.ToDoStatusEnum;
import hu.nextent.peas.jpa.entity.ToDoTypeEnum;
import hu.nextent.peas.model.TaskEvaluationModel;
import hu.nextent.peas.model.TaskModel;
import hu.nextent.peas.model.UserSimpleModel;
import hu.nextent.peas.service.TestServiceConfig;

@ActiveProfiles("test")
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { TestServiceConfig.class }, loader = AnnotationConfigContextLoader.class)
@Transactional
@DirtiesContext
public class TestStartEvaluation extends TestTaskBase {


	@Test
	@Rollback
	public void testStartEvaluation() {
		givenUser(TestConstant.USER_NORMAL);
		givenFirstTaskByParameterization();
		mockAuthService();

		taskService.startEvaluation(getSelectedTask().getId());
		
		//Assert
		setSelectedTask(taskRepository.getOne(getSelectedTask().getId()));
		Assert.assertEquals(TaskStatusEnum.UNDER_EVALUATION, getSelectedTask().getStatus());
		Assert.assertNotNull(getSelectedTask().getStartDate());
		Assert.assertEquals(getSelectedTask().getEvaluations().size(), getSelectedTask().getEvaluaterCount().intValue());
		assertEvaluations(getSelectedTask());
		assertToDos(getSelectedTask());
		assertNotification(getSelectedTask());
		
	}
	
	// Értékelés meglét viszgálat
	private void assertEvaluations(Task task) {
		int cnt = 0;
		for(Evaluation evaluation: task.getEvaluations()) {
			cnt ++;
			Assert.assertEquals(EvaluationStatusEnum.EVALUATING, evaluation.getStatus());
			Assert.assertNotNull(evaluation.getDeadline());
		}
		Assert.assertEquals(cnt, task.getEvaluations().size());
	}

	// Todo meglét viszgálat
	private void assertToDos(Task task) {
		int cnt = 0;
		for(Evaluation evaluation: task.getEvaluations()) {
			cnt++;
			List<ToDo> todos = toDoRepository.findAllByToUserAndEvaluationAndToDoType(evaluation.getEvaluator(), evaluation, ToDoTypeEnum.EVALUATION);
			Assert.assertTrue("ToDo Not Exists, Evaluation: " + evaluation.getId(), !todos.isEmpty());
			ToDo todo = todos.get(0);
			Assert.assertEquals(ToDoStatusEnum.OPEN, todo.getStatus());
			Assert.assertEquals(ReferenceTypeEnum.EVALUATION, todo.getReferenceType());
			Assert.assertEquals(evaluation, todo.getEvaluation());
			Assert.assertEquals(task, todo.getTask());
			Assert.assertNull(todo.getDone());
			Assert.assertEquals(evaluation.getDeadline(), todo.getDeadline());
			Assert.assertEquals(evaluation.getEvaluator(), todo.getToUser());
			Assert.assertEquals(evaluation.getCompany(), todo.getCompany());
		}
		Assert.assertTrue(cnt > 0);
	}
	
	// Notifications meglét viszgálat
	private void assertNotification(Task task) {
		int cnt = 0;
		for(Evaluation evaluation: task.getEvaluations()) {
			cnt++;
			List<Notification> notifications = notificationRepository.findAllByToUserAndEvaluationAndNotificationType(evaluation.getEvaluator(), evaluation, NotificationTypeEnum.EVALUATION_START);
			Assert.assertTrue(!notifications.isEmpty());
			Assert.assertEquals(1, notifications.size());
			Notification notification = notifications.get(0);
			Assert.assertEquals(NotificationStatusEnum.OPEN, notification.getStatus());
			Assert.assertEquals(evaluation.getEvaluator(), notification.getToUser());
			Assert.assertEquals(evaluation, notification.getEvaluation());
			Assert.assertEquals(task, notification.getTask());
			Assert.assertEquals(evaluation.getCompany(), notification.getCompany());
		}
		Assert.assertTrue(cnt > 0);
	}
	
	
	@Rule
	public ExpectedException thrown = ExpectedException.none();
	
	@Test
	@Rollback
	public void testStartEvaluationWhenEvalIsStarted() {
		givenUser(TestConstant.USER_NORMAL);
		givenFirstTaskByParameterization();
		mockAuthService();
		// Az adott taskot megpróbálom újra értékelés alá tenni
		taskService.startEvaluation(getSelectedTask().getId());
		
		thrown.expect(BaseResponseException.class);
		taskService.startEvaluation(getSelectedTask().getId());
		
	}

	
	@Test
	@Rollback
	public void testStartEvaluation_PEAS_119() {
		// https://jira.nextent.hu/browse/PEAS-119
		// Olyan task esetén, amelynél a task tulajdonos nincs megadva és indítják az értékelést, ott a saját felhasználóhoz nem jön létre todo
		
		givenUser(TestConstant.USER_NORMAL);
		givenFirstTaskByParameterization();
		mockAuthService();

		ResponseEntity<TaskModel> respCopyTaskModel = taskService.copyTask(getSelectedTask().getId());
		TaskModel copyTaskModel = respCopyTaskModel.getBody();
		// Hozzáadom a task tulajdonos főnökét
		copyTaskModel.getEvaluators().add(
				new TaskEvaluationModel()
					.evaluator(
							new UserSimpleModel().id(getSelectedUser().getLeader().getId())
							)
					);
		Long copyTaskId = copyTaskModel.getId();
		taskService.modifyTask(copyTaskModel, copyTaskModel.getId());
		
		// WHEN
		taskService.startEvaluation(copyTaskId);
		
		//Assert
		Task copyTask = taskRepository.getOne(copyTaskId);
		Assert.assertEquals(TaskStatusEnum.UNDER_EVALUATION, copyTask.getStatus());
		Assert.assertNotNull(copyTask.getStartDate());
		Assert.assertEquals(getSelectedTask().getEvaluations().size(), copyTask.getEvaluations().size());
		assertEvaluations(copyTask);
		assertToDos(copyTask);
		assertNotification(copyTask);
		
	}
}
