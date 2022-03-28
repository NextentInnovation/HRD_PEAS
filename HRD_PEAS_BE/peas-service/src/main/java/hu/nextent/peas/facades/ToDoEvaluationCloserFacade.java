package hu.nextent.peas.facades;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import hu.nextent.peas.jpa.dao.NotificationRepository;
import hu.nextent.peas.jpa.dao.ToDoRepository;
import hu.nextent.peas.jpa.entity.Evaluation;
import hu.nextent.peas.jpa.entity.Notification;
import hu.nextent.peas.jpa.entity.NotificationStatusEnum;
import hu.nextent.peas.jpa.entity.NotificationTypeEnum;
import hu.nextent.peas.jpa.entity.Rating;
import hu.nextent.peas.jpa.entity.ToDo;
import hu.nextent.peas.jpa.entity.ToDoStatusEnum;
import hu.nextent.peas.jpa.entity.ToDoTypeEnum;

@Service
@Transactional
public class ToDoEvaluationCloserFacade {

	
	@Autowired
	private ToDoRepository toDoRepository;
	
	@Autowired
	private NotificationRepository notificationRepository;
	
	/**
	 * Értékeléshez tartozó ToDo lezárása
	 * @param evaluation
	 */
	public void apply(Evaluation evaluation) {
		List<ToDo> todos = toDoRepository.findAllByToUserAndEvaluationAndToDoType(evaluation.getEvaluator(), evaluation, ToDoTypeEnum.EVALUATION);
		OffsetDateTime now = OffsetDateTime.now(ZoneOffset.UTC);

		
		todos.stream()
			.forEach(todo -> {
				todo.setStatus(getToDoStatusByEvaluation(evaluation,todo));
				todo.setDone(
						Arrays
							.asList(evaluation.getEvaluatedEndDate(), todo.getDone())
							.stream()
							.filter(p -> p != null)
							.findFirst().orElse(now)
						);
			});
		toDoRepository.saveAll(todos);
		
		// Lezárandó notifikációk:
		// - EVALUATION_DEADLINE
		// - EVALUATION_START
		Collection<Notification> notifications = notificationRepository.findAllByEvaluationAndNotificationType(evaluation,NotificationTypeEnum.EVALUATION_DEADLINE);
		notifications.stream().forEach(
				n -> n.setStatus(getNotificationStatusByEvaluation(evaluation,n))
				);
		notificationRepository.saveAll(notifications);

		notifications = notificationRepository.findAllByEvaluationAndNotificationType(evaluation,NotificationTypeEnum.EVALUATION_START);
		notifications.stream().forEach(
				n -> n.setStatus(getNotificationStatusByEvaluation(evaluation,n))
				);
		notificationRepository.saveAll(notifications);
	}
	
	private ToDoStatusEnum getToDoStatusByEvaluation(Evaluation evaluation, ToDo todo) {
		ToDoStatusEnum todoStatus = todo.getStatus();
		switch (evaluation.getStatus()) {
			case BEFORE_EVALUATING: case EVALUATING:
				todoStatus = todo.getStatus();
				break;
			case EVALUATED: case CLOSED: case DELETED: 
				todoStatus = ToDoStatusEnum.CLOSE;
				break;
			case EXPIRED:
				todoStatus = ToDoStatusEnum.EXPIRED;
				break;
		}
		return todoStatus;
	}
	
	private NotificationStatusEnum getNotificationStatusByEvaluation(Evaluation evaluation, Notification notification) {
		NotificationStatusEnum notificationStatus = notification.getStatus();
		if (NotificationStatusEnum.INFORMATION == notificationStatus) {
			return notificationStatus;
		}
		switch (evaluation.getStatus()) {
			case BEFORE_EVALUATING: case EVALUATING:
				notificationStatus = notification.getStatus();
				break;
			case EVALUATED: case CLOSED: case DELETED: 
				notificationStatus = NotificationStatusEnum.CLOSE;
				break;
			case EXPIRED:
				notificationStatus = NotificationStatusEnum.EXPIRED;
				break;
		}
		return notificationStatus;
	}
	
	
	public void apply(Rating rating) {
		ToDoStatusEnum toDoStatus = getToDoStatusByRating(rating);
		List<ToDo> todos = toDoRepository.findAllByRatingAndToDoType(rating, ToDoTypeEnum.RATING);
		OffsetDateTime now = OffsetDateTime.now(ZoneOffset.UTC);
		todos.stream()
			.forEach(todo -> {
				todo.setStatus(toDoStatus);
				todo.setDone(todo.getDone() == null ? now : todo.getDone());
			});
		toDoRepository.saveAll(todos);
		
		// Lezárandó notifikációk:
		// - RATING_DEADLINE
		// - RATING_START
		List<Notification> notifications = notificationRepository.findAllByRatingAndNotificationType(rating, NotificationTypeEnum.RATING_DEADLINE);
		notifications.stream().forEach(n -> n.setStatus(getNotificationStatusByRating(rating,n)));
		notificationRepository.saveAll(notifications);

		notifications = notificationRepository.findAllByRatingAndNotificationType(rating, NotificationTypeEnum.RATING_START);
		notifications.stream().forEach(n -> n.setStatus(getNotificationStatusByRating(rating,n)));
		notificationRepository.saveAll(notifications);
	}
	
	private ToDoStatusEnum getToDoStatusByRating(Rating rating) {
		ToDoStatusEnum todoStatus;
		switch (rating.getStatus()) {
			case OPEN: case EVALUATED: case CLOSE:
				todoStatus = ToDoStatusEnum.CLOSE;
				break;
			case EXPIRED:
				todoStatus = ToDoStatusEnum.EXPIRED;
				break;
			default:
				todoStatus = ToDoStatusEnum.CLOSE;
				break;
		}
		return todoStatus;
	}
	
	private NotificationStatusEnum getNotificationStatusByRating(Rating rating, Notification notification) {
		NotificationStatusEnum notificationStatus = notification.getStatus();
		if (NotificationStatusEnum.INFORMATION == notificationStatus) {
			return notificationStatus;
		}
		
		switch (rating.getStatus()) {
			case OPEN:
				notificationStatus = notification.getStatus();
				break;
			case EVALUATED: case CLOSE:
				notificationStatus = NotificationStatusEnum.CLOSE;
				break;
			case EXPIRED:
				notificationStatus = NotificationStatusEnum.EXPIRED;
				break;
		}
		return notificationStatus;
	}
}
