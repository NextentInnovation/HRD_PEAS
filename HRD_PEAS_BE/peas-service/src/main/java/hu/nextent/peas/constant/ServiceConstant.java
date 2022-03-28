package hu.nextent.peas.constant;

public class ServiceConstant {
	
	private ServiceConstant() { }
		
	public static final String DEFAULT_LANGUAGE = "default.language";
	
	public static final String PAGE_SIZE = "page.size";
	
	public static final String TASK_EDITOR_FACTOR_MIN = "taskeditor.factor.min"; 
	public static final String TASK_EDITOR_FACTOR_MAX = "taskeditor.factor.max"; 
	
	public static final String TASK_EDITOR_FACTOR_REQUIRED_MIN = "taskeditor.factor.required.min"; 
	
	public static final String TASK_EDITOR_COMPANY_VIRTUE_MIN = "taskeditor.company.virtue.min"; 
	public static final String TASK_EDITOR_COMPANY_VIRTUE_MAX = "taskeditor.company.virtue.max"; 
	public static final String TASK_EDITOR_LEADER_VIRTUE_MIN = "taskeditor.leader.virtue.min"; 
	public static final String TASK_EDITOR_LEADER_VIRTUE_MAX = "taskeditor.leader.virtue.max"; 

	public static final String TASK_EDITOR_USER_MIN = "taskeditor.user.min"; 
	public static final String TASK_EDITOR_USER_MAX = "taskeditor.user.max"; 
	
	// Feladat lejárta közeleg, nap tartomány, ennyi nappal elötte figyelmeztessen
	public static final String DEADLINE_TASK_NOTIFICATION_WARNING = "task.deadline.notification.warning";
	// Feladat lejárta után, ennyi nappal elötte figyelmeztessen
	public static final String DEADLINE_TASK_NOTIFICATION_AFTER_WARNING = "task.deadline.notification.after.warning";
	
	
	// Feladat lejárta közeleg, felületen emelje ki
	public static final String DEADLINE_TASK_WARNING = 			"task.deadline.warning.day";
	// Értékelés lejárta közeleg, nap tartomány, ennyi nappal elötte figyelmeztessen
	public static final String DEADLINE_EVALUATION_NOTIFICATION_WARNING = 	"evaluation.deadline.notification.warning";
	// Értékelés vége beállításához
	public static final String DEADLINE_EVALUATION_EXIPRED = 	"evaluation.expired.day";
	// Minősítés deadline beállításához
	public static final String DEADLINE_RATING_EXIPRED = 		"rating.expired.day";
	// Minősítés lejárta közeleg, nap tartomány, ennyi nappal elötte figyelmeztessen
	public static final String DEADLINE_RATING_NOTIFICATION_WARNING = 		"rating.deadline.notification.warning";
	// Periódus lejárta közeleg, nap tartomány, ennyi nappal elötte figyelmeztessen
	public static final String DEADLINE_PERIOD_NOTIFICATION_WARNING = 		"period.deadline.notification.warning";
	
	public static final String PERIOD_RATING_DAY = "period.rating.day"; // Periódus minősítés időszak vége számoláshoz
	public static final String PERIOD_MINIMAL_RANGE_DAYS = "period.minimal.range.days"; // Periódus minimum hossza mentés validálásnál
	
	// Template
	public static final String PERIOD_TEMPLATE_END_DAYS = "period.template.end.days"; // Minta periódus vége napok
	public static final String PERIOD_TEMPLATE_NAME = "period.template.name"; // Minta periódus neve
	public static final String PERIOD_TEMPLATE_RANGE_DAYS = "period.template.range.days"; // Minimum generált range
	public static final String PERIOD_TEMPLATE_RATING_DAY = "period.template.rating.day"; // Periódus minősítés időszak vége számoláshoz
	
	
	
	// EMAIL
	public static final String EMAIL_SYSTEM_ENABLE = "email.system.enable";
	public static final String EMAIL_ENABLE = "email.enable";
	public static final String EMAIL_TEST = "email.test";
	public static final String EMAIL_FROM = "email.from";
	public static final String EMAIL_TEST_TO = "email.test.to";
	public static final String EMAIL_BCC = "email.bcc";
	public static final String EMAIL_CC = "email.cc";
	
	
}
