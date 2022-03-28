package hu.nextent.peas.facades;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import hu.nextent.peas.cache.ServiceCaches;
import hu.nextent.peas.constant.NotificationLabelConstant;
import hu.nextent.peas.jpa.dao.NotificationRepository;
import hu.nextent.peas.jpa.dao.UserRepository;
import hu.nextent.peas.jpa.entity.Company;
import hu.nextent.peas.jpa.entity.Evaluation;
import hu.nextent.peas.jpa.entity.Notification;
import hu.nextent.peas.jpa.entity.NotificationAction;
import hu.nextent.peas.jpa.entity.NotificationSendStatusEnum;
import hu.nextent.peas.jpa.entity.NotificationStatusEnum;
import hu.nextent.peas.jpa.entity.NotificationTypeEnum;
import hu.nextent.peas.jpa.entity.Period;
import hu.nextent.peas.jpa.entity.Rating;
import hu.nextent.peas.jpa.entity.ReferenceTypeEnum;
import hu.nextent.peas.jpa.entity.RoleEnum;
import hu.nextent.peas.jpa.entity.Task;
import hu.nextent.peas.jpa.entity.User;
import hu.nextent.peas.utils.SpelFormater;
import lombok.val;

@Service
@Transactional
public class FactoryServiceNotification {
	
	@Autowired
	NotificationRepository notificationRepository;
	
	@Autowired
	UserRepository userRepository;
	
	@Autowired
	ServiceCaches serviceCaches;
	

	private Notification createTaskBaseNotification(
			NotificationTypeEnum notificationType, 
			Task task,
			LocalDate currentDay,
			NotificationAction notificationAction
			) {
		
		currentDay = currentDay == null ? getCurrentDay() : currentDay;
		Notification notification = 
				Notification.builder()
					.notificationType(notificationType)
					.company(task.getCompany())
					.notifacededDay(currentDay)
					.status(notificationType.getDefaultStatus())
					.subject(NotificationLabelConstant.SUBJECT_MAP.get(notificationType))
					.body(NotificationLabelConstant.BODY_MAP.get(notificationType))
					.referenceType(ReferenceTypeEnum.TASK)
					.task(task)
					.period(task.getPeriod())
					.toUser(task.getOwner())
					.sendedStatus(notificationAction.getSendable() ? NotificationSendStatusEnum.NEW: NotificationSendStatusEnum.NOT_SEND)
					.build();
		
		format(notification);
		notificationRepository.save(notification);
		
		return notification;
	}
	
	private Notification createEvaulationBaseNotification(
			NotificationTypeEnum notificationType, 
			Evaluation evaluation,
			LocalDate currentDay,
			NotificationAction notificationAction
			) {
		
		currentDay = currentDay == null ? getCurrentDay() : currentDay;
		
		Notification notification = 
				Notification.builder()
					.notificationType(notificationType)
					.status(notificationType.getDefaultStatus())
					.notifacededDay(currentDay)
					.subject(NotificationLabelConstant.SUBJECT_MAP.get(notificationType))
					.body(NotificationLabelConstant.BODY_MAP.get(notificationType))
					.referenceType(ReferenceTypeEnum.EVALUATION)
					.toUser(evaluation.getEvaluator())
					.evaluation(evaluation)
					.task(evaluation.getTask())
					.period(evaluation.getTask().getPeriod() == null ? null : evaluation.getTask().getPeriod())
					.company(evaluation.getCompany())
					.sendedStatus(notificationAction.getSendable() ? NotificationSendStatusEnum.NEW: NotificationSendStatusEnum.NOT_SEND)
					.build();
		
		format(notification);
		notificationRepository.save(notification);
		
		return notification;
	}
	
