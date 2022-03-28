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
import hu.nextent.peas.jpa.dao.EvaluationRepository;
import hu.nextent.peas.jpa.dao.NotificationRepository;
import hu.nextent.peas.jpa.entity.Company;
import hu.nextent.peas.jpa.entity.Evaluation;
import hu.nextent.peas.jpa.entity.EvaluationStatusEnum;
import hu.nextent.peas.jpa.entity.Notification;
import hu.nextent.peas.jpa.entity.NotificationTypeEnum;
import lombok.val;


@ActiveProfiles("test")
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { TestSchedulerConfig.class }, loader = AnnotationConfigContextLoader.class)
@Transactional
@DirtiesContext
public class TestNotificationEvaluationDeadlineCommingSchedulder2 {

	private final String DEFAULT_COMPANY = "nextent";
	
	@Autowired
	private CompanyParameterRepository companyParameterRepository;
	
	@Autowired
	private CompanyRepository companyRepository;

	@Autowired
	private EvaluationRepository evaluationRepository;

	@Autowired
	private NotificationRepository notificationRepository;

	@Autowired 
	private NotificationEvaluationDeadlineCommingSchedulder testedSchedulder;
	
	
	private Company defaultCompany;
	private String companyParameter;
	private List<Integer> deadlineDays;
	private Evaluation preparedEvaluation;
	
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
		companyParameter = companyParameterRepository.findByCompanyAndCode(defaultCompany, ServiceConstant.DEADLINE_EVALUATION_NOTIFICATION_WARNING).get().getValue();
	}
	
	@Test
	@Rollback
	public void testNotHaveDeadline() {
		testedSchedulder.scheduleEvaluationDeadlineComming();
	}
	
	@Test
	@Rollback
	public void testHaveLeastOneDeadlinedEvaluation() {
		// Preparálom az értékelést, hogy elgyen deadline
		givenDefaultCompany();
		givenCompanyParameterDeadlineStr();
		givenDeadlineDays();
		preparedOneEvaluation();
		
		// WHEN
		testedSchedulder.scheduleEvaluationDeadlineComming();
		
		// ASSERT
		// Megviszgálom, hogy elkészült-e a notifikáció
		Evaluation evaluation = evaluationRepository.getOne(preparedEvaluation.getId());
		List<Notification> notifications = notificationRepository.findAllByToUserAndEvaluationAndNotificationType(
				evaluation.getEvaluator(), evaluation, NotificationTypeEnum.EVALUATION_DEADLINE
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
	
	void preparedOneEvaluation() {
		List<Evaluation> evaluations = evaluationRepository.findAllByCompanyAndStatus(defaultCompany, EvaluationStatusEnum.EVALUATING);
		Assert.assertTrue(!evaluations.isEmpty());
		Integer days = deadlineDays.get(deadlineDays.size() - 1);
		OffsetDateTime now = OffsetDateTime.now(ZoneOffset.UTC);
		OffsetDateTime deadlinedDate = now.minusDays(days);
		preparedEvaluation = evaluations.get(0);
		preparedEvaluation.setDeadline(deadlinedDate);
		evaluationRepository.save(preparedEvaluation);
		evaluationRepository.flush();
	}
	
	
	@Test
	@Rollback
	public void testHaveLeastOneDeadlinedEvaluationAndNotDuplicate() {
		// Preparálom az értékelést, hogy elgyen deadline
		givenDefaultCompany();
		givenCompanyParameterDeadlineStr();
		givenDeadlineDays();
		preparedOneEvaluation();
		
		testedSchedulder.scheduleEvaluationDeadlineComming();
		// WHEN
		testedSchedulder.scheduleEvaluationDeadlineComming();
		
		// ASSERT
		// Megviszgálom, hogy elkészült-e, de csak egy notifikáció az adott napra
		Evaluation evaluation = evaluationRepository.getOne(preparedEvaluation.getId());
		List<Notification> notifications = notificationRepository.findAllByToUserAndEvaluationAndNotificationType(
				evaluation.getEvaluator(), evaluation, NotificationTypeEnum.EVALUATION_DEADLINE
				);
		// Egy elemre számítok
		Assert.assertEquals(1, notifications.size());
		
	}
}
