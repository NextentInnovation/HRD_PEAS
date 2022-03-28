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
import hu.nextent.peas.jpa.entity.Rating;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@Transactional
public class NotificationRatingDeadlineCommingSchedulder {
	
	@PersistenceContext
	EntityManager entityManager;
	
	@Autowired
	SchedulerServiceHelper schedulerServiceHelper;
	
	@Autowired
	CompanyRepository companyRepository;
	
	@Autowired
	FactoryServiceNotification factoryServiceNotification;
	
	@Scheduled(fixedDelay = 3600000) // óránként
	public void scheduleRatingDeadlineComming() {
		log.debug("Notification Rating Deadline Comming Schedulder");
		LocalDate currentDay = OffsetDateTime.now(ZoneOffset.UTC).toLocalDate();
		List<Company> companies = getAllCompany();
		for(Company company : companies) {
			List<Integer> deadlineDays = getDeadlineDays(company);
			// TODO: Átírni Stream<Evaluation> helyet lapozásra Page<Evaluation>
			Stream<Rating> evaluationStream = findDeadlinesRating(company, deadlineDays);
			long cnt = 
					evaluationStream
						.peek(
								rating -> factoryServiceNotification.createRatingDeadlineComming(rating, currentDay))
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
		return schedulerServiceHelper.getDeadlineRatingWarningDays(company);
	}
	
	
	private final static String JPQL_BASE =
			"select r\n"
			+ "from Rating r\n"
			+ "where r.company = :company\n"
			+ "      and r.deadline is not null\n"
			+ "      and r.status = 'OPEN'\n"
			// Ha már az adott napon küldött üzenete, akkor ne küldjön mégegyet
			+ "      and not exists (\n"
			+ "             select 1 from Notification n\n"
			+ "             where n.rating = r\n"
			+ "                   and n.notifacededDay = current_date()\n"
			+ "                   and n.notificationType = 'RATING_DEADLINE'\n"
			+ "          )\n"
			+ "      and cast(r.deadline as date) in :datePart\n";
			;
			
	Stream<Rating> findDeadlinesRating(Company company, List<Integer> deadlineDays) {
		TypedQuery<Rating> q = entityManager.createQuery(JPQL_BASE, Rating.class);
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
