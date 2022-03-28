package hu.nextent.peas.constant;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import hu.nextent.peas.jpa.entity.NotificationTypeEnum;

public class NotificationLabelConstant {
	
	private NotificationLabelConstant() {}
	
	public static String NOTIFICATION_TASK_DEADLINE_SUBJECT = "notification.TASK_DEADLINE.subject";
	public static String NOTIFICATION_TASK_DEADLINE_BODY = "notification.TASK_DEADLINE.body";
		
	public static String NOTIFICATION_EVALUATION_START_SUBJECT = "notification.EVALUATION_START.subject";
	public static String NOTIFICATION_EVALUATION_START_BODY = "notification.EVALUATION_START.body";
	
	public static String NOTIFICATION_EVALUATION_DEADLINE_SUBJECT = "notification.EVALUATION_DEADLINE.subject";
	public static String NOTIFICATION_EVALUATION_DEADLINE_BODY = "notification.EVALUATION_DEADLINE.body";
	
	public static String NOTIFICATION_EVALUATION_EXPIRED_SUBJECT = "notification.EVALUATION_EXPIRED.subject";
	public static String NOTIFICATION_EVALUATION_EXPIRED_BODY = "notification.EVALUATION_EXPIRED.body";
	
	public static String NOTIFICATION_EVALUATION_END_SUBJECT = "notification.EVALUATION_END.subject";
	public static String NOTIFICATION_EVALUATION_END_BODY = "notification.EVALUATION_END.body";

	public static String NOTIFICATION_RATING_START_SUBJECT = "notification.RATING_START.subject";
	public static String NOTIFICATION_RATING_START_BODY = "notification.RATING_START.body";

	public static String NOTIFICATION_RATING_DEADLINE_SUBJECT = "notification.RATING_DEADLINE.subject";
	public static String NOTIFICATION_RATING_DEADLINE_BODY = "notification.RATING_DEADLINE.body";
	
	public static String NOTIFICATION_RATING_EXPIRED_SUBJECT = "notification.RATING_EXPIRED.subject";
	public static String NOTIFICATION_RATING_EXPIRED_BODY = "notification.RATING_EXPIRED.body";
	
	public static String NOTIFICATION_RATING_END_SUBJECT = "notification.RATING_END.subject";
	public static String NOTIFICATION_RATING_END_BODY = "notification.RATING_END.body";

	public static String NOTIFICATION_PERIOD_DEADLINE_SUBJECT = "notification.PERIOD_DEADLINE.subject";
	public static String NOTIFICATION_PERIOD_DEADLINE_BODY = "notification.PERIOD_DEADLINE.body";
	
	public static String NOTIFICATION_PERIOD_ACTIVE_CLOSE_SUBJECT = "notification.PERIOD_ACTIVE_CLOSE.subject";
	public static String NOTIFICATION_PERIOD_ACTIVE_CLOSE_BODY = "notification.PERIOD_ACTIVE_CLOSE.body";

	public static String NOTIFICATION_PERIOD_RATING_CLOSE_SUBJECT = "notification.PERIOD_RATING_CLOSE.subject";
	public static String NOTIFICATION_PERIOD_RATING_CLOSE_BODY = "notification.PERIOD_RATING_CLOSE.subject";

	public static String NOTIFICATION_PERIOD_ACTIVATED_SUBJECT = "notification.PERIOD_ACTIVATED.subject";
	public static String NOTIFICATION_PERIOD_ACTIVATED_BODY = "notification.PERIOD_ACTIVATED.subject";
	
	public static final Map<NotificationTypeEnum, String> SUBJECT_MAP = new HashMap<NotificationTypeEnum, String>();
	public static final Map<NotificationTypeEnum, String> BODY_MAP = new HashMap<NotificationTypeEnum, String>();
	
