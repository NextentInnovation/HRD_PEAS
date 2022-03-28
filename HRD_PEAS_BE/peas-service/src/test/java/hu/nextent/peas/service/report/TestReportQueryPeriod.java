package hu.nextent.peas.service.report;

import hu.nextent.peas.TestConstant;
import hu.nextent.peas.model.ReportPeriodListModel;
import hu.nextent.peas.service.TestServiceBase;
import hu.nextent.peas.service.TestServiceConfig;
import hu.nextent.peas.service.ReportService;
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

@ActiveProfiles("test")
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { TestServiceConfig.class }, loader = AnnotationConfigContextLoader.class)
@Transactional
@DirtiesContext
public class TestReportQueryPeriod extends TestServiceBase {

	@Autowired
	private ReportService reportService;

	@Test
	@Rollback
	public void testEmptyQueryParameter() {
		givenUser(TestConstant.USER_NORMAL);
		mockAuthService();
		
		// WHEN
		ResponseEntity<ReportPeriodListModel> respQueryPeriod = reportService.queryPeriod();
		
		// ASSERT
		Assert.assertNotNull(respQueryPeriod);
		Assert.assertEquals(respQueryPeriod.getStatusCode(), HttpStatus.OK);
		Assert.assertNotNull(respQueryPeriod.getBody());
		Assert.assertTrue(respQueryPeriod.getBody().size() > 0);
	}

}
