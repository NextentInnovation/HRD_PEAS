package hu.nextent.peas.jpa.dao;

import java.util.List;

import hu.nextent.peas.jpa.entity.*;

public interface EvaluationRepository extends DaoRepository<Evaluation, Long> {

	
	List<Evaluation> findAllByEvaluatorAndStatusOrderById(User evaluator, EvaluationStatusEnum status);
	
	List<Evaluation> findAllByCompanyAndStatus(Company company, EvaluationStatusEnum status);

	List<Evaluation> findAllByEvaluatorAndTaskAndStatus(User evaluator, Task task, EvaluationStatusEnum status);
}
