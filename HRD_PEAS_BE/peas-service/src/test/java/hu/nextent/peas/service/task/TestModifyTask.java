package hu.nextent.peas.service.task;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;
import org.springframework.transaction.annotation.Transactional;

import hu.nextent.peas.TestConstant;
import hu.nextent.peas.jpa.entity.CompanyVirtue;
import hu.nextent.peas.jpa.entity.Evaluation;
import hu.nextent.peas.jpa.entity.LeaderVirtue;
import hu.nextent.peas.jpa.entity.Task;
import hu.nextent.peas.jpa.entity.User;
import hu.nextent.peas.model.CompanyVirtueModel;
import hu.nextent.peas.model.LeaderVirtueModel;
import hu.nextent.peas.model.TaskEvaluationModel;
import hu.nextent.peas.model.TaskModel;
import hu.nextent.peas.model.UserSimpleModel;
import hu.nextent.peas.service.TestServiceConfig;

@ActiveProfiles("test")
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { TestServiceConfig.class }, loader = AnnotationConfigContextLoader.class)
@Transactional
@DirtiesContext
public class TestModifyTask extends TestTaskBase {

	
	private static final String NEW_NAME = "changed name";
	private static final String NEW_DESCRIPTION = "changed description";
	
	@Test
	@Rollback
	public void testChangeName() {
		givenUser(TestConstant.USER_NORMAL);
		givenFirstTaskByParameterization();
		mockAuthService();
		ResponseEntity<TaskModel> respTaskModel = taskService.getTask(getSelectedTask().getId());
		TaskModel taskModel = respTaskModel.getBody();
		
		taskModel.setName(NEW_NAME);
		ResponseEntity<TaskModel> respChangedTaskModel = taskService.modifyTask(taskModel, getSelectedTask().getId());
		
		Assert.assertNotNull(respChangedTaskModel);
		Assert.assertEquals(respChangedTaskModel.getStatusCode(), HttpStatus.OK);
		Assert.assertNotNull(respChangedTaskModel.getBody());
		Assert.assertEquals(getSelectedTask().getId(), respChangedTaskModel.getBody().getId());
		
		// Check TaskModel
		TaskModel changedTaskModel = respChangedTaskModel.getBody();
		Assert.assertEquals(NEW_NAME, changedTaskModel.getName());
		// Check Task
		Task changedTask = taskRepository.getOne(getSelectedTask().getId());
		Assert.assertEquals(NEW_NAME, changedTask.getName());
	}
	
	@Test
	@Rollback
	public void testChangeDescription() {
		givenUser(TestConstant.USER_NORMAL);
		givenFirstTaskByParameterization();
		mockAuthService();
		ResponseEntity<TaskModel> respTaskModel = taskService.getTask(getSelectedTask().getId());
		TaskModel taskModel = respTaskModel.getBody();
		
		taskModel.setDescription(NEW_DESCRIPTION);
		ResponseEntity<TaskModel> respChangedTaskModel = taskService.modifyTask(taskModel, getSelectedTask().getId());
		
		Assert.assertNotNull(respChangedTaskModel);
		Assert.assertEquals(respChangedTaskModel.getStatusCode(), HttpStatus.OK);
		Assert.assertNotNull(respChangedTaskModel.getBody());
		Assert.assertEquals(getSelectedTask().getId(), respChangedTaskModel.getBody().getId());
		
		// Check TaskModel
		TaskModel changedTaskModel = respChangedTaskModel.getBody();
		Assert.assertEquals(NEW_DESCRIPTION, changedTaskModel.getDescription());
		// Check Task
		Task changedTask = taskRepository.getOne(getSelectedTask().getId());
		Assert.assertEquals(NEW_DESCRIPTION, changedTask.getDescription());
	}
	
	
	@Test
	@Rollback
	public void testChangeDeadline() {
		givenUser(TestConstant.USER_NORMAL);
		givenFirstTaskByParameterization();
		mockAuthService();
		ResponseEntity<TaskModel> respTaskModel = taskService.getTask(getSelectedTask().getId());
		TaskModel taskModel = respTaskModel.getBody();
		
		OffsetDateTime newDeadline = OffsetDateTime.now(ZoneOffset.UTC).plusDays(14);
		
		taskModel.setDeadline(newDeadline);
		ResponseEntity<TaskModel> respChangedTaskModel = taskService.modifyTask(taskModel, getSelectedTask().getId());
		
		Assert.assertNotNull(respChangedTaskModel);
		Assert.assertEquals(respChangedTaskModel.getStatusCode(), HttpStatus.OK);
		Assert.assertNotNull(respChangedTaskModel.getBody());
		Assert.assertEquals(getSelectedTask().getId(), respChangedTaskModel.getBody().getId());
		
		// Check TaskModel
		TaskModel changedTaskModel = respChangedTaskModel.getBody();
		Assert.assertEquals(newDeadline, changedTaskModel.getDeadline());
		// Check Task
		Task changedTask = taskRepository.getOne(getSelectedTask().getId());
		Assert.assertEquals(newDeadline, changedTask.getDeadline());
	}

