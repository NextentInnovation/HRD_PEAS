package hu.nextent.peas.service.evaluation;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.beans.factory.annotation.Autowired;

import hu.nextent.peas.jpa.dao.EvaluationRepository;
import hu.nextent.peas.jpa.dao.NotificationRepository;
import hu.nextent.peas.jpa.dao.PeriodRepository;
import hu.nextent.peas.jpa.dao.TaskRepository;
import hu.nextent.peas.jpa.dao.ToDoRepository;
import hu.nextent.peas.jpa.entity.Evaluation;
import hu.nextent.peas.jpa.entity.EvaluationStatusEnum;
import hu.nextent.peas.jpa.entity.User;
import hu.nextent.peas.service.EvaluationService;
import hu.nextent.peas.service.TestServiceBase;
import lombok.Getter;
import lombok.Setter;
import lombok.val;

@Getter
@Setter
public abstract class TestEvaluationBase extends TestServiceBase {
	
	@PersistenceContext
	protected EntityManager entityManager;
	
	@Autowired
	protected ToDoRepository toDoRepository;
	
	@Autowired
	protected NotificationRepository notificationRepository;
	
	@Autowired
	protected EvaluationRepository evaluationRepository;
	
	@Autowired
	protected TaskRepository taskRepository;

	@Autowired
	protected PeriodRepository periodRepository;

	@Autowired
	protected EvaluationService evaluationService;

	private Evaluation selectedEvaluation;
	
	protected void givenFirstEvaluationByStatus(User evaluator, EvaluationStatusEnum status) {
		val evaluations = evaluationRepository.findAllByEvaluatorAndStatusOrderById(evaluator, status);
		selectedEvaluation = evaluations.get(0);
	}
	
}
