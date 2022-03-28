package hu.nextent.peas.utils;

import javax.mail.internet.MimeMessage;

import org.springframework.lang.Nullable;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class MockMailSender extends JavaMailSenderImpl {

	
	static Long cnt = 0l;
	
	@Override
	protected void doSend(MimeMessage[] mimeMessages, @Nullable Object[] originalMessages) throws MailException {
		if (mimeMessages != null && mimeMessages.length > 0) {
			cnt += mimeMessages.length;
			log.debug("MockMailSender Sended message cnt: {}, all sended: {}", mimeMessages.length, cnt );
		}
	}

	public static Long getCnt() {
		return cnt;
	}

	public static void setCnt(Long cnt) {
		MockMailSender.cnt = cnt;
	}


}
