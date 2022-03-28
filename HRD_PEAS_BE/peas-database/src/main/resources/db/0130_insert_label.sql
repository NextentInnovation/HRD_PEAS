--liquibase formatted sql

/*
	SpEl Formated Label
	
	Érték készlete a ToDo értékei
	
	TODO:
	status : ToDoStatusEnum
	messageCode : String
	deadline : OffsetDateTime
	done : OffsetDateTime
	referenceType : ReferenceTypeEnum
	toUser : User
	task : Task
	evaluation : Evaluation
	rating : Rating
	leaderVirtue : LeaderVirtue
	period : Period
	company : Company
	
	
	
*/
INSERT INTO label (
    `language`, code, label
) VALUES 
  ( 'hu', 'todo.summon.evaluator', '{{evaluation.task.owner.fullName}} feladatának értékelése: {{evaluation.task.name}}' ),
  ( 'en', 'todo.summon.evaluator', '{{evaluation.task.owner.fullName}} task evaluation: {{evaluation.task.name}}'),
  
  ( 'hu', 'todo.summon.rating', '{{rating.user.fullName}} vezetői periódus minősítés: {{period.name}}' ),
  ( 'en', 'todo.summon.rating', '{{rating.user.fullName}} rating {{period.name}}')
  ;

/*
	SpEl Formated Label
	
	Érték készlete a ToDo értékei
	
	Notification:
	notificationType : NotificationTypeEnum
	status : NotificationStatusEnum
	subject : String
	body : String
	readed : OffsetDateTime
	notifacededDay : LocalDate
	referenceType : ReferenceTypeEnum
	sended : Boolean
	toUser : User
	task : Task
	evaluation : Evaluation
	rating : Rating
	leaderVirtue : LeaderVirtue
	period : Period
	company : Company
	
	
*/
-- Notifikációk
INSERT INTO label (
    `language`, code, label
) VALUES 
  ( 'hu', 'notification.TASK_DEADLINE.subject', '{{#HU_DATE.format(task.deadline)}}-ig feladat' ),
  ( 'en', 'notification.TASK_DEADLINE.subject', '{{#EN_DATE.format(task.deadline)}}-ig feladat' ),
  ( 'hu', 'notification.TASK_DEADLINE.body', 'Tájékoztatjuk, hogy {{task.name}} feladatnak {{#HU_DATE.format(task.deadline)}} a határideje.' ),
  ( 'en', 'notification.TASK_DEADLINE.body', 'Tájékoztatjuk, hogy {{task.name}} feladatnak {{#EN_DATE.format(task.deadline)}} a határideje.' ),

  ( 'hu', 'notification.EVALUATION_START.subject', '{{#HU_DATETIME.format(evaluation.deadline)}}-ig értékelés' ),
  ( 'en', 'notification.EVALUATION_START.subject', '{{#EN_DATETIME.format(evaluation.deadline)}}-ig értékelés' ),
  ( 'hu', 'notification.EVALUATION_START.body', 'Kérjük, hogy {{evaluation.task.owner.fullName}} {{evaluation.task.name}} feladatát {{#HU_DATETIME.format(evaluation.deadline)}}-ig értékelje.'),
  ( 'en', 'notification.EVALUATION_START.body', 'Kérjük, hogy {{evaluation.task.owner.fullName}} {{evaluation.task.name}} feladatát {{#EN_DATETIME.format(evaluation.deadline)}}-ig értékelje.'),
  
  ( 'hu', 'notification.EVALUATION_DEADLINE.subject', '{{#HU_DATETIME.format(evaluation.deadline)}}-ig értékelés' ),
  ( 'en', 'notification.EVALUATION_DEADLINE.subject', '{{#EN_DATETIME.format(evaluation.deadline)}}-ig értékelés' ),
  ( 'hu', 'notification.EVALUATION_DEADLINE.body', '{{evaluation.task.owner.fullName}} {{evaluation.task.name}} feladat értékelésének a határideje: {{#HU_DATETIME.format(evaluation.deadline)}}'),
  ( 'en', 'notification.EVALUATION_DEADLINE.body', '{{evaluation.task.owner.fullName}} {{evaluation.task.name}} feladat értékelésének a határideje: {{#EN_DATETIME.format(evaluation.deadline)}}'),

  ( 'hu', 'notification.EVALUATION_EXPIRED.subject', 'Értékelés lejárt'),
  ( 'en', 'notification.EVALUATION_EXPIRED.subject', 'Értékelés lejárt'),
  ( 'hu', 'notification.EVALUATION_EXPIRED.body', '{{evaluation.task.owner.fullName}} {{evaluation.task.name}} feladat értékelésének a határideje lejárt.'),
  ( 'en', 'notification.EVALUATION_EXPIRED.body', '{{evaluation.task.owner.fullName}} {{evaluation.task.name}} feladat értékelésének a határideje lejárt.'),
  
  ( 'hu', 'notification.EVALUATION_END.subject', 'Sikeres értékelés' ),
  ( 'en', 'notification.EVALUATION_END.subject', 'Sikeres értékelés' ),
  ( 'hu', 'notification.EVALUATION_END.body', '{{evaluation.task.owner.fullName}} {{evaluation.task.name}} feladatát sikeresen értékelte.'),
  ( 'en', 'notification.EVALUATION_END.body', '{{evaluation.task.owner.fullName}} {{evaluation.task.name}} feladatát sikeresen értékelte.'),

  ( 'hu', 'notification.RATING_START.subject', '{{#HU_DATETIME.format(rating.deadline)}}-ig vezetői periódus minősítés' ),
  ( 'en', 'notification.RATING_START.subject', '{{#EN_DATETIME.format(rating.deadline)}}-ig vezetői periódus minősítés' ),
  ( 'hu', 'notification.RATING_START.body', 'Kérjük, hogy {{rating.user.fullName}} {{rating.period.name}} periódusbeli munkáját {{#HU_DATETIME.format(rating.deadline)}}-ig minősítse.'),
  ( 'en', 'notification.RATING_START.body', 'Kérjük, hogy {{rating.user.fullName}} {{rating.period.name}} periódusbeli munkáját {{#EN_DATETIME.format(rating.deadline)}}-ig minősítse.'),
  
  ( 'hu', 'notification.RATING_DEADLINE.subject', '{{#HU_DATETIME.format(rating.deadline)}}-ig vezetői periódus minősítés' ),
  ( 'en', 'notification.RATING_DEADLINE.subject', '{{#EN_DATETIME.format(rating.deadline)}}-ig vezetői periódus minősítés' ),
  ( 'hu', 'notification.RATING_DEADLINE.body', 'Tájékoztatjuk, hogy {{rating.user.fullName}} {{rating.period.name}} periódus minősítésnek {{#HU_DATETIME.format(rating.deadline)}} a határideje.' ),
  ( 'en', 'notification.RATING_DEADLINE.body', 'Tájékoztatjuk, hogy {{rating.user.fullName}} {{rating.period.name}} periódus minősítésnek {{#EN_DATETIME.format(rating.deadline)}} a határideje.' ),
  
  ( 'hu', 'notification.RATING_EXPIRED.subject', 'Periódus minősítés lejárt' ),
  ( 'en', 'notification.RATING_EXPIRED.subject', 'Periódus minősítés lejárt' ),
  ( 'hu', 'notification.RATING_EXPIRED.body', '{{rating.user.fullName}} {{rating.period.name}} periódus minősítésnek a határideje lejárt.' ),
  ( 'en', 'notification.RATING_EXPIRED.body', '{{rating.user.fullName}} {{rating.period.name}} periódus minősítésnek a határideje lejárt.' ),

  ( 'hu', 'notification.RATING_END.subject', 'Sikeres minősítés' ),
  ( 'en', 'notification.RATING_END.subject', 'Sikeres minősítés' ),
  ( 'hu', 'notification.RATING_END.body', '{{rating.user.fullName}} {{rating.period.name}} periódusbeli munkáját sikeresen minősítette.'),
  ( 'en', 'notification.RATING_END.body', '{{rating.user.fullName}} {{rating.period.name}} periódusbeli munkáját sikeresen minősítette.'),

  ( 'hu', 'notification.PERIOD_DEADLINE.subject', '{{#HU_DATETIME.format(period.endDate)}}-kor periódus zárás' ),
  ( 'en', 'notification.PERIOD_DEADLINE.subject', '{{#EN_DATETIME.format(period.endDate)}}-kor periódus zárás' ),
  ( 'hu', 'notification.PERIOD_DEADLINE.body', 'Tájékoztatjuk, hogy {{#HU_DATETIME.format(period.endDate)}}-kor {{period.name}} periódus lezárásra kerül.' ),
  ( 'en', 'notification.PERIOD_DEADLINE.body', 'Tájékoztatjuk, hogy {{#EN_DATETIME.format(period.endDate)}}-kor {{period.name}} periódus lezárásra kerül.' ),

  ( 'hu', 'notification.PERIOD_ACTIVE_CLOSE.subject', '{{period.name}} periódus lezárva' ),
  ( 'en', 'notification.PERIOD_ACTIVE_CLOSE.subject', '{{period.name}} periódus lezárva' ),
  ( 'hu', 'notification.PERIOD_ACTIVE_CLOSE.body', '{{period.name}} aktív periódus lezárásra került.' ),
  ( 'en', 'notification.PERIOD_ACTIVE_CLOSE.body', '{{period.name}} aktív periódus lezárásra került.' ),

  ( 'hu', 'notification.PERIOD_RATING_CLOSE.subject', '{{period.name}} periódus minősítés lezárva' ),
  ( 'en', 'notification.PERIOD_RATING_CLOSE.subject', '{{period.name}} periódus minősítés lezárva' ),
  ( 'hu', 'notification.PERIOD_RATING_CLOSE.body', '{{period.name}} periódus minősítés lezárásra került.' ),
  ( 'en', 'notification.PERIOD_RATING_CLOSE.body', '{{period.name}} periódus minősítés lezárásra került.' ),

  ( 'hu', 'notification.PERIOD_ACTIVATED.subject', '{{period.name}} periódus aktiválva' ),
  ( 'en', 'notification.PERIOD_ACTIVATED.subject', '{{period.name}} periódus aktiválva' ),
  ( 'hu', 'notification.PERIOD_ACTIVATED.body', '{{period.name}} periódus aktiválásra került.' ),
  ( 'en', 'notification.PERIOD_ACTIVATED.body', '{{period.name}} periódus aktiválásra került.' )
