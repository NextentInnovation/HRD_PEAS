package hu.nextent.peas.service.todo;

import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
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
import hu.nextent.peas.exceptions.BaseResponseException;
import hu.nextent.peas.jpa.dao.NotificationActionRepository;
import hu.nextent.peas.jpa.dao.NotificationRepository;
import hu.nextent.peas.model.NotificationPageModel;
import hu.nextent.peas.model.NotificationQueryParameterModel;
import hu.nextent.peas.service.TestServiceBase;
import hu.nextent.peas.service.TestServiceConfig;
import hu.nextent.peas.service.ToDoAndNotificationService;

@ActiveProfiles("test")
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { TestServiceConfig.class }, loader = AnnotationConfigContextLoader.class)
@Transactional
@DirtiesContext
public class TestQueryNotification extends TestServiceBase {
	
	@Autowired
	private ToDoAndNotificationService toDoAndNotificationService;
	
	@Autowired
	private NotificationRepository notificationRepository;
	
	@Autowired
	private NotificationActionRepository notificationActionRepository;

	@Test
	@Rollback
	public void testEmptyQueryParameter() {
		givenUser(TestConstant.USER_NORMAL);
		mockAuthService();
		
		// WHEN
		// Nincs szürő paraméter, ezért automatikusan rejti az olvasottakat
		NotificationQueryParameterModel emptyParameters = new NotificationQueryParameterModel().hideReaded(null).markReaded(null);
		ResponseEntity<NotificationPageModel> respQueryNotif = toDoAndNotificationService.queryNotification(emptyParameters);
		
		// ASSERT
		Assert.assertNotNull(respQueryNotif);
		Assert.assertEquals(respQueryNotif.getStatusCode(), HttpStatus.OK);
		Assert.assertNotNull(respQueryNotif.getBody());
		Assert.assertTrue(respQueryNotif.getBody().getTotalElements() > 0);
		
		Long cnt = notificationRepository.countByToUser(getSelectedUser());
		Assert.assertEquals(respQueryNotif.getBody().getTotalElements(), cnt);
		
	}
	
	@Test
	@Rollback
	public void testMarkReadedQueryParameter() {
		/*
		 * Olvasottá teszi az elemeket a lekérdezés után
		 */
		givenUser(TestConstant.USER_NORMAL);
		mockAuthService();
		
		// WHEN
		// A lekérdezés után olvasottá teszi
		NotificationQueryParameterModel markReaded = new NotificationQueryParameterModel().hideReaded(null).markReaded(true);
		ResponseEntity<NotificationPageModel> respQueryNotif = toDoAndNotificationService.queryNotification(markReaded);
		
		// ASSERT
		Assert.assertNotNull(respQueryNotif);
		Assert.assertEquals(respQueryNotif.getStatusCode(), HttpStatus.OK);
		Assert.assertNotNull(respQueryNotif.getBody());
		Assert.assertTrue(respQueryNotif.getBody().getTotalElements() > 0);
		
		// Elérhető elemek száma
		Long cnt = notificationRepository.countByToUser(getSelectedUser());
		Assert.assertEquals(respQueryNotif.getBody().getTotalElements(), cnt);
		
		// Még nem olvasott elemek száma
		Long cntNotReaded = notificationRepository.countByToUserAndReadedIsNull(getSelectedUser());
		Assert.assertEquals(Long.valueOf(0), cntNotReaded);

		// Már olvasott elemek száma
		Long cntReaded = notificationRepository.countByToUserAndReadedIsNotNull(getSelectedUser());
		Assert.assertEquals(cnt, cntReaded);
		
	}
	
	
	@Rule
	public ExpectedException thrown = ExpectedException.none();
	
	@Test
	@Rollback
	public void testReadedButNotHideQueryParameter() {
		/*
		 * Az olvasott elemeket már nem kérdezi le
		 */
		givenUser(TestConstant.USER_NORMAL);
		mockAuthService();
		
		// A lekérdezés után olvasottá teszi
		NotificationQueryParameterModel markReaded = new NotificationQueryParameterModel().hideReaded(false).markReaded(true);
		toDoAndNotificationService.queryNotification(markReaded);

		// WHEN
		thrown.expect(BaseResponseException.class);
		// Az olvasottakat nem kérdezi le
		NotificationQueryParameterModel emptyParameters = new NotificationQueryParameterModel().hideReaded(true).markReaded(null);
		//ResponseEntity<NotificationPageModel> respQueryNotif = toDoAndNotificationService.queryNotification(emptyParameters);
		toDoAndNotificationService.queryNotification(emptyParameters);
		
		// ASSERT
	}
	

	
	@Test
	@Rollback
	public void testReadedHideQueryParameter() {
		/*
		 * Az olvasott elemeket is lekérdezi
		 */
		givenUser(TestConstant.USER_NORMAL);
		mockAuthService();
		
		// A lekérdezés után olvasottá teszi
		NotificationQueryParameterModel markReaded = new NotificationQueryParameterModel().hideReaded(null).markReaded(true);
		toDoAndNotificationService.queryNotification(markReaded);

		// WHEN
		// Az olvasottakat nem kérdezi le
		NotificationQueryParameterModel allReaded = new NotificationQueryParameterModel().hideReaded(false).markReaded(null);
		ResponseEntity<NotificationPageModel> respQueryNotif = toDoAndNotificationService.queryNotification(allReaded);
		
		// ASSERT
		Assert.assertNotNull(respQueryNotif);
		Assert.assertEquals(respQueryNotif.getStatusCode(), HttpStatus.OK);
		Assert.assertNotNull(respQueryNotif.getBody());
		Assert.assertTrue(respQueryNotif.getBody().getTotalElements() > 0);
		
		Long cnt = notificationRepository.countByToUser(getSelectedUser());
		Assert.assertEquals(respQueryNotif.getBody().getTotalElements(), cnt);
	}

	
	@Test
	@Rollback
	public void testDisableNotifActionsParameter() {
		givenUser(TestConstant.USER_NORMAL);
		mockAuthService();
		
		notificationActionRepository
			.findAllByCompany(getSelectedUser().getCompany())
			.stream()
			.forEach(na -> {
				na.setShowable(false);
				notificationActionRepository.save(na);
			});
		notificationActionRepository.flush();
		
		// WHEN
		// Nincs szürő paraméter, ezért automatikusan rejti az olvasottakat
		
		// WHEN
		thrown.expect(BaseResponseException.class);
				
		NotificationQueryParameterModel emptyParameters = new NotificationQueryParameterModel().hideReaded(null).markReaded(null);
		ResponseEntity<NotificationPageModel> respQueryNotif = toDoAndNotificationService.queryNotification(emptyParameters);
		
	}
}
