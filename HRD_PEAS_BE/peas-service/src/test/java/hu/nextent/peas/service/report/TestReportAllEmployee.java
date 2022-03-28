package hu.nextent.peas.service.report;

import hu.nextent.peas.TestConstant;
import hu.nextent.peas.jpa.dao.PeriodRepository;
import hu.nextent.peas.jpa.dao.RatingRepository;
import hu.nextent.peas.jpa.entity.Period;
import hu.nextent.peas.jpa.entity.PeriodStatusEnum;
import hu.nextent.peas.model.*;
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
public class TestReportAllEmployee extends TestServiceBase {

	@Autowired
	private ReportService reportService;

	@Autowired
	RatingRepository ratingRepository;

	@Autowired
	PeriodRepository periodRepository;

	ReportAllEmployeeQueryParameterModel reportAllEmployeeQueryParameterModel;
	Period period;

	@Test
	@Rollback
	public void testReportAllEmployeeByPeriod() {
		givenUser(TestConstant.USER_HR);
		mockAuthService();

		givenPeriod();
		Assert.assertNotNull(period);

		givenReportAllEmployeeQueryParameterModelByPeriod();

		// WHEN
		ResponseEntity<ReportAllEmployeePageModel> response = reportService.reportAllEmployee(reportAllEmployeeQueryParameterModel);
		
		// ASSERT
		Assert.assertNotNull(response);
		Assert.assertEquals(response.getStatusCode(), HttpStatus.OK);
		Assert.assertNotNull(response.getBody());
		Assert.assertTrue(response.getBody().getContent().size() > 0);
	}

	@Test
	@Rollback
	public void testReportAllEmployeeByPeriodSpecial() {
		givenUser(TestConstant.USER_HR);
		mockAuthService();

		givenPeriod();
		Assert.assertNotNull(period);

		givenReportAllEmployeeQueryParameterModelByPeriodSpecial();

		// WHEN
		ResponseEntity<ReportAllEmployeePageModel> response = reportService.reportAllEmployee(reportAllEmployeeQueryParameterModel);

		// ASSERT
		Assert.assertNotNull(response);
		Assert.assertEquals(response.getStatusCode(), HttpStatus.OK);
		Assert.assertNotNull(response.getBody());
		Assert.assertTrue(response.getBody().getContent().size() > 0);
	}

	@Test
	@Rollback
	public void testReportAllEmployeeByPeriodWithLeader() {
		givenUser(TestConstant.USER_LEADER);
		mockAuthService();

		givenPeriod();
		Assert.assertNotNull(period);

		givenReportAllEmployeeQueryParameterModelByPeriod();

		// WHEN
		ResponseEntity<ReportAllEmployeePageModel> response = reportService.reportAllEmployee(reportAllEmployeeQueryParameterModel);

		// ASSERT
		Assert.assertNotNull(response);
		Assert.assertEquals(response.getStatusCode(), HttpStatus.OK);
		Assert.assertNotNull(response.getBody());
		Assert.assertTrue(response.getBody().getContent().size() > 0);
	}

	@Test
	@Rollback
	public void testReportAllEmployeeByPeriodWithFilter() {
		givenUser(TestConstant.USER_HR);
		mockAuthService();

		givenPeriod();
		Assert.assertNotNull(period);

		givenReportAllEmployeeQueryParameterModelByPeriodWithFilter();

		// WHEN
		ResponseEntity<ReportAllEmployeePageModel> response = reportService.reportAllEmployee(reportAllEmployeeQueryParameterModel);

		// ASSERT
		Assert.assertNotNull(response);
		Assert.assertEquals(response.getStatusCode(), HttpStatus.OK);
		Assert.assertNotNull(response.getBody());
		Assert.assertTrue(response.getBody().getContent().size() > 0);
	}

	private void givenPeriod() {
		// minosites alatti periodus
		period = periodRepository.findAllByCompanyAndStatusInOrderByEndDateDesc(getSelectedUser().getCompany(), Arrays.asList(PeriodStatusEnum.RATING)).get(0);
	}

	private void givenReportAllEmployeeQueryParameterModelByPeriod() {
		reportAllEmployeeQueryParameterModel = new ReportAllEmployeeQueryParameterModel();
		reportAllEmployeeQueryParameterModel.setPeriodId(period.getId());
		reportAllEmployeeQueryParameterModel.setNumber(0);
		reportAllEmployeeQueryParameterModel.setSize(5);
		reportAllEmployeeQueryParameterModel.setSort("employee.fullName");
		reportAllEmployeeQueryParameterModel.setOrder(PagingAndSortingModel.OrderEnum.DESC);
	}

	private void givenReportAllEmployeeQueryParameterModelByPeriodSpecial() {
		reportAllEmployeeQueryParameterModel = new ReportAllEmployeeQueryParameterModel();
		reportAllEmployeeQueryParameterModel.setPeriodId(period.getId());
		reportAllEmployeeQueryParameterModel.setNumber(0);
		reportAllEmployeeQueryParameterModel.setSize(5);
		reportAllEmployeeQueryParameterModel.setSort("employee.fullName");
		reportAllEmployeeQueryParameterModel.setOrder(PagingAndSortingModel.OrderEnum.DESC);

		reportAllEmployeeQueryParameterModel.setOrganization("fejl");
		reportAllEmployeeQueryParameterModel.setScore(new RangeDoubleModel().max(9d));
	}

	private void givenReportAllEmployeeQueryParameterModelByPeriodWithFilter() {
		reportAllEmployeeQueryParameterModel = new ReportAllEmployeeQueryParameterModel();
		reportAllEmployeeQueryParameterModel.setPeriodId(period.getId());
		reportAllEmployeeQueryParameterModel.setAsLeaderOrganization("fejlesztés");
		reportAllEmployeeQueryParameterModel.setNumber(0);
		reportAllEmployeeQueryParameterModel.setSize(5);
		reportAllEmployeeQueryParameterModel.setSort("employee.fullName");
		reportAllEmployeeQueryParameterModel.setOrder(PagingAndSortingModel.OrderEnum.DESC);
	}

}
