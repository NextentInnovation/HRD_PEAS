package hu.nextent.peas.service.report;

import hu.nextent.peas.TestConstant;
import hu.nextent.peas.jpa.dao.PeriodRepository;
import hu.nextent.peas.jpa.dao.RatingRepository;
import hu.nextent.peas.jpa.entity.Period;
import hu.nextent.peas.jpa.entity.PeriodStatusEnum;
import hu.nextent.peas.model.ReportEmployee;
import hu.nextent.peas.model.ReportEmployeeQueryModel;
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
public class TestReportEmployee extends TestServiceBase {

	@Autowired
	private ReportService reportService;

	@Autowired
	RatingRepository ratingRepository;

	@Autowired
	PeriodRepository periodRepository;

	ReportEmployeeQueryModel reportEmployeeQueryModel;
	Period period;

	@Test
	@Rollback
	public void testReportEmployeeByPeriodAndUser() {
		givenUser(TestConstant.USER_NORMAL);
		mockAuthService();

		givenPeriod();
		Assert.assertNotNull(period);

		givenReportEmployeeQueryModelByPeriodAndUser();

		// WHEN
		ResponseEntity<ReportEmployee> respReportEmployee = reportService.reportEmployee(reportEmployeeQueryModel);
		
		// ASSERT
		Assert.assertNotNull(respReportEmployee);
		Assert.assertEquals(respReportEmployee.getStatusCode(), HttpStatus.OK);
		Assert.assertNotNull(respReportEmployee.getBody());
}

	@Test
	@Rollback
	public void testReportEmployeeByPeriodAndUser2() {
		givenUser(TestConstant.USER_LEADER);
		mockAuthService();

		givenPeriod();
		Assert.assertNotNull(period);

		givenReportEmployeeQueryModelByPeriodAndUser();

		// WHEN
		ResponseEntity<ReportEmployee> respReportEmployee = reportService.reportEmployee(reportEmployeeQueryModel);

		// ASSERT
		Assert.assertNotNull(respReportEmployee);
		Assert.assertEquals(respReportEmployee.getStatusCode(), HttpStatus.OK);
		Assert.assertNotNull(respReportEmployee.getBody());
}

	@Test
	@Rollback
	public void testReportEmployeeByRating() {
		givenUser(TestConstant.USER_NORMAL);
		mockAuthService();

		givenPeriod();
		Assert.assertNotNull(period);

		givenReportEmployeeQueryModelByRating();

		// WHEN
		ResponseEntity<ReportEmployee> respReportEmployee = reportService.reportEmployee(reportEmployeeQueryModel);

		// ASSERT
		Assert.assertNotNull(respReportEmployee);
		Assert.assertEquals(respReportEmployee.getStatusCode(), HttpStatus.OK);
		Assert.assertNotNull(respReportEmployee.getBody());
		Assert.assertTrue(respReportEmployee.getBody().getTasks().size() > 0);
	}

	private void givenPeriod() {
		// minosites alatti periodus
		period = periodRepository.findAllByCompanyAndStatusInOrderByEndDateDesc(getSelectedUser().getCompany(), Arrays.asList(PeriodStatusEnum.RATING)).get(0);
	}

	private void givenReportEmployeeQueryModelByPeriodAndUser() {
		reportEmployeeQueryModel = new ReportEmployeeQueryModel();
		reportEmployeeQueryModel.setUserId(getSelectedUser().getId());
		reportEmployeeQueryModel.setPeriodId(period.getId());
		reportEmployeeQueryModel.setRatingId(null);
	}

	private void givenReportEmployeeQueryModelByRating() {
		reportEmployeeQueryModel = new ReportEmployeeQueryModel();
		reportEmployeeQueryModel.setUserId(null);
		reportEmployeeQueryModel.setPeriodId(null);
		reportEmployeeQueryModel.setRatingId(ratingRepository.findByPeriodAndUser(period, getSelectedUser()).get().getId());
	}

}
