package hu.nextent.peas.scheduler;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import hu.nextent.peas.facades.FactoryServiceNotification;
import hu.nextent.peas.jpa.dao.CompanyRepository;
import hu.nextent.peas.jpa.entity.Company;
import hu.nextent.peas.jpa.entity.Task;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@Transactional
public class NotificationTaskDeadlineCommingSchedulder {
	
	@PersistenceContext
	EntityManager entityManager;
	
	@Autowired
	SchedulerServiceHelper schedulerServiceHelper;
	
	@Autowired
	CompanyRepository companyRepository;
	
	@Autowired
	FactoryServiceNotification factoryServiceNotification; 
	
	@Scheduled(fixedDelay = 3600000) // óránként
	public void scheduleNotificationTaskDeadlineComming() {
		log.debug("Notification Task Deadline Comming Schedulder");
		OffsetDateTime checkedDateTime = OffsetDateTime.now(ZoneOffset.UTC);
		LocalDate currentDay = checkedDateTime.toLocalDate();
		List<Company> companies = getAllCompany();
		for(Company company : companies) {
			List<Integer> deadlineDays = getDeadlineDays(company);
			Integer afterDeadlineDay = getDeadlineAfterDays(company);
			Stream<Task> taskStream = findDeadlinesOrExpiredTask(company, deadlineDays, currentDay);
			// TODO Átírni Stream-ből Page-re
			long cnt = taskStream
					.filter(task -> task.getDeadline() != null)
					.filter(
							task -> 
								task.getDeadline().isBefore(checkedDateTime)
								|| this.afterDeadlineFilter(
										task,
										afterDeadlineDay,
										checkedDateTime
										)
						)
					.peek(task -> factoryServiceNotification.createTaskDeadlineComming(task, currentDay))
					.count();
			if (cnt > 0) {
				entityManager.flush();
			}
		}
	}
	
	List<Company> getAllCompany() {
		return companyRepository.findAllByActiveTrue();
	}
	
	List<Integer> getDeadlineDays(Company company) {
		return schedulerServiceHelper.getDeadlineTaskWarningDays(company);
	}
	
	Integer getDeadlineAfterDays(Company company) {
		return schedulerServiceHelper.getDeadlineAfterTaskWarningDays(company);
	}
	
	
	private final static String JPQL_BASE =
			"select t\n"
			+ "from Task t\n"
			+ "where t.company = :company\n"
			+ "      and t.deadline is not null\n"
			+ "      and t.status = 'PARAMETERIZATION'\n"
			+ "      and t.taskType = 'NORMAL'\n"
			// Ha már az adott napon küldött üzenete, akkor ne küldjön mégegyet
			+ "      and not exists (\n"
			+ "             select 1 from Notification n\n"
			+ "             where n.task = t\n"
			+ "                   and n.notifacededDay = current_date()\n"
			+ "                   and n.notificationType = 'TASK_DEADLINE'\n"
			+ "          )\n"
			;
	
	private final static String WITH_DATE =
			"and (t.deadline >= current_timestamp() or cast(t.deadline as date) in :datePart)\n";
	
	private final static String WITHOUT_DATE =
			"and t.deadline >= current_timestamp()\n";
	
	Stream<Task> findDeadlinesOrExpiredTask(
			Company company, 
			List<Integer> deadlineDays,
			LocalDate currentDay
			) {
		TypedQuery<Task> q = null;
		
		Map<String, Object> parameters = new HashMap<>();
		parameters.put("company", company);
		String jpql = JPQL_BASE;
		if (deadlineDays != null && !deadlineDays.isEmpty()) {
			jpql += WITH_DATE;
			List<Date> datePart = deadlineDays.stream()
					.map(currentDay::minusDays)
					.map(d -> d.atStartOfDay(ZoneOffset.UTC))
					.map(d -> Date.from(d.toInstant()))
					.collect(Collectors.toList());
			parameters.put("datePart", datePart);
		} else {
			jpql += WITHOUT_DATE;
		}
		
		q = entityManager.createQuery(jpql, Task.class);
		for(Map.Entry<String, Object> entry: parameters.entrySet()) {
			q.setParameter(entry.getKey(), entry.getValue());
		}
		return q.getResultStream();
	}
	
	
	boolean afterDeadlineFilter(
			Task task, 
			Integer afterDeadlineDay,
			OffsetDateTime checkedDateTime
			) {
		
		// Kell vizsgálni a következő napokat
		if (task.getDeadline().isBefore(checkedDateTime) || task.getDeadline().isEqual(checkedDateTime)) {
			// Ha a task határideje elött vagyunk, akkor még nem ezzel kell vizsgálni, tehet mehet tovább
			return true;
		}
		
		// Van értelmezhető paraméterünk ?
		if (afterDeadlineDay == null || afterDeadlineDay <= 1) {
			// Ha nincs, akkor mehet tovább
			return true;
		}
		
		// A most és a határidő közötti külömbség napban
		long diffDays = ChronoUnit.DAYS.between(task.getDeadline(), checkedDateTime);
		// modulus osztás, hogy megfelelő-e
		long check = diffDays % afterDeadlineDay;
		
		// Ha nics maradék, akkor igaz, ha van, akkor ki van szűrve
		return check == 0L;
	}
	
	
}