	private Notification createRatingBaseNotification(
			NotificationTypeEnum notificationType, 
			Rating rating,
			User toUser,
			LocalDate currentDay,
			NotificationAction notificationAction
			) {

		currentDay = currentDay == null ? getCurrentDay() : currentDay;

		Notification notification = 
				Notification.builder()
					.notificationType(notificationType)
					.status(notificationType.getDefaultStatus())
					.notifacededDay(currentDay)
					.subject(NotificationLabelConstant.SUBJECT_MAP.get(notificationType))
					.body(NotificationLabelConstant.BODY_MAP.get(notificationType))
					.referenceType(ReferenceTypeEnum.RATING)
					.toUser(toUser)
					.rating(rating)
					.period(rating.getPeriod())
					.company(rating.getCompany())
					.sendedStatus(notificationAction.getSendable() ? NotificationSendStatusEnum.NEW: NotificationSendStatusEnum.NOT_SEND)
					.build();
		
		format(notification);
		notificationRepository.save(notification);

		return notification;
	}
	
	private Notification createPeriodBaseNotification(
			NotificationTypeEnum notificationType, 
			Period period, 
			User toUser,
			LocalDate currentDay,
			NotificationAction notificationAction
			) {
		
		currentDay = currentDay == null ? getCurrentDay() : currentDay;
		
		Notification notification = 
				Notification.builder()
					.notificationType(notificationType)
					.status(NotificationStatusEnum.INFORMATION)
					.notifacededDay(currentDay)
					.subject(NotificationLabelConstant.SUBJECT_MAP.get(notificationType))
					.body(NotificationLabelConstant.BODY_MAP.get(notificationType))
					.referenceType(ReferenceTypeEnum.PERIOD)
					.toUser(toUser)
					.period(period)
					.company(period.getCompany())
					.sendedStatus(notificationAction.getSendable() ? NotificationSendStatusEnum.NEW: NotificationSendStatusEnum.NOT_SEND)
					.build();
		
		format(notification);
		notificationRepository.save(notification);

		return notification;
	}

	public Notification createTaskDeadlineComming(Task task, LocalDate currentDay) {
		NotificationAction notificationAction = serviceCaches.getNotificationAction(NotificationTypeEnum.TASK_DEADLINE, task.getCompany());
		if (!notificationAction.getCreateable()) {
			return null;
		}
		
		currentDay = currentDay == null ? getCurrentDay() : currentDay;
		val optNotification = notificationRepository.findByToUserAndTaskAndNotificationTypeAndNotifacededDay(task.getOwner(), task, NotificationTypeEnum.TASK_DEADLINE, currentDay);
		if (optNotification.isPresent()) {
			return optNotification.get();
		}
		
		return createTaskBaseNotification(NotificationTypeEnum.TASK_DEADLINE, task, currentDay, notificationAction);
	}
	
	public List<Notification> createEvaluationStart(Task task) {
		NotificationAction notificationAction = serviceCaches.getNotificationAction(NotificationTypeEnum.EVALUATION_START, task.getCompany());
		if (!notificationAction.getCreateable()) {
			return Collections.emptyList();
		}
		
		LocalDate currentDay = getCurrentDay();
		return task.getEvaluations()
					.stream()
					.map(evaluation -> createEvaulationBaseNotification(NotificationTypeEnum.EVALUATION_START, evaluation, currentDay, notificationAction))
					.collect(Collectors.toList());
	}
	
	public Notification createEvaluationDeadlineComming(Evaluation evaluation, LocalDate currentDay) {
		NotificationAction notificationAction = serviceCaches.getNotificationAction(NotificationTypeEnum.EVALUATION_DEADLINE, evaluation.getCompany());
		if (!notificationAction.getCreateable()) {
			return null;
		}
		currentDay = currentDay == null ? getCurrentDay() : currentDay;
		val optNotification = notificationRepository.findByToUserAndEvaluationAndNotificationTypeAndNotifacededDay(evaluation.getEvaluator(), evaluation, NotificationTypeEnum.EVALUATION_DEADLINE, currentDay);
		if (optNotification.isPresent()) {
			return optNotification.get();
		}
		return createEvaulationBaseNotification(NotificationTypeEnum.EVALUATION_DEADLINE, evaluation, currentDay, notificationAction);
	}

