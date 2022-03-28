package hu.nextent.peas.scheduler;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import hu.nextent.peas.constant.TodoLabelConstant;
import hu.nextent.peas.facades.FactoryServiceNotification;
import hu.nextent.peas.facades.PeriodGenerator;
import hu.nextent.peas.jpa.dao.CompanyRepository;
import hu.nextent.peas.jpa.dao.DifficultyRepository;
import hu.nextent.peas.jpa.dao.FactorRepository;
import hu.nextent.peas.jpa.dao.NotificationRepository;
import hu.nextent.peas.jpa.dao.PeriodRepository;
import hu.nextent.peas.jpa.dao.TaskRepository;
import hu.nextent.peas.jpa.dao.UserRepository;
import hu.nextent.peas.jpa.entity.Company;
import hu.nextent.peas.jpa.entity.Difficulty;
import hu.nextent.peas.jpa.entity.Factor;
import hu.nextent.peas.jpa.entity.NotificationStatusEnum;
import hu.nextent.peas.jpa.entity.NotificationTypeEnum;
import hu.nextent.peas.jpa.entity.Period;
import hu.nextent.peas.jpa.entity.PeriodStatusEnum;
import hu.nextent.peas.jpa.entity.ReferenceTypeEnum;
import hu.nextent.peas.jpa.entity.RoleEnum;
import hu.nextent.peas.jpa.entity.Task;
import hu.nextent.peas.jpa.entity.TaskStatusEnum;
import hu.nextent.peas.jpa.entity.TaskTypeEnum;
import hu.nextent.peas.jpa.entity.TaskXFactor;
import hu.nextent.peas.jpa.entity.User;
import lombok.val;
import lombok.extern.slf4j.Slf4j;

/**
 * Automatikus periódus zárás
 * Feladata:
 * 	- Lezárja az aktuális feladatot, ha az endDate kissebb, mint a kurrens dátum
 * 		- Nem kell mert más ütemező gondoskodik erről
 * 	- Megnyitja a vezetői értékelés tartományt
 * 	- Kalkulálja a vezetői értékelésekhez a pontszámokat 
 * 	- Elküld egy figyelmeztetést a zárás tényéről a BUSINESS_ADMIN-nak
 * 	- Ha nincs megnyitható periódus, akkor létrehoz egyet
 * 	- Aktiválja a következő periódust
 * 	- Az aktivált periódushoz automatikus taskokat vesz fel
 *
 */
@Slf4j
@Service
@Transactional
public class PeriodActiveCloseAndActivatedNextSchedulder {

	@PersistenceContext
    private EntityManager em;
	
	@Autowired
	private CompanyRepository companyRepository;

	@Autowired
	private PeriodRepository periodRepository;
	
	@Autowired
	private FactoryServiceNotification factoryServiceNotification;
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private TaskRepository taskRepository;

	@Autowired
	private DifficultyRepository difficultyRepository;
	
	@Autowired
	private FactorRepository factorRepository;
	
	@Autowired
	private PeriodGenerator periodGenerator;
	
	@Autowired
	private NotificationRepository notificationRepository;
	
