package hu.nextent.peas.service.task;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;

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
import hu.nextent.peas.exceptions.BaseResponseException;
import hu.nextent.peas.model.RangeDateTimeModel;
import hu.nextent.peas.model.TaskItemPageModel;
import hu.nextent.peas.model.TaskQueryParameterModel;
import hu.nextent.peas.service.TestServiceConfig;
import lombok.val;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@ActiveProfiles("test")
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { TestServiceConfig.class }, loader = AnnotationConfigContextLoader.class)
@Transactional
@DirtiesContext
public class TestTaskQuery extends TestTaskBase {
	
	@Test
	@Rollback
	public void testNormalUserQuery() {
		givenUser(TestConstant.USER_NORMAL);
		mockAuthService();
		
		ResponseEntity<TaskItemPageModel> page = taskService.queryTask(null);
		
		Assert.assertNotNull(page);
		Assert.assertEquals(page.getStatusCode(), HttpStatus.OK);
		Assert.assertNotNull(page.getBody());
		log.info(page.getBody().toString());
		/*
		size: 10
        totalPages: 1
        totalElements: 2
        number: 0
		 */
		
		// Paging
		log.info(
				"size: {}, totalPages: {}, totalElements:{}, number:{}"
				, page.getBody().getSize()
				, page.getBody().getTotalPages()
				, page.getBody().getTotalElements()
				, page.getBody().getNumber()
				
				);
		Assert.assertTrue(page.getBody().getSize() > 0);
		Assert.assertTrue(page.getBody().getTotalPages() > 0);
		Assert.assertTrue(page.getBody().getTotalElements() > 0);
		
		// Content
		Assert.assertNotNull(page.getBody().getContent());
		Assert.assertTrue(!page.getBody().getContent().isEmpty());
	}
	
	private TaskQueryParameterModel createQueryTemplate() {
		val template = new TaskQueryParameterModel();
		template.setSize(10);
		template.setNumber(0);
		return template;
	}
	
	@Test
	@Rollback
	public void testNormalUserQueryFilterName() {
		givenUser(TestConstant.USER_NORMAL);
		mockAuthService();
		
		val query = createQueryTemplate();
		query.setName("tesztelek");
		
		ResponseEntity<TaskItemPageModel> page = taskService.queryTask(query);
		
		Assert.assertNotNull(page);
		Assert.assertEquals(page.getStatusCode(), HttpStatus.OK);
		Assert.assertNotNull(page.getBody());
		
		Assert.assertTrue(page.getBody().getTotalElements() == 1);
		Assert.assertTrue(page.getBody().getContent().get(0).getName().contains("tesztelek"));
	}
	
	// TODO Test LEADER

	@Test
	@Rollback
	public void testLeaderUser() {
		givenUser(TestConstant.USER_LEADER);
		mockAuthService();

		val query = createQueryTemplate();

		query.setOwnerId(getSelectedUser().getId());

		ResponseEntity<TaskItemPageModel> page = taskService.queryTask(query);

		Assert.assertNotNull(page);
		Assert.assertEquals(page.getStatusCode(), HttpStatus.OK);
		Assert.assertNotNull(page.getBody());

		Assert.assertTrue(page.getBody().getTotalElements() > 0);
	}

	@Test
	@Rollback
	public void testHrUserAndEndDate() {
		givenUser(TestConstant.USER_HR);
		mockAuthService();
		
		val query = createQueryTemplate();
		
		query.setEndDateRange(new RangeDateTimeModel());
		query.getEndDateRange().setMin(OffsetDateTime.now(ZoneOffset.UTC));
		
		try {
			@SuppressWarnings("unused")
			ResponseEntity<TaskItemPageModel> page = taskService.queryTask(query);
		} catch (BaseResponseException e) {
			// Elvárt, hogy itt ne legyen elemek, és akkor hibát dob
		}
	}
}