;



-- EvaluationStatus
/*
	BEFORE_EVALUATING, // Még nincs értékelve
	EVALUATING, // Értékelendő
	EVALUATED, // Értékelt
	CLOSED // Lezárt
*/
INSERT INTO label (
    `language`, code, label
) VALUES 
  ( 'hu', 'status.EvaluationStatus.BEFORE_EVALUATING', 'Még nincs értékelve' ),
  ( 'en', 'status.EvaluationStatus.BEFORE_EVALUATING', 'Még nincs értékelve'),
  ( 'hu', 'status.EvaluationStatus.EVALUATING', 'Értékelendő' ),
  ( 'en', 'status.EvaluationStatus.EVALUATING', 'Értékelendő'),
  ( 'hu', 'status.EvaluationStatus.EVALUATED', 'Értékelt' ),
  ( 'en', 'status.EvaluationStatus.EVALUATED', 'Értékelt'),
  ( 'hu', 'status.EvaluationStatus.CLOSED', 'Lezárt' ),
  ( 'en', 'status.EvaluationStatus.CLOSED', 'Lezárt')
;

-- TaskStatusEnum
/*
	PARAMETERIZATION,	// Paraméterezés
	UNDER_EVALUATION,	// Értékelés alatt
	EVALUATED, // Értékelt
	CLOSED // Lezárt
*/
INSERT INTO label (
    `language`, code, label
) VALUES 
  ( 'hu', 'status.TaskStatus.PARAMETERIZATION', 'Paraméterezés' ),
  ( 'en', 'status.TaskStatus.PARAMETERIZATION', 'Paraméterezés'),
  ( 'hu', 'status.TaskStatus.UNDER_EVALUATION', 'Értékelés alatt' ),
  ( 'en', 'status.TaskStatus.UNDER_EVALUATION', 'Értékelés alatt'),
  ( 'hu', 'status.TaskStatus.EVALUATED', 'Értékelt' ),
  ( 'en', 'status.TaskStatus.EVALUATED', 'Értékelt'),
  ( 'hu', 'status.TaskStatus.CLOSED', 'Lezárt' ),
  ( 'en', 'status.TaskStatus.CLOSED', 'Lezárt')
;

-- TaskTypeEnum
/*
	NORMAL, // Normál
	AUTOMATIC, // Automatikus
	TEMPLATE

*/
INSERT INTO label (
    `language`, code, label
) VALUES 
  ( 'hu', 'type.TaskType.NORMAL', 'Normál' ),
  ( 'en', 'type.TaskType.NORMAL', 'Normál'),
  ( 'hu', 'type.TaskType.AUTOMATIC', 'Automatikus' ),
  ( 'en', 'type.TaskType.AUTOMATIC', 'Automatikus'),
  ( 'hu', 'type.TaskType.TEMPLATE', 'Template' ),
  ( 'en', 'type.TaskType.TEMPLATE', 'Template' )
;


