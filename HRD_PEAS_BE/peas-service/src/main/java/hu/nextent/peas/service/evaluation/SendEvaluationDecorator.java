package hu.nextent.peas.service.evaluation;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import hu.nextent.peas.exceptions.ExceptionList;
import hu.nextent.peas.jpa.entity.Evaluation;
import hu.nextent.peas.jpa.entity.EvaluationSelection;
import hu.nextent.peas.jpa.entity.EvaluationStatusEnum;
import hu.nextent.peas.jpa.entity.FactorOption;
import hu.nextent.peas.jpa.entity.TaskXFactor;
import hu.nextent.peas.model.EvaluationModel;
import hu.nextent.peas.model.EvaluationSelectionModel;
import hu.nextent.peas.model.FactorOptionModel;
import lombok.val;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@Transactional
public class SendEvaluationDecorator 
extends AbstarctEvaluationDecorator 
{

	public ResponseEntity<EvaluationModel> modifyAndSendEvaluation( EvaluationModel  body, Long  evaluationId) {
		log.debug("modifyAndSendEvaluation, evaluationId: {}, body: {}", evaluationId, body);
		// TODO Validációt átírni, mert csak a kódokat hoztam át a modifyEvaluation-ből
		validateModifyEvaluation(evaluationId, body);
		val evaluation = evaluationRepository.getOne(evaluationId);
		save(body, evaluation);
		validateSendEvaluated(evaluationId);
		automaticTaskRatingFacade.apply(evaluation);
		changeStatusToEvaluated(evaluation);
		// Innentől EVALUATED lőnek a feladatok
		scoreCalculationFacade.calculateEvaluationScore(evaluation);
		scoreCalculationFacade.calculateTaskScoreAndProgress(evaluation.getTask());
		// Todo Zárás
		toDoEvaluationCloserFacede.apply(evaluation);
		// Notifikáció küldés, hogy értékelték
		factoryServiceNotification.createEvaluationEnd(evaluation);
		// Ha minde értékelés le van zárva, akkor zárható a task
		taskEvaluatedFacede.apply(evaluation.getTask());
		return getEvaluationModel(evaluation);
	}


	private void validateModifyEvaluation(Long checkEvaluationId, EvaluationModel body) {
		if (body.getId() == null || checkEvaluationId == null) {
			throw ExceptionList.evaluation_id_reqired();
    	}
		Evaluation evaluation = checkExists(checkEvaluationId);
		if (checkEvaluationId != null && !checkEvaluationId.equals(body.getId())) {
			throw ExceptionList.evaluation_invalid_id(checkEvaluationId);
		}
		checkCompany(evaluation);
		checkStatus(evaluation, EvaluationStatusEnum.EVALUATING);
		checkEvaluator(evaluation, getCurrentUser());
		
		if (body.getFactors().isEmpty()) {
			throw ExceptionList.evaluation_selection_empty(checkEvaluationId);
		}
	}
	
	void save(EvaluationModel body, Evaluation evaluation) {
		evaluation.setNote(body.getNote());
		
		// Ellenőrizni a kötelezőket majd akkor kell, amikor jóváhagyják az értékelést
		// Régebben kiválasztott szempontok
		List<Long> oldSelectedOptionIds =
				evaluation.getEvaluationSelections()
					.stream()
					.map(EvaluationSelection::getFactorOption)
					.map(FactorOption::getId)
					.collect(Collectors.toList());

		// Új megadott szempontok
		List<Long> newSelectedOtionIds = body.getFactors().stream()
				.map(EvaluationSelectionModel::getSelected)
				.filter(p -> p != null)
				.map(FactorOptionModel::getId)
				.filter(p -> p != null)
				.collect(Collectors.toList())
		;
		
		List<Long> delOptsIds = new ArrayList<Long>(oldSelectedOptionIds);
		delOptsIds.removeAll(newSelectedOtionIds);
		
		List<Long> addOptsIds = new ArrayList<Long>(newSelectedOtionIds);
		addOptsIds.removeAll(oldSelectedOptionIds);
		
		// Már nem létezők törlése
		List<EvaluationSelection> selections = evaluation.getEvaluationSelections();
		List<EvaluationSelection> removed = new ArrayList<EvaluationSelection>(); 
		for(EvaluationSelection sel: selections) {
			if (delOptsIds.contains(sel.getFactorOption().getId())) {
				evaluationSelectionRepository.delete(sel);
				removed.add(sel);
			}
		}
		selections.removeAll(removed);
		
		for(Long addId: addOptsIds) {
			Optional<FactorOption> optFacOpt = factorOptionRepository.findById(addId);
			if (optFacOpt.isPresent()) {
				val sel = EvaluationSelection
						.builder()
						.evaluation(evaluation)
						.factorOption(optFacOpt.get())
						.build();
				evaluationSelectionRepository.save(sel);
				selections.add(sel);
			}
		}
	}
	
	Optional<EvaluationSelection> getSelected(Evaluation evaluation, Long optId) {
		return evaluation.getEvaluationSelections()
					.stream()
					.filter(p -> p.getFactorOption().getId().equals(optId))
					.findFirst();
	}
	
	void validateSendEvaluated(Long  checkEvaluationId) {
		Evaluation evaluation = checkExists(checkEvaluationId);
		checkCompany(evaluation);
		checkStatus(evaluation, EvaluationStatusEnum.EVALUATING);
		checkEvaluator(evaluation, getCurrentUser());
		// Minimálisan ki van töltve ?
		
		// Hány szempont van feltöltve ?
		int cntFactSel = evaluation.getEvaluationSelections().size();
		if (cntFactSel == 0) {
			throw ExceptionList.evaluation_selection_empty(checkEvaluationId);
		}
		
		// Kötelezők feltöltöttek ?
		// Kötelező szempontok:
		val requiredFactors = 
			evaluation
				.getTask().getTaskXFactors()
				.stream()
				.filter(TaskXFactor::getRequired)
				.map(TaskXFactor::getFactor)
				.collect(Collectors.toList())
				;
		
		val selectedFactor = 
				evaluation
					.getEvaluationSelections()
					.stream()
					.map(EvaluationSelection::getFactorOption)
					.map(FactorOption::getFactor)
					.filter(p -> requiredFactors.contains(p))
					.collect(Collectors.toList());
	
		
		
		if (requiredFactors.size() != selectedFactor.size()) {
			throw ExceptionList.evaluation_evaluation_selection_required_violation(checkEvaluationId, requiredFactors.size(), selectedFactor.size());
		}
		

	}
	
	private void changeStatusToEvaluated(Evaluation evaluation) {
		evaluation.setStatus(EvaluationStatusEnum.EVALUATED);
		evaluation.setEvaluatedEndDate(OffsetDateTime.now(ZoneOffset.UTC));
		evaluationRepository.save(evaluation);
	}


}
