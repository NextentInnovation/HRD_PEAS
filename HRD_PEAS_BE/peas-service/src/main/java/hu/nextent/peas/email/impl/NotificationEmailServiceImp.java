package hu.nextent.peas.email.impl;

import java.util.Map;
import java.util.Optional;

import javax.annotation.Nullable;
import javax.mail.MessagingException;
import javax.validation.constraints.NotNull;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import hu.nextent.peas.cache.ServiceCaches;
import hu.nextent.peas.email.EmailService;
import hu.nextent.peas.email.NotificationEmailService;
import hu.nextent.peas.jpa.dao.EmailTemplateRepository;
import hu.nextent.peas.jpa.dao.NotificationRepository;
import hu.nextent.peas.jpa.entity.EmailTemplate;
import hu.nextent.peas.jpa.entity.Notification;
import hu.nextent.peas.jpa.entity.NotificationSendStatusEnum;
import hu.nextent.peas.utils.CompanyServiceHelper;
import hu.nextent.peas.utils.SpelFormater;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@Transactional
public class NotificationEmailServiceImp implements NotificationEmailService {

	@Autowired
	EmailService emailService;
	
	@Autowired
    ServiceCaches serviceCaches;
	
	@Autowired
	NotificationRepository notificationRepository;
	
	@Autowired 
	CompanyServiceHelper companyServiceHelper; 
	
	@Autowired
	EmailTemplateRepository emailTemplateRepository;
	
	Boolean needReload = false;
	Notification notification;
	String defaultLanguage;
	String language;
	String subject;
	String body;
	String from;
	String to;
	String bcc;
	String cc;
	
	@Override
	public void sendNotificationEmail(
			@NotNull Notification notification,
			@Nullable String defaultLanguage,
			@Nullable Boolean reloadNotification
			) {
		log.debug("start sendNotificationEmail");
		clear();
		this.needReload = reloadNotification;
		this.notification = notification;
		this.defaultLanguage = defaultLanguage;
		Long companyId = notification.getCompany().getId();
		reloadIfNeeded();
		beforeSend();
		loadMailHeader();
		if (companyServiceHelper.isEmailEnable(companyId)) {
			try {
				getLanguage();
				generateNotificationTextIfNeeded();
				loadTemplate();
				if (!StringUtils.isEmpty(subject) || !StringUtils.isEmpty(body)) {
					generateEmail(); 
					sendEmail();
				}
			} catch (Exception e) {
				afterNotificationSendSetError(e);
				clear();
				log.warn("end sendNotificationEmail", e);
				return;
			}
		} else {
			log.debug("Email Send Disable");
		}
		afterNotificationSendSetOk();
		clear();
		log.debug("end sendNotificationEmail");
	}

	void clear() {
		needReload = null;
		defaultLanguage = null;
		language = null;
		notification = null;
		subject = null;
		body = null;
	}
	
	void reloadIfNeeded() {
		if (needReload) {
			log.debug("reload notification, id: {}", notification.getId());
			notification = notificationRepository.getOne(notification.getId());
		}
	}
	
	void beforeSend() {
		log.debug("prepared notification, id: {}", notification.getId());
		if (!notification.getSendedStatus().equals(NotificationSendStatusEnum.BEFORE_SEND)) {
			notification.setSendedStatus(NotificationSendStatusEnum.BEFORE_SEND);
		}
		notification.setSendedError(null);
		notificationRepository.saveAndFlush(notification);
	}

	void loadMailHeader() {
		Long companyId = notification.getCompany().getId();
		boolean testEmail = companyServiceHelper.isEmailTest(companyId);
		to = null;
		if (testEmail) {
			to = companyServiceHelper.getEmailTestTo(companyId);
			log.debug("Test Email Reciver: {}", to);
		} else {
			to = notification.getToUser().getEmail();
		}
		
		if (StringUtils.isEmpty(to)) {
			log.error("send email, id: {}, not founded message To", notification.getId());
			throw new RuntimeException("Not Founded Message To");
		}

		from = companyServiceHelper.getEmailFrom(companyId);
		bcc = companyServiceHelper.getEmailBcc(companyId);
		cc = companyServiceHelper.getEmailCc(companyId);
		
	}
	
	void getLanguage() {
		language = notification.getToUser().getLanguage();
		if (StringUtils.isEmpty(language)) {
			language = companyServiceHelper.getDeaultLanguage();
		}
		if (StringUtils.isEmpty(language)) {
			language = defaultLanguage;
		}
		if (StringUtils.isEmpty(language)) {
			log.error("generate language, id: {}, Language Not Founded", notification.getId());
			throw new RuntimeException("Language Not Founded");
		}
		log.debug("generate language, id: {}, language> {}", notification.getId(), language);
	}	
	
	void loadTemplate() {
		Optional<EmailTemplate> template = emailTemplateRepository.findByCodeAndLanguage("notification" + notification.getNotificationType().name(), language);
		template = template.isPresent() ? template : emailTemplateRepository.findByCodeAndLanguage("notification", language);
		if (!template.isPresent()) {
			log.error("loadTemplate, id: {}, Template Not Founded", notification.getId());
		}
		template.orElseThrow(() -> new RuntimeException("Template Not Founded") );
		subject = template.get().getSubject();
		body = template.get().getContent();
	}
	
	void generateEmail() {
		try {
			Map<String, String> labelMap = serviceCaches.labelMapProxy(language);
			subject = SpelFormater.format(subject, notification, labelMap);
			body = SpelFormater.format(body, notification, labelMap);
			log.debug("generate email texts, id: {}", notification.getId());
		} catch (Exception e) {
			log.error("generate email texts, id: {}, error: {}", notification.getId(), e.getMessage());
			throw e;
		}
	}
	
	void sendEmail() throws MessagingException {
		try {
			emailService.sendEmail(from, to, cc, bcc, subject, body);
		} catch (Exception e) {
			log.error("send email, id: {}, problem with send: {}", notification.getId(), e.getMessage());
			throw e;
		}
	}

	void afterNotificationSendSetError(Exception e) {
		notification.setSendedStatus(NotificationSendStatusEnum.ERROR);
		notification.setSendedError(StringUtils.right(e.getMessage(),1000));
		notificationRepository.saveAndFlush(notification);
	}
	
	void afterNotificationSendSetOk() {
		notification.setSendedStatus(NotificationSendStatusEnum.SEND);
		notification.setSendedError(null);
		notificationRepository.saveAndFlush(notification);
	}

	
	void generateNotificationTextIfNeeded() {
		if (notification.getNeedGenerate()) {
			try {
				Map<String, String> labelMap = serviceCaches.labelMapProxy(language);
				notification.setSubject(SpelFormater.format(notification.getSubject(), notification, labelMap));
				notification.setBody(SpelFormater.format(notification.getBody(), notification, labelMap));
				notification.setNeedGenerate(false);
				notificationRepository.saveAndFlush(notification);
				log.debug("generate notification subject and body, id: {}", notification.getId());
			} catch (Exception e) {
				log.error("generate notification subject and body, id: {}, error: {}", notification.getId(), e.getMessage());
				throw e;
			}
		}
	}

}
