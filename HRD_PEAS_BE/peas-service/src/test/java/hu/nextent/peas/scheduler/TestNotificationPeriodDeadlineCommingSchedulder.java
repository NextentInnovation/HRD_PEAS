package hu.nextent.peas.scheduler;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

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
import hu.nextent.peas.jpa.dao.UserRepository;
import hu.nextent.peas.jpa.entity.Company;
import hu.nextent.peas.jpa.entity.Notification;
import hu.nextent.peas.jpa.entity.NotificationTypeEnum;
import hu.nextent.peas.jpa.entity.Period;
import hu.nextent.peas.jpa.entity.PeriodStatusEnum;
import hu.nextent.peas.jpa.entity.RoleEnum;
import lombok.val;


@ActiveProfiles("test")
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { TestSchedulerConfig.class }, loader = AnnotationConfigContextLoader.class)
@Transactional
@DirtiesContext
public class TestNotificationPeriodDeadlineCommingSchedulder {

	private final String DEFAULT_COMPANY = "nextent";
	
	@Autowired
	private CompanyParameterRepository companyParameterRepository;
	
	@Autowired
	private CompanyRepository companyRepository;

	@Autowired
	private PeriodRepository periodRepository;

	@Autowired
	private NotificationRepository notificationRepository;
	
	@Autowired
	private UserRepository userRepository;

	@Autowired 
	private NotificationPeriodDeadlineCommingSchedulder testedSchedulder;
	
	
	private Company defaultCompany;
	private String companyParameter;
	private List<Integer> deadlineDays;
	private Period preparedPeriod;
	
	@Test
	@Rollback
	public void testGetDeadlineDays() {
		givenDefaultCompany();
		givenCompanyParameterDeadlineStr();
		
		Assert.assertNotNull(companyParameter);
	}
	
	private void givenDefaultCompany() {
		defaultCompany = companyRepository.findByName(DEFAULT_COMPANY).get();
	}
	
	private void givenCompanyParameterDeadlineStr() {
		companyParameter = companyParameterRepository.findByCompanyAndCode(defaultCompany, ServiceConstant.DEADLINE_PERIOD_NOTIFICATION_WARNING).get().getValue();
	}
	
	@Test
	@Rollback
	public void testNotHaveDeadline() {
		testedSchedulder.scheduleNotificationPeriodDeadlineCommingSchedulder();
	}
	
	@Test
	@Rollback
	public void testHaveLeastOneDeadlinedTask() {
		// Preparálom az értékelést, hogy elgyen deadline
		givenDefaultCompany();
		givenCompanyParameterDeadlineStr();
		givenDeadlineDays();
		preparedOnePeriod();
		
		// WHEN
		testedSchedulder.scheduleNotificationPeriodDeadlineCommingSchedulder();
		notificationRepository.flush();
		
		// ASSERT
		// Megviszgálom, hogy elkészült-e a notifikáció
		preparedPeriod = periodRepository.getOne(preparedPeriod.getId());
		List<Notification> notifications = notificationRepository.findAllByPeriodAndNotificationType(
				preparedPeriod, NotificationTypeEnum.PERIOD_DEADLINE
				);
		// Annyi elemre számítok, ahány BUSINESS_ADMIN és HR vam
		long userCnt = userRepository.countByCompanyAndRoleEnums(defaultCompany, Arrays.asList(RoleEnum.BUSINESS_ADMIN, RoleEnum.HR));
		Assert.assertEquals(Long.valueOf(userCnt).intValue(), notifications.size());
		
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
	
	void preparedOnePeriod() {
		Optional<Period> optPeriod = periodRepository.findByCompanyAndStatus(defaultCompany, PeriodStatusEnum.ACTIVE);
		Assert.assertTrue(optPeriod.isPresent());
		Integer days = deadlineDays.get(deadlineDays.size() - 1);
		OffsetDateTime now = OffsetDateTime.now(ZoneOffset.UTC);
		OffsetDateTime deadlinedDate = now.minusDays(days);
		preparedPeriod = optPeriod.get();
		preparedPeriod.setEndDate(deadlinedDate);
		periodRepository.save(preparedPeriod);
		periodRepository.flush();
	}
	
	
	@Test
	@Rollback
	public void testHaveLeastOneDeadlinedEvaluationAndNotDuplicate() {
		// Preparálom az értékelést, hogy elgyen deadline
		givenDefaultCompany();
		givenCompanyParameterDeadlineStr();
		givenDeadlineDays();
		preparedOnePeriod();
		
		testedSchedulder.scheduleNotificationPeriodDeadlineCommingSchedulder();
		
		// WHEN
		testedSchedulder.scheduleNotificationPeriodDeadlineCommingSchedulder();
		
		// ASSERT
		// Megviszgálom, hogy elkészült-e, de csak egy notifikáció az adott napra
		List<Notification> notifications = notificationRepository.findAllByPeriodAndNotificationType(
				preparedPeriod, NotificationTypeEnum.PERIOD_DEADLINE
				);
		// Annyi elemre számítok, ahány BUSINESS_ADMIN és HR vam
		long userCnt = userRepository.countByCompanyAndRoleEnums(defaultCompany, Arrays.asList(RoleEnum.BUSINESS_ADMIN, RoleEnum.HR));
		Assert.assertEquals(Long.valueOf(userCnt).intValue(), notifications.size());
		
	}
}
