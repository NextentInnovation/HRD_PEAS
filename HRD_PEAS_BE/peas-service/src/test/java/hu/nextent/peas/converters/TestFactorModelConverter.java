package hu.nextent.peas.converters;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.core.convert.ConversionService;

import hu.nextent.peas.jpa.entity.Factor;
import hu.nextent.peas.jpa.entity.FactorOption;
import hu.nextent.peas.model.FactorOptionModel;
import lombok.val;

@RunWith(MockitoJUnitRunner.class)
public class TestFactorModelConverter {

	private final static Long FACTOR_OPTION_ID = 11L;
	private final static String FACTOR_OPTION_NAME = "FACTOR_OPTION name";
	private final static Double FACTOR_OPTION_SCORE = 123.0;
	
	private final static Long FACTOR_ID = 1L;
	private final static String FACTOR_NAME = "FACTOR name";
	
	@Mock
	private ConversionService conversionService;
	
	private FactorModelConverter converter;
	private FactorOption factorOption;
	private Factor factor;
	
	private void givenConversionService() {
		FactorOptionModelConverter factorOptionConverter = new FactorOptionModelConverter();
		Mockito
			.when(conversionService.convert(Mockito.any(FactorOption.class), Mockito.eq(FactorOptionModel.class)))
			.thenAnswer(t -> factorOptionConverter.convert((FactorOption)t.getArgument(0)));
	}
	
	private void givenFactorOption() {
		factorOption = new FactorOption(); 
		factorOption.setId(FACTOR_OPTION_ID);
		factorOption.setName(FACTOR_OPTION_NAME);
		factorOption.setScore(BigDecimal.valueOf(FACTOR_OPTION_SCORE));
	}
	
	private void givenFactor() {
		factor = new Factor();
		factor.setId(FACTOR_ID);
		factor.setName(FACTOR_NAME);
		
		List<FactorOption> options = Arrays.asList(factorOption);
		factor.setFactorOptions(options);
	}
	
	private void givenConverter() {
		converter = new FactorModelConverter();
		converter.conversionService = conversionService;
	}
	
	@Test
	public void testFactorModelConverter() {
		givenConversionService();
		givenFactorOption();
		givenFactor();
		givenConverter();
		
		val model = converter.convert(factor);
		
		Assert.assertNotNull(model);
		Assert.assertEquals(FACTOR_ID, model.getId());
		Assert.assertEquals(FACTOR_NAME, model.getName());
		
		val optionModel = model.getOptions().get(0);
		Assert.assertEquals(FACTOR_OPTION_ID, optionModel.getId());
		Assert.assertEquals(FACTOR_OPTION_NAME, optionModel.getName());
		Assert.assertEquals(FACTOR_OPTION_SCORE, optionModel.getScore());

	}
}
