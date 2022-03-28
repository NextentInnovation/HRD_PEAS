package hu.nextent.peas.converters;

import org.junit.Assert;
import org.junit.Test;

import hu.nextent.peas.jpa.entity.Period;
import lombok.val;

public class TestReportPeriodModelConverter {

	private final static Long PERIOD_ID = 11L;
	private final static String PERIOD_NAME = "name";

	private ReportPeriodModelConverter converter;
	private Period period;
	
	private void givenFactorOption() {
		period = new Period();
		period.setId(PERIOD_ID);
		period.setName(PERIOD_NAME);
	}
	
	private void givenConverter() {
		converter = new ReportPeriodModelConverter();
	}
	
	@Test
	public void testNormal() {
		givenFactorOption();
		givenConverter();
		
		val model = converter.convert(period);
		
		Assert.assertNotNull(model);
		Assert.assertEquals(PERIOD_ID, model.getId());
		Assert.assertEquals(PERIOD_NAME, model.getName());
	}
}
