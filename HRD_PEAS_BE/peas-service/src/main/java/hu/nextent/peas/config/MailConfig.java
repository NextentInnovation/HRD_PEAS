package hu.nextent.peas.config;

import javax.mail.Session;
import javax.naming.NamingException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.PropertySource;
import org.springframework.jndi.JndiTemplate;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import hu.nextent.peas.utils.MockMailSender;
import lombok.extern.slf4j.Slf4j;

@Configuration
@PropertySource("classpath:application.properties")
@ComponentScan(
		excludeFilters = 
				@ComponentScan.Filter(type = FilterType.ANNOTATION, classes = Configuration.class)
		)
@Slf4j
public class MailConfig {

	private static final String MAIL_JNDI_NAME = "mail.jndi-name";
	
	@Value("${" + MAIL_JNDI_NAME + "}")
	private String mailJndiName;
	
	private Session getMailSession() throws NamingException {
        JndiTemplate template = new JndiTemplate();
        Session session = null;
        try {
            session = (Session) template.lookup(mailJndiName);
        } catch (NamingException e) {
            log.error("getMailSession error in jndi name", e);
            throw e;
        }
        return session;
    }
	
	@Bean
    public JavaMailSender javaMailService() {
		try {
			JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
			mailSender.setSession(getMailSession());
			return mailSender;
		} catch (NamingException e) {
			log.warn("Mocked Mail Service");
			return new MockMailSender();
		}
    }
}