	@Scheduled(fixedDelay = 3600000) // óránként
	public void scheduleFixedDelayTask() {
		log.debug("PeriodActiveCloseAndActivatedNextService");
		// TODO Ide kell valami lokkolás, hogy más ütemezett task ne zavarjon be
		OffsetDateTime now = OffsetDateTime.now(ZoneOffset.UTC);
		List<Company> companies = getAllCompany();
		for(Company company : companies) {
			Period activePeriod = getActivePeriod(company);
			// Egyszerre csak egy céget zár le
			boolean close = false;
			if (activePeriod.getEndDate().isBefore(now)) {
				log.debug("close period, company: \"{}\" {}, period> \"{}\" {}", company.getName(), company.getId(), activePeriod.getName(), activePeriod.getId());
				close = true;
				int cnt = 0;
				// Nyitott Automatikus Taskok lezárása, amely ehhez a lezárandó periódushoz tartoznak
				cnt = closeAutomaticTasks(activePeriod, now);
				log.debug("Period {}, Close {}. cnt automatic task", activePeriod.getName(), cnt);
				// Minősítést generál
				cnt = createRatings(activePeriod);
				log.debug("Period {}, Create Rating {}. cnt", activePeriod.getName(), cnt);
				// Minősítést feltölti értékekkel 
				// Automatikus taskoks átlag pontjai
				cnt = automaticTaskScoreStoreToRating(activePeriod);
				log.debug("Period {}, Create Fill rating automaticTaskScore {}. cnt", activePeriod.getName(), cnt);
				// Normál taskoks átlag pontjai
				cnt = normalTaskScoreStoreToRating(activePeriod);
				log.debug("Period {}, Create Fill rating normalTaskScore {}. cnt", activePeriod.getName(), cnt);
				// Automatikus és Normál taskok összege
				cnt = periodScoreStoreToRating(activePeriod);
				log.debug("Period {}, Create Fill rating periodScore {}. cnt", activePeriod.getName(), cnt);
				// Minősítéshez ToDo létrehozása
				cnt = createRatingToDos(activePeriod);
				log.debug("Period {}, Create Rating todos {}. cnt", activePeriod.getName(), cnt);
				// Következő periódus keresése vagy létrehozása				
				Period nextPeriod = getOrCreateNextPeriod(activePeriod);
				log.debug("Get Or Create {} period", nextPeriod.getName());
				// Automatikus taskok generálása
				cnt = createAutomaticTasks(nextPeriod);
				log.debug("Period {}, Create {}. cnt automatic task", nextPeriod.getName(), cnt);
				// Periódus aktiválása régi zárása
				closeActivePeriod(activePeriod);
				log.debug("Period {}, Closed", activePeriod.getName());				
				// Nortifikáció küldése a zárásról
				cnt = generateNotificationToClosePeriod(activePeriod);
				log.debug("Period {}, Generate Closed notification {}. cnt", activePeriod.getName(), cnt);				
				// Nortifikáció PERIOD_DEADLINE zárása
				cnt = closePeriodDeadlineNotifications(activePeriod);
				log.debug("Period {}, Closed PERIOD_DEADLINE notifications {}. cnt", activePeriod.getName(), cnt);			
				// Periódus aktiválása következő nyitása
				activateNextPeriod(nextPeriod);
				log.debug("Period {}, Activated", nextPeriod.getName());				
				// Nortifikáció küldése a nyitásról
				cnt = generateNotificationToActivatedPeriod(nextPeriod);
				log.debug("Period {}, Generate Activated notification {}. cnt", nextPeriod.getName(), cnt);				
			}
			if (close) {
				// Ha megtörtént egy periódus zárása, akkor a következő zárás egy másik ütemezési időszakban történhet
				break;
			}
		}
	}
	
	private List<Company> getAllCompany() {
		return companyRepository.findAllByActiveTrue();
	}
	
	private Period getActivePeriod(Company company) {		
		return periodRepository.findByCompanyAndStatus(company, PeriodStatusEnum.ACTIVE).orElse(null);
	}
	
	/**
	 * Az adott periódus összes automatikus taskjának a lezárása
	 * 
	 * @param activePeriod: Vizsgált periódus
	 * @param endDate: Ezzel a dátummal zárja le az automatikus taskokat
	 * @return: Lezárt automatikus taskok száma
	 */
	int closeAutomaticTasks(Period activePeriod, OffsetDateTime endDate) {
		// TODO áttenni a repository-ba modosító lekérdezésbe 
		Query query = em.createNamedQuery("Task.AutomaticTaskClose")
				.setParameter("activePeriod", activePeriod)
				.setParameter("endDate", endDate)
				;
		
		int cnt = query.executeUpdate();
		
		if (cnt > 0) {
			em.flush();
		}
		return cnt;
	}

	
	
