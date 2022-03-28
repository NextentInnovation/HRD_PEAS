package hu.nextent.peas.converters;

import java.math.BigDecimal;

import org.junit.Assert;
import org.junit.Test;

import hu.nextent.peas.jpa.entity.FactorOption;
import lombok.val;

public class TestFactorOptionModelConverter {

	private final static Long FACTOR_OPTION_ID = 11L;
	private final static String FACTOR_OPTION_NAME = "name";
	private final static Double FACTOR_OPTION_SCORE = 123.0;
	
	private FactorOptionModelConverter converter;
	private FactorOption factorOption;
	
	private void givenFactorOption() {
		factorOption = new FactorOption(); 
		factorOption.setId(FACTOR_OPTION_ID);
		factorOption.setName(FACTOR_OPTION_NAME);
		factorOption.setScore(BigDecimal.valueOf(FACTOR_OPTION_SCORE));
	}
	
	private void givenConverter() {
		converter = new FactorOptionModelConverter();
	}
	
	@Test
	public void testNormal() {
		givenFactorOption();
		givenConverter();
		
		val model = converter.convert(factorOption);
		
		Assert.assertNotNull(model);
		Assert.assertEquals(FACTOR_OPTION_ID, model.getId());
		Assert.assertEquals(FACTOR_OPTION_NAME, model.getName());
		Assert.assertEquals(FACTOR_OPTION_SCORE, model.getScore());
	}
}
