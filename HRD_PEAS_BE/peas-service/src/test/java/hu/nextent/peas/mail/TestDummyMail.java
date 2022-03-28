package hu.nextent.peas.mail;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

@ActiveProfiles("test")
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { TestDummyMailConfig.class }, loader = AnnotationConfigContextLoader.class)
@DirtiesContext
public class TestDummyMail {

	@Autowired
    public JavaMailSender emailSender;
	
	
	public void sendSimpleMessage(String to, String subject, String text) throws MessagingException {
		String timeStamp = new SimpleDateFormat("yyyymmdd_hh-mm-ss").format(new Date());
		
		MimeMessage message = emailSender.createMimeMessage();
        message.setSubject(subject + " " + timeStamp);
        message.setContent(text, "text/html; charset=utf-8");
        message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to, false));
        message.setSentDate(new Date());
        message.setHeader("XPriority", "1");
        
        emailSender.send(message);
		
        /*
		SimpleMailMessage message = new SimpleMailMessage(); 
		message.setFrom("peas_demo@nextent.hu");
		message.setTo(to); 
		message.setSubject(subject); 
		message.setText(text);
		emailSender.send(message);
		*/
	}
	
	@Test
	public void testSimpleMail() throws MessagingException {
		sendSimpleMessage("peter.tamas@nextent.hu", "test peas mail", "test peas mail");
	}
}