	/**
	 * Minősítések létrehozása
	 * Mindegyik felhasználóhoz létrehoz egy minősítést az adott periódushot
	 * 
	 * Natív SQL Insert segítségével hozom létre a minősítéseket
	 * Előnye, hogy gyors, hátránya, hogy ha változik a JPA entitás, akkor ez hibára futhat
	 * 
	 * @param activePeriod: Periódus, amelyhez létre kell hozni a minősítéseket
	 * @return Létrehozott minősítések száma
	 */
	int createRatings(Period activePeriod) {
		String insertSql = 
				"INSERT INTO rating(company_id, period_id, user_id, leader_id, status, deadline)\n"
				+ "SELECT :companyId, :periodId, u.id, u.leader_id, 'OPEN', :deadline\n"
				+ "FROM user u\n"
				+ "WHERE u.company_id = :companyId\n"
				+ "      AND EXISTS (\n"
				+ "			SELECT 1\n"
				+ "			FROM userxrole uxr join role r on (uxr.role_id = r.id)\n"
				+ "			WHERE uxr.user_id = u.id\n"
				+ "				  AND r.name = :leaderRole\n"
				+ "			)\n"
				+ "      AND u.leader_id is not null\n"
				;
		
		Query query = em.createNativeQuery(insertSql);
		query.setParameter("companyId", activePeriod.getCompany().getId());
		query.setParameter("periodId", activePeriod.getId());
		query.setParameter("deadline", activePeriod.getEndDate());
		query.setParameter("leaderRole", RoleEnum.USER.name());
		
		int cnt = query.executeUpdate();
		if (cnt > 0) {
			em.flush();
		}
		return cnt;
	}

	int createRatingToDos(Period activePeriod) {
		String insertSql = 
				"INSERT INTO todo(company_id, period_id, to_user_id, status, toDoType, deadline, rating_id, messagecode, referencetype)\n"
				+ "SELECT :companyId, :periodId, r.leader_id, 'OPEN', 'RATING', r.deadline, r.id, :messagecode, :referencetype\n"
				+ "FROM rating r\n"
				+ "WHERE r.period_id = :periodId\n"
				;
		
		Query query = em.createNativeQuery(insertSql);
		query.setParameter("companyId", activePeriod.getCompany().getId());
		query.setParameter("periodId", activePeriod.getId());
		query.setParameter("messagecode", TodoLabelConstant.TODO_SUMMON_RATING);
		query.setParameter("referencetype", ReferenceTypeEnum.RATING.name());
		
		int cnt = query.executeUpdate();
		if (cnt > 0) {
			em.flush();
		}
		return cnt;
	}

	/**
	 * Az adott periódushoz tartozó automatikus taskok értékének átlagát felhasználókként a minősítés automaticTaskScore értékébe menti
	 * 
	 * Itt feltételezzük, hogy az automatikus taskok rendben létrejöttek, és a hozzá tartozó értékelések rendben le elettek zárva
	 * 
	 * @param activePeriod: Vizsgált periódus
	 * @return Feltöltött minősítések száma
	 */
	int automaticTaskScoreStoreToRating(Period activePeriod) {
		// a, Automatikus taskok értékelésének beírása
		// Feltételezve, hogy az automatikus taskok rendben lezárva és pontozva
		
		// TODO áttenni a repository-ba modosító lekérdezésbe 
		
		String jpqlScore = 
				"UPDATE Rating r\n"
				+ "SET r.automaticTaskScore = coalesce(\n"
				+ "    (\n"
				+ "    SELECT avg(t.score)\n"
				+ "    FROM Task t\n"
				+ "    WHERE t.period = r.period\n"
				+ "          AND t.owner = r.user\n"
				+ "          AND t.taskType = :automaticType\n"
				+ "          AND t.status in :validTaskStatus\n"
				+ "		)\n"
				+ "     , 0)\n"
				+ "     , r.version = r.version + 1\n"
				+ "WHERE r.period = :period\n"
				;
		
		Query queryScore = em.createQuery(jpqlScore);
		queryScore.setParameter("period", activePeriod);
		queryScore.setParameter("automaticType", TaskTypeEnum.AUTOMATIC);
		queryScore.setParameter("validTaskStatus", Arrays.asList(TaskStatusEnum.CLOSED, TaskStatusEnum.EVALUATED));
		
		int cnt = queryScore.executeUpdate();
		if (cnt > 0) {
			em.flush();
		}
		
		return cnt;
	}
	
