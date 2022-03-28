package hu.nextent.peas.facades;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import hu.nextent.peas.jpa.dao.EvaluationRepository;
import hu.nextent.peas.jpa.dao.TaskRepository;
import hu.nextent.peas.jpa.entity.Evaluation;
import hu.nextent.peas.jpa.entity.EvaluationSelection;
import hu.nextent.peas.jpa.entity.EvaluationStatusEnum;
import hu.nextent.peas.jpa.entity.FactorOption;
import hu.nextent.peas.jpa.entity.Task;
import hu.nextent.peas.jpa.entity.TaskTypeEnum;
import lombok.var;

/**
 * Ponszám kalkulációk
 * 
 *
 */
@Service
@Transactional
public class ScoreCalculationFacade {
	
	private final static Collection<EvaluationStatusEnum> EVAULATED_STATUS = 
			Arrays.asList(EvaluationStatusEnum.EVALUATED, EvaluationStatusEnum.CLOSED);

	private final static Collection<EvaluationStatusEnum> SCORED_STATUS = 
			Arrays.asList(EvaluationStatusEnum.EVALUATED, EvaluationStatusEnum.CLOSED);

	@Autowired
	private EvaluationRepository evaluationRepository;
	
	@Autowired
	private TaskRepository taskRepository;
	
	public Evaluation calculateEvaluationScore(Evaluation evaluation) {
		BigDecimal multiplier = evaluation.getTask().getDifficulty().getMultiplier();
		BigDecimal score = getScore(evaluation, multiplier);
		score = score.setScale(5, BigDecimal.ROUND_HALF_UP);
		evaluation.setScore(score);
		evaluationRepository.save(evaluation);
		return evaluation;
	}
	
	private BigDecimal getScore(Evaluation evaluation, BigDecimal multiplier) {
		BigDecimal score = 
				evaluation.getEvaluationSelections()
				.stream()
				.map(EvaluationSelection::getFactorOption)
				.map(FactorOption::getScore)
				.filter(p -> p != null)
				.map(p -> multiplier.multiply(p))
				.reduce(BigDecimal.ZERO, BigDecimal::add);
		return score;
	}
	
	
	
	public Task calculateTaskScoreAndProgress(Task task) {
		var evaluatedCount = getEvaulatedCount(task);
		
		task.setEvaluaterCount(task.getEvaluations().size());

		if (evaluatedCount == 0) {
			task.setScore(BigDecimal.ZERO);
			task.setEvaluatedCount(0);
			task.setEvaluationPercentage(BigDecimal.ZERO);
		} else {
			var taskScore = getTaskScoreSum(task);
			taskScore = taskScore.divide(BigDecimal.valueOf(evaluatedCount), 5, BigDecimal.ROUND_HALF_UP);
			task.setScore(taskScore);
			// Progress
			task.setEvaluatedCount(Long.valueOf(evaluatedCount).intValue());
			BigDecimal bdEvaluatedCount = BigDecimal.valueOf(evaluatedCount);
			BigDecimal bdEvaluaterCount = BigDecimal.valueOf(task.getEvaluaterCount());
			BigDecimal percent = BigDecimal.valueOf(100);
			percent = percent.multiply(bdEvaluatedCount);
			percent = percent.divide(bdEvaluaterCount, 2, BigDecimal.ROUND_HALF_UP);
			task.setEvaluationPercentage(percent);
		}
		
		taskRepository.save(task);
		return task;
	}
	
	private long getEvaulatedCount(Task task) {
		var evaluatedCount = task.getEvaluations()
				.stream()
				.filter(evaluation -> EVAULATED_STATUS.contains(evaluation.getStatus()))
				.count();
		return evaluatedCount;
	}
	
	private BigDecimal getTaskScoreSum(Task task) {
		BigDecimal taskScore = task.getEvaluations()
				.stream()
				.filter(evaluation -> SCORED_STATUS.contains(evaluation.getStatus()))
				.map(Evaluation::getScore)
				.filter(p -> p != null)
				.reduce(BigDecimal.ZERO, BigDecimal::add);
		return taskScore;
	}
	
	public Task calculateAutomaticTaskScore(Task automaticTask) {
		if (!TaskTypeEnum.AUTOMATIC.equals(automaticTask.getTaskType())) {
			automaticTask.setScore(null);
			return automaticTask;
		}
		if (automaticTask.getEvaluations().isEmpty()) {
			automaticTask.setScore(null);
			return automaticTask;
		}
		BigDecimal taskScore = 
				automaticTask.getEvaluations()
					.stream()
					.map(Evaluation::getScore)
					.filter(p -> p != null)
					.reduce(BigDecimal.ZERO, BigDecimal::add);
		taskScore = taskScore.divide(BigDecimal.valueOf(automaticTask.getEvaluations().size()), 5, BigDecimal.ROUND_HALF_UP);
		automaticTask.setScore(taskScore);
		taskRepository.save(automaticTask);
		return automaticTask;
	}


}
