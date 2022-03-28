package hu.nextent.peas.jpa.dao;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;

import hu.nextent.peas.jpa.entity.Evaluation;
import hu.nextent.peas.jpa.entity.Notification;
import hu.nextent.peas.jpa.entity.NotificationTypeEnum;
import hu.nextent.peas.jpa.entity.Period;
import hu.nextent.peas.jpa.entity.Rating;
import hu.nextent.peas.jpa.entity.Task;
import hu.nextent.peas.jpa.entity.User;

public interface NotificationRepository 
extends DaoRepository<Notification, Long> {	

	Optional<Notification> findByToUserAndTaskAndNotificationTypeAndNotifacededDay(User toUser, Task task, NotificationTypeEnum notificationType, LocalDate notifacededDay);
	
	Optional<Notification> findByToUserAndEvaluationAndNotificationTypeAndNotifacededDay(User toUser, Evaluation evaluation, NotificationTypeEnum notificationType, LocalDate notifacededDay);

	Optional<Notification> findByToUserAndPeriodAndNotificationTypeAndNotifacededDay(User toUser, Period period, NotificationTypeEnum notificationType, LocalDate notifacededDay);

	List<Notification> findAllByToUserAndEvaluationAndNotificationType(User toUser, Evaluation evaluation, NotificationTypeEnum notificationType);

	List<Notification> findAllByToUserAndRatingAndNotificationType(User toUser, Rating rating, NotificationTypeEnum notificationType);

	Optional<Notification> findByToUserAndRatingAndNotificationTypeAndNotifacededDay(User toUser, Rating rating, NotificationTypeEnum notificationType, LocalDate notifacededDay);

	List<Notification> findAllByTaskAndNotificationType(Task task, NotificationTypeEnum notificationType);
	List<Notification> findAllByEvaluationAndNotificationType(Evaluation evaluation, NotificationTypeEnum notificationType);
	List<Notification> findAllByRatingAndNotificationType(Rating rating, NotificationTypeEnum notificationType);

	List<Notification> findAllByToUser(User toUser);
	List<Notification> findAllByToUserAndReadedIsNull(User toUser);
	
	List<Notification> findAllByPeriodAndNotificationType(Period period, NotificationTypeEnum notificationType);
	
	Long countByToUser(User toUser);
	Long countByToUserAndReadedIsNull(User toUser);
	Long countByToUserAndReadedIsNotNull(User toUser);
	
	
	@Query("SELECT n FROM Notification n WHERE n.sendedStatus = 'NEW' ORDER BY coalesce(n.modifiedDate, n.createdDate) DESC")
	List<Notification> findAllSendable(Pageable page);
	
	@Query("SELECT n FROM Notification n WHERE n.sendedStatus = 'NEW' ORDER BY coalesce(n.modifiedDate, n.createdDate) DESC")
	List<Notification> findAllSendable();

	@Query("SELECT count(n) FROM Notification n WHERE n.sendedStatus = 'NEW'")
	Long countAllSendable();
}
