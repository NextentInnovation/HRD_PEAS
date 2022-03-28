package hu.nextent.peas.converters;

import java.math.BigDecimal;

import org.junit.Assert;
import org.junit.Test;

import hu.nextent.peas.jpa.entity.Difficulty;
import lombok.val;

public class TestDifficultyModelConverter {

	private static Long ID = 1L;
	private static String NAME = "name";
	private static String DESCRIPTION = "description";
	private static Double MULTIPLIER = 123.0;
	
	private DifficultyModelConverter converter;
	private Difficulty difficulty;
	
	
	private void givenConverter() {
		converter = new DifficultyModelConverter();
	}
	
	private void givenDifficulty() {
		difficulty = new Difficulty();
		difficulty.setId(ID);
		difficulty.setName(NAME);
		difficulty.setDescription(DESCRIPTION);
		difficulty.setMultiplier(BigDecimal.valueOf(MULTIPLIER));
	}
	
	@Test
	public void testNormal() {
		givenConverter();
		givenDifficulty();
		val model = converter.convert(difficulty);
		Assert.assertEquals(ID, model.getId());
		Assert.assertEquals(NAME, model.getName());
		Assert.assertEquals(DESCRIPTION, model.getDescription());
		Assert.assertEquals(MULTIPLIER, model.getMultiplier());
	}
	
}