	@Test
	@Rollback
	public void testChangeCompanyVirtues() {
		givenUser(TestConstant.USER_NORMAL);
		givenFirstTaskByParameterization();
		mockAuthService();
		ResponseEntity<TaskModel> respTaskModel = taskService.getTask(getSelectedTask().getId());
		TaskModel taskModel = respTaskModel.getBody();
		
		
		// Add CompanyVirtues
		CompanyVirtue companyVirtue = getSelectedTask().getTaskXCompanyVirtues().get(0).getCompanyVirtue();
		List<CompanyVirtue> companyVirtues = companyVirtueRepository.findAll();
		companyVirtues.remove(companyVirtue);
		CompanyVirtue otherCompanyVirtue = companyVirtues.get(0);
		
		CompanyVirtueModel otherCompanyVirtueModel = new CompanyVirtueModel()
				.id(otherCompanyVirtue.getId())
				.value(otherCompanyVirtue.getValue());
		
		taskModel.addCompanyVirtuesItem(otherCompanyVirtueModel);
		
		
		ResponseEntity<TaskModel> respChangedTaskModel = taskService.modifyTask(taskModel, getSelectedTask().getId());
		TaskModel changedTaskModel = respChangedTaskModel.getBody();
		Assert.assertEquals(2, changedTaskModel.getCompanyVirtues().size());

		
		// Delete CompanyVirtues
		changedTaskModel.getCompanyVirtues().remove(otherCompanyVirtueModel);
		respChangedTaskModel = taskService.modifyTask(changedTaskModel, getSelectedTask().getId());
		Assert.assertEquals(1, changedTaskModel.getCompanyVirtues().size());
		
		
		
		// Clean CompanyVirtues
		changedTaskModel.setCompanyVirtues(null);
		respChangedTaskModel = taskService.modifyTask(changedTaskModel, getSelectedTask().getId());

		// Check TaskModel
		changedTaskModel = respChangedTaskModel.getBody();
		Assert.assertTrue(changedTaskModel.getCompanyVirtues() == null || changedTaskModel.getCompanyVirtues().isEmpty());
		
		
	}
	
	@Test
	@Rollback
	public void testChangeLeaderVirtues() {
		givenUser(TestConstant.USER_NORMAL);
		givenFirstTaskByParameterization();
		mockAuthService();
		ResponseEntity<TaskModel> respTaskModel = taskService.getTask(getSelectedTask().getId());
		TaskModel taskModel = respTaskModel.getBody();

		// Add LeaderVirtue
		LeaderVirtue leaderVirtue = getSelectedTask().getTaskXLeaderVirtues().get(0).getLeaderVirtue();
		List<LeaderVirtue> leaderVirtues = leaderVirtueRepository.findAllByActiveTrueAndOwnerOrderByValue(getSelectedUser().getLeader());
		leaderVirtues.remove(leaderVirtue);
		LeaderVirtue otherLeaderVirtue = leaderVirtues.get(0);
		
		LeaderVirtueModel otherLeaderVirtueModel = new LeaderVirtueModel()
				.id(otherLeaderVirtue.getId())
				.value(otherLeaderVirtue.getValue());
		
		taskModel.addLeaderVirtuesItem(otherLeaderVirtueModel);
		
		
		ResponseEntity<TaskModel> respChangedTaskModel = taskService.modifyTask(taskModel, getSelectedTask().getId());
		TaskModel changedTaskModel = respChangedTaskModel.getBody();
		Assert.assertEquals(2, changedTaskModel.getLeaderVirtues().size());

		
		// Delete LeaderVirtue
		
		changedTaskModel.getLeaderVirtues().removeIf(p -> p.getId().equals(otherLeaderVirtueModel.getId()));
		respChangedTaskModel = taskService.modifyTask(changedTaskModel, getSelectedTask().getId());
		Assert.assertEquals(1, changedTaskModel.getLeaderVirtues().size());
		
		// Clean LeaderVirtues
		changedTaskModel.setLeaderVirtues(null);
		respChangedTaskModel = taskService.modifyTask(changedTaskModel, getSelectedTask().getId());

		// Check TaskModel
		changedTaskModel = respChangedTaskModel.getBody();
		Assert.assertTrue(changedTaskModel.getLeaderVirtues() == null || changedTaskModel.getLeaderVirtues().isEmpty());
		
	}
	