-- Errors
-- MessageFormater alapján
INSERT INTO label ( language, code, label) VALUES ('hu','error.no_content','Nincs tartalom.');
INSERT INTO label ( language, code, label) VALUES ('hu','error.not_founded','A keresett elem nem található.');
INSERT INTO label ( language, code, label) VALUES ('hu','error.not_founded_object','A keresett elem nem található. Típus: {0}, azonosító: {1}.');
INSERT INTO label ( language, code, label) VALUES ('hu','error.page_index_under','A lapozás indexe negatív értéket vett fel.');
INSERT INTO label ( language, code, label) VALUES ('hu','error.task.id_reqired','Feladat azonosító szükséges.');
INSERT INTO label ( language, code, label) VALUES ('hu','error.task.invalid_id','A feladat azonosítója nem megfelelő.');
INSERT INTO label ( language, code, label) VALUES ('hu','error.task.not_founded','A feladat nem található.');
INSERT INTO label ( language, code, label) VALUES ('hu','error.task.invalid_company','A feladat nem található.');
INSERT INTO label ( language, code, label) VALUES ('hu','error.task.invalid_status','A feladat állapota nem megfelelő. Feladat állapota: {2}, elfogadott állapotok: {1}.');
INSERT INTO label ( language, code, label) VALUES ('hu','error.task.invalid_type','A feladat típusa nem megfelelő. Feladat típusa: {2}, elfogadott típusok: {1}.');
INSERT INTO label ( language, code, label) VALUES ('hu','error.task.invalid_user','A felhasználó nem a feladat tulajdonosa. Feladat tulajdonosa: {1}.');
INSERT INTO label ( language, code, label) VALUES ('hu','error.task.name_empty','Nincs megadva a feladat neve.');
INSERT INTO label ( language, code, label) VALUES ('hu','error.task.difficulty_not_founded','A megadott nehézségi szint nem megfelelő. Nehézségi szint: {1}.');
INSERT INTO label ( language, code, label) VALUES ('hu','error.task.factor_not_founded','A megadott értékelési szempont nem található. Szempont: {1}.');
INSERT INTO label ( language, code, label) VALUES ('hu','error.task.leadervirtue_not_founded','A vezetői érték nem található. Vezetői érték: {1}.');
INSERT INTO label ( language, code, label) VALUES ('hu','error.task.companyvirtue_not_founded','A céges érték nem található. Céges érték: {1}.');
INSERT INTO label ( language, code, label) VALUES ('hu','error.task.evaluator_not_founded','Az értékelő nem található. Értékelő: {1}.');
INSERT INTO label ( language, code, label) VALUES ('hu','error.task.factor_limit_violation','Legalább {2}, legfeljebb {3} értékelési szempontot meg kell adni a feladathoz. Megadott szempontok száma: {1}.');
INSERT INTO label ( language, code, label) VALUES ('hu','error.task.factor_required_limit_violation','Legalább {2} kötelező értékelési szempontot meg kell adni a feladathoz. Megadott kötelező szempontok száma: {1}.');
INSERT INTO label ( language, code, label) VALUES ('hu','error.task.virtue_limit_violation','Legalább {2}, legfeljebb {3} vállalati és csoport értéket lehet megadni a feladathoz. Megadott értékek száma: {1}.');
INSERT INTO label ( language, code, label) VALUES ('hu','error.task.company_virtue_limit_violation','Legalább {2}, legfeljebb {3} vállalati értéket lehet megadni a feladathoz. Megadott értékek száma: {1}.');
INSERT INTO label ( language, code, label) VALUES ('hu','error.task.leader_virtue_limit_violation','Legalább {2}, legfeljebb {3} csoport értéket lehet megadni a feladathoz. Megadott értékek száma: {1}.');
INSERT INTO label ( language, code, label) VALUES ('hu','error.task.evaluator_limit_violation','Legalább {2}, legfeljebb {3} értékelőt meg kell adni a feladathoz. Megadott értékelők száma: {1}.');
INSERT INTO label ( language, code, label) VALUES ('hu','error.evaluation.id_reqired','Nincs megadva az értékelés azonosítója.');
INSERT INTO label ( language, code, label) VALUES ('hu','error.evaluation.invalid_id','Az értékelés azonosítója nem megfelelő');
INSERT INTO label ( language, code, label) VALUES ('hu','error.evaluation.not_founded','Az értékelés nem található.');
INSERT INTO label ( language, code, label) VALUES ('hu','error.evaluation.invalid_status','Az értékelés állapota nem megfelelő. Állapot: {2}, elfogadott állapotok: {1}.');
INSERT INTO label ( language, code, label) VALUES ('hu','error.evaluation.invalid_user','A felhasználó nem értékelője az értékelésnek.');
INSERT INTO label ( language, code, label) VALUES ('hu','error.evaluation.selection_empty','Az értékeléshez nem lettek szempont opciók kiválasztva.');
INSERT INTO label ( language, code, label) VALUES ('hu','error.evaluation.selection_required_violation','Nincs minden kötelező értékelési szempont kitöltve. Kötelező szempontok száma: {1}, ebből kitöltve: {2}');
INSERT INTO label ( language, code, label) VALUES ('hu','error.rating.id_reqired','A minősítés azonosító szükséges.');
INSERT INTO label ( language, code, label) VALUES ('hu','error.rating.not_founded','A minősítés nem található.');
INSERT INTO label ( language, code, label) VALUES ('hu','error.rating.invalid_status','A minősítés állapota nem megfelelő. Állapot: {2}, elfogadott állapotok: {1}.');
INSERT INTO label ( language, code, label) VALUES ('hu','error.rating.invalid_user','A felhasználó nem értékelője a minősítésnek.');
INSERT INTO label ( language, code, label) VALUES ('hu','error.rating.invalid_company','A minősítés nem található.');
INSERT INTO label ( language, code, label) VALUES ('hu','error.rating.graderecommendation_empty','A minősítésnél a "Besorolás és juttatás változtatás javaslat" nics kitöltve.');
INSERT INTO label ( language, code, label) VALUES ('hu','error.rating.textualevaluation_empty','A minősítésnél a "Szöveges értékelés" nics kitöltve.');
INSERT INTO label ( language, code, label) VALUES ('hu','error.rating.invalid_cooperation_empty','A minősítésnél a "További együttműködés az értékelttel" nincs kitöltve.');
INSERT INTO label ( language, code, label) VALUES ('hu','error.period.not_founded','A periódus nem található.');
INSERT INTO label ( language, code, label) VALUES ('hu','error.period.invalid_company','A periódus nem található.');
INSERT INTO label ( language, code, label) VALUES ('hu','error.period.id_reqired','A periódus azonosító nincs megadva.');
INSERT INTO label ( language, code, label) VALUES ('hu','error.period.invalid_id','A periódus azonosító nem megfelelő.');
INSERT INTO label ( language, code, label) VALUES ('hu','error.period.invalid_status','A periódus állapota nem megfelelő. Állapot: {2}, elfogadott állapotok: {1}.');
INSERT INTO label ( language, code, label) VALUES ('hu','error.user.not_founded','A felhasználó nem található.');
INSERT INTO label ( language, code, label) VALUES ('hu','error.companyvirtue.id_reqired','Nincs megadva a céges érték azonosítója.');
INSERT INTO label ( language, code, label) VALUES ('hu','error.companyvirtue.invalid_id','Nem megfelelő a céges érték azonosítója.');
INSERT INTO label ( language, code, label) VALUES ('hu','error.companyvirtue.invalid_company','A céges érték nem található');
INSERT INTO label ( language, code, label) VALUES ('hu','error.companyvirtue.invalid_status','A céges érték állapota nem megfelelő. Állapot: {2}, elfogadott állapotok: {1}');
INSERT INTO label ( language, code, label) VALUES ('hu','error.companyvirtue.not_founded','A céges érték nem található.');
INSERT INTO label ( language, code, label) VALUES ('hu','error.report.period.not_founded','A periódus nem található.');
INSERT INTO label ( language, code, label) VALUES ('hu','error.report.user.not_founded','A felhasználó nem található.');
INSERT INTO label ( language, code, label) VALUES ('hu','error.report.rating.not_founded','A minősítés felhasználója nem található.');
INSERT INTO label ( language, code, label) VALUES ('hu','error.report.task.not_founded','A feladat nem található.');

