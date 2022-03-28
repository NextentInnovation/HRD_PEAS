package hu.nextent.peas.jpa.entity;

public enum NotificationTypeEnum {

	TASK_DEADLINE(NotificationStatusEnum.INFORMATION),
	EVALUATION_START(NotificationStatusEnum.OPEN),
	EVALUATION_DEADLINE(NotificationStatusEnum.OPEN),
	EVALUATION_EXPIRED(NotificationStatusEnum.CLOSE),
	EVALUATION_END(NotificationStatusEnum.CLOSE),
	RATING_START(NotificationStatusEnum.OPEN),
	RATING_DEADLINE(NotificationStatusEnum.OPEN),
	RATING_EXPIRED(NotificationStatusEnum.CLOSE),
	RATING_END(NotificationStatusEnum.CLOSE),
	PERIOD_DEADLINE(NotificationStatusEnum.INFORMATION),
	PERIOD_ACTIVE_CLOSE(NotificationStatusEnum.INFORMATION),
	PERIOD_RATING_CLOSE(NotificationStatusEnum.INFORMATION),
	PERIOD_ACTIVATED(NotificationStatusEnum.INFORMATION),
	LEADER_VIRTUE(NotificationStatusEnum.INFORMATION),
	OTHER(NotificationStatusEnum.INFORMATION)
	;
	
	private NotificationStatusEnum defaultStatus;
	
	private NotificationTypeEnum(NotificationStatusEnum defaultStatus) {
		this.defaultStatus = defaultStatus;
	}
	
	
	public NotificationStatusEnum getDefaultStatus() {
		return defaultStatus;
	}
}
