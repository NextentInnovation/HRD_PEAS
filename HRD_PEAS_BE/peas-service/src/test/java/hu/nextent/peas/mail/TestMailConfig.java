package hu.nextent.peas.mail;

import java.util.Properties;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Profile;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import hu.nextent.peas.config.WebMvcConfiguration;
import lombok.extern.slf4j.Slf4j;

@Profile("test")
@Configuration
@ComponentScan(
		excludeFilters = 
				@ComponentScan.Filter(type = FilterType.ANNOTATION, classes = Configuration.class)
		)
@Import(
		{
		WebMvcConfiguration.class,
		}
		)
@Slf4j
public class TestMailConfig {

	private static final String MAIL_HOST = "smtp.gmail.com";
	private static final Integer MAIL_PORT = 587;
	// Your account
	private static final String MAIL_USERNAME = "smtp@nextent.hu";
	private static final String MAIL_PASSWORD = "bzrqabemlxeazffq";
	private static final Boolean MAIL_SMTP_AUTH = true;
	private static final Boolean MAIL_SMTP_STARTTLS_ENABLE= true;
	
	@Bean
    public JavaMailSender javaMailService() {
		log.debug("Configure javaMailService");
		
		try {
		    JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
		    mailSender.setHost(MAIL_HOST);
		    mailSender.setPort(MAIL_PORT);
		     
		    mailSender.setUsername(MAIL_USERNAME);
		    mailSender.setPassword(MAIL_PASSWORD);
		     
		    Properties props = mailSender.getJavaMailProperties();
		    props.put("mail.transport.protocol", "smtp");
		    props.put("mail.smtp.auth", MAIL_SMTP_AUTH);
		    props.put("mail.smtp.starttls.enable", MAIL_SMTP_STARTTLS_ENABLE);
		    props.put("mail.debug", true);
		    
		    return mailSender;
		} catch (Exception e) {
			log.error("Can't Configure JavaMailSender", e);
			throw e;
		}
    }
}