-- english
INSERT INTO label ( language, code, label) VALUES ('en','error.no_content','Nincs tartalom.');
INSERT INTO label ( language, code, label) VALUES ('en','error.not_founded','A keresett elem nem található.');
INSERT INTO label ( language, code, label) VALUES ('en','error.not_founded_object','A keresett elem nem található. Típus: {0}, azonosító: {1}.');
INSERT INTO label ( language, code, label) VALUES ('en','error.page_index_under','A lapozás indexe negatív értéket vett fel.');
INSERT INTO label ( language, code, label) VALUES ('en','error.task.id_reqired','Feladat azonosító szükséges.');
INSERT INTO label ( language, code, label) VALUES ('en','error.task.invalid_id','A feladat azonosítója nem megfelelő.');
INSERT INTO label ( language, code, label) VALUES ('en','error.task.not_founded','A feladat nem található.');
INSERT INTO label ( language, code, label) VALUES ('en','error.task.invalid_company','A feladat nem található.');
INSERT INTO label ( language, code, label) VALUES ('en','error.task.invalid_status','A feladat állapota nem megfelelő. Feladat állapota: {2}, elfogadott állapotok: {1}.');
INSERT INTO label ( language, code, label) VALUES ('en','error.task.invalid_type','A feladat típusa nem megfelelő. Feladat típusa: {2}, elfogadott típusok: {1}.');
INSERT INTO label ( language, code, label) VALUES ('en','error.task.invalid_user','A felhasználó nem a feladat tulajdonosa. Feladat tulajdonosa: {1}.');
INSERT INTO label ( language, code, label) VALUES ('en','error.task.name_empty','Nincs megadva a feladat neve.');
INSERT INTO label ( language, code, label) VALUES ('en','error.task.difficulty_not_founded','A megadott nehézségi szint nem megfelelő. Nehézségi szint: {1}.');
INSERT INTO label ( language, code, label) VALUES ('en','error.task.factor_not_founded','A megadott értékelési szempont nem található. Szempont: {1}.');
INSERT INTO label ( language, code, label) VALUES ('en','error.task.leadervirtue_not_founded','A vezetői érték nem található. Vezetői érték: {1}.');
INSERT INTO label ( language, code, label) VALUES ('en','error.task.companyvirtue_not_founded','A céges érték nem található. Céges érték: {1}.');
INSERT INTO label ( language, code, label) VALUES ('en','error.task.evaluator_not_founded','Az értékelő nem található. Értékelő: {1}.');
INSERT INTO label ( language, code, label) VALUES ('en','error.task.factor_limit_violation','Legalább {2}, legfeljebb {3} értékelési szempontot meg kell adni a feladathoz. Megadott szempontok száma: {1}.');
INSERT INTO label ( language, code, label) VALUES ('en','error.task.factor_required_limit_violation','Legalább {2} kötelező értékelési szempontot meg kell adni a feladathoz. Megadott kötelező szempontok száma: {1}.');
INSERT INTO label ( language, code, label) VALUES ('en','error.task.company_virtue_limit_violation','Legalább {2}, legfeljebb {3} vállalati értéket lehet megadni a feladathoz. Megadott értékek száma: {1}.');
INSERT INTO label ( language, code, label) VALUES ('en','error.task.leader_virtue_limit_violation','Legalább {2}, legfeljebb {3} csoport értéket lehet megadni a feladathoz. Megadott értékek száma: {1}.');
INSERT INTO label ( language, code, label) VALUES ('en','error.task.evaluator_limit_violation','Legalább {2}, legfeljebb {3} értékelőt meg kell adni a feladathoz. Megadott értékelők száma: {1}.');
INSERT INTO label ( language, code, label) VALUES ('en','error.evaluation.id_reqired','Nincs megadva az értékelés azonosítója.');
INSERT INTO label ( language, code, label) VALUES ('en','error.evaluation.invalid_id','Az értékelés azonosítója nem megfelelő');
INSERT INTO label ( language, code, label) VALUES ('en','error.evaluation.not_founded','Az értékelés nem található.');
INSERT INTO label ( language, code, label) VALUES ('en','error.evaluation.invalid_status','Az értékelés állapota nem megfelelő. Állapot: {2}, elfogadott állapotok: {1}.');
INSERT INTO label ( language, code, label) VALUES ('en','error.evaluation.invalid_user','A felhasználó nem értékelője az értékelésnek.');
INSERT INTO label ( language, code, label) VALUES ('en','error.evaluation.selection_empty','Az értékeléshez nem lettek szempont opciók kiválasztva.');
INSERT INTO label ( language, code, label) VALUES ('en','error.evaluation.selection_required_violation','Nincs minden kötelező értékelési szempont kitöltve. Kötelező szempontok száma: {1}, ebből kitöltve: {2}');
INSERT INTO label ( language, code, label) VALUES ('en','error.rating.id_reqired','A minősítés azonosító szükséges.');
INSERT INTO label ( language, code, label) VALUES ('en','error.rating.not_founded','A minősítés nem található.');
INSERT INTO label ( language, code, label) VALUES ('en','error.rating.invalid_status','A minősítés állapota nem megfelelő. Állapot: {2}, elfogadott állapotok: {1}.');
INSERT INTO label ( language, code, label) VALUES ('en','error.rating.invalid_user','A felhasználó nem értékelője a minősítésnek.');
INSERT INTO label ( language, code, label) VALUES ('en','error.rating.invalid_company','A minősítés nem található.');
INSERT INTO label ( language, code, label) VALUES ('en','error.rating.graderecommendation_empty','A minősítésnél a "Besorolás és juttatás változtatás javaslat" nics kitöltve.');
INSERT INTO label ( language, code, label) VALUES ('en','error.rating.textualevaluation_empty','A minősítésnél a "Szöveges értékelés" nics kitöltve.');
INSERT INTO label ( language, code, label) VALUES ('en','error.rating.invalid_cooperation_empty','A minősítésnél a "További együttműködés az értékelttel" nincs kitöltve.');
INSERT INTO label ( language, code, label) VALUES ('en','error.period.not_founded','A periódus nem található.');
INSERT INTO label ( language, code, label) VALUES ('en','error.period.invalid_company','A periódus nem található.');
INSERT INTO label ( language, code, label) VALUES ('en','error.period.id_reqired','A periódus azonosító nincs megadva.');
INSERT INTO label ( language, code, label) VALUES ('en','error.period.invalid_id','A periódus azonosító nem megfelelő.');
INSERT INTO label ( language, code, label) VALUES ('en','error.period.invalid_status','A periódus állapota nem megfelelő. Állapot: {2}, elfogadott állapotok: {1}.');
INSERT INTO label ( language, code, label) VALUES ('en','error.user.not_founded','A felhasználó nem található.');
INSERT INTO label ( language, code, label) VALUES ('en','error.companyvirtue.id_reqired','Nincs megadva a céges érték azonosítója.');
INSERT INTO label ( language, code, label) VALUES ('en','error.companyvirtue.invalid_id','Nem megfelelő a céges érték azonosítója.');
INSERT INTO label ( language, code, label) VALUES ('en','error.companyvirtue.invalid_company','A céges érték nem található');
INSERT INTO label ( language, code, label) VALUES ('en','error.companyvirtue.invalid_status','A céges érték állapota nem megfelelő. Állapot: {2}, elfogadott állapotok: {1}');
INSERT INTO label ( language, code, label) VALUES ('en','error.companyvirtue.not_founded','A céges érték nem található.');
INSERT INTO label ( language, code, label) VALUES ('en','error.report.period.not_founded','A periódus nem található.');
INSERT INTO label ( language, code, label) VALUES ('en','error.report.user.not_founded','A felhasználó nem található.');
INSERT INTO label ( language, code, label) VALUES ('en','error.report.rating.not_founded','A minősítés felhasználója nem található.');
INSERT INTO label ( language, code, label) VALUES ('en','error.report.task.not_founded','A feladat nem található.');

