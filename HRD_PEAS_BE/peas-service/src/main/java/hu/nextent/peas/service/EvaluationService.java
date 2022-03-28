package hu.nextent.peas.service;

import org.springframework.http.ResponseEntity;

import hu.nextent.peas.model.EvaluationModel;

public interface EvaluationService {

    public ResponseEntity<EvaluationModel> getEvaluation(Long evaluationId);
    
    public ResponseEntity<EvaluationModel> modifyAndSendEvaluation(EvaluationModel body,Long evaluationId);
}
