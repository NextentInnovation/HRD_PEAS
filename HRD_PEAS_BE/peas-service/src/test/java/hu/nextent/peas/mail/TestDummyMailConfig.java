package hu.nextent.peas.mail;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.Profile;
import org.springframework.mail.javamail.JavaMailSender;

import hu.nextent.peas.utils.MockMailSender;

@Profile("test")
@Configuration
@ComponentScan(
		excludeFilters = 
				@ComponentScan.Filter(type = FilterType.ANNOTATION, classes = Configuration.class)
		)
public class TestDummyMailConfig {
	
	

	@Bean
    public JavaMailSender javaMailService() {
    	MockMailSender mockMailSender = new MockMailSender();
		return mockMailSender;
    }
	
}
