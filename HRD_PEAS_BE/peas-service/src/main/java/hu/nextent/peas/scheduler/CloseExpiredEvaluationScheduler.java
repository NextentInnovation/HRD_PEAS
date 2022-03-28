package hu.nextent.peas.scheduler;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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

import hu.nextent.peas.facades.AutomaticTaskEvaluationAddRatingFacade;
import hu.nextent.peas.facades.FactoryServiceNotification;
import hu.nextent.peas.facades.ScoreCalculationFacade;
import hu.nextent.peas.facades.TaskEvaluatedFacede;
import hu.nextent.peas.facades.ToDoEvaluationCloserFacade;
import hu.nextent.peas.jpa.dao.CompanyRepository;
import hu.nextent.peas.jpa.dao.EvaluationRepository;
import hu.nextent.peas.jpa.dao.NotificationRepository;
import hu.nextent.peas.jpa.dao.ToDoRepository;
import hu.nextent.peas.jpa.entity.Company;
import hu.nextent.peas.jpa.entity.Evaluation;
import hu.nextent.peas.jpa.entity.EvaluationStatusEnum;
import hu.nextent.peas.jpa.entity.Evaluation_;
import hu.nextent.peas.jpa.entity.Task;
import hu.nextent.peas.jpa.filter.Criterion;
import hu.nextent.peas.jpa.filter.ExpressionFactory;
import lombok.val;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@Transactional
public class CloseExpiredEvaluationScheduler {
	
	int MAX_ITEM = 1000;
	
	@PersistenceContext
	EntityManager entityManager;
	
	@Autowired
	CompanyRepository companyRepository;
	
	@Autowired
	EvaluationRepository evaluationRepository;
	
	@Autowired
	ToDoRepository toDoRepository;
	
	@Autowired
	NotificationRepository notificationRepository;
	
	@Autowired
	ScoreCalculationFacade scoreCalculationFacade;
	
	@Autowired
	TaskEvaluatedFacede taskEvaluatedFacede;

	@Autowired
	ToDoEvaluationCloserFacade toDoEvaluationCloserFacede;
	
	@Autowired
	AutomaticTaskEvaluationAddRatingFacade automaticTaskEvaluationAddRatingFacade;

	@Autowired
	FactoryServiceNotification factoryServiceNotification;

	@Scheduled(fixedDelay = 3600000) // 1 óránként
	public void scheduleCloseExpiredEvaluation() {
		log.debug("CloseExpiredEvaluationSchedulder");
		List<Company> companies = getAllCompany();
		for(Company company : companies) {
			Map<Long, Task> checkedTask = new HashMap<>();
			Stream<Evaluation> expiredEvaluations = getExpiredEvaluation(company);
			expiredEvaluations.forEach(
					evaluation -> {
						Task task = evaluation.getTask();
						checkedTask.putIfAbsent(task.getId(), task);
						closeExpiredEvaluation(evaluation);	// Értékelés lezárása, ha lejárt a határidő
						toDoEvaluationCloserFacede.apply(evaluation); // ToDo/Notification lezárása, ha lejárt a határidő
						automaticTaskEvaluationAddRatingFacade.apply(evaluation);
						sendExpiredNotification(evaluation);
					}
					);
			// Task viszgálat
			for(Task task: checkedTask.values()) {
				taskEvaluatedFacede.apply(task); // Task lezárása, ha lehet
				scoreCalculationFacade.calculateTaskScoreAndProgress(task);
			}
		}
		
		evaluationRepository.flush();
	}
	


	List<Company> getAllCompany() {
		return companyRepository.findAllByActiveTrue();
	}
	
	/**
	 * Lejárt értékelések keresése
	 * @param company
	 * @return
	 */
	Stream<Evaluation> getExpiredEvaluation(Company company) {
		OffsetDateTime now = OffsetDateTime.now(ZoneOffset.UTC);
		Criterion criterion = 
				ExpressionFactory.and(
							ExpressionFactory.eq(Evaluation_.COMPANY, company)
							, ExpressionFactory.eq(Evaluation_.STATUS, EvaluationStatusEnum.EVALUATING)
							, ExpressionFactory.lt(Evaluation_.DEADLINE, now)
						);
		
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<Evaluation> evaluationQuery = criteriaBuilder.createQuery(Evaluation.class);
		Root<Evaluation> evaluationRoot = evaluationQuery.from(Evaluation.class);
		evaluationQuery.where(criterion.toPredicate(evaluationRoot, evaluationQuery, criteriaBuilder));
		TypedQuery<Evaluation> query = entityManager.createQuery(evaluationQuery);
		return query.getResultStream();
	}
	
	private void sendExpiredNotification(Evaluation evaluation) {
		factoryServiceNotification.createEvaluationExpired(evaluation);
	}
	
	/**
	 * Lejárt értékelés lezárása
	 * @param evaluation
	 */
	void closeExpiredEvaluation(Evaluation evaluation) {
		evaluation.setScore(BigDecimal.ZERO);
		evaluation.setStatus(EvaluationStatusEnum.EXPIRED);
		evaluation.setEvaluatedEndDate(OffsetDateTime.now(ZoneOffset.UTC));
		evaluationRepository.save(evaluation);
	}
	
}