-- Kliens
INSERT INTO label ( language, code, label ) VALUES ('hu','header.notification.empty','Jelenleg nincs egy értesítése sem!');
INSERT INTO label ( language, code, label ) VALUES ('hu','header.notification.show_all','Összes értesítés megjelentése');
INSERT INTO label ( language, code, label ) VALUES ('hu','header.notification.tooltip','{{number}} db olvasatlan értesítés');
INSERT INTO label ( language, code, label ) VALUES ('hu','header.notification.tooltip.none','Nincs olvasatlan értesítés');
INSERT INTO label ( language, code, label ) VALUES ('hu','header.todo.tooltip','Elvégzendő teendők száma: {{number}}');
INSERT INTO label ( language, code, label ) VALUES ('hu','header.todo.tooltip.none','Nincs elvégzendő teendő');
INSERT INTO label ( language, code, label ) VALUES ('hu','header.create-new-task.tooltip','Új feladat felvétele');
INSERT INTO label ( language, code, label ) VALUES ('hu','header.logout','Kilépés');

INSERT INTO label ( language, code, label ) VALUES ('hu','menu.main_site','Főoldal');
INSERT INTO label ( language, code, label ) VALUES ('hu','menu.to_do','Teendők');
INSERT INTO label ( language, code, label ) VALUES ('hu','menu.task','Feladatok');
INSERT INTO label ( language, code, label ) VALUES ('hu','menu.personal','Értékelések');
INSERT INTO label ( language, code, label ) VALUES ('hu','menu.report','Riportok');
INSERT INTO label ( language, code, label ) VALUES ('hu','menu.settings','Beállítások');
INSERT INTO label ( language, code, label ) VALUES ('hu','menu.periods','Periódusok');

INSERT INTO label ( language, code, label ) VALUES ('hu','login.title','Bejelentkezés');
INSERT INTO label ( language, code, label ) VALUES ('hu','login.user_name','Felhasználónév');
INSERT INTO label ( language, code, label ) VALUES ('hu','login.password','Jelszó');
INSERT INTO label ( language, code, label ) VALUES ('hu','login.button.login','Belépés');

INSERT INTO label ( language, code, label ) VALUES ('hu','table_control.table.all_number.text','Összesen:');
INSERT INTO label ( language, code, label ) VALUES ('hu','table_control.page_size.size.text','Oldal méret:');
INSERT INTO label ( language, code, label ) VALUES ('hu','page-not-found.title.text','Az oldal nem található!');

INSERT INTO label ( language, code, label ) VALUES ('hu','notification.general.success_delete','Sikeres törlés!');
INSERT INTO label ( language, code, label ) VALUES ('hu','notification.general.success_save','Sikeres mentés!');
INSERT INTO label ( language, code, label ) VALUES ('hu','notification.general.success_create','Sikeres létrehozás!');
INSERT INTO label ( language, code, label ) VALUES ('hu','notification.general.success_copy','Sikeres másolat létrehozás!');

INSERT INTO label ( language, code, label ) VALUES ('hu','delete_modal.title','Törlés');
INSERT INTO label ( language, code, label ) VALUES ('hu','delete_modal.question','Biztosan törölni szeretné?');
INSERT INTO label ( language, code, label ) VALUES ('hu','delete_modal.delete','Törlés');

INSERT INTO label ( language, code, label ) VALUES ('hu','global.button.cancel','Mégse');
INSERT INTO label ( language, code, label ) VALUES ('hu','button.new.task','Új feladat');
INSERT INTO label ( language, code, label ) VALUES ('hu','global.button.yes','Igen');
INSERT INTO label ( language, code, label ) VALUES ('hu','global.button.no','Nem');
INSERT INTO label ( language, code, label ) VALUES ('hu','global.button.delete','Törlés');
INSERT INTO label ( language, code, label ) VALUES ('hu','global.button.save','Mentés');
INSERT INTO label ( language, code, label ) VALUES ('hu','global.button.create','Létrehozás');
INSERT INTO label ( language, code, label ) VALUES ('hu','global.button.back','Vissza');
INSERT INTO label ( language, code, label ) VALUES ('hu','global.select.search.not_found','Nincs találat');

INSERT INTO label ( language, code, label ) VALUES ('hu','constants.task_status.parameterization','Paraméterezés');
INSERT INTO label ( language, code, label ) VALUES ('hu','constants.task_status.under_evaluation','Értékelés alatt');
INSERT INTO label ( language, code, label ) VALUES ('hu','constants.task_status.evaluated','Értékelve');
INSERT INTO label ( language, code, label ) VALUES ('hu','constants.task_status.closed','Lezárt');
INSERT INTO label ( language, code, label ) VALUES ('hu','constants.task_status.deleted','Törölt');

INSERT INTO label ( language, code, label ) VALUES ('hu','constants.to_do_status.open','Nyitott');
INSERT INTO label ( language, code, label ) VALUES ('hu','constants.to_do_status.close','Kész');
INSERT INTO label ( language, code, label ) VALUES ('hu','constants.to_do_status.expired','Lejárt');
INSERT INTO label ( language, code, label ) VALUES ('hu','constants.to_do_status.information','');

