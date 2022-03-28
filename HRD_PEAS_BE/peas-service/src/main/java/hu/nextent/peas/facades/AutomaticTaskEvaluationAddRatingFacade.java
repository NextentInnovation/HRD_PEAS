package hu.nextent.peas.facades;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.Arrays;
import java.util.Collection;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import hu.nextent.peas.exceptions.ExceptionList;
import hu.nextent.peas.jpa.dao.DifficultyRepository;
import hu.nextent.peas.jpa.dao.EvaluationRepository;
import hu.nextent.peas.jpa.dao.EvaluationSelectionRepository;
import hu.nextent.peas.jpa.dao.FactorRepository;
import hu.nextent.peas.jpa.dao.PeriodRepository;
import hu.nextent.peas.jpa.dao.TaskRepository;
import hu.nextent.peas.jpa.dao.TaskXFactorRepository;
import hu.nextent.peas.jpa.entity.Difficulty;
import hu.nextent.peas.jpa.entity.Evaluation;
import hu.nextent.peas.jpa.entity.EvaluationSelection;
import hu.nextent.peas.jpa.entity.EvaluationStatusEnum;
import hu.nextent.peas.jpa.entity.Factor;
import hu.nextent.peas.jpa.entity.FactorOption;
import hu.nextent.peas.jpa.entity.Period;
import hu.nextent.peas.jpa.entity.PeriodStatusEnum;
import hu.nextent.peas.jpa.entity.Task;
import hu.nextent.peas.jpa.entity.TaskStatusEnum;
import hu.nextent.peas.jpa.entity.TaskTypeEnum;
import hu.nextent.peas.jpa.entity.TaskXFactor;
import lombok.extern.slf4j.Slf4j;

/*
 * Értékelés esetén az automatikus task állítása
 */
@Slf4j
@Service
@Transactional
public class AutomaticTaskEvaluationAddRatingFacade {

	private final static Collection<EvaluationStatusEnum> CHECKED_STATUS = 
			Arrays.asList(EvaluationStatusEnum.EVALUATING, EvaluationStatusEnum.EXPIRED);

	private final static Collection<EvaluationStatusEnum> GOOD_STATUS = 
			Arrays.asList(EvaluationStatusEnum.EVALUATED, EvaluationStatusEnum.EVALUATING);
	
	@Autowired
	private TaskRepository taskRepository;
	
	@Autowired
	private TaskXFactorRepository taskXFactorRepository;
	
	@Autowired
	private DifficultyRepository difficultyRepository;
	
	@Autowired
	private PeriodRepository periodRepository;
	
	@Autowired
	private FactorRepository factorRepository;
	
	@Autowired
	private EvaluationRepository evaluationRepository;
	
	@Autowired
	private EvaluationSelectionRepository evaluationSelectionRepository;
	
	@Autowired
	private ScoreCalculationFacade scoreCalculationFacade;

	public void apply(Evaluation checkedEvaluation) {
		log.debug("start");
		if (checkedEvaluation == null) {
			log.debug("end, evaluation = null");
			return;
		}
		if (!CHECKED_STATUS.contains(checkedEvaluation.getStatus())) {
			log.debug("end, wrong status evaluation.status : {}", checkedEvaluation.getStatus().name());
			return;
		}
		
		Task automaticTask = getOrCreateAutomaticTask(checkedEvaluation);
		setValue(automaticTask, checkedEvaluation);
		calculatedAutomaticTaskValues(automaticTask);
	}

