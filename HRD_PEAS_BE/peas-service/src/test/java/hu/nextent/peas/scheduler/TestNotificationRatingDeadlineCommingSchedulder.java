package hu.nextent.peas.scheduler;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
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

import hu.nextent.peas.constant.ServiceConstant;
import hu.nextent.peas.jpa.dao.CompanyParameterRepository;
import hu.nextent.peas.jpa.dao.CompanyRepository;
import hu.nextent.peas.jpa.dao.NotificationRepository;
import hu.nextent.peas.jpa.dao.PeriodRepository;
import hu.nextent.peas.jpa.dao.RatingRepository;
import hu.nextent.peas.jpa.entity.Company;
import hu.nextent.peas.jpa.entity.Notification;
import hu.nextent.peas.jpa.entity.NotificationTypeEnum;
import hu.nextent.peas.jpa.entity.Period;
import hu.nextent.peas.jpa.entity.PeriodStatusEnum;
import hu.nextent.peas.jpa.entity.Rating;
import hu.nextent.peas.jpa.entity.RatingStatusEnum;
import lombok.val;


@ActiveProfiles("test")
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { TestSchedulerConfig.class }, loader = AnnotationConfigContextLoader.class)
@Transactional
@DirtiesContext
public class TestNotificationRatingDeadlineCommingSchedulder {

	private final String DEFAULT_COMPANY = "nextent";
	
	@Autowired
	private CompanyParameterRepository companyParameterRepository;
	
	@Autowired
	private CompanyRepository companyRepository;

	@Autowired
	private RatingRepository ratingRepository;
	
	@Autowired
	private PeriodRepository periodRepository;

	@Autowired
	private NotificationRepository notificationRepository;

	@Autowired 
	private NotificationRatingDeadlineCommingSchedulder testedSchedulder;
	
	
	private Company defaultCompany;
	private String companyParameter;
	private List<Integer> deadlineDays;
	private Rating preparedRating;
	
	@Test
	public void testGetDeadlineDays() {
		givenDefaultCompany();
		givenCompanyParameterDeadlineStr();
		
		Assert.assertNotNull(companyParameter);
	}
	
	private void givenDefaultCompany() {
		defaultCompany = companyRepository.findByName(DEFAULT_COMPANY).get();
	}
	
	private void givenCompanyParameterDeadlineStr() {
		companyParameter = companyParameterRepository.findByCompanyAndCode(defaultCompany, ServiceConstant.DEADLINE_RATING_NOTIFICATION_WARNING).get().getValue();
	}
	
	@Test
	@Rollback
	public void testNotHaveDeadline() {
		testedSchedulder.scheduleRatingDeadlineComming();
	}
	
	@Test
	@Rollback
	public void testHaveLeastOneDeadlinedRating() {
		// Preparálom a minősítést, hogy elgyen deadline
		givenDefaultCompany();
		givenCompanyParameterDeadlineStr();
		givenDeadlineDays();
		preparedOneRating();
		
		// WHEN
		testedSchedulder.scheduleRatingDeadlineComming();
		
		// ASSERT
		// Megviszgálom, hogy elkészült-e a notifikáció az értékeltnek
		Rating rating = ratingRepository.getOne(preparedRating.getId());
		List<Notification> notifications = notificationRepository.findAllByToUserAndRatingAndNotificationType(
				rating.getUser(), rating, NotificationTypeEnum.RATING_DEADLINE
				);
		// Egy elemre számítok
		Assert.assertEquals(1, notifications.size());
		// Megviszgálom, hogy elkészült-e a notifikáció a vezetőnek
		notifications = notificationRepository.findAllByToUserAndRatingAndNotificationType(
				rating.getLeader(), rating, NotificationTypeEnum.RATING_DEADLINE
				);
		// Egy elemre számítok
		Assert.assertEquals(1, notifications.size());
		
	}
	
	void givenDeadlineDays() {
		val deadlineArray = companyParameter;
		val days = StringUtils.split(deadlineArray, ",");
		deadlineDays = new ArrayList<Integer>();
		for(String day: days) {
			deadlineDays.add(Integer.valueOf(day));
		}
		Collections.sort(deadlineDays);
	}
	
	void preparedOneRating() {
		Period ratingPeriod = periodRepository.findByCompanyAndStatus(defaultCompany, PeriodStatusEnum.RATING).get();
		List<Rating> ratings = ratingRepository.findAllByPeriodAndStatus(ratingPeriod, RatingStatusEnum.OPEN);
		Assert.assertTrue(!ratings.isEmpty());
		Integer days = deadlineDays.get(deadlineDays.size() - 1);
		OffsetDateTime now = OffsetDateTime.now(ZoneOffset.UTC);
		OffsetDateTime deadlinedDate = now.minusDays(days);
		preparedRating = ratings.get(0);
		preparedRating.setDeadline(deadlinedDate);
		ratingRepository.save(preparedRating);
		ratingRepository.flush();
	}
	
	
	@Test
	@Rollback
	public void testHaveLeastOneDeadlinedRatingAndNotDuplicate() {
		// Preparálom a minősítést, hogy elgyen deadline
		givenDefaultCompany();
		givenCompanyParameterDeadlineStr();
		givenDeadlineDays();
		preparedOneRating();
		
		testedSchedulder.scheduleRatingDeadlineComming();
		notificationRepository.flush();
		
		Rating rating = ratingRepository.getOne(preparedRating.getId());
		List<Notification> notifications = notificationRepository.findAllByToUserAndRatingAndNotificationType(
				rating.getUser(), rating, NotificationTypeEnum.RATING_DEADLINE
				);

		testedSchedulder.scheduleRatingDeadlineComming();
		notificationRepository.flush();
		
		rating = ratingRepository.getOne(preparedRating.getId());
		notifications = notificationRepository.findAllByToUserAndRatingAndNotificationType(
				rating.getUser(), rating, NotificationTypeEnum.RATING_DEADLINE
				);

		// WHEN
		testedSchedulder.scheduleRatingDeadlineComming();
		notificationRepository.flush();
		
		// ASSERT
		// Megviszgálom, hogy elkészült-e, de csak egy notifikáció az adott napra
		// Dolgozónak
		rating = ratingRepository.getOne(preparedRating.getId());
		notifications = notificationRepository.findAllByToUserAndRatingAndNotificationType(
				rating.getUser(), rating, NotificationTypeEnum.RATING_DEADLINE
				);
		// Egy elemre számítok
		Assert.assertEquals(1, notifications.size());
		// Vezetőnek
		notifications = notificationRepository.findAllByToUserAndRatingAndNotificationType(
				rating.getLeader(), rating, NotificationTypeEnum.RATING_DEADLINE
				);
		// Egy elemre számítok
		Assert.assertEquals(1, notifications.size());
		
	}
}
