package hu.nextent.peas.service.task;

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
import hu.nextent.peas.jpa.entity.TaskStatusEnum;
import hu.nextent.peas.jpa.entity.TaskTypeEnum;
import hu.nextent.peas.model.CompanyVirtueModel;
import hu.nextent.peas.model.LeaderVirtueModel;
import hu.nextent.peas.model.TaskFactorModel;
import hu.nextent.peas.model.TaskModel;
import hu.nextent.peas.service.TestServiceConfig;

@ActiveProfiles("test")
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { TestServiceConfig.class }, loader = AnnotationConfigContextLoader.class)
@Transactional
@DirtiesContext
public class TestCopyTask extends TestTaskBase {
	
	@Test
	@Rollback
	public void testCopy() {
		givenUser(TestConstant.USER_NORMAL);
		givenFirstTaskByParameterization();
		mockAuthService();
		ResponseEntity<TaskModel> respTaskModel = taskService.getTask(getSelectedTask().getId());		
		
		ResponseEntity<TaskModel> respCopyTaskModel = taskService.copyTask(getSelectedTask().getId());
		
		Assert.assertNotNull(respCopyTaskModel);
		Assert.assertEquals(respCopyTaskModel.getStatusCode(), HttpStatus.OK);
		Assert.assertNotNull(respCopyTaskModel.getBody());
		Assert.assertNotNull(respCopyTaskModel.getBody().getId());
		// Compare
		
		TaskModel taskModel = respTaskModel.getBody();
		TaskModel copyTaskModel = respCopyTaskModel.getBody();
		
		// Compare TaskSimpleModel
		Assert.assertEquals(TaskTypeEnum.NORMAL.name(), copyTaskModel.getTaskType());
		Assert.assertEquals(TaskStatusEnum.PARAMETERIZATION.name(), copyTaskModel.getStatus());
		Assert.assertEquals(taskModel.getName(), copyTaskModel.getName());
		Assert.assertEquals(taskModel.getDescription(), copyTaskModel.getDescription());
		Assert.assertEquals(taskModel.getOwner(), copyTaskModel.getOwner());
		Assert.assertEquals(taskModel.getDifficulty(), copyTaskModel.getDifficulty());
		// Compare TaskModel
		// companyVirtues
		if (taskModel.getCompanyVirtues() != null && !taskModel.getCompanyVirtues().isEmpty()) {
			Assert.assertEquals(taskModel.getCompanyVirtues().size(), copyTaskModel.getCompanyVirtues().size());
			for(CompanyVirtueModel m: taskModel.getCompanyVirtues()) {
				Assert.assertTrue(copyTaskModel.getCompanyVirtues().contains(m));
			}
		}
		// leaderVirtues
		if (taskModel.getLeaderVirtues() != null && !taskModel.getLeaderVirtues().isEmpty()) {
			Assert.assertEquals(taskModel.getLeaderVirtues().size(), copyTaskModel.getLeaderVirtues().size());
			for(LeaderVirtueModel m: taskModel.getLeaderVirtues()) {
				Assert.assertTrue(copyTaskModel.getLeaderVirtues().contains(m));
			}
		}
		// taskfactors
		if (taskModel.getTaskfactors() != null && !taskModel.getTaskfactors().isEmpty()) {
			Assert.assertEquals(taskModel.getTaskfactors().size(), copyTaskModel.getTaskfactors().size());
			for(TaskFactorModel m: taskModel.getTaskfactors()) {
				Assert.assertTrue(copyTaskModel.getTaskfactors().contains(m));
			}
		}
		
		// taskfactors
		if (taskModel.getTaskfactors() != null && !taskModel.getTaskfactors().isEmpty()) {
			Assert.assertEquals(taskModel.getTaskfactors().size(), copyTaskModel.getTaskfactors().size());
			for(TaskFactorModel m: taskModel.getTaskfactors()) {
				Assert.assertTrue(copyTaskModel.getTaskfactors().contains(m));
			}
		}
		// Evaulations
		if (taskModel.getEvaluators() != null && !taskModel.getEvaluators().isEmpty()) {
			Assert.assertEquals(taskModel.getEvaluators().size(), copyTaskModel.getEvaluators().size());
		}

		
	}
}
