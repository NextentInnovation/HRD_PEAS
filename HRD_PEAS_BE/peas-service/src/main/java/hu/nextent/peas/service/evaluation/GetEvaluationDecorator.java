package hu.nextent.peas.service.evaluation;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import hu.nextent.peas.jpa.entity.Evaluation;
import hu.nextent.peas.jpa.entity.EvaluationStatusEnum;
import hu.nextent.peas.model.EvaluationModel;

@Service
@Transactional
public class GetEvaluationDecorator 
extends AbstarctEvaluationDecorator 
{

	public ResponseEntity<EvaluationModel> getEvaluation(Long evaluationId) {
		Evaluation eval = checkExists(evaluationId);
		validateGetEvaluation(eval);
		return getEvaluationModel(eval);
	}
	
    private void validateGetEvaluation(Evaluation evaluation) {
    	checkCompany(evaluation);
    	checkStatus(evaluation, EvaluationStatusEnum.BEFORE_EVALUATING, EvaluationStatusEnum.EVALUATING);
	} 
}
