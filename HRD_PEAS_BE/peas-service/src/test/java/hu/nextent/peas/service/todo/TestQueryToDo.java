package hu.nextent.peas.service.todo;

import hu.nextent.peas.jpa.dao.ViewToDoRepository;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
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
import hu.nextent.peas.jpa.dao.ToDoRepository;
import hu.nextent.peas.jpa.entity.ToDoTypeEnum;
import hu.nextent.peas.model.ToDoPageModel;
import hu.nextent.peas.model.ToDoQueryParameterModel;
import hu.nextent.peas.service.TestServiceBase;
import hu.nextent.peas.service.TestServiceConfig;
import hu.nextent.peas.service.ToDoAndNotificationService;

@ActiveProfiles("test")
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { TestServiceConfig.class }, loader = AnnotationConfigContextLoader.class)
@Transactional
@DirtiesContext
public class TestQueryToDo extends TestServiceBase {

	@Autowired
	private ViewToDoRepository viewToDoRepository;

	@Autowired
	private ToDoAndNotificationService toDoAndNotificationService;
	
	@Test
	@Rollback
	public void testEmptyQueryParameter() {
		givenUser(TestConstant.USER_NORMAL);
		mockAuthService();
		
		// WHEN
		ResponseEntity<ToDoPageModel> respQueryTodo = toDoAndNotificationService.queryTodo(emptyParameters());
		
		// ASSERT
		Assert.assertNotNull(respQueryTodo);
		Assert.assertEquals(respQueryTodo.getStatusCode(), HttpStatus.OK);
		Assert.assertNotNull(respQueryTodo.getBody());
		Assert.assertTrue(respQueryTodo.getBody().getTotalElements() > 0);
		
		Long cnt = 0L;
		cnt += viewToDoRepository.countByToUserAndToDoType(getSelectedUser(), ToDoTypeEnum.EVALUATION);
		cnt += viewToDoRepository.countByToUserAndToDoType(getSelectedUser(), ToDoTypeEnum.RATING);
		
		Assert.assertEquals(respQueryTodo.getBody().getTotalElements(), cnt);
		
	}

	@Test
	@Rollback
	public void testLeader() {
		givenUser(TestConstant.USER_LEADER);
		mockAuthService();

		// WHEN
		ResponseEntity<ToDoPageModel> respQueryTodo = toDoAndNotificationService.queryTodo(emptyParameters());

		// ASSERT
		Assert.assertNotNull(respQueryTodo);
		Assert.assertEquals(respQueryTodo.getStatusCode(), HttpStatus.OK);
		Assert.assertNotNull(respQueryTodo.getBody());
		Assert.assertTrue(respQueryTodo.getBody().getTotalElements() > 0);

		Long cnt = 0L;
		cnt += viewToDoRepository.countByToUserAndToDoType(getSelectedUser(), ToDoTypeEnum.EVALUATION);
		cnt += viewToDoRepository.countByToUserAndToDoType(getSelectedUser(), ToDoTypeEnum.RATING);

		Assert.assertEquals(respQueryTodo.getBody().getTotalElements(), cnt);

	}

	private ToDoQueryParameterModel emptyParameters() {
		return new ToDoQueryParameterModel();
	}
	
	
	
	// TODO Test Filter
	
}
