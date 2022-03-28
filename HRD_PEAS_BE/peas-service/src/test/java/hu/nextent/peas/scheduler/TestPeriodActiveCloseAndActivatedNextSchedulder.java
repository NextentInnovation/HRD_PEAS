package hu.nextent.peas.scheduler;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;
import org.springframework.transaction.annotation.Transactional;

import hu.nextent.peas.jpa.dao.CompanyRepository;
import hu.nextent.peas.jpa.dao.NotificationRepository;
import hu.nextent.peas.jpa.dao.PeriodRepository;
import hu.nextent.peas.jpa.dao.TaskRepository;
import hu.nextent.peas.jpa.dao.UserRepository;
import hu.nextent.peas.jpa.entity.Company;
import hu.nextent.peas.jpa.entity.Notification;
import hu.nextent.peas.jpa.entity.NotificationTypeEnum;
import hu.nextent.peas.jpa.entity.Period;
import hu.nextent.peas.jpa.entity.PeriodStatusEnum;
import hu.nextent.peas.jpa.entity.RoleEnum;
import hu.nextent.peas.jpa.entity.TaskStatusEnum;
import hu.nextent.peas.jpa.entity.TaskTypeEnum;
import hu.nextent.peas.jpa.entity.User;

@ActiveProfiles("test")
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(
		classes = { 
				TestSchedulerConfig.class 
				} 
		,loader = AnnotationConfigContextLoader.class
				)
@DirtiesContext
@Transactional
public class TestPeriodActiveCloseAndActivatedNextSchedulder {

	protected final String DEFAULT_COMPANY = "nextent";
	
	@Autowired
	private PeriodRepository periodRepository;
	
	@Autowired
	private CompanyRepository companyRepository;
	
	@Autowired
	private TaskRepository taskRepository;

	@Autowired
	private UserRepository userRepository;
	
	@Autowired 
	private PeriodActiveCloseAndActivatedNextSchedulder testedSchedulder;
	
	@Autowired
	private NotificationRepository notificationRepository;
	
	Company company;
	OffsetDateTime endDate;
	Period activePeriod;

	@Test
	@Rollback
	public void testCloseAutomaticTasks() {
		// Átállítom a periódus végét kissebre a kurrens dátumnál, hogy a teszt esetben lezárásra kerüljön 
		// GIVEN
		givenCompany();
		givenActivePeriod();
		givenEndDate();
		prepareCloasableActivePeriod();
		
		int expectedCount = taskRepository.countByPeriodAndTaskTypeAndStatus(activePeriod, TaskTypeEnum.AUTOMATIC, TaskStatusEnum.UNDER_EVALUATION);
		
		// WHEN
		int cnt = testedSchedulder.closeAutomaticTasks(activePeriod, endDate);
		
		// ASSERT
		Assert.assertTrue(cnt > 0);
		Assert.assertEquals(expectedCount, cnt);
	}
	
	
	private void givenCompany() {
		Optional<Company> optCompany = companyRepository.findByName(DEFAULT_COMPANY);
		company = optCompany.get();
	}
	
	private void givenActivePeriod() {
		Optional<Period> optPeriod = periodRepository.findByCompanyAndStatus(company, PeriodStatusEnum.ACTIVE);
		activePeriod = optPeriod.get();
	}
	
	private void givenEndDate() {
		OffsetDateTime now = OffsetDateTime.now(ZoneOffset.UTC);
		endDate = now.minusMinutes(1);
	}
	
	private void prepareCloasableActivePeriod() {
		activePeriod.setEndDate(endDate);
		periodRepository.save(activePeriod);
		periodRepository.flush();
	}

	
	@Test
	@Rollback
	public void testCreateRatings() {
		// GIVEN
		givenCompany();
		givenActivePeriod();
		givenEndDate();
		prepareCloasableActivePeriod();
		
		// Annyi minősítést kell létrehozni, ahány user van
		
		// WHEN
		int cnt = testedSchedulder.createRatings(activePeriod);
		
		// ASSERT
		Assert.assertTrue(cnt > 0);
	}
	
	@Test
	@Rollback
	public void testCreateRatingToDos() {
		// GIVEN
		givenCompany();
		givenActivePeriod();
		givenEndDate();
		prepareCloasableActivePeriod();

		// Annyi ToDo-t kell létrehozni, ahány user van
		int cntRating = testedSchedulder.createRatings(activePeriod);
		// WHEN
		int cntToDo = testedSchedulder.createRatingToDos(activePeriod);
		
		// ASSERT
		Assert.assertTrue(cntToDo > 0);
		Assert.assertEquals(cntRating, cntToDo);
	}
	
	@Test
	@Rollback
	public void testAutomaticTaskScoreStoreToRating() {
		// GIVEN
		givenCompany();
		givenActivePeriod();
		givenEndDate();
		prepareCloasableActivePeriod();
		
		// Annyi minősítést módosítás történik ahány felhasználó
		
		// WHEN
		int cntRating = testedSchedulder.createRatings(activePeriod);
		int cnt = testedSchedulder.automaticTaskScoreStoreToRating(activePeriod);
		
		// ASSERT
		Assert.assertTrue(cnt > 0);
		Assert.assertEquals(cntRating, cnt);
	}
	
	@Test
	@Rollback
	public void testNormalTaskScoreStoreToRating() {
		// GIVEN
		givenCompany();
		givenActivePeriod();
		givenEndDate();
		prepareCloasableActivePeriod();
		
		// Annyi minősítést módosítás történik ahány felhasználó
		
		// WHEN
		int cntRating = testedSchedulder.createRatings(activePeriod);
		int cnt = testedSchedulder.normalTaskScoreStoreToRating(activePeriod);
		
		// ASSERT
		Assert.assertTrue(cnt > 0);
		Assert.assertEquals(cntRating, cnt);
	}
	