	static {
		SUBJECT_MAP.put(NotificationTypeEnum.TASK_DEADLINE, NOTIFICATION_TASK_DEADLINE_SUBJECT);
		BODY_MAP.put(NotificationTypeEnum.TASK_DEADLINE, NOTIFICATION_TASK_DEADLINE_BODY);
		
		SUBJECT_MAP.put(NotificationTypeEnum.EVALUATION_START, NOTIFICATION_EVALUATION_START_SUBJECT);
		BODY_MAP.put(NotificationTypeEnum.EVALUATION_START, NOTIFICATION_EVALUATION_START_BODY);
		
		SUBJECT_MAP.put(NotificationTypeEnum.EVALUATION_DEADLINE, NOTIFICATION_EVALUATION_DEADLINE_SUBJECT);
		BODY_MAP.put(NotificationTypeEnum.EVALUATION_DEADLINE, NOTIFICATION_EVALUATION_DEADLINE_BODY);
		
		SUBJECT_MAP.put(NotificationTypeEnum.EVALUATION_EXPIRED, NOTIFICATION_EVALUATION_EXPIRED_SUBJECT);
		BODY_MAP.put(NotificationTypeEnum.EVALUATION_EXPIRED, NOTIFICATION_EVALUATION_EXPIRED_BODY);
		
		SUBJECT_MAP.put(NotificationTypeEnum.EVALUATION_END, NOTIFICATION_EVALUATION_END_SUBJECT);
		BODY_MAP.put(NotificationTypeEnum.EVALUATION_END, NOTIFICATION_EVALUATION_END_BODY);
		
		SUBJECT_MAP.put(NotificationTypeEnum.RATING_START, NOTIFICATION_RATING_START_SUBJECT);
		BODY_MAP.put(NotificationTypeEnum.RATING_START, NOTIFICATION_RATING_START_BODY);

		SUBJECT_MAP.put(NotificationTypeEnum.RATING_DEADLINE, NOTIFICATION_RATING_DEADLINE_SUBJECT);
		BODY_MAP.put(NotificationTypeEnum.RATING_DEADLINE, NOTIFICATION_RATING_DEADLINE_BODY);

		SUBJECT_MAP.put(NotificationTypeEnum.RATING_EXPIRED, NOTIFICATION_RATING_EXPIRED_SUBJECT);
		BODY_MAP.put(NotificationTypeEnum.RATING_EXPIRED, NOTIFICATION_RATING_EXPIRED_BODY);
		
		SUBJECT_MAP.put(NotificationTypeEnum.RATING_END, NOTIFICATION_RATING_END_SUBJECT);
		BODY_MAP.put(NotificationTypeEnum.RATING_END, NOTIFICATION_RATING_END_BODY);

		SUBJECT_MAP.put(NotificationTypeEnum.PERIOD_DEADLINE, NOTIFICATION_PERIOD_DEADLINE_SUBJECT);
		BODY_MAP.put(NotificationTypeEnum.PERIOD_DEADLINE, NOTIFICATION_PERIOD_DEADLINE_BODY);

		SUBJECT_MAP.put(NotificationTypeEnum.PERIOD_ACTIVE_CLOSE, NOTIFICATION_PERIOD_ACTIVE_CLOSE_SUBJECT);
		BODY_MAP.put(NotificationTypeEnum.PERIOD_ACTIVE_CLOSE, NOTIFICATION_PERIOD_ACTIVE_CLOSE_BODY);

		SUBJECT_MAP.put(NotificationTypeEnum.PERIOD_RATING_CLOSE, NOTIFICATION_PERIOD_RATING_CLOSE_SUBJECT);
		BODY_MAP.put(NotificationTypeEnum.PERIOD_RATING_CLOSE, NOTIFICATION_PERIOD_RATING_CLOSE_BODY);

		SUBJECT_MAP.put(NotificationTypeEnum.PERIOD_ACTIVATED, NOTIFICATION_PERIOD_ACTIVATED_SUBJECT);
		BODY_MAP.put(NotificationTypeEnum.PERIOD_ACTIVATED, NOTIFICATION_PERIOD_ACTIVATED_BODY);

		SUBJECT_MAP.put(NotificationTypeEnum.LEADER_VIRTUE, null);
		BODY_MAP.put(NotificationTypeEnum.LEADER_VIRTUE, null);

		SUBJECT_MAP.put(NotificationTypeEnum.OTHER, null);
		BODY_MAP.put(NotificationTypeEnum.OTHER, null);
	}
	

	public static void main(String ... args) throws IllegalArgumentException, IllegalAccessException {
		Field[] fields = NotificationLabelConstant.class.getDeclaredFields();
		NotificationLabelConstant instane = new NotificationLabelConstant();
		for(Field field : fields) {
			if (Arrays.asList("SUBJECT_MAP", "BODY_MAP").contains(field.getName())) {
				continue;
			}
			System.out.println(String.format("('hu', '%s', '%s'),", field.get(instane), field.get(instane)));
		}
		
	}
}