	// TODO: taskfactors : List<TaskFactorModel>

	@Test
	@Rollback
	public void testChangeEvaluators() {
		givenUser(TestConstant.USER_NORMAL);
		givenFirstTaskByParameterization();
		mockAuthService();
		ResponseEntity<TaskModel> respTaskModel = taskService.getTask(getSelectedTask().getId());
		TaskModel taskModel = respTaskModel.getBody();

		int defSize = getSelectedTask().getEvaluations().size();
		// Add Evaluation
		Evaluation evaluation = getSelectedTask().getEvaluations().get(0);
		List<User> users = userRepository.findAllByCompanyAndActiveIsTrue(getSelectedUser().getCompany());
		users.removeIf(p -> p.getId().equals(evaluation.getEvaluator().getId()));
		users.removeIf(p -> p.getId().equals(getSelectedUser().getId()));
		User otherUser = users.get(0);
		
		UserSimpleModel otherUserSimpleModel = new UserSimpleModel().id(otherUser.getId());
		
		taskModel.getEvaluators().add(new TaskEvaluationModel().evaluator(otherUserSimpleModel));
		
		ResponseEntity<TaskModel> respChangedTaskModel = taskService.modifyTask(taskModel, getSelectedTask().getId());
		TaskModel changedTaskModel = respChangedTaskModel.getBody();
		Assert.assertEquals(defSize + 1, changedTaskModel.getEvaluators().size());

		
		// Delete Evaluation
		changedTaskModel.getEvaluators().removeIf(p -> p.getEvaluator().getId().equals(otherUser.getId()));
		respChangedTaskModel = taskService.modifyTask(changedTaskModel, getSelectedTask().getId());
		Assert.assertEquals(defSize, respChangedTaskModel.getBody().getEvaluators().size());
		
		// Clean Evaluation
		changedTaskModel.setEvaluators(null);
		respChangedTaskModel = taskService.modifyTask(changedTaskModel, getSelectedTask().getId());

		// Check TaskModel
		changedTaskModel = respChangedTaskModel.getBody();
		Assert.assertTrue(changedTaskModel.getEvaluators() == null || changedTaskModel.getEvaluators().isEmpty());
		
	}

	
	@Test
	@Rollback
	public void testChangeEvaluators_PEAS_119() {
		// https://jira.nextent.hu/browse/PEAS-119
		// Saját értékelő hozzáadása hibára megy
		givenUser(TestConstant.USER_NORMAL);
		givenFirstTaskByParameterization();
		mockAuthService();
		
		// WHEN
		// Másolom a taskot, és a tulajdonost adom hozzá
		ResponseEntity<TaskModel> respCopyTaskModel = taskService.copyTask(getSelectedTask().getId());
		TaskModel copyTaskModel = respCopyTaskModel.getBody();
		copyTaskModel.getEvaluators().add(
				new TaskEvaluationModel()
					.evaluator(
							new UserSimpleModel().id(getSelectedUser().getId())
							)
					);
		Long copyTaskId = copyTaskModel.getId();
		taskService.modifyTask(copyTaskModel, copyTaskModel.getId());
		
		// ASSERT
		Task copyTask = taskRepository.getOne(copyTaskId);
		
		// Elvárt, hogy egy elem hozzá legyen adva, ami a kurrens felhasználó
		Assert.assertEquals(true, copyTask.getEvaluations().size() > 0);
		Assert.assertEquals(Integer.valueOf(0), copyTask.getEvaluatedCount());
		Assert.assertEquals(getSelectedUser().getId(), copyTask.getEvaluations().get(copyTask.getEvaluations().size() - 1).getEvaluator().getId());

	}
}
