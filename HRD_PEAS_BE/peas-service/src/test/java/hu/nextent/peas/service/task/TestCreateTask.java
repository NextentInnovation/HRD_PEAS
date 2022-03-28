package hu.nextent.peas.service.task;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.Arrays;

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
import hu.nextent.peas.jpa.entity.Difficulty;
import hu.nextent.peas.jpa.entity.Factor;
import hu.nextent.peas.jpa.entity.LeaderVirtue;
import hu.nextent.peas.model.CompanyVirtueModel;
import hu.nextent.peas.model.DifficultyModel;
import hu.nextent.peas.model.LeaderVirtueModel;
import hu.nextent.peas.model.TaskCreateModel;
import hu.nextent.peas.model.TaskEvaluationListModel;
import hu.nextent.peas.model.TaskEvaluationModel;
import hu.nextent.peas.model.TaskFactorModel;
import hu.nextent.peas.model.TaskModel;
import hu.nextent.peas.model.UserSimpleModel;
import hu.nextent.peas.service.TestServiceConfig;


@ActiveProfiles("test")
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { TestServiceConfig.class }, loader = AnnotationConfigContextLoader.class)
@Transactional
@DirtiesContext
public class TestCreateTask extends TestTaskBase {
	
	private static final String NAME = "TASK NAME";
	private static final String DESCRIPTION = "TASK DESCRIPTION";
	
	private Difficulty selectedDifficulty;
	private OffsetDateTime deadline;
	private CompanyVirtue selectedCompanyVirtue;
	private LeaderVirtue selectedLeaderVirtue;
	private Factor selectedFactor; 
	
	// Ez egy minimális pozitív teszt
	@Test
	@Rollback
	public void testCreate() {
		givenUser(TestConstant.USER_NORMAL);
		mockAuthService();
		givenDefaultData();
		
		TaskCreateModel createTaskModel = prepareFullTaskCreateModel();
		
		// When
		ResponseEntity<TaskModel> respTaskModel = taskService.createTask(createTaskModel);
		
		// Assert
		Assert.assertNotNull(respTaskModel);
		Assert.assertEquals(respTaskModel.getStatusCode(), HttpStatus.OK);
		Assert.assertNotNull(respTaskModel.getBody());
		TaskModel taskModel = respTaskModel.getBody();
		
		Assert.assertEquals(NAME, taskModel.getName());
		Assert.assertEquals(DESCRIPTION, taskModel.getDescription());
		Assert.assertEquals(selectedDifficulty.getId(), taskModel.getDifficulty().getId());
		Assert.assertEquals(1, taskModel.getCompanyVirtues().size());
		Assert.assertEquals(selectedCompanyVirtue.getId(), taskModel.getCompanyVirtues().get(0).getId());
		Assert.assertEquals(1, taskModel.getLeaderVirtues().size());
		Assert.assertEquals(selectedLeaderVirtue.getId(), taskModel.getLeaderVirtues().get(0).getId());
		Assert.assertEquals(1, taskModel.getTaskfactors().size());
		Assert.assertEquals(selectedFactor.getId(), taskModel.getTaskfactors().get(0).getId());
		Assert.assertEquals(Boolean.TRUE, taskModel.getTaskfactors().get(0).isRequired());
		Assert.assertEquals(2, taskModel.getEvaluators().size());
		int foundeCnt = 0;
		for(TaskEvaluationModel evaluationModel: taskModel.getEvaluators()) {
			if (evaluationModel.getEvaluator().getId().equals(getSelectedUser().getId()) ||
					evaluationModel.getEvaluator().getId().equals(getSelectedUser().getLeader().getId())
					) {
				foundeCnt++;
			}
		}
		Assert.assertEquals(2, foundeCnt);
		
	
	}
	
	private void givenDefaultData() {
		selectedDifficulty = 
				difficultyRepository.findAllByActiveTrueAndAutomaticFalseAndCompanyOrderByName(getSelectedUser().getCompany()).get(0);
		
		OffsetDateTime now = OffsetDateTime.now(ZoneOffset.UTC);
		
		deadline = now.plusDays(14);
		
		selectedCompanyVirtue = 
				companyVirtueRepository.findAllByActiveTrueAndCompanyAndValueIsNotNullOrderByValue(getSelectedUser().getCompany()).get(0);
		
		selectedLeaderVirtue =
				leaderVirtueRepository.findAllByActiveTrueAndOwnerOrderByValue(getSelectedUser().getLeader()).get(0);
		
		selectedFactor =
				factorRepository.findAllByActiveTrueAndAutomaticFalseAndCompanyOrderByName(getSelectedUser().getCompany()).get(0);
	}
	

	
	private TaskCreateModel prepareFullTaskCreateModel() {
		
		TaskCreateModel model = new TaskCreateModel();
		model.setName(NAME);
		model.setDescription(DESCRIPTION);
		model.setDeadline(deadline);
		model.setDifficulty(new DifficultyModel().id(selectedDifficulty.getId()));
		model.setCompanyVirtues(Arrays.asList(new CompanyVirtueModel().id(selectedCompanyVirtue.getId())));
		model.setLeaderVirtues(Arrays.asList(new LeaderVirtueModel().id(selectedLeaderVirtue.getId())));
		model.setTaskfactors(Arrays.asList(
				(TaskFactorModel)new TaskFactorModel()
					.required(true)
					.id(selectedFactor.getId())
				));
		model.setEvaluators(new TaskEvaluationListModel());
		// Leader mint értékelő
		model.getEvaluators().add(new TaskEvaluationModel().evaluator(new UserSimpleModel().id(getSelectedUser().getLeader().getId())));
		// Sajátmaga mint értékelő
		model.getEvaluators().add(new TaskEvaluationModel().evaluator(new UserSimpleModel().id(getSelectedUser().getId())));
		
		return model;
	}

}
