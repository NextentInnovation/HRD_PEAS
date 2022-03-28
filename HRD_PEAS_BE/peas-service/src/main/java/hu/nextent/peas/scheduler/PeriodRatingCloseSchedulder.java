package hu.nextent.peas.scheduler;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import hu.nextent.peas.facades.FactoryServiceNotification;
import hu.nextent.peas.jpa.dao.CompanyRepository;
import hu.nextent.peas.jpa.dao.PeriodRepository;
import hu.nextent.peas.jpa.dao.RatingRepository;
import hu.nextent.peas.jpa.entity.Company;
import hu.nextent.peas.jpa.entity.Period;
import hu.nextent.peas.jpa.entity.PeriodStatusEnum;
import hu.nextent.peas.jpa.entity.RatingStatusEnum;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@Transactional
public class PeriodRatingCloseSchedulder {

	@PersistenceContext
    private EntityManager em;
	
	@Autowired
	private CompanyRepository companyRepository;

	@Autowired
	private PeriodRepository periodRepository;

	@Autowired
	private RatingRepository ratingRepository;

	@Autowired
	private FactoryServiceNotification factoryServiceNotification;
	
	@Scheduled(fixedDelay = 3600000) // óránként
	public void scheduleFixedDelayTask() {
		log.debug("PeriodRatingCloseSchedulder");
		// TODO Ide kell valami lokkolás, hogy más ütemezett task ne zavarjon be
		OffsetDateTime now = OffsetDateTime.now(ZoneOffset.UTC);
		List<Company> companies = getAllCompany();
		for(Company company : companies) {
			Period ratingPeriod = getRatingPeriod(company);
			if (ratingPeriod == null) {
				log.debug("PeriodRatingCloseSchedulder, not founded rating period, continue, company: \"{}\" {}", company.getName(), company.getId());
				// Nincs minősítési periódus, ki lehet lépni
				continue;
			}
			
			if (ratingPeriod.getEndDate() == null) {
				log.warn("PeriodRatingCloseSchedulder, period.endDate is null, continue, company: \"{}\" {}, period: \"{}\" {}", company.getName(), company.getId(), ratingPeriod.getName(), ratingPeriod.getId());
			}

			if (hasOpenRating(ratingPeriod)) {
				// Ha van még OPEN státuszú minősítés, akkkor
				// a CloseExpiredRatingScheduler vagy a minősítés nem zárta le őket
				// emiatt a tényleges zárást el kell még halasztani
				log.debug("PeriodRatingCloseSchedulder, founded open rating, continue, company: \"{}\" {}, period: \"{}\" {}", company.getName(), company.getId(), ratingPeriod.getName(), ratingPeriod.getId());
				continue;
			}
			
			// Egyszerre csak egy céget zár le
			boolean close = false;
			if (ratingPeriod.getEndDate().isBefore(now)) {
				log.debug("rating close period, company: \"{}\" {}, period: \"{}\" {}", company.getName(), company.getId(), ratingPeriod.getName(), ratingPeriod.getId());
				close = true;
				// Lezárja a minősítéseket
				int cnt = closeRatings(ratingPeriod);
				log.debug("Period {}, Close Rating {}. cnt", ratingPeriod.getName(), cnt);
				// Lezárja a periódust
				closeRatingPeriod(ratingPeriod);
				log.debug("Period {}, Rating Closed", ratingPeriod.getName());	
				// Generál notifikációkat a rating zárásához
				cnt = generateRatingCloseNotifictaions(ratingPeriod);
				log.debug("Period {}, Generate Rating Close Notifications {}. cnt", ratingPeriod.getName(), cnt);	
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
	
	private Period getRatingPeriod(Company company) {		
		return periodRepository.findByCompanyAndStatus(company, PeriodStatusEnum.RATING).orElse(null);
	}
	
	
	/**
	 * Visgálja, hogy van-e még nyitott minődítés
	 * Ha van, akkor true értéket ad vissza
	 * @param ratingPeriod
	 * @return
	 */
	boolean hasOpenRating(Period ratingPeriod) {
		return ratingRepository.countByPeriodAndStatus(ratingPeriod, RatingStatusEnum.OPEN) != 0;
	}
	
	/**
	 * Csak az EVALUATED állapotban lévő értékeléseket kell lezárni, mert az EXPIRED állapot már vég állapotnak számít
	 * @param ratingPeriod: Minősítési periódus
	 * @return Lezárt minősítések
	 */
	int closeRatings(Period ratingPeriod) {
		// TODO áttenni a repository-ba modosító lekérdezésbe 
		
		String jpqlCloseRating = 
				"UPDATE Rating r\n"
				+ "SET r.status = 'CLOSE'\n"
				+ "    , r.version = r.version + 1\n"
				+ "WHERE r.status in ('EVALUATED', 'OPEN')\n"
				+ "      AND r.period = :period";
		
		Query query = em
				.createQuery(jpqlCloseRating)
				.setParameter("period", ratingPeriod)
				;
				
		int cnt = query.executeUpdate();
		
		if (cnt > 0) {
			em.flush();
		}
		
		return cnt;
	}


	void closeRatingPeriod(Period ratingPeriod) {
		ratingPeriod.setStatus(PeriodStatusEnum.CLOSED);
		periodRepository.save(ratingPeriod);
	}

	int generateRatingCloseNotifictaions(Period ratingPeriod) {
		return factoryServiceNotification.createPeriodRatingClose(ratingPeriod).size();
	}


}
