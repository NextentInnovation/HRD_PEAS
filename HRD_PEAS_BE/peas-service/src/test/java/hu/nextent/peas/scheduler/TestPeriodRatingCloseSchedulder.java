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
import hu.nextent.peas.jpa.dao.RatingRepository;
import hu.nextent.peas.jpa.dao.UserRepository;
import hu.nextent.peas.jpa.entity.Company;
import hu.nextent.peas.jpa.entity.Notification;
import hu.nextent.peas.jpa.entity.NotificationTypeEnum;
import hu.nextent.peas.jpa.entity.Period;
import hu.nextent.peas.jpa.entity.PeriodStatusEnum;
import hu.nextent.peas.jpa.entity.Rating;
import hu.nextent.peas.jpa.entity.RatingStatusEnum;
import hu.nextent.peas.jpa.entity.RoleEnum;
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
public class TestPeriodRatingCloseSchedulder {

protected final String DEFAULT_COMPANY = "nextent";
	
	@Autowired
	private PeriodRepository periodRepository;
	
	@Autowired
	private CompanyRepository companyRepository;

	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private NotificationRepository notificationRepository;
	
	@Autowired
	private RatingRepository ratingRepository;
	
	@Autowired 
	private PeriodRatingCloseSchedulder testedSchedulder;

	
	Company company;
	Period ratingPeriod;
	
	
	private void givenCompany() {
		Optional<Company> optCompany = companyRepository.findByName(DEFAULT_COMPANY);
		company = optCompany.get();
	}
	
	private void givenRatingPeriod() {
		Optional<Period> optPeriod = periodRepository.findByCompanyAndStatus(company, PeriodStatusEnum.RATING);
		ratingPeriod = optPeriod.get();
	}
	
	@Test
	@Rollback
	public void testHasOpenRatingWhenHasOpen() {
		// A adott periódus alatt, ha van OPEN, akkor true értéket kell adnia 
		// GIVEN
		givenCompany();
		givenRatingPeriod();
		
		// WHEN
		boolean hasOpen = testedSchedulder.hasOpenRating(ratingPeriod);
		
		// ASSERT
		Assert.assertTrue(hasOpen);
	}

	@Test
	@Rollback
	public void testHasOpenRatingWhenHasNotOpen() {
		// A adott periódus alatt, ha van OPEN, akkor true értéket kell adnia 
		// GIVEN
		givenCompany();
		givenRatingPeriod();
		
		int openCnt = ratingRepository.countByPeriodAndStatus(ratingPeriod, RatingStatusEnum.OPEN);
		if (openCnt > 0) {
			List<Rating> ratings = ratingRepository.findAllByPeriodAndStatus(ratingPeriod, RatingStatusEnum.OPEN);
			ratings.forEach(r -> r.setStatus(RatingStatusEnum.EVALUATED));
			ratingRepository.saveAll(ratings);
		}
		
		// WHEN
		boolean hasOpen = testedSchedulder.hasOpenRating(ratingPeriod);
		
		// ASSERT
		Assert.assertFalse(hasOpen);
	}

	@Test
	@Rollback
	public void testCreateRatings() {
		// GIVEN
		givenCompany();
		givenRatingPeriod();
		
		// A adott periódus összes minősítését zárni kell
		int openCnt = ratingRepository.countByPeriodAndStatus(ratingPeriod, RatingStatusEnum.OPEN);
		int evaulatedCnt = ratingRepository.countByPeriodAndStatus(ratingPeriod, RatingStatusEnum.EVALUATED);
		//int expiredCnt = ratingRepository.countByPeriodAndStatus(ratingPeriod, RatingStatusEnum.EXPIRED);
		
		// Át kell raknom értékeltté
		if (openCnt > 0) {
			List<Rating> ratings = ratingRepository.findAllByPeriodAndStatus(ratingPeriod, RatingStatusEnum.OPEN);
			ratings.forEach(r -> r.setStatus(RatingStatusEnum.EVALUATED));
			ratingRepository.saveAll(ratings);
			
			evaulatedCnt = ratingRepository.countByPeriodAndStatus(ratingPeriod, RatingStatusEnum.EVALUATED);
		}
		
		// WHEN
		int cnt = testedSchedulder.closeRatings(ratingPeriod);
		
		// ASSERT
		Assert.assertEquals(evaulatedCnt, cnt);
	}
	
	@Test
	@Rollback
	public void testGenerateRatingCloseNotifictaions() {
		// GIVEN
		givenCompany();
		givenRatingPeriod();
		
		List<User> users = findUsersBuisnessAdminAndHr(company);
		// WHEN
		testedSchedulder.generateRatingCloseNotifictaions(ratingPeriod);
		
		// ASSERT
		List<Notification> notifications = notificationRepository.findAllByPeriodAndNotificationType(ratingPeriod, NotificationTypeEnum.PERIOD_RATING_CLOSE);
		Assert.assertEquals(users.size(), notifications.size());
	}
	
	private List<User> findUsersBuisnessAdminAndHr(Company company) {
		return userRepository.findAllByCompanyAndRoleEnums(company, Arrays.asList(RoleEnum.BUSINESS_ADMIN, RoleEnum.HR));
	}
	
	@Test
	@Rollback
	public void testCloseStatus() {
		// GIVEN
		givenCompany();
		givenRatingPeriod();
		
		// WHEN
		testedSchedulder.closeRatingPeriod(ratingPeriod);
		
		// ASSERT
		ratingPeriod = periodRepository.getOne(ratingPeriod.getId());
		Assert.assertEquals(PeriodStatusEnum.CLOSED, ratingPeriod.getStatus());
	}
	
	@Test
	@Rollback
	public void testAll() {
		// GIVEN
		givenCompany();
		givenRatingPeriod();
		
		int openCnt = ratingRepository.countByPeriodAndStatus(ratingPeriod, RatingStatusEnum.OPEN);
		// Át kell raknom értékeltté
		if (openCnt > 0) {
			List<Rating> ratings = ratingRepository.findAllByPeriodAndStatus(ratingPeriod, RatingStatusEnum.OPEN);
			ratings.forEach(r -> r.setStatus(RatingStatusEnum.EVALUATED));
			ratingRepository.saveAll(ratings);
		}
		
		ratingPeriod.setRatingEndDate(OffsetDateTime.now(ZoneOffset.UTC).minusMinutes(1));
		periodRepository.save(ratingPeriod);
		
		// WHEN
		testedSchedulder.scheduleFixedDelayTask();
		
		// ASSERT
		ratingPeriod = periodRepository.getOne(ratingPeriod.getId());
		Assert.assertEquals(PeriodStatusEnum.CLOSED, ratingPeriod.getStatus());
	}
	
	
}
