package hu.nextent.peas.service.evaluation;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;

import hu.nextent.peas.exceptions.ExceptionList;
import hu.nextent.peas.facades.AutomaticTaskEvaluationAddRatingFacade;
import hu.nextent.peas.facades.FactoryServiceNotification;
import hu.nextent.peas.facades.ScoreCalculationFacade;
import hu.nextent.peas.facades.TaskEvaluatedFacede;
import hu.nextent.peas.facades.ToDoEvaluationCloserFacade;
import hu.nextent.peas.jpa.dao.EvaluationRepository;
import hu.nextent.peas.jpa.dao.EvaluationSelectionRepository;
import hu.nextent.peas.jpa.dao.FactorOptionRepository;
import hu.nextent.peas.jpa.dao.TaskRepository;
import hu.nextent.peas.jpa.entity.Evaluation;
import hu.nextent.peas.jpa.entity.EvaluationStatusEnum;
import hu.nextent.peas.jpa.entity.User;
import hu.nextent.peas.model.EvaluationModel;
import hu.nextent.peas.service.base.BaseDecorator;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public abstract class AbstarctEvaluationDecorator extends BaseDecorator {

	@Autowired protected EvaluationRepository evaluationRepository;
	@Autowired protected EvaluationSelectionRepository evaluationSelectionRepository;
	@Autowired protected FactorOptionRepository factorOptionRepository;
	@Autowired protected TaskRepository taskRepository;
	@Autowired protected ScoreCalculationFacade scoreCalculationFacade;
	@Autowired protected TaskEvaluatedFacede taskEvaluatedFacede;
	@Autowired protected ToDoEvaluationCloserFacade toDoEvaluationCloserFacede;
	@Autowired protected AutomaticTaskEvaluationAddRatingFacade automaticTaskRatingFacade;
	@Autowired protected FactoryServiceNotification factoryServiceNotification;
	
	
	protected Evaluation checkExists(Long evaluationId) {
		return evaluationRepository.findById(evaluationId).orElseThrow(() -> ExceptionList.evaluation_not_founded(evaluationId));
	}
	
	protected void checkCompany(Evaluation evaluation) {
		if (!evaluation.getCompany().equals(getCurrentCompany())) {
			throw ExceptionList.evaluation_not_founded(evaluation.getId());
		}
	}
	
	protected ResponseEntity<EvaluationModel> getEvaluationModel(Evaluation evaluation) {
    	return ResponseEntity.ok(conversionService.convert(evaluation, EvaluationModel.class));
    }
	
	protected ResponseEntity<EvaluationModel> getEvaluationModel(Long evaluationId) {
		Evaluation eval = checkExists(evaluationId);
    	return getEvaluationModel(eval);
    }
	
	protected void checkStatus(Evaluation evaluation, EvaluationStatusEnum ... validStatuses) {
    	if (!Arrays.asList(validStatuses).contains(evaluation.getStatus())) {
    		throw ExceptionList.evaluation_invalid_status(evaluation.getId(), Arrays.asList(validStatuses), evaluation.getStatus());
    	}
    }
	
	protected void checkEvaluator(Evaluation evaluation, User ... validUsers) {
    	if (!Arrays.asList(validUsers).contains(evaluation.getEvaluator())) {
    		log.debug(String.format("Evaluation %s not valid evaluator user", evaluation.getId()));
    		throw ExceptionList.evaluation_invalid_user(evaluation.getId(), evaluation.getEvaluator().getId());
    	}
    }
    
}
