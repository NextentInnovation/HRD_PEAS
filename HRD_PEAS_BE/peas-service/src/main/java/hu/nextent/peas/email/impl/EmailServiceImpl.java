package hu.nextent.peas.email.impl;

import java.util.Date;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import hu.nextent.peas.cache.ServiceCaches;
import hu.nextent.peas.constant.ServiceConstant;
import hu.nextent.peas.email.EmailService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class EmailServiceImpl implements EmailService {

	@Autowired
    private JavaMailSender emailSender;
	
	@Autowired
    private ServiceCaches serviceCaches;
	
	@Override
	public void sendEmail(
			String from, 
			String to, 
			String cc, 
			String bcc, 
			String subject, 
			String body
		) 
	throws MessagingException  
	{
		
		if (!isSystemEnable()) {
			log.debug("Send Email system disable");
		}
		
		try {
			MimeMessage message = emailSender.createMimeMessage();
	        message.setSubject(subject);
	        message.setContent(body, "text/html; charset=utf-8");
	        message.setFrom(new InternetAddress(from));
	        message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to, false));
	        if (!StringUtils.isEmpty(bcc)) {
	        	message.setRecipients(Message.RecipientType.BCC, InternetAddress.parse(bcc, false));
	        }
	        if (!StringUtils.isEmpty(cc)) {
	        	message.setRecipients(Message.RecipientType.CC, InternetAddress.parse(cc, false));
	        }
	        message.setSentDate(new Date());
	        message.setHeader("XPriority", "1");
			        
			emailSender.send(message);
		} catch (Exception e) {
			log.error("send email error:", e);
			throw e;
		}
	}

	private Boolean isSystemEnable() {
		return serviceCaches.getSystemParam(ServiceConstant.EMAIL_SYSTEM_ENABLE, Boolean.class);
	}
	
}