	@Test
	@Rollback
	public void testPeriodScoreStoreToRating() {
		// GIVEN
		givenCompany();
		givenActivePeriod();
		givenEndDate();
		prepareCloasableActivePeriod();
		
		// Annyi minősítést módosítás történik ahány felhasználó
		
		// WHEN
		int cntRating = testedSchedulder.createRatings(activePeriod);
		int cnt = testedSchedulder.periodScoreStoreToRating(activePeriod);
		
		// ASSERT
		Assert.assertTrue(cnt > 0);
		Assert.assertEquals(cntRating, cnt);
	}
	
	@Test
	@Rollback
	public void testGetOrCreateNextPeriodIfHasExistsPeriod() {
		// GIVEN
		givenCompany();
		givenActivePeriod();
		givenEndDate();
		prepareCloasableActivePeriod();
		
		// A következő nyitott periódus megtalálása
		// WHEN
		Period period = testedSchedulder.getOrCreateNextPeriod(activePeriod);
		
		// ASSERT
		Assert.assertNotNull(period);
		Assert.assertEquals(PeriodStatusEnum.OPEN, period.getStatus());
	}
	
	@Test
	@Rollback
	public void testGetOrCreateNextPeriodIfNotExistsPeriod() {
		// GIVEN
		givenCompany();
		givenActivePeriod();
		givenEndDate();
		prepareCloasableActivePeriod();
		
		// A következp periódusok törlése
		List<Period> periods = periodRepository.findAllByCompanyAndStatusInOrderByEndDateDesc(company, Arrays.asList(PeriodStatusEnum.OPEN));
		periods.stream().forEach(period -> periodRepository.delete(period));
		periodRepository.flush();
		
		// WHEN
		Period period = testedSchedulder.getOrCreateNextPeriod(activePeriod);
		
		// ASSERT
		Assert.assertNotNull(period);
		Assert.assertEquals(PeriodStatusEnum.OPEN, period.getStatus());
	}
	
	
	@Test
	@Rollback
	public void testCreateAutomaticTasks() {
		// GIVEN
		givenCompany();
		givenActivePeriod();
		givenEndDate();
		prepareCloasableActivePeriod();
		
		// Annyi automatikus task, ahány felhasználó van
		int expectedCount = userRepository.countByCompanyAndActiveIsTrue(company);
		// A következő nyitott periódus megtalálása
		Period nextPeriod = testedSchedulder.getOrCreateNextPeriod(activePeriod);

		// WHEN
		int cnt = testedSchedulder.createAutomaticTasks(nextPeriod);
		
		// ASSERT
		Assert.assertTrue(cnt > 0);
		Assert.assertEquals(expectedCount, cnt);
	}
	
	@Test
	@Rollback
	public void testCloseActivePeriod() {
		// GIVEN
		givenCompany();
		givenActivePeriod();
		givenEndDate();
		prepareCloasableActivePeriod();
		
		// WHEN
		testedSchedulder.closeActivePeriod(activePeriod);
		
		// ASSERT
		activePeriod = periodRepository.getOne(activePeriod.getId());
		Assert.assertEquals(PeriodStatusEnum.RATING, activePeriod.getStatus());
	}
	
	@Test
	@Rollback
	public void testActivateNextPeriod() {
		// GIVEN
		givenCompany();
		givenActivePeriod();
		givenEndDate();
		prepareCloasableActivePeriod();
		
		Period period = testedSchedulder.getOrCreateNextPeriod(activePeriod);

		// WHEN
		testedSchedulder.closeActivePeriod(period);
		
		// ASSERT
		period = periodRepository.getOne(period.getId());
		Assert.assertEquals(PeriodStatusEnum.ACTIVE, activePeriod.getStatus());
	}
	
	
	private List<User> findUsersBuisnessAdminAndHr(Company company) {
		return userRepository.findAllByCompanyAndRoleEnums(company, Arrays.asList(RoleEnum.BUSINESS_ADMIN, RoleEnum.HR));
	}
	
	@Test
	@Rollback
	public void testGenerateNotificationToClosePeriod() {
		// GIVEN
		givenCompany();
		givenActivePeriod();
		givenEndDate();
		prepareCloasableActivePeriod();
		
		List<User> users = findUsersBuisnessAdminAndHr(company);
		// WHEN
		testedSchedulder.generateNotificationToClosePeriod(activePeriod);
		
		// ASSERT
		List<Notification> notifications = notificationRepository.findAllByPeriodAndNotificationType(activePeriod, NotificationTypeEnum.PERIOD_ACTIVE_CLOSE);
		Assert.assertEquals(users.size(), notifications.size());
	}
	
	
	@Test
	@Rollback
	public void testGenerateNotificationToActivatedPeriod() {
		// GIVEN
		givenCompany();
		givenActivePeriod();
		givenEndDate();
		prepareCloasableActivePeriod();
		
		List<User> users = findUsersBuisnessAdminAndHr(company);
		// WHEN
		testedSchedulder.generateNotificationToActivatedPeriod(activePeriod);
		
		// ASSERT
		List<Notification> notifications = notificationRepository.findAllByPeriodAndNotificationType(activePeriod, NotificationTypeEnum.PERIOD_ACTIVATED);
		Assert.assertEquals(users.size(), notifications.size());
	}
	

	@Test
	@Rollback
	public void testAll() {
		// GIVEN
		givenCompany();
		givenActivePeriod();
		givenEndDate();
		prepareCloasableActivePeriod();
		
		// WHEN
		testedSchedulder.scheduleFixedDelayTask();
		
		// ASSERT
		// Hiba nélkül végigfut
	}
	
	
}
