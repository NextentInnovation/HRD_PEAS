package hu.nextent.peas.service.report;

import hu.nextent.peas.TestConstant;
import hu.nextent.peas.jpa.dao.PeriodRepository;
import hu.nextent.peas.jpa.dao.RatingRepository;
import hu.nextent.peas.jpa.entity.Period;
import hu.nextent.peas.jpa.entity.PeriodStatusEnum;
import hu.nextent.peas.model.ReportEvaluationModel;
import hu.nextent.peas.service.ReportService;
import hu.nextent.peas.service.TestServiceBase;
import hu.nextent.peas.service.TestServiceConfig;
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

import java.util.Arrays;

@ActiveProfiles("test")
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { TestServiceConfig.class }, loader = AnnotationConfigContextLoader.class)
@Transactional
@DirtiesContext
public class TestReportEvaluation extends TestServiceBase {

	@Autowired
	private ReportService reportService;

	@Autowired
	RatingRepository ratingRepository;

	@Autowired
	PeriodRepository periodRepository;

	Long taskId;
	Period period;

	@Test
	@Rollback
	public void testReportEmployeeByHR() {
		givenUser(TestConstant.USER_HR);
		mockAuthService();

		givenPeriod();
		Assert.assertNotNull(period);

		taskId = 1L;

		// WHEN
		ResponseEntity<ReportEvaluationModel> response = reportService.reportEvaluation(taskId);
		
		// ASSERT
		Assert.assertNotNull(response);
		Assert.assertEquals(response.getStatusCode(), HttpStatus.OK);
		Assert.assertNotNull(response.getBody());
}

	@Test
	@Rollback
	public void testReportEmployeeByLeader() {
		givenUser(TestConstant.USER_LEADER);
		mockAuthService();

		givenPeriod();
		Assert.assertNotNull(period);

		taskId = 7L;

		// WHEN
		ResponseEntity<ReportEvaluationModel> response = reportService.reportEvaluation(taskId);

		// ASSERT
		Assert.assertNotNull(response);
		Assert.assertEquals(response.getStatusCode(), HttpStatus.OK);
		Assert.assertNotNull(response.getBody());
	}

	@Test
	@Rollback
	public void testReportEmployeeByUser() {
		givenUser(TestConstant.USER_NORMAL);
		mockAuthService();

		givenPeriod();
		Assert.assertNotNull(period);

		taskId = 4L;

		// WHEN
		ResponseEntity<ReportEvaluationModel> response = reportService.reportEvaluation(taskId);

		// ASSERT
		Assert.assertNotNull(response);
		Assert.assertEquals(response.getStatusCode(), HttpStatus.OK);
		Assert.assertNotNull(response.getBody());
	}

	private void givenPeriod() {
		// minosites alatti periodus
		period = periodRepository.findAllByCompanyAndStatusInOrderByEndDateDesc(getSelectedUser().getCompany(), Arrays.asList(PeriodStatusEnum.RATING)).get(0);
	}

}
