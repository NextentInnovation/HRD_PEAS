package hu.nextent.peas.email;

import javax.mail.MessagingException;

public interface EmailService {

	public void sendEmail(
			String from,
			String to,
			String cc,
			String bcc,
			String subject,
			String body
			) throws MessagingException;
	
}
