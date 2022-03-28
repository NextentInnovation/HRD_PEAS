--liquibase formatted sql

INSERT INTO systemparameter (
    code, value, name
) VALUES 
  ( 'secret.user_expire_minute', '1440', 'Web Klien JWT token lejárati ideje 24*60' )
, ( 'secret.secret', '', 'Klien JWT token jelszó' )
, ( 'secret.remote_address_check', '1', 'Vizsgálja a távoli címet a JWT token-ben' )
, ( 'secret.default_company', '', 'Alapértelmezett cég, ezt <név>@<company> login esetén, ha nincs <company megadva>' )
;


INSERT INTO systemparameter (
    code, value, name
) VALUES 
  ( 'page.size', '10', 'Alapértelmezet lapozási méret' ),
  -- Task szerkesztés
  -- Task szerkesztés / Factorok
  ( 'taskeditor.factor.min', '1', 'Taskonként az értékelési szempontok minimális száma' ),
  ( 'taskeditor.factor.max', '5', 'Taskonként az értékelési szempontok maximális száma' ),
  ( 'taskeditor.factor.required.min', '1', 'Taskonként az értékelési a kötelező szempontok minimális száma' ),
  -- Task szerkesztés / Virtue
  ( 'taskeditor.company.virtue.min', '0', 'Taskonként a céges értékek szempontok minimális száma' ),
  ( 'taskeditor.company.virtue.max', '5', 'Taskonként a céges értékek szempontok maximális száma' ),
  ( 'taskeditor.leader.virtue.min', '0', 'Taskonként a csoport értékek szempontok minimális száma' ),
  ( 'taskeditor.leader.virtue.max', '5', 'Taskonként a csoport értékek szempontok maximális száma' ),
  -- Task szerkesztés / értékelő száma
  ( 'taskeditor.user.min', '1', 'Taskonként az értékelő minimális száma' ),
  ( 'taskeditor.user.max', '5', 'Taskonként az értékelő maximális száma' ),
  -- Nyelv
  ( 'default.language', 'hu', 'Alapértelmezett nyelv' ),
  -- Task figyelmeztetés  
  ( 'task.deadline.notification.warning',  	'10,5,3,1', 'Feladat lejárta közeleg, nap tartomány, ennyi nappal elötte figyelmeztessen' ),
  ( 'task.deadline.notification.after.warning',  		'2', 		'Feladat lejárta után ennyi naponként figyelmeztessen' ),
  ( 'task.deadline.warning.day',           	'10',       'Feladat lejárta közeleg, felületen emelje ki' ),
  -- Értékelés
  ( 'evaluation.expired.day', 			   	'14', 'Értékelés határideje, a kezdéshez viszonyatva, napban megadva' ),
  -- Értékelés figyelmeztetés
  ( 'evaluation.deadline.notification.warning',  '5,3,1', 'Értékelés lejárta közeleg, nap tartomány, ennyi nappal elötte figyelmeztessen' ),
  -- Minősítés lejárta
  ( 'rating.expired.day', 					'14', 'Minősítés határideje, a kezdéshez viszonyatva, napban megadva' ),
  -- Minősítés figyelmeztetés
  ( 'rating.deadline.notification.warning', '5,3,1', 'Értékelés lejárta közeleg, nap tartomány, ennyi nappal elötte figyelmeztessen' ),
  -- Periódus
  ( 'period.rating.day',  '14', 'Periódus létrehozásakor az ajánlott minősítési napok száma' ),
  ( 'period.minimal.range.days',  '14', 'Periódus minimum hossza validálásnál' ),
  ( 'period.template.end.days',  '12.31, 03.31, 06.30, 09.30', 'Minta periódus napok mm.dd formában, ehhez kell a kövezetző periódus végét illeszteni' ),
  ( 'period.template.range.days',  '14', 'A minta peri=dus minimum hossza' ),
  ( 'period.template.name',  'Generált periódus: %s - %s', 'Minta periódus neve' ),
  -- Értékelés figyelmeztetés
  ( 'period.deadline.notification.warning',  '5,3,1', 'Periódus lejárta közeleg, nap tartomány, ennyi nappal elötte figyelmeztessen' )
;

INSERT INTO systemparameter (
    code, value, name
) VALUES 
( 'email.enable', 'true', 'Engedélyezett a levél küldése (companyparameter alapértéke) ?'),
( 'email.system.enable', 'true', 'Engedélyezett a levél küldése a rendszerben ?'),
( 'email.test', 'true', 'Test mód, ha igen, akkor a email.test.to-re küldi ki a levelet'),
( 'email.test.to', '', 'Teszteléshez a cél cím, felülírja az eredeti címet'),
( 'email.from', '', 'Levél küldője, ha az email szolgáltató engedi az átírást'),
( 'email.bcc', '', 'BCC cím, ha adott'),
( 'email.cc', '', 'CC cím, ha adott')
;

INSERT INTO notificationaction ( 
	notificationType, createable, sendable, showable
) VALUES 
  ('TASK_DEADLINE', true, true, true),
  ('EVALUATION_START', true, true, true),
  ('EVALUATION_DEADLINE', true, true, true),
  ('EVALUATION_EXPIRED', true, true, true),
  ('EVALUATION_END', true, true, true),
  ('RATING_START', true, true, true),
  ('RATING_DEADLINE', true, true, true),
  ('RATING_EXPIRED', true, true, true),
  ('RATING_END', true, true, true),
  ('PERIOD_DEADLINE', true, true, true),
  ('PERIOD_ACTIVE_CLOSE', true, true, true),
  ('PERIOD_RATING_CLOSE', true, true, true),
  ('PERIOD_ACTIVATED', true, true, true),
  ('LEADER_VIRTUE', true, true, true),
  ('OTHER', true, true, true)
;


