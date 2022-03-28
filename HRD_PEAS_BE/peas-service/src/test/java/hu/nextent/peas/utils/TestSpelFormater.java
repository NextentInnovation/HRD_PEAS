package hu.nextent.peas.utils;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

import org.junit.Assert;
import org.junit.Test;

import hu.nextent.peas.jpa.entity.Evaluation;
import hu.nextent.peas.jpa.entity.EvaluationStatusEnum;
import hu.nextent.peas.jpa.entity.Notification;
import hu.nextent.peas.jpa.entity.NotificationStatusEnum;
import hu.nextent.peas.jpa.entity.NotificationTypeEnum;
import hu.nextent.peas.jpa.entity.Period;
import hu.nextent.peas.jpa.entity.PeriodStatusEnum;
import hu.nextent.peas.jpa.entity.Rating;
import hu.nextent.peas.jpa.entity.RatingStatusEnum;
import hu.nextent.peas.jpa.entity.ReferenceTypeEnum;
import hu.nextent.peas.jpa.entity.Task;
import hu.nextent.peas.jpa.entity.TaskStatusEnum;
import hu.nextent.peas.jpa.entity.TaskTypeEnum;
import hu.nextent.peas.jpa.entity.ToDo;
import hu.nextent.peas.jpa.entity.ToDoStatusEnum;
import hu.nextent.peas.jpa.entity.ToDoTypeEnum;
import hu.nextent.peas.jpa.entity.User;


/**
 * 
 *	Nem telejs körű teszt, csak azokat tesztelem, amik előfordulhatnak
 */
public class TestSpelFormater {
	
	Period period;
	User user;
	Task task;
	Evaluation evaluation;
	Rating rating;
	ToDo todo;
	Notification notification;
	
	@Test
	public void testToDoFormater() {
		OffsetDateTime now = OffsetDateTime.now();
		givenPeriod(now);
		givenUser();
		givenTask(now);
		givenEvaluation(now);
		givenRating(now);
		givenToDo(now);
		
		String expression;
		String value;
		
		expression = "{{evaluation.task.owner.userName}}";
		value = SpelFormater.format(expression, todo);
		Assert.assertEquals(expression, user.getUserName(), value);
		
		expression = "{{evaluation.task.name}}";
		value = SpelFormater.format(expression, todo);
		Assert.assertEquals(expression, task.getName(), value);
		
		expression = "{{rating.user.userName}}";
		value = SpelFormater.format(expression, todo);
		Assert.assertEquals(expression, user.getUserName(), value);
		
		expression = "{{period.name}}";
		value = SpelFormater.format(expression, todo);
		Assert.assertEquals(expression, period.getName(), value);

	}
	
	private void givenPeriod(OffsetDateTime now) {
		period = Period.builder()
				.status(PeriodStatusEnum.OPEN)
				.name("Periódus")
				.startDate(now)
				.endDate(now)
				.ratingEndDate(now)
				.build();
	}
	
	private void givenUser() {
		user = User.builder()
				.userName("teszt.elek")
				.fullName("Teszt Elek")
				.email("teszt.elek@com")
				.initial("TE")
				.organization("org")
				.build()
				;
	}
	
	private void givenTask(OffsetDateTime now) {
		task = Task.builder()
				.status(TaskStatusEnum.UNDER_EVALUATION)
				.taskType(TaskTypeEnum.NORMAL)
				.name("Task")
				.description("Task description")
				.deadline(now)
				.evaluaterCount(10)
				.evaluatedCount(5)
				.evaluationPercentage(BigDecimal.valueOf(10.0))
				.score(BigDecimal.valueOf(10.0))
				.owner(user)
				.period(period)
				.build();
	}
	
	private void givenEvaluation(OffsetDateTime now) {
		evaluation = Evaluation.builder()
				.status(EvaluationStatusEnum.BEFORE_EVALUATING)
				.score(BigDecimal.valueOf(10.0))
				.evaluatedStartDate(now)
				.deadline(now)
				.note("Note")
				.evaluator(user)
				.task(task)
				.build();
	}
	
	private void givenRating(OffsetDateTime now) {
		rating = Rating.builder()
				.status(RatingStatusEnum.OPEN)
				.normalTaskScore(BigDecimal.valueOf(10.0))
				.automaticTaskScore(BigDecimal.valueOf(10.0))
				.periodScore(BigDecimal.valueOf(10.0))
				.textualEvaluation("String: textualEvaluation")
				.gradeRecommendation("String: gradeRecommendation")
				.cooperation(true)
				.deadline(now)
				.ratingStartDate(now)
				.user(user)
				.leader(user)
				.period(period)
				.build();
	}
	