	public Notification createEvaluationExpired(Evaluation evaluation) {
		NotificationAction notificationAction = serviceCaches.getNotificationAction(NotificationTypeEnum.EVALUATION_EXPIRED, evaluation.getCompany());
		if (!notificationAction.getCreateable()) {
			return null;
		}
		return createEvaulationBaseNotification(NotificationTypeEnum.EVALUATION_EXPIRED, evaluation, getCurrentDay(), notificationAction);
	}
	
	public Notification createEvaluationEnd(Evaluation evaluation) {
		NotificationAction notificationAction = serviceCaches.getNotificationAction(NotificationTypeEnum.EVALUATION_END, evaluation.getCompany());
		if (!notificationAction.getCreateable()) {
			return null;
		}
		return createEvaulationBaseNotification(NotificationTypeEnum.EVALUATION_END, evaluation, getCurrentDay(), notificationAction);
	}
	
	
	public List<Notification> createRatingStart(Rating rating) {
		NotificationAction notificationAction = serviceCaches.getNotificationAction(NotificationTypeEnum.EVALUATION_END, rating.getCompany());
		if (!notificationAction.getCreateable()) {
			return Collections.emptyList();
		}
		List<Notification> notifications = new ArrayList<>();
		for (User user: Arrays.asList(rating.getUser(), rating.getLeader())) {
			notifications.add(createRatingBaseNotification(NotificationTypeEnum.RATING_START, rating, user, getCurrentDay(), notificationAction));
		}
		return notifications;
	}
	
	
	public List<Notification> createRatingDeadlineComming(Rating rating, LocalDate currentDay) {
		NotificationAction notificationAction = serviceCaches.getNotificationAction(NotificationTypeEnum.RATING_DEADLINE, rating.getCompany());
		if (!notificationAction.getCreateable()) {
			return Collections.emptyList();
		}
		LocalDate lCurrentDay = currentDay == null ? getCurrentDay() : currentDay;
		List<Notification> notifications = new ArrayList<>();
		for (User user: Arrays.asList(rating.getUser(), rating.getLeader())) {
			val optNotifications = notificationRepository.findByToUserAndRatingAndNotificationTypeAndNotifacededDay(user, rating, NotificationTypeEnum.RATING_DEADLINE, lCurrentDay);
			if (optNotifications.isPresent()) {
				notifications.add(optNotifications.get());
			} else {
				notifications.add(createRatingBaseNotification(NotificationTypeEnum.RATING_DEADLINE,rating,user,lCurrentDay, notificationAction));
			}
		}
		return notifications;
	}

	public List<Notification> createRatingExpired(Rating rating) {
		NotificationAction notificationAction = serviceCaches.getNotificationAction(NotificationTypeEnum.RATING_DEADLINE, rating.getCompany());
		if (!notificationAction.getCreateable()) {
			return Collections.emptyList();
		}
		List<Notification> notifications = new ArrayList<>();
		for (User user: Arrays.asList(rating.getUser(), rating.getLeader())) {
			notifications.add(createRatingBaseNotification(NotificationTypeEnum.RATING_EXPIRED, rating, user, getCurrentDay(), notificationAction));
		}
		return notifications;
	}
	
	public List<Notification> createRatingEnd(Rating rating) {
		NotificationAction notificationAction = serviceCaches.getNotificationAction(NotificationTypeEnum.RATING_DEADLINE, rating.getCompany());
		if (!notificationAction.getCreateable()) {
			return Collections.emptyList();
		}
		
		List<Notification> notifications = new ArrayList<>();
		for (User user: Arrays.asList(rating.getUser(), rating.getLeader())) {
			notifications.add(createRatingBaseNotification(NotificationTypeEnum.RATING_END, rating, user, getCurrentDay(), notificationAction));
		}
		
		return notifications;
	}

