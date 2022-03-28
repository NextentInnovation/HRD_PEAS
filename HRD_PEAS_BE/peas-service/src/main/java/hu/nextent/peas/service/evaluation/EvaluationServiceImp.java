package hu.nextent.peas.service.evaluation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import hu.nextent.peas.jpa.dao.DatabaseInfoRepository;
import hu.nextent.peas.model.EvaluationModel;
import hu.nextent.peas.service.EvaluationService;
import lombok.val;

@Service
@Transactional
public class EvaluationServiceImp 
implements EvaluationService
{
	@Autowired private GetEvaluationDecorator getEvaluationDecorator;
	@Autowired private SendEvaluationDecorator sendEvaluationDecorator;
	@Autowired private DatabaseInfoRepository databaseInfoRepository;

	@Override
	public ResponseEntity<EvaluationModel> getEvaluation(Long evaluationId) {
		val result = getEvaluationDecorator.getEvaluation(evaluationId); 
		databaseInfoRepository.flush();
		return result;
	}

	@Override
	public ResponseEntity<EvaluationModel> modifyAndSendEvaluation(EvaluationModel body, Long evaluationId) {
		val result = sendEvaluationDecorator.modifyAndSendEvaluation(body, evaluationId); 
		databaseInfoRepository.flush();
		return result;
	}

}
