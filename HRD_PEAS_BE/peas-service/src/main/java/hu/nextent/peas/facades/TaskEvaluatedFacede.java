package hu.nextent.peas.facades;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.Comparator;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import hu.nextent.peas.jpa.dao.PeriodRepository;
import hu.nextent.peas.jpa.dao.TaskRepository;
import hu.nextent.peas.jpa.entity.Evaluation;
import hu.nextent.peas.jpa.entity.EvaluationStatusEnum;
import hu.nextent.peas.jpa.entity.Period;
import hu.nextent.peas.jpa.entity.PeriodStatusEnum;
import hu.nextent.peas.jpa.entity.Task;
import hu.nextent.peas.jpa.entity.TaskStatusEnum;
import lombok.extern.slf4j.Slf4j;

/**
 * Task értékelt státuszba állítása
 * @author Peter.Tamas
 *
 */
@Slf4j
@Service
@Transactional
public class TaskEvaluatedFacede {
	
	@Autowired
	TaskRepository taskRepository;
	
	@Autowired
	PeriodRepository periodRepository;

	/**
	 * Task értékelt státuszba állítása
	 * @param task
	 */
	public void apply(Task task) {
		// Ha mindegyik értékelés kész, akkor zárja az taskhoz tartozó értékelést
		log.debug("Close Task {} If all Evaluation status is EVALUATED or EXPIRED");
		EvaluationStatusEnum calculatedEvaluationEndStatus =  calculatedEvaluationEndStatus(task);
		log.debug("Calculated Status: {}", calculatedEvaluationEndStatus.name());
		changeStatus(task, calculatedEvaluationEndStatus);
	}
	
	EvaluationStatusEnum calculatedEvaluationEndStatus(Task task) {
		int cntEvaluation_EVALUATED = 0;
		int cntEvaluation_EXPIRED = 0;
		int cntEvaluation = task.getEvaluations().size();
		
		for(Evaluation eval: task.getEvaluations()) {
			if (eval.getStatus().equals(EvaluationStatusEnum.EVALUATED)) {
				cntEvaluation_EVALUATED++;
			} else if (eval.getStatus().equals(EvaluationStatusEnum.EXPIRED)) {
				cntEvaluation_EXPIRED++;
			} 
		}
		
		int cntOther = cntEvaluation - cntEvaluation_EVALUATED - cntEvaluation_EXPIRED;
		log.debug("Counter: Evaluation: {}, EVALUATED: {}, EXPIRED: {}, Other: {}", 
				cntEvaluation, 
				cntEvaluation_EVALUATED, 
				cntEvaluation_EXPIRED,
				cntOther
				);
		
		if (cntOther != 0) {
			// Nincs változás, mert van olyan értékelés, amely még nem végeztek el
			return EvaluationStatusEnum.EVALUATING;
		}
		
		if (cntEvaluation_EXPIRED > 0) {
			// Ha legalább egy lejárt, akkor a Task is lejárt
			return EvaluationStatusEnum.EXPIRED;
		}
		
		return EvaluationStatusEnum.EVALUATED;
	}
	
	
	void changeStatus(Task task, EvaluationStatusEnum evaulationEndStatus) {
		if (!task.getStatus().equals(TaskStatusEnum.UNDER_EVALUATION)) {
			log.debug("Task status: {}, Task not changed", task.getStatus().name());
			return;
		}
		switch (evaulationEndStatus) {
			case EXPIRED:
				log.debug("Task old status: {}, new status: {}, endDate: NotChanged", task.getStatus().name(), TaskStatusEnum.EVALUATED.name());
				task.setStatus(TaskStatusEnum.EVALUATED);
				task.setPeriod(getActivePeriod(task));
				task.setEndDate(getLastEvaulationEndDate(task));
				taskRepository.save(task);
				break;
			case EVALUATED:
				log.debug("Task old status: {}, new status: {}, endDate: Changed", task.getStatus().name(), TaskStatusEnum.EVALUATED.name());
				task.setStatus(TaskStatusEnum.EVALUATED);
				task.setPeriod(getActivePeriod(task));
				task.setEndDate(getLastEvaulationEndDate(task));
				taskRepository.save(task);
				break;
			default:
				log.debug("Task not changed");
				break;
		}
	}
	
	Period getActivePeriod(Task task) {
		Optional<Period> optPeriod = 
				periodRepository
					.findByCompanyAndStatus(task.getCompany(), PeriodStatusEnum.ACTIVE);
		return optPeriod.orElse(null);
	}
	
	OffsetDateTime getLastEvaulationEndDate(Task task) {
		OffsetDateTime lastStartDate = 
				task.getEvaluations()
					.stream()
					.filter(e -> e.getEvaluatedEndDate() != null)
					.sorted(Comparator.comparing(Evaluation::getEvaluatedEndDate).reversed())
					.map(Evaluation::getEvaluatedEndDate)
					.findFirst().orElse(OffsetDateTime.now(ZoneOffset.UTC));
		return lastStartDate;
	}

	
	
}