INSERT INTO label ( language, code, label ) VALUES ('hu','constants.to_do_type.evaluation','Értékelés');
INSERT INTO label ( language, code, label ) VALUES ('hu','constants.to_do_type.rating','Vezetői periódus minősítés');

INSERT INTO label ( language, code, label ) VALUES ('hu','task_list.card.title','Feladatok');
INSERT INTO label ( language, code, label ) VALUES ('hu','task_list.table.header.name','Feladat');
INSERT INTO label ( language, code, label ) VALUES ('hu','task_list.table.header.owner_name','Tulajdonos');
INSERT INTO label ( language, code, label ) VALUES ('hu','task_list.table.header.create_to','Létrehozva');
INSERT INTO label ( language, code, label ) VALUES ('hu','task_list.table.header.task_status','Állapot');
INSERT INTO label ( language, code, label ) VALUES ('hu','task_list.table.header.deadline','Határidő');
INSERT INTO label ( language, code, label ) VALUES ('hu','task_list.table.header.end-date','Értékelés vége');
INSERT INTO label ( language, code, label ) VALUES ('hu','task_list.table.header.percentage','Értékeltség');
INSERT INTO label ( language, code, label ) VALUES ('hu','task_list.table.header.score','Eredmény');

INSERT INTO label ( language, code, label ) VALUES ('hu','task_list.table.row.button.view','Megtekintés');
INSERT INTO label ( language, code, label ) VALUES ('hu','task_list.table.row.button.edit','Paraméterezés');
INSERT INTO label ( language, code, label ) VALUES ('hu','task_list.table.row.button.evaluation','Értékelés');
INSERT INTO label ( language, code, label ) VALUES ('hu','task_list.table.row.button.copy','Másolat');
INSERT INTO label ( language, code, label ) VALUES ('hu','task_list.table.row.button.delete','Törlés');

INSERT INTO label ( language, code, label ) VALUES ('hu','task_edit.form.base_data.title','Alapadatok');
INSERT INTO label ( language, code, label ) VALUES ('hu','task_edit.form.base_auto_data.title','Nem szerkeszthető alapadatok');
INSERT INTO label ( language, code, label ) VALUES ('hu','task_edit.form.evaluation.title','Értékelés');
INSERT INTO label ( language, code, label ) VALUES ('hu','task_edit.form.virtues.title','Értékek');
INSERT INTO label ( language, code, label ) VALUES ('hu','task_edit.form.owner','Tulajdonos');
INSERT INTO label ( language, code, label ) VALUES ('hu','task_edit.form.task_status','Állapot');
INSERT INTO label ( language, code, label ) VALUES ('hu','task_edit.form.created_date','Létrehozva');
INSERT INTO label ( language, code, label ) VALUES ('hu','task_edit.form.name','Adja meg a feladat nevét!*');
INSERT INTO label ( language, code, label ) VALUES ('hu','task_edit.form.deadline','Határidő');
INSERT INTO label ( language, code, label ) VALUES ('hu','task_edit.form.description','Leírás');
INSERT INTO label ( language, code, label ) VALUES ('hu','task_edit.form.company_virtues','Vállalati értékek ({{min}} - {{max}})');
INSERT INTO label ( language, code, label ) VALUES ('hu','task_edit.form.leader_virtues','Csoport értékek ({{min}} - {{max}})');
INSERT INTO label ( language, code, label ) VALUES ('hu','task_edit.form.difficulty','Nehézségi szint');
INSERT INTO label ( language, code, label ) VALUES ('hu','task_edit.form.task_factors','Értékelési szempontok ({{min}} - {{max}})');
INSERT INTO label ( language, code, label ) VALUES ('hu','task_edit.form.task_factors.required','Kötelező: ');
INSERT INTO label ( language, code, label ) VALUES ('hu','task_edit.form.evaluators','Értékelők ({{min}} - {{max}})');
INSERT INTO label ( language, code, label ) VALUES ('hu','task_edit.form.button.copy','Másolat készítése');
INSERT INTO label ( language, code, label ) VALUES ('hu','task_edit.form.button.evaluation','Értékelés');
INSERT INTO label ( language, code, label ) VALUES ('hu','task_edit.form.button.start_evaluation','Feladat kész - értékelés elindítása');
INSERT INTO label ( language, code, label ) VALUES ('hu','task_edit.form.notification.start_evaluation.success','A faladat értékelésre átadva');

INSERT INTO label ( language, code, label ) VALUES ('hu','task_view.base_data.title','Alapadatok');
INSERT INTO label ( language, code, label ) VALUES ('hu','task_view.evaluation_result.title','Értékelés eredménye');
INSERT INTO label ( language, code, label ) VALUES ('hu','task_view.evaluation.title','Értékelés');
INSERT INTO label ( language, code, label ) VALUES ('hu','task_view.virtues.title','Vállalati és csoport értékek');
INSERT INTO label ( language, code, label ) VALUES ('hu','task_view.owner','Tulajdonos:');
INSERT INTO label ( language, code, label ) VALUES ('hu','task_view.task_status','Állapot:');
INSERT INTO label ( language, code, label ) VALUES ('hu','task_view.created_date','Létrehozva:');
INSERT INTO label ( language, code, label ) VALUES ('hu','task_view.name','Feladat neve');
INSERT INTO label ( language, code, label ) VALUES ('hu','task_view.deadline','Feladat határideje:');
INSERT INTO label ( language, code, label ) VALUES ('hu','task_view.end_date','Feladat elkészülte:');
INSERT INTO label ( language, code, label ) VALUES ('hu','task_view.description','Leírás:');
INSERT INTO label ( language, code, label ) VALUES ('hu','task_view.company_virtues','Vállalati értékek:');
INSERT INTO label ( language, code, label ) VALUES ('hu','task_view.leader_virtues','Csoport értékek:');
INSERT INTO label ( language, code, label ) VALUES ('hu','task_view.difficulty','Nehézségi szint:');
INSERT INTO label ( language, code, label ) VALUES ('hu','task_view.task_factors','Értékelési szempontok:');
INSERT INTO label ( language, code, label ) VALUES ('hu','task_view.task_factors.required','Kötelező: ');
INSERT INTO label ( language, code, label ) VALUES ('hu','task_view.evaluators','Értékelők:');
INSERT INTO label ( language, code, label ) VALUES ('hu','task_view.evaluation_percentage','Értékeltség:');
INSERT INTO label ( language, code, label ) VALUES ('hu','task_view.score','Eredmény:');
INSERT INTO label ( language, code, label ) VALUES ('hu','task_view.evaluation_end_date','Értékelés vége:');
INSERT INTO label ( language, code, label ) VALUES ('hu','task_view.button.copy','Másolat készítése');

