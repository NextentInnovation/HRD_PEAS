package hu.nextent.peas.scheduler;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.Date;
import java.util.List;
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
import hu.nextent.peas.jpa.entity.Evaluation;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@Transactional
public class NotificationEvaluationDeadlineCommingSchedulder {
	
	@PersistenceContext
	EntityManager entityManager;
	
	@Autowired
	SchedulerServiceHelper schedulerServiceHelper;
	
	@Autowired
	CompanyRepository companyRepository;
	
	@Autowired
	FactoryServiceNotification factoryServiceNotification;
	
	@Scheduled(fixedDelay = 3600000) // 1 óránként
	public void scheduleEvaluationDeadlineComming() {
		log.debug("Notification Evaluation Deadline Comming Schedulder");
		LocalDate currentDay = OffsetDateTime.now(ZoneOffset.UTC).toLocalDate();
		List<Company> companies = getAllCompany();
		for(Company company : companies) {
			List<Integer> deadlineDays = getDeadlineDays(company);
			// TODO: Átírni Stream<Evaluation> helyet lapozásra Page<Evaluation>
			Stream<Evaluation> evaluationStream = findDeadlinesEvaluation(company, deadlineDays);
			long cnt = 
					evaluationStream
						.peek(evaluation -> factoryServiceNotification.createEvaluationDeadlineComming(evaluation, currentDay))
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
		return schedulerServiceHelper.getDeadlineEvaluationWarningDays(company);
	}
	
	
	private final static String JPQL_BASE =
			"select e\n"
			+ "from Evaluation e\n"
			+ "where e.company = :company\n"
			+ "      and e.deadline is not null\n"
			+ "      and e.status = 'EVALUATING'\n"
			// Ha már az adott napon küldött üzenete, akkor ne küldjön mégegyet
			+ "      and not exists (\n"
			+ "             select 1 from Notification n\n"
			+ "             where n.evaluation = e\n"
			+ "                   and n.notifacededDay = current_date()\n"
			+ "                   and n.notificationType = 'EVALUATION_DEADLINE'\n"
			+ "          )\n"
			+ "      and cast(e.deadline as date) in :datePart\n";
			;
	
	Stream<Evaluation> findDeadlinesEvaluation(Company company, List<Integer> deadlineDays) {
		
		TypedQuery<Evaluation> q = entityManager.createQuery(JPQL_BASE, Evaluation.class);
		LocalDate nowDay = LocalDate.now(ZoneOffset.UTC);
		List<Date> datePart = deadlineDays.stream()
				.map(nowDay::minusDays)
				.map(d -> d.atStartOfDay(ZoneOffset.UTC))
				.map(d -> Date.from(d.toInstant()))
				.collect(Collectors.toList());
		q.setParameter("company", company);
		q.setParameter("datePart", datePart);
		return q.getResultStream();
	}


}
