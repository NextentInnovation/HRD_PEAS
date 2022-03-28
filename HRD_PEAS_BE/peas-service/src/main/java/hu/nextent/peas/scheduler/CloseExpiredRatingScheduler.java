package hu.nextent.peas.scheduler;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.stream.Stream;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import hu.nextent.peas.facades.AutomaticTaskRatingAddRatingFacade;
import hu.nextent.peas.facades.FactoryServiceNotification;
import hu.nextent.peas.facades.ToDoEvaluationCloserFacade;
import hu.nextent.peas.jpa.dao.CompanyRepository;
import hu.nextent.peas.jpa.dao.NotificationRepository;
import hu.nextent.peas.jpa.dao.RatingRepository;
import hu.nextent.peas.jpa.entity.Company;
import hu.nextent.peas.jpa.entity.Rating;
import hu.nextent.peas.jpa.entity.RatingStatusEnum;
import hu.nextent.peas.jpa.entity.Rating_;
import hu.nextent.peas.jpa.filter.Criterion;
import hu.nextent.peas.jpa.filter.ExpressionFactory;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@Transactional
public class CloseExpiredRatingScheduler {
	
	int MAX_ITEM = 1000;
	
	@PersistenceContext
	EntityManager entityManager;
	
	@Autowired
	CompanyRepository companyRepository;
	
	@Autowired
	RatingRepository ratingRepository;
	
	@Autowired
	NotificationRepository notificationRepository;
	
	@Autowired
	ToDoEvaluationCloserFacade toDoEvaluationCloserFacede;
	
	@Autowired
	AutomaticTaskRatingAddRatingFacade automaticTaskRatingAddRatingFacade;

	@Autowired
	FactoryServiceNotification factoryServiceNotification;

	@Scheduled(fixedDelay = 3600000) // 1 óránként
	public void scheduleCloseExpiredRating() {
		log.debug("CloseExpiredEvaluationSchedulder");
		List<Company> companies = getAllCompany();
		for(Company company : companies) {
			Stream<Rating> expiredRating = getExpiredRating(company);
			expiredRating.forEach(
					rating -> {
						closeExpiredRating(rating);	// Minősítés lezárása, ha lejárt a határidő
						toDoEvaluationCloserFacede.apply(rating); // ToDo/Notification lezárása, ha lejárt a határidő
						automaticTaskRatingAddRatingFacade.apply(rating);
						sendExpiredNotification(rating);
					}
					);
		}
		
		ratingRepository.flush();
	}
	


	List<Company> getAllCompany() {
		return companyRepository.findAllByActiveTrue();
	}
	
	/**
	 * Lejárt értékelések keresése
	 * @param company
	 * @return
	 */
	Stream<Rating> getExpiredRating(Company company) {
		OffsetDateTime now = OffsetDateTime.now(ZoneOffset.UTC);
		Criterion criterion = 
				ExpressionFactory.and(
							ExpressionFactory.eq(Rating_.COMPANY, company)
							, ExpressionFactory.eq(Rating_.STATUS, RatingStatusEnum.OPEN)
							, ExpressionFactory.lt(Rating_.DEADLINE, now)
						);
		
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<Rating> ratingQuery = criteriaBuilder.createQuery(Rating.class);
		Root<Rating> ratingRoot = ratingQuery.from(Rating.class);
		ratingQuery.where(criterion.toPredicate(ratingRoot, ratingQuery, criteriaBuilder));
		TypedQuery<Rating> query = entityManager.createQuery(ratingQuery);
		return query.getResultStream();
	}
	
	private void sendExpiredNotification(Rating rating) {
		factoryServiceNotification.createRatingExpired(rating);
	}
	
	/**
	 * Lejárt értékelés lezárása
	 * @param evaluation
	 */
	void closeExpiredRating(Rating rating) {
		rating.setStatus(RatingStatusEnum.EXPIRED);
		ratingRepository.save(rating);
	}
	
}