	private List<User> findUsersBuisnessAdminAndHr(Company company) {
		return userRepository.findAllByCompanyAndRoleEnums(company, Arrays.asList(RoleEnum.BUSINESS_ADMIN, RoleEnum.HR));
	}

	public List<Notification> createPeriodDeadlineComming(Period period) {
		NotificationAction notificationAction = serviceCaches.getNotificationAction(NotificationTypeEnum.RATING_DEADLINE, period.getCompany());
		if (!notificationAction.getCreateable()) {
			return Collections.emptyList();
		}
		
		LocalDate lCurrentDay = getCurrentDay();
		return findUsersBuisnessAdminAndHr(period.getCompany())
				.stream()
				.map(user -> createOnePeriodDeadlineComming(period, user, lCurrentDay, notificationAction))
				.collect(Collectors.toList());
	}

	private Notification createOnePeriodDeadlineComming(Period period, User user, LocalDate currentDay, NotificationAction notificationAction) {
		currentDay = currentDay == null ? getCurrentDay() : currentDay;
		val optNotification = notificationRepository.findByToUserAndPeriodAndNotificationTypeAndNotifacededDay(user, period, NotificationTypeEnum.PERIOD_DEADLINE, currentDay);
		if (optNotification.isPresent()) {
			return optNotification.get();
		}
		return createPeriodBaseNotification(NotificationTypeEnum.PERIOD_DEADLINE, period, user, currentDay, notificationAction);
	}

	public List<Notification> createPeriodActiveClosed(Period period) {
		NotificationAction notificationAction = serviceCaches.getNotificationAction(NotificationTypeEnum.RATING_DEADLINE, period.getCompany());
		if (!notificationAction.getCreateable()) {
			return Collections.emptyList();
		}
		
		LocalDate lCurrentDay = getCurrentDay();
		return findUsersBuisnessAdminAndHr(period.getCompany())
				.stream()
				.map(user -> createPeriodBaseNotification(NotificationTypeEnum.PERIOD_ACTIVE_CLOSE, period, user, lCurrentDay, notificationAction))
				.collect(Collectors.toList());
	}

	public List<Notification> createPeriodActiveted(Period period) {
		NotificationAction notificationAction = serviceCaches.getNotificationAction(NotificationTypeEnum.RATING_DEADLINE, period.getCompany());
		if (!notificationAction.getCreateable()) {
			return Collections.emptyList();
		}
		
		LocalDate lCurrentDay = getCurrentDay();
		return findUsersBuisnessAdminAndHr(period.getCompany())
				.stream()
				.map(user -> createPeriodBaseNotification(NotificationTypeEnum.PERIOD_ACTIVATED, period, user, lCurrentDay, notificationAction))
				.collect(Collectors.toList());
	}

	public List<Notification> createPeriodRatingClose(Period period) {
		NotificationAction notificationAction = serviceCaches.getNotificationAction(NotificationTypeEnum.RATING_DEADLINE, period.getCompany());
		if (!notificationAction.getCreateable()) {
			return Collections.emptyList();
		}
		
		LocalDate lCurrentDay = getCurrentDay();
		return findUsersBuisnessAdminAndHr(period.getCompany())
				.stream()
				.map(user -> createPeriodBaseNotification(NotificationTypeEnum.PERIOD_RATING_CLOSE, period, user, lCurrentDay, notificationAction))
				.collect(Collectors.toList());
	}
	
	private LocalDate getCurrentDay() {
		return OffsetDateTime.now(ZoneOffset.UTC).toLocalDate();
	}
	
	private void format(Notification notification) {
		String lang = notification.getToUser().getLanguage();
		notification.setSubject(format(lang, notification.getSubject(), notification));
		notification.setBody(format(lang, notification.getBody(), notification));
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
}
