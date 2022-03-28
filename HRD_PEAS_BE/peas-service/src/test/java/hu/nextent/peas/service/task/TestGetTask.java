package hu.nextent.peas.service.task;

import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
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
import hu.nextent.peas.exceptions.BaseResponseException;
import hu.nextent.peas.exceptions.ExceptionLabelConstant;
import hu.nextent.peas.model.TaskModel;
import hu.nextent.peas.service.TestServiceConfig;

@ActiveProfiles("test")
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { TestServiceConfig.class }, loader = AnnotationConfigContextLoader.class)
@Transactional
@DirtiesContext
public class TestGetTask extends TestTaskBase {
	
	
	@Test
	@Rollback
	public void testNormalUserOneCurrentUserTask() {
		givenUser(TestConstant.USER_NORMAL);
		givenFirstTaskByParameterization();
		mockAuthService();
		
		ResponseEntity<TaskModel> respTaskModel = taskService.getTask(getSelectedTask().getId());
		
		Assert.assertNotNull(respTaskModel);
		Assert.assertEquals(respTaskModel.getStatusCode(), HttpStatus.OK);
		Assert.assertNotNull(respTaskModel.getBody());
		Assert.assertEquals(getSelectedTask().getId(), respTaskModel.getBody().getId());
	}
	
	@Rule
	public ExpectedException thrown = ExpectedException.none();
	
	@Test
	@Rollback
	public void testNotFoundedTask() {
		givenUser(TestConstant.USER_NORMAL);
		givenFirstTaskByParameterization();
		mockAuthService();
		
		thrown.expect(BaseResponseException.class);
		@SuppressWarnings("unused")
		ResponseEntity<TaskModel> respTaskModel = taskService.getTask(-1L);
		
		thrown.expectMessage(ExceptionLabelConstant.ERROR_TASK_NOT_FOUNDED);
	}
	
	@Test
	@Rollback
	public void testCanReadLeaderTheNormalUserTask() {
		// A Leader tudja olvasni a beosztott task-ját
		givenUser(TestConstant.USER_NORMAL);
		givenFirstTaskByParameterization();
		givenUser(TestConstant.USER_LEADER); // Set Leader
		mockAuthService();
		
		ResponseEntity<TaskModel> respTaskModel = taskService.getTask(getSelectedTask().getId());
		Assert.assertNotNull(respTaskModel);
		Assert.assertEquals(respTaskModel.getStatusCode(), HttpStatus.OK);
		Assert.assertNotNull(respTaskModel.getBody());
		Assert.assertEquals(getSelectedTask().getId(), respTaskModel.getBody().getId());
	}
	
}
