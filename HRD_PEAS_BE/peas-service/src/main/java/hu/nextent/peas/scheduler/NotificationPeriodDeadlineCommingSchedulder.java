package hu.nextent.peas.scheduler;

import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import hu.nextent.peas.facades.FactoryServiceNotification;
import hu.nextent.peas.jpa.dao.CompanyRepository;
import hu.nextent.peas.jpa.dao.PeriodRepository;
import hu.nextent.peas.jpa.entity.Company;
import hu.nextent.peas.jpa.entity.Period;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@Transactional
public class NotificationPeriodDeadlineCommingSchedulder {
	
	@PersistenceContext
	EntityManager entityManager;
	
	@Autowired
	SchedulerServiceHelper schedulerServiceHelper;
	
	@Autowired
	CompanyRepository companyRepository;
	
	@Autowired
	FactoryServiceNotification factoryServiceNotification; 
	
	@Autowired
	PeriodRepository periodRepository;
	
	@Scheduled(fixedDelay = 3600000) // óránként
	public void scheduleNotificationPeriodDeadlineCommingSchedulder() {
		log.debug("Notification Period Deadline Comming Schedulder");
		List<Company> companies = getAllCompany();
		for(Company company : companies) {
			List<Integer> deadlineDays = getDeadlineDays(company);
			List<Period> periodList = findDeadlinesPeriod(company, deadlineDays);
			if (CollectionUtils.isEmpty(periodList)) {
				continue;
			}
			long cnt = periodList.stream()
							.peek(period -> factoryServiceNotification.createPeriodDeadlineComming(period))
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
		return schedulerServiceHelper.getDeadlinePeriodWarningDays(company);
	}
	
	
	private final static String JPQL_BASE =
			"select p\n"
			+ "from Period p\n"
			+ "where p.company = :company\n"
			+ "      and p.endDate is not null\n"
			+ "      and p.status = 'ACTIVE'\n"
			// Ha már az adott napon küldött üzenete, akkor ne küldjön mégegyet
			+ "      and not exists (\n"
			+ "             select 1 from Notification n\n"
			+ "             where n.period = p\n"
			+ "                   and n.notifacededDay = current_date()\n"
			+ "                   and n.notificationType = 'PERIOD_DEADLINE'\n"
			+ "          )\n"
			+ "      and cast(p.endDate as date) in :datePart\n";
			;
	
	private List<Period> findDeadlinesPeriod(Company company, List<Integer> deadlineDays) {
		TypedQuery<Period> q = entityManager.createQuery(JPQL_BASE, Period.class);
		LocalDate nowDay = LocalDate.now(ZoneOffset.UTC);
		List<Date> datePart = deadlineDays.stream()
				.map(nowDay::minusDays)
				.map(d -> d.atStartOfDay(ZoneOffset.UTC))
				.map(d -> Date.from(d.toInstant()))
				.collect(Collectors.toList());
		q.setParameter("company", company);
		q.setParameter("datePart", datePart);
		return q.getResultList();
	}
	
}
