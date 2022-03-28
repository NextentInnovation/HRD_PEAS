package hu.nextent.peas.converters;

import org.junit.Assert;
import org.junit.Test;

import hu.nextent.peas.jpa.entity.CompanyVirtue;
import lombok.val;

public class TestCompanyVirtueModelConverter {

	private static Long ID = 1L;
	private static String VALUE = "value";
	
	private CompanyVirtueModelConverter converter = null;
	private CompanyVirtue companyVirtue;
	
	private void givenConverter() {
		converter = new CompanyVirtueModelConverter();
	}
	
	private void givenCompanyVirtue() {
		companyVirtue = new CompanyVirtue();
		companyVirtue.setId(ID);
		companyVirtue.setValue(VALUE);
	}
	
	@Test
	public void testNormal() {
		givenConverter();
		givenCompanyVirtue();
		val model = converter.convert(companyVirtue);
		Assert.assertEquals(ID, model.getId());
		Assert.assertEquals(VALUE, model.getValue());
	}
}