INSERT INTO label ( language, code, label ) VALUES ('hu','task_list.confirm_modal.title','Másolás');
INSERT INTO label ( language, code, label ) VALUES ('hu','task_list.confirm_modal.sub_title','A kiválasztott feladat alapján egy új feladat létrehozása');
INSERT INTO label ( language, code, label ) VALUES ('hu','task_edit.copy.confirm_modal.title','Másolás');
INSERT INTO label ( language, code, label ) VALUES ('hu','task_edit.copy.confirm_modal.sub_title','A kiválasztott feladat alapján egy új feladat létrehozása');

INSERT INTO label ( language, code, label ) VALUES ('hu','main_page.card.in_progress.title','Folyamatban lévő feladataim');
INSERT INTO label ( language, code, label ) VALUES ('hu','main_page.in_progress.table.header.name','Feladat');
INSERT INTO label ( language, code, label ) VALUES ('hu','main_page.in_progress.table.header.create_to','Létrehozva');
INSERT INTO label ( language, code, label ) VALUES ('hu','main_page.in_progress.table.header.deadline','Határidő');
INSERT INTO label ( language, code, label ) VALUES ('hu','main_page.in_progress.table.more_data_link','Összes folyamatban lévő feladatom megtekintése');

INSERT INTO label ( language, code, label ) VALUES ('hu','main_page.card.finished.title','Befejezett feladataim');
INSERT INTO label ( language, code, label ) VALUES ('hu','main_page.finished.table.header.name','Feladat');
INSERT INTO label ( language, code, label ) VALUES ('hu','main_page.finished.table.header.end_date','Értékelés vége');
INSERT INTO label ( language, code, label ) VALUES ('hu','main_page.finished.table.header.task_status','Állapot');
INSERT INTO label ( language, code, label ) VALUES ('hu','main_page.finished.table.header.percentage','Értékeltség');
INSERT INTO label ( language, code, label ) VALUES ('hu','main_page.finished.table.header.score','Eredmény');
INSERT INTO label ( language, code, label ) VALUES ('hu','main_page.finished.table.more_data_link','Összes befejezett feladatom megtekintése');

INSERT INTO label ( language, code, label ) VALUES ('hu','main_page.to_do_list.card.title','Teendőim');
INSERT INTO label ( language, code, label ) VALUES ('hu','main_page.to_do_list.table.header.deadline','Határidő');
INSERT INTO label ( language, code, label ) VALUES ('hu','main_page.to_do_list.table.header.message','Teendő');
INSERT INTO label ( language, code, label ) VALUES ('hu','main_page.to_do_list.table.row.button.execute','Elvégez');
INSERT INTO label ( language, code, label ) VALUES ('hu','main_page.to_do_list.table.more_data_link','Összes nyitott teendőm megtekintése');

INSERT INTO label ( language, code, label ) VALUES ('hu','evaluation.base_data.title','Feladat alapadatai');
INSERT INTO label ( language, code, label ) VALUES ('hu','evaluation.owner','Értékelt:');
INSERT INTO label ( language, code, label ) VALUES ('hu','evaluation.description','Leírás:');
INSERT INTO label ( language, code, label ) VALUES ('hu','evaluation.difficulty','Nehézségi szint:');
INSERT INTO label ( language, code, label ) VALUES ('hu','evaluation.deadline','Értékelés határideje:');
INSERT INTO label ( language, code, label ) VALUES ('hu','evaluation.end_task','Feladat elkészülte:');
INSERT INTO label ( language, code, label ) VALUES ('hu','evaluation.evaluation.title','Értékelési szempontok');
INSERT INTO label ( language, code, label ) VALUES ('hu','evaluation.factor.required','*');
INSERT INTO label ( language, code, label ) VALUES ('hu','evaluation.note','Megjegyzés');
INSERT INTO label ( language, code, label ) VALUES ('hu','evaluation.send','Értékelés beküldése');
INSERT INTO label ( language, code, label ) VALUES ('hu','evaluation.send.success','Értékelés sikeresen beküldve!');

INSERT INTO label ( language, code, label ) VALUES ('hu','rating.rate.main.title','{{userName}} vezetői periódus minősítés: {{period}}');
INSERT INTO label ( language, code, label ) VALUES ('hu','rating.rate.title','Vezetői periódus minősítés');
INSERT INTO label ( language, code, label ) VALUES ('hu','rating.rate.textual_evaluation','Szöveges értékelés');
INSERT INTO label ( language, code, label ) VALUES ('hu','rating.rate.grade_recommendation','Besorolás és juttatás változtatás javaslat');
INSERT INTO label ( language, code, label ) VALUES ('hu','rating.rate.cooperation','További együttműködés az értékelttel');
INSERT INTO label ( language, code, label ) VALUES ('hu','rating.rate.button.rate','Minősítés');

INSERT INTO label ( language, code, label ) VALUES ('hu','to_do_list.card.title','Teendők');
INSERT INTO label ( language, code, label ) VALUES ('hu','to_do_list.table.header.deadline','Határidő');
INSERT INTO label ( language, code, label ) VALUES ('hu','to_do_list.table.header.message','Teendő');
INSERT INTO label ( language, code, label ) VALUES ('hu','to_do_list.table.header.status','Állapot');
INSERT INTO label ( language, code, label ) VALUES ('hu','to_do_list.table.header.task_status','Elvégezve');
INSERT INTO label ( language, code, label ) VALUES ('hu','to_do_list.table.row.button.execute','Elvégzés');

INSERT INTO label ( language, code, label ) VALUES ('hu','notification_list.card.title','Értesítések');
INSERT INTO label ( language, code, label ) VALUES ('hu','notification_list.table.header.created_date','Időpont');
INSERT INTO label ( language, code, label ) VALUES ('hu','notification_list.table.header.subject','Tárgy');
INSERT INTO label ( language, code, label ) VALUES ('hu','notification_list.table.header.body','Leírás');
INSERT INTO label ( language, code, label ) VALUES ('hu','notification_list.table.header.status','Állapot');
INSERT INTO label ( language, code, label ) VALUES ('hu','notification_list.table.row.button.execute','Elvégzés');
INSERT INTO label ( language, code, label ) VALUES ('hu','notification.click_event.information_type','Az értesítés információ jellegű, nem elvégezhető művelet!');
INSERT INTO label ( language, code, label ) VALUES ('hu','notification.click_event.not_open_status','Az értesítéshez tartozó művelet már teljesítve lett!');
INSERT INTO label ( language, code, label ) VALUES ('hu','notification.click_event.unmanaged_type','Az értesítéshez nem tartozik elvégezhető művelet!');

INSERT INTO label ( language, code, label ) VALUES ('hu','report.period.selector','Periódus: ');