	private Task getOrCreateAutomaticTask(Evaluation evaluation) {
		log.debug("getOrCreateAutomaticTask");
		if (evaluation == null) {
			log.debug("end, evaluation = null");
			return null;
		}
		
		Optional<Period> optPeriodActive = periodRepository.findByCompanyAndStatus(evaluation.getCompany(), PeriodStatusEnum.ACTIVE);
		if (!optPeriodActive.isPresent()) {
			log.debug("end, active period not founded");
			return null;
		}
		Period period = optPeriodActive.get(); 
		
		Optional<Task> optAutomaticTask = 
				taskRepository.findByOwnerAndPeriodAndTaskType(
						evaluation.getEvaluator(), 
						period, 
						TaskTypeEnum.AUTOMATIC
						);
		
		Task automaticTask = null;
		Difficulty difficulty = difficultyRepository.findByActiveTrueAndAutomaticTrueAndCompany(evaluation.getCompany()).get();
		if (!optAutomaticTask.isPresent()) {
			log.debug("create automatic task");
			automaticTask = 
					Task.builder()
					.status(TaskStatusEnum.UNDER_EVALUATION)
					.taskType(TaskTypeEnum.AUTOMATIC)
					.name(TaskTypeEnum.AUTOMATIC.name())
					.startDate(period.getStartDate())
					.endDate(period.getEndDate())
					.deadline(period.getEndDate())
					.evaluaterCount(1)
					.evaluatedCount(1)
					.evaluationPercentage(BigDecimal.valueOf(100l))
					.owner(evaluation.getEvaluator())
					.period(period)
					.difficulty(difficulty)
					.company(evaluation.getCompany())
					.build();
				taskRepository.save(automaticTask);
		} else {
			log.debug("founded automatic task");
			automaticTask = optAutomaticTask.get();
		}
		
		if (automaticTask.getDifficulty() == null) {
			automaticTask.setDifficulty(difficulty);
			taskRepository.save(automaticTask);
		}
		
		return automaticTask;
	}
	
	private void setValue(Task automaticTask, Evaluation checkedEvaluation) {
		if (checkedEvaluation == null || automaticTask == null) {
			log.debug("end, evaluation = null");
			return;
		}
		
		Factor factor = refreshTaskXFactor(automaticTask);
		FactorOption selected = GOOD_STATUS.contains(checkedEvaluation.getStatus()) ? findGood(factor) : findBad(factor);
		BigDecimal score = selected.getScore().multiply(automaticTask.getDifficulty().getMultiplier());
		score = score.setScale(5, BigDecimal.ROUND_HALF_UP); // Kerekítés
		OffsetDateTime now = OffsetDateTime.now();
		Evaluation evaluationToAutomatic = 
				Evaluation.builder()
					.status(EvaluationStatusEnum.EVALUATED)
					.deadline(now)
					.evaluator(checkedEvaluation.getEvaluator())
					.task(automaticTask)
					.company(checkedEvaluation.getCompany())
					.score(score)
					.automaticStartDate(OffsetDateTime.now(ZoneOffset.UTC))
					.build();
		
		evaluationRepository.save(evaluationToAutomatic);
		
		EvaluationSelection evaluationSelection = 
				EvaluationSelection.builder()
					.evaluation(evaluationToAutomatic)
					.factorOption(selected)
					.build();
		
		evaluationSelectionRepository.save(evaluationSelection);
		evaluationToAutomatic.getEvaluationSelections().add(evaluationSelection);
		evaluationRepository.save(evaluationToAutomatic);
		
		automaticTask.getEvaluations().add(evaluationToAutomatic);
		taskRepository.save(automaticTask);
		
	}
	
	private Factor refreshTaskXFactor(Task automaticTask) {
		Factor factor = null;
		if (automaticTask.getTaskXFactors() == null || automaticTask.getTaskXFactors().isEmpty()) {
			log.debug("bind factor to automatic task");
			factor = factorRepository.findByActiveTrueAndAutomaticTrueAndCompany(automaticTask.getCompany()).get();
			TaskXFactor taskXFactor = 
					TaskXFactor.builder().factor(factor).task(automaticTask).build();
			taskXFactorRepository.save(taskXFactor);
			automaticTask.getTaskXFactors().add(taskXFactor);
			taskRepository.save(automaticTask);
		} else {
			log.debug("founded factor");
			factor = automaticTask.getTaskXFactors().get(0).getFactor();
		}
		return factor;
	}
	
	
	private FactorOption findGood(Factor factor) {
		return factor.getFactorOptions().stream().filter(o -> Boolean.TRUE.equals(o.getBest())).findFirst().orElseThrow(() -> ExceptionList.not_founded_object(Factor.class.getSimpleName(), factor.getId()));
	}
	
	private FactorOption findBad(Factor factor) {
		return factor.getFactorOptions().stream().filter(o -> !Boolean.TRUE.equals(o.getBest())).findFirst().orElseThrow(() -> ExceptionList.not_founded_object(Factor.class.getSimpleName(), factor.getId()));
	}
	
	private void calculatedAutomaticTaskValues(Task automaticTask) {
		log.debug("calculatedAutomaticTaskValues start");
		scoreCalculationFacade.calculateAutomaticTaskScore(automaticTask);
	}
	
}
