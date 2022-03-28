package hu.nextent.peas.service.other;

import java.util.Locale;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.MessageSource;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;
import org.springframework.transaction.annotation.Transactional;

import hu.nextent.peas.exceptions.ExceptionLabelConstant;
import hu.nextent.peas.service.TestServiceConfig;

@ActiveProfiles("test")
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { TestServiceConfig.class }, loader = AnnotationConfigContextLoader.class)
@Transactional
@DirtiesContext
public class TestLabelBaseMessageFormater {

	@Autowired
	@Qualifier("messageSource")
	private MessageSource messageSource;
	
	@Test
	public void testLabelDefaultLanguage() {
		String code = ExceptionLabelConstant.ERROR_NOT_FOUNDED;
		String message = applyReason(code);
		Assert.assertNotEquals(code, message);
	}
	
	
    private String applyReason(String code, Object ... args) {
    	Locale langLocale = new Locale("hu");
    	return messageSource.getMessage(code, args, code, langLocale);
    }
}
