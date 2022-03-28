package hu.nextent.peas.service.task;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;
import org.springframework.transaction.annotation.Transactional;

import hu.nextent.peas.TestConstant;
import hu.nextent.peas.jpa.entity.Evaluation;
import hu.nextent.peas.jpa.entity.EvaluationStatusEnum;
import hu.nextent.peas.jpa.entity.TaskStatusEnum;
import hu.nextent.peas.service.TestServiceConfig;

@ActiveProfiles("test")
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { TestServiceConfig.class }, loader = AnnotationConfigContextLoader.class)
@Transactional
@DirtiesContext
public class TestDeleteTask extends TestTaskBase {

	@Test
	@Rollback
	public void testMe() {
		givenUser(TestConstant.USER_NORMAL);
		givenFirstTaskByParameterization();
		mockAuthService();
		
		taskService.deleteTask(getSelectedTask().getId());
		
		// Refresh Task
		setSelectedTask(taskRepository.getOne(getSelectedTask().getId()));
		Assert.assertEquals(TaskStatusEnum.DELETED, getSelectedTask().getStatus());
		
    	for(Evaluation eval: getSelectedTask().getEvaluations()) {
    		Assert.assertEquals(EvaluationStatusEnum.DELETED, eval.getStatus());
    	}
	}
}