INSERT INTO label ( language, code, label ) VALUES ('hu','report.employees.title','Összes dolgozói riport');
INSERT INTO label ( language, code, label ) VALUES ('hu','report.employees.table.title','Összes dolgozói riport');
INSERT INTO label ( language, code, label ) VALUES ('hu','report.employees.table.header.employee.full_name','Értékelt neve');
INSERT INTO label ( language, code, label ) VALUES ('hu','report.employees.table.header.leader.full_name','Vezetője');
INSERT INTO label ( language, code, label ) VALUES ('hu','report.employees.table.header.employee.organization','Csoport');
INSERT INTO label ( language, code, label ) VALUES ('hu','report.employees.table.header.as_leader_organization','Vezetőként csoportja');
INSERT INTO label ( language, code, label ) VALUES ('hu','report.employees.table.header.as_leader_organization_score','Csoportjának pontja');
INSERT INTO label ( language, code, label ) VALUES ('hu','report.employees.table.header.score','Pont');
INSERT INTO label ( language, code, label ) VALUES ('hu','report.employees.table.scoreAvg','Összátlag');

INSERT INTO label ( language, code, label ) VALUES ('hu','report.employee.title','Dolgozói riport');
INSERT INTO label ( language, code, label ) VALUES ('hu','report.evaluated.title','Értékelések');
INSERT INTO label ( language, code, label ) VALUES ('hu','report.employee.user_data.title','Értékelt adatai');
INSERT INTO label ( language, code, label ) VALUES ('hu','report.employee.user_data.employee','Értékelt neve');
INSERT INTO label ( language, code, label ) VALUES ('hu','report.employee.user_data.leader','Vezetője');
INSERT INTO label ( language, code, label ) VALUES ('hu','report.employee.user_data.employee.organization','Csoportja');
INSERT INTO label ( language, code, label ) VALUES ('hu','report.employee.user_data.employee.score','Pont');
INSERT INTO label ( language, code, label ) VALUES ('hu','report.employee.user_data.as_leader.organization','Vezetőként csoportja');
INSERT INTO label ( language, code, label ) VALUES ('hu','report.employee.user_data.as_leader.score','Csoportjának pontja');
INSERT INTO label ( language, code, label ) VALUES ('hu','report.employee.user_data.organization.score','Csoport pont');
INSERT INTO label ( language, code, label ) VALUES ('hu','report.employee.user_data.company.score','Céges átlag');
INSERT INTO label ( language, code, label ) VALUES ('hu','report.employee.user_score_data.title','Periodusra vontakozó pontszámok');

INSERT INTO label ( language, code, label ) VALUES ('hu','report.employee.rating.title','Vezetői periódus minősítés eredménye');
INSERT INTO label ( language, code, label ) VALUES ('hu','report.employee.rating.textual_evaluation','Szöveges minősítés');
INSERT INTO label ( language, code, label ) VALUES ('hu','report.employee.rating.grade_recommendation','Besorolás és juttatás változtatás javaslat');
INSERT INTO label ( language, code, label ) VALUES ('hu','report.employee.rating.cooperation','További együttműködés az értékelttel');
INSERT INTO label ( language, code, label ) VALUES ('hu','report.rating.cooperation.yes','Igen');
INSERT INTO label ( language, code, label ) VALUES ('hu','report.rating.cooperation.no','Nem');

INSERT INTO label ( language, code, label ) VALUES ('hu','report.employee.table.title','Feladatok');
INSERT INTO label ( language, code, label ) VALUES ('hu','report.employee.table.header.name','Feladat');
INSERT INTO label ( language, code, label ) VALUES ('hu','report.employee.table.header.difficulty','Nehézségi szint');
INSERT INTO label ( language, code, label ) VALUES ('hu','report.employee.table.header.created_date','Létrehozva');
INSERT INTO label ( language, code, label ) VALUES ('hu','report.employee.table.header.deadline','Határidő');
INSERT INTO label ( language, code, label ) VALUES ('hu','report.employee.table.header.end_date','Értékelés vége');
INSERT INTO label ( language, code, label ) VALUES ('hu','report.employee.table.header.evaluation_percentage','Értékeltség');
INSERT INTO label ( language, code, label ) VALUES ('hu','report.employee.table.header.score','Pont');
INSERT INTO label ( language, code, label ) VALUES ('hu','report.employee.table.score','Periódus pontszám');
INSERT INTO label ( language, code, label ) VALUES ('hu','report.employee.table.score.details','A feladatok átlaga ({{normalTaskScoreAvg}}) plusz az automatikus feladat után járó pontszám ({{automaticTaskScore}}).');

INSERT INTO label ( language, code, label ) VALUES ('hu','report.evaluation.task.title','Feladat adatai');
INSERT INTO label ( language, code, label ) VALUES ('hu','report.evaluation.task.name','Feladat');
INSERT INTO label ( language, code, label ) VALUES ('hu','report.evaluation.task.period','Periódus');
INSERT INTO label ( language, code, label ) VALUES ('hu','report.evaluation.task.difficulty','Nehézségi szint');
INSERT INTO label ( language, code, label ) VALUES ('hu','report.evaluation.task.createdDate','Létrehozva');
INSERT INTO label ( language, code, label ) VALUES ('hu','report.evaluation.task.deadline','Határidő');
INSERT INTO label ( language, code, label ) VALUES ('hu','report.evaluation.task.endDate','Értékelés vége');
INSERT INTO label ( language, code, label ) VALUES ('hu','report.evaluation.task.evaluationPercentage','Értékeltség');
INSERT INTO label ( language, code, label ) VALUES ('hu','report.evaluation.task.score','Pont');
INSERT INTO label ( language, code, label ) VALUES ('hu','report.evaluation.table.title','Értékelések');
INSERT INTO label ( language, code, label ) VALUES ('hu','report.evaluation.table.header.evaluator_name','Értékelők');
INSERT INTO label ( language, code, label ) VALUES ('hu','report.evaluation.table.header.scoreAvg','Átlag');
INSERT INTO label ( language, code, label ) VALUES ('hu','report.evaluation.table.header.note','Szöveges értékelés');
INSERT INTO label ( language, code, label ) VALUES ('hu','report.evaluation.table.foot.avg','Összátlag');
INSERT INTO label ( language, code, label ) VALUES ('hu','report.evaluation.table.button.show_names','Nevek megjelenítése');
INSERT INTO label ( language, code, label ) VALUES ('hu','report.evaluation.table.button.hide_names','Nevek elrejtése');

INSERT INTO label ( language, code, label ) VALUES ('hu','report.evaluation.table.anonymous','Értékelő');

INSERT INTO label ( language, code, label ) VALUES ('hu','period.table.card.title','Periódusok');
INSERT INTO label ( language, code, label ) VALUES ('hu','period.table.header.name','Neve');
INSERT INTO label ( language, code, label ) VALUES ('hu','period.table.header.start_date','Kezdete');
INSERT INTO label ( language, code, label ) VALUES ('hu','period.table.header.end_date','Vége');
INSERT INTO label ( language, code, label ) VALUES ('hu','period.table.header.rating_end_date','Minősítés vége');
INSERT INTO label ( language, code, label ) VALUES ('hu','period.table.header.status','Állapota');