	private void givenToDo(OffsetDateTime now) {
		todo = ToDo
				.builder()
				.toDoType(ToDoTypeEnum.EVALUATION)
				.status(ToDoStatusEnum.OPEN)
				.deadline(now)
				.done(now)
				.referenceType(ReferenceTypeEnum.TASK)
				.toUser(user)
				.task(task)
				.evaluation(evaluation)
				.rating(rating)
				.period(period)
				.build();
	}
	
	
	@Test
	public void testNotificationFormater() {
		OffsetDateTime now = OffsetDateTime.now();
		givenPeriod(now);
		givenUser();
		givenTask(now);
		givenEvaluation(now);
		givenRating(now);
		givenNotification(now);
		
		String expression;
		String value;

		expression = "{{T(java.time.format.DateTimeFormatter).ISO_LOCAL_DATE_TIME.format(task.deadline)}}";
		value = SpelFormater.format(expression, notification);
		Assert.assertEquals(expression, java.time.format.DateTimeFormatter.ISO_LOCAL_DATE_TIME.format(now), value);
		System.out.println(expression + " -> " + value);

		expression = "{{task.name}}";
		value = SpelFormater.format(expression, notification);
		Assert.assertEquals(expression, task.getName(), value);
		System.out.println(expression + " -> " + value);

		expression = "{{T(java.time.format.DateTimeFormatter).ISO_LOCAL_DATE_TIME.format(evaluation.deadline)}}";
		value = SpelFormater.format(expression, notification);
		Assert.assertEquals(expression, java.time.format.DateTimeFormatter.ISO_LOCAL_DATE_TIME.format(now), value);
		System.out.println(expression + " -> " + value);

		expression = "{{T(java.time.format.DateTimeFormatter).ofPattern(\"yyyy.MM.dd. hh:mm\").format(evaluation.deadline)}}";
		value = SpelFormater.format(expression, notification);
		Assert.assertEquals(expression, java.time.format.DateTimeFormatter.ofPattern("yyyy.MM.dd. hh:mm").format(now), value);
		System.out.println(expression + " -> " + value);

		expression = "{{#HU_DATETIME.format(evaluation.deadline)}}";
		value = SpelFormater.format(expression, notification);
		Assert.assertEquals(expression, SpelConstant.HU_DATETIME.format(now), value);
		System.out.println(expression + " -> " + value);
		
		expression = "{{#HU_DATETIME.format(null)}}";
		value = SpelFormater.format(expression, notification);
		Assert.assertEquals(expression, SpelConstant.HU_DATETIME.format(null), value);
		System.out.println(expression + " -> " + value);


		
		expression = "{{evaluation.task.owner.userName}}";
		value = SpelFormater.format(expression, notification);
		Assert.assertEquals(expression, user.getUserName(), value);
		System.out.println(expression + " -> " + value);

		expression = "{{evaluation.task.name}}";
		value = SpelFormater.format(expression, notification);
		Assert.assertEquals(expression, task.getName(), value);
		System.out.println(expression + " -> " + value);

		expression = "{{T(java.time.format.DateTimeFormatter).ISO_LOCAL_DATE_TIME.format(rating.deadline)}}";
		value = SpelFormater.format(expression, notification);
		Assert.assertEquals(expression, java.time.format.DateTimeFormatter.ISO_LOCAL_DATE_TIME.format(now), value);
		System.out.println(expression + " -> " + value);
		
		expression = "{{rating.user.userName}}";
		value = SpelFormater.format(expression, notification);
		Assert.assertEquals(expression, user.getUserName(), value);
		System.out.println(expression + " -> " + value);
		
		expression = "{{rating.period.name}}";
		value = SpelFormater.format(expression, notification);
		Assert.assertEquals(expression, period.getName(), value);
		System.out.println(expression + " -> " + value);

		expression = "{{T(java.time.format.DateTimeFormatter).ISO_LOCAL_DATE_TIME.format(period.endDate)}}";
		value = SpelFormater.format(expression, notification);
		Assert.assertEquals(expression, java.time.format.DateTimeFormatter.ISO_LOCAL_DATE_TIME.format(now), value);
		System.out.println(expression + " -> " + value);

		expression = "{{period.name}}";
		value = SpelFormater.format(expression, notification);
		Assert.assertEquals(expression, period.getName(), value);
		System.out.println(expression + " -> " + value);
		
	}
	
	
	private void givenNotification(OffsetDateTime now) {
		notification = Notification.builder()
				.notificationType(NotificationTypeEnum.EVALUATION_DEADLINE)
				.status(NotificationStatusEnum.OPEN)
				.referenceType(ReferenceTypeEnum.EVALUATION)
				.toUser(user)
				.task(task)
				.evaluation(evaluation)
				.rating(rating)
				.period(period)
				.build();
	}
	
}