	/**
	 * Az adott periódushoz tartozó normál taskok értékének átlagát felhasználókként a minősítés automaticTaskScore értékébe menti.
	 * 
	 * Itt feltételezzük, hogy az értékelések rendben le lettek zárva az adott tasknál, amelyiknél ez nem történt meg, majd a következő periódusban 
	 * lesznek használva.
	 * 
	 * @param activePeriod: Vizsgált periódus
	 * @return Feltöltött minősítések száma
	 */
	int normalTaskScoreStoreToRating(Period activePeriod) {
		// b, Elvégzett értékelések értékelésének beírása
		// Feltételezve, hogy a taskok rendben lezárva és pontozva
		
		// TODO áttenni a repository-ba modosító lekérdezésbe 
		String jpqlScore = 
				"UPDATE Rating r\n"
				+ "SET r.normalTaskScore = coalesce(\n"
				+ "     (\n"
				+ "		SELECT avg(t.score)\n"
				+ "		FROM Task t\n"
				+ "    	WHERE t.period = r.period\n"
				+ "			  and t.owner = r.user\n"
				+ "			  and t.taskType = :normalType\n"
				+ "			  and t.status in :validTaskStatus\n"
				+ "		)\n"
				+ "     ,0)\n"
				+ "     , r.version = r.version + 1\n"
				+ "WHERE r.period = :period\n"
				;
		
		Query queryScore = em.createQuery(jpqlScore);
		queryScore.setParameter("period", activePeriod);
		queryScore.setParameter("normalType", TaskTypeEnum.NORMAL);
		queryScore.setParameter("validTaskStatus", Arrays.asList(TaskStatusEnum.CLOSED, TaskStatusEnum.EVALUATED));
		
		int cnt = queryScore.executeUpdate();
		if (cnt > 0) {
			em.flush();
		}
		
		return cnt;
	}
	
	
	/**
	 * Az adott periódushoz tartozó normál taskok értékének átlagát és automatikus taskok értékének az átlagnak az összege
	 * 
	 * @param activePeriod: Vizsgált periódus
	 * @return Feltöltött minősítések száma
	 */

	int periodScoreStoreToRating(Period activePeriod) {
		// a, Automatikus taskok értékelésének beírása
		// b, Elvégzett értékelések értékelésének beírása
		// a+b
		
		// TODO áttenni a repository-ba modosító lekérdezésbe 
		String jpql = 
				"UPDATE Rating r\n"
				+ "SET r.periodScore = coalesce(r.automaticTaskScore,0) + coalesce(r.normalTaskScore,0)\n"
				+ "     , r.version = r.version + 1\n"
				+ "WHERE r.period = :period\n"
				;
		
		Query query = em.createQuery(jpql);
		query.setParameter("period", activePeriod);
		
		int cnt = query.executeUpdate();
		if (cnt > 0) {
			em.flush();
		}
		return cnt;
	}

	
	/**
	 * A következő periódus keresése, amit meg kell nyitni.
	 * Ha nincs következő periódus, akkor újjat kell készíteni
	 * 
	 * @param activePeriod: Elöző aktív periódus
	 * @return: Következő periódus
	 */
	Period getOrCreateNextPeriod(Period activePeriod) {
		// Keresem a következő periódust
		// Az a következő, akinek a startDate-je az aktív periódus után van
		
		Optional<Period> optNextPeriod = periodRepository.findFirstByCompanyAndStatusAndStartDateGreaterThanOrderByStartDateAsc(
				activePeriod.getCompany(),
				PeriodStatusEnum.OPEN,
				activePeriod.getStartDate()
				);
		
		// Van megfelelő periódus
		if (optNextPeriod.isPresent()) {
			return optNextPeriod.get();
		}
		
		// Ha nincs megfelelő periódus, akkor készíteni kell egyet
		
		Period nextPeriod = periodGenerator.nextPeriodGenerator(activePeriod.getCompany());
		
		periodRepository.save(nextPeriod);
		periodRepository.flush();
		
		return nextPeriod;
	}

