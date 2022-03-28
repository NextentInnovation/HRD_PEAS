package hu.nextent.peas.email.impl;

import java.util.Optional;

import javax.mail.MessagingException;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;
import org.springframework.transaction.annotation.Transactional;

import hu.nextent.peas.TestConstant;
import hu.nextent.peas.cache.ServiceCaches;
import hu.nextent.peas.email.EmailService;
import hu.nextent.peas.email.impl.NotificationEmailServiceImp;
import hu.nextent.peas.jpa.dao.EmailTemplateRepository;
import hu.nextent.peas.jpa.dao.NotificationRepository;
import hu.nextent.peas.jpa.dao.UserRepository;
import hu.nextent.peas.jpa.entity.EmailTemplate;
import hu.nextent.peas.jpa.entity.Notification;
import hu.nextent.peas.jpa.entity.NotificationSendStatusEnum;
import hu.nextent.peas.jpa.entity.User;
import hu.nextent.peas.service.TestServiceConfig;
import hu.nextent.peas.utils.CompanyServiceHelper;
import hu.nextent.peas.utils.SpelFormater;
import lombok.val;

@ActiveProfiles("test")
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { TestServiceConfig.class }, loader = AnnotationConfigContextLoader.class)
@Transactional
@DirtiesContext
@Rollback
public class TestNotificationEmailServiceImp {

	@Mock
	private EmailService emailService;
	
	@Autowired
	private ServiceCaches serviceCaches;
	
	@Autowired
	private NotificationRepository notificationRepository;
	
	@Autowired 
	private CompanyServiceHelper companyServiceHelper; 
	
	@Autowired
	private EmailTemplateRepository emailTemplateRepository;
	
	@Autowired
	protected UserRepository userRepository;
	
	private NotificationEmailServiceImp tested;

	private User selectedUser;
	private Notification notification;
	
	@Before
	public void before() {
		MockitoAnnotations.initMocks(this);
	}
	
	private void givenUser(String username) {
		val userOpt = userRepository.findByUserNameAndCompany_Name(username, TestConstant.DEFAULT_COMPANY);
		selectedUser = userOpt.get();
	}
	
	private void givenNotification() {
		notification = notificationRepository.findAllByToUser(selectedUser).get(0);
	}
	
	private String format(String lang, String template, Notification notification) {
		String message = serviceCaches.getLabel(template, lang);
		String formated = SpelFormater.format(
				message, 
				notification, 
				serviceCaches.labelMapProxy(lang)
				);
		return formated;
	}
	
	private void prepareNotificationIfNotGenerated() {
		if (notification.getNeedGenerate()) {
			String lang = selectedUser.getLanguage();
			notification.setSubject(format(lang, notification.getSubject(), notification));
			notification.setBody(format(lang, notification.getBody(), notification));
		}
	}
	
	private void givenImpl() {
		tested = new NotificationEmailServiceImp();
		tested.emailService = emailService;
		tested.serviceCaches = serviceCaches;
		tested.notificationRepository = notificationRepository;
		tested.companyServiceHelper = companyServiceHelper;
		tested.emailTemplateRepository = emailTemplateRepository;
		
		tested.notification = notification;
		tested.needReload = false;
	}
	
	@Test
	public void testPrepare() {
		givenUser(TestConstant.USER_NORMAL);
		givenNotification();
		prepareNotificationIfNotGenerated();
		givenImpl();
		
		Assert.assertNotNull(notification);
		
	}
	
	
	@Test
	public void clear() {
		//
	}
	
	@Test
	public void reloadIfNeeded(){
		givenUser(TestConstant.USER_NORMAL);
		givenNotification();
		prepareNotificationIfNotGenerated();
		givenImpl();
		
		tested.needReload = true;
		
		tested.reloadIfNeeded();
		
		// Itt csak ayt várom el, hogy lefusson
	}
	
	@Test
	public void beforeSend(){
		givenUser(TestConstant.USER_NORMAL);
		givenNotification();
		prepareNotificationIfNotGenerated();
		givenImpl();
		
		tested.beforeSend();
		
		Assert.assertEquals(NotificationSendStatusEnum.BEFORE_SEND, notification.getSendedStatus());
	}
	
	@Test
	public void getLanguage(){
		givenUser(TestConstant.USER_NORMAL);
		givenNotification();
		prepareNotificationIfNotGenerated();
		givenImpl();

		tested.getLanguage();
		
		Assert.assertEquals(notification.getToUser().getLanguage(), tested.language);
	}
	
	@Test
	public void loadTemplate(){
		givenUser(TestConstant.USER_NORMAL);
		givenNotification();
		prepareNotificationIfNotGenerated();
		givenImpl();
		tested.getLanguage();
		
		tested.loadTemplate();
		
		Optional<EmailTemplate> templateBase = emailTemplateRepository.findByCodeAndLanguage("notification", notification.getToUser().getLanguage());
		Optional<EmailTemplate> template = emailTemplateRepository.findByCodeAndLanguage("notification" + notification.getNotificationType().name(), notification.getToUser().getLanguage());
		String subject = template.orElse(templateBase.get()).getSubject();
		String body = template.orElse(templateBase.get()).getContent();
		Assert.assertEquals(subject, tested.subject);
		Assert.assertEquals(body, tested.body);
	}
	
	@Test
	public void generateEmail(){
		givenUser(TestConstant.USER_NORMAL);
		givenNotification();
		prepareNotificationIfNotGenerated();
		givenImpl();
		tested.getLanguage();
		tested.loadTemplate();
		
		tested.generateEmail();
		
		// elvárom, hogy az összes {{xx}} elem megszünt
		Assert.assertFalse(tested.subject.contains("{{"));
		Assert.assertFalse(tested.subject.contains("}}"));
		Assert.assertFalse(tested.body.contains("{{"));
		Assert.assertFalse(tested.body.contains("}}"));
	}
	
	@Test
	public void sendEmail() throws MessagingException{
		givenUser(TestConstant.USER_NORMAL);
		givenNotification();
		prepareNotificationIfNotGenerated();
		givenImpl();
		tested.loadMailHeader();
		tested.getLanguage();
		tested.loadTemplate();
		
		tested.sendEmail();
		
		
		Mockito.verify(emailService, Mockito.times(1)).sendEmail(
				Mockito.anyString(), 
				Mockito.anyString(), 
				Mockito.anyString(),
				Mockito.anyString(),
				Mockito.anyString(),
				Mockito.anyString()
				);
		
	}
	
	@Test
	public void afterNotificationSendSetError(){
		// Most nem
	}
	
	@Test
	public void afterNotificationSendSetOk(){
		// Most nem
	}
	
	
}