	/**
	 * Új automatikus taskok létrehozása a periódushoz
	 * 
	 * Ez az automatikus taskok előfeltöltése, mert értékelés zárásakor "biztonsági játék" miatt, ha nincs, akkor létrehozunk egyet
	 * 
	 * @param nextPeriod: Viszgált periódus, amnely számára létre kell hozni az automatikus taskokat
	 * @return: Létrejött automatikus taszkok száma
	 */
	int createAutomaticTasks(Period nextPeriod) {
		Pageable page = PageRequest.of(0, 100);
		
		Company checkedCompany = nextPeriod.getCompany();
		Page<User> pageUser = userRepository.findAllByCompanyAndActiveIsTrueOrderById(checkedCompany, page);
		if (pageUser.isEmpty()) {
			return 0;
		}
		
		// Automatikus nehézség
		Optional<Difficulty> optDifficultyAutomatic = difficultyRepository.findByActiveTrueAndAutomaticTrueAndCompany(checkedCompany);
		Difficulty difficultyAutomatic = optDifficultyAutomatic.get();
		Optional<Factor> optFactorAutomatic = factorRepository.findByActiveTrueAndAutomaticTrueAndCompany(checkedCompany);
		Factor factorAutomatic = optFactorAutomatic.get();
		
		
		int cnt = 0;
		while(pageUser.hasContent()) {
			List<Task> automaticTasks = 
					pageUser.stream()
						.map(
								user -> 
									createOneAutomaticTask(
											nextPeriod, 
											user, 
											difficultyAutomatic, 
											factorAutomatic
											)
							)
						.collect(Collectors.toList());
			
			taskRepository.saveAll(automaticTasks);
			cnt += automaticTasks.size();
			
			pageUser = userRepository.findAllByCompanyAndActiveIsTrueOrderById(checkedCompany, page.next());
		}
		
		
		return cnt;
	}
	
	private Task createOneAutomaticTask(
			Period period, 
			User user, 
			Difficulty difficultyAutomatic, 
			Factor factorAutomatic
	) {
		Task automaticTask = Task.builder()
				.status(TaskStatusEnum.UNDER_EVALUATION)
				.taskType(TaskTypeEnum.AUTOMATIC)
				.name(TaskTypeEnum.AUTOMATIC.name())
				.startDate(period.getStartDate())
				.endDate(period.getEndDate())
				.deadline(period.getEndDate())
				.evaluaterCount(0)
				.evaluatedCount(0)
				.evaluationPercentage(BigDecimal.valueOf(100l))
				.owner(user)
				.period(period)
				.difficulty(difficultyAutomatic)
				.company(period.getCompany())
				.taskXFactors(new ArrayList<TaskXFactor>())
				.build();
		
		TaskXFactor taskXFactor = TaskXFactor.builder()
				.factor(factorAutomatic)
				.task(automaticTask)
				.build();
		
		automaticTask.getTaskXFactors().add(taskXFactor);
		
		return automaticTask;
		
	}
	
	/**
	 * Aktiv periódus átállítása RATING-be
	 * @param activePeriod: aktív periódus
	 */
	void closeActivePeriod(Period activePeriod) {
		activePeriod.setStatus(PeriodStatusEnum.RATING);
		periodRepository.save(activePeriod);
		periodRepository.flush();
	}

	/**
	 * Következő periódus átállítása ACTIVE-ba
	 * @param activePeriod: következő periódus
	 */
	private void activateNextPeriod(Period nextPeriod) {
		nextPeriod.setStatus(PeriodStatusEnum.ACTIVE);
		periodRepository.save(nextPeriod);
		periodRepository.flush();
	}

	/**
	 * Figyelmeztetés küldése a BL és HR felhasználóknak, hogy a periódus lezárás alatt van
	 * 
	 * @param activePeriod: Visgált periódus
	 * @param now: Lezárás dátuma
	 */
	int generateNotificationToClosePeriod(Period activePeriod) {
		// Üzenet küldése az összes HR és BL felhasználónak
		return factoryServiceNotification.createPeriodActiveClosed(activePeriod).size();
	}
	
	int closePeriodDeadlineNotifications(Period activePeriod) {
		val notifications = notificationRepository.findAllByPeriodAndNotificationType(activePeriod, NotificationTypeEnum.PERIOD_DEADLINE);
		notifications.stream().forEach(n -> n.setStatus(NotificationStatusEnum.CLOSE));
		notificationRepository.saveAll(notifications);
		return notifications.size();
	}

	/**
	 * Figyelmeztetés küldése a BL és HR felhasználóknak, hogy a periódus nyitva van
	 * 
	 * @param activePeriod: Visgált periódus
	 * @param now: Lezárás dátuma
	 */
	int generateNotificationToActivatedPeriod(Period nextPeriod) {
		// Üzenet küldése az összes HR és BL felhasználónak
		return factoryServiceNotification.createPeriodActiveted(nextPeriod).size();
	}	
}