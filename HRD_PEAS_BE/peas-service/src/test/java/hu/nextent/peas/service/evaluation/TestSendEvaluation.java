package hu.nextent.peas.service.evaluation;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;
import org.springframework.transaction.annotation.Transactional;

import hu.nextent.peas.TestConstant;
import hu.nextent.peas.jpa.entity.Evaluation;
import hu.nextent.peas.jpa.entity.EvaluationStatusEnum;
import hu.nextent.peas.jpa.entity.Notification;
import hu.nextent.peas.jpa.entity.NotificationTypeEnum;
import hu.nextent.peas.jpa.entity.Period;
import hu.nextent.peas.jpa.entity.PeriodStatusEnum;
import hu.nextent.peas.jpa.entity.Task;
import hu.nextent.peas.jpa.entity.TaskStatusEnum;
import hu.nextent.peas.jpa.entity.TaskTypeEnum;
import hu.nextent.peas.jpa.entity.ToDo;
import hu.nextent.peas.jpa.entity.ToDoStatusEnum;
import hu.nextent.peas.jpa.entity.ToDoTypeEnum;
import hu.nextent.peas.jpa.entity.User;
import hu.nextent.peas.model.EvaluationModel;
import hu.nextent.peas.model.EvaluationSelectionModel;
import hu.nextent.peas.model.FactorOptionModel;
import hu.nextent.peas.service.TestServiceConfig;

@ActiveProfiles("test")
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { TestServiceConfig.class }, loader = AnnotationConfigContextLoader.class)
@Transactional
@DirtiesContext
public class TestSendEvaluation extends TestEvaluationBase {

	private static final String NOTE = "test note";
	
	@Test
	@Rollback
	public void testEvaluation() {
		givenUser(TestConstant.USER_NORMAL);
		givenFirstEvaluationByStatus(getSelectedUser(), EvaluationStatusEnum.EVALUATING);
		mockAuthService();
		
		Optional<Period> optActivePeriod = periodRepository.findByCompanyAndStatus(getSelectedUser().getCompany(), PeriodStatusEnum.ACTIVE);
		Optional<Task> optAutomaticTask = taskRepository.findByOwnerAndPeriodAndTaskType(getSelectedUser(), optActivePeriod.get(), TaskTypeEnum.AUTOMATIC);
		Task automaticTask = optAutomaticTask.get();
		int size = automaticTask.getEvaluations().size();
		
		ResponseEntity<EvaluationModel> respOldModel = evaluationService.getEvaluation(getSelectedEvaluation().getId());
		// Hozzáadok egy kiválasztása
		EvaluationModel oldModel = respOldModel.getBody();
		EvaluationSelectionModel firstFactor = oldModel.getFactors().get(0);
		FactorOptionModel firstOption = firstFactor.getFactor().getOptions().get(0);
		firstFactor.setSelected(firstOption);
		oldModel.setNote(NOTE);
		
		// WHEN
		ResponseEntity<EvaluationModel> respEval = evaluationService.modifyAndSendEvaluation(oldModel, getSelectedEvaluation().getId());
		
		// Mentés tesztelése
		Assert.assertNotNull(respEval);
		Assert.assertEquals(respEval.getStatusCode(), HttpStatus.OK);
		Assert.assertNotNull(respEval.getBody());
		EvaluationModel model = respEval.getBody();
		Assert.assertTrue(!model.getFactors().isEmpty());
		Assert.assertTrue(model.getFactors().get(0).getSelected() != null);
		Assert.assertEquals(firstOption.getId(), model.getFactors().get(0).getSelected().getId());
		Assert.assertEquals(model.getNote(), NOTE);
		
		// Kitöltött a számolás
		Evaluation evaluation = evaluationRepository.getOne(model.getId());
		Assert.assertNotNull(evaluation.getScore());
		Assert.assertTrue(BigDecimal.ZERO.compareTo(evaluation.getScore()) < 0);
		
		// Státusz
		Assert.assertEquals(evaluation.getStatus(), EvaluationStatusEnum.EVALUATED);
		
		// Task számolás
		Task task = evaluation.getTask();
		Assert.assertNotNull(task.getScore());
		Assert.assertNotNull(task.getEvaluatedCount());
		Assert.assertNotNull(task.getEvaluaterCount());
		Assert.assertEquals(Integer.valueOf(1), task.getEvaluatedCount());
		Assert.assertNotNull(task.getEvaluationPercentage());
		Assert.assertTrue(BigDecimal.ZERO.compareTo(task.getEvaluationPercentage()) < 0);
		Assert.assertEquals(evaluation.getScore(), task.getScore());
		
		// ToDo lezárása
		List<ToDo> todos = toDoRepository.findAllByToUserAndEvaluationAndToDoType(getSelectedUser(), getSelectedEvaluation(), ToDoTypeEnum.EVALUATION);
		Assert.assertTrue(!todos.isEmpty());
		ToDo todo = todos.get(0);
		Assert.assertEquals(ToDoStatusEnum.CLOSE, todo.getStatus());
		
		// Notifikáció létrejöttének tesztelése
		List<Notification> notificationEnds = 
				notificationRepository.findAllByToUserAndEvaluationAndNotificationType(
						getSelectedEvaluation().getEvaluator(), getSelectedEvaluation(), NotificationTypeEnum.EVALUATION_END
				);
		Assert.assertEquals(1, notificationEnds.size());
		
		// Automatic Task új értékelés létrejötte
		optAutomaticTask = taskRepository.findByOwnerAndPeriodAndTaskType(getSelectedUser(), optActivePeriod.get(), TaskTypeEnum.AUTOMATIC);
		automaticTask = optAutomaticTask.get();
		Assert.assertTrue(!automaticTask.getEvaluations().isEmpty());
		Assert.assertEquals(size + 1, automaticTask.getEvaluations().size());
	}
	
	
	@Test
	@Rollback
	public void testAllEvaluation() {
		/**
		 * Adott task összes értékelését elvégzem, hogy le tudjam tesztelni,
		 * hogy a task endDate, status és period megfelelő értéket vesz fel  
		 */
		givenUser(TestConstant.USER_NORMAL);
		givenFirstEvaluationByStatus(getSelectedUser(), EvaluationStatusEnum.EVALUATING);

		// WHEN
		Task task = getSelectedEvaluation().getTask();
		task = taskRepository.getOne(task.getId());
		List<Evaluation> evaluations = new ArrayList<>(task.getEvaluations());
		// Összes értékelés lekérdezése és értékelése
		for(Evaluation eval: evaluations) {
			setSelectedUser(eval.getEvaluator()); // Kiválasztom az értékelő felhasználót
			mockAuthService(); // Az értékelő felhasználóval belépek
			// Lekérdezem az értékelést
			ResponseEntity<EvaluationModel> respOldModel = 
					evaluationService.getEvaluation(eval.getId());
			
			EvaluationModel oldModel = respOldModel.getBody();
			EvaluationSelectionModel firstFactor = oldModel.getFactors().get(0);
			FactorOptionModel firstOption = firstFactor.getFactor().getOptions().get(0);
			firstFactor.setSelected(firstOption);
			oldModel.setNote(NOTE);

			// Értékelem
			evaluationService.modifyAndSendEvaluation(oldModel, eval.getId());
		}

		entityManager.flush();
		
		// ASSERT
		task = taskRepository.getOne(task.getId());
		Assert.assertEquals(TaskStatusEnum.EVALUATED, task.getStatus());  // Elvárom, hogy le legyen zárva a task EVALUATED státusszal
		Assert.assertNotNull(task.getEndDate()); // Elvárom, hogy értékelés vége dátum ki legyen töltve
		
		Optional<Period> optActivePeriod = periodRepository.findByCompanyAndStatus(task.getCompany(), PeriodStatusEnum.ACTIVE);
		Assert.assertEquals(optActivePeriod.get(), task.getPeriod()); // Elvárom, hogy az aktív periódussal legyen a periódus kitöltve
		
	}
	
	
	@Test
	@Rollback
	public void testOneEvaulatorAllEvaulation() {
		givenUser(TestConstant.USER_NORMAL);
		// Keresek egy olyan felhazsnálót, akinek egynél több értékelése van
		User checkedUser = 
			getEntityManager().createQuery(
				"SELECT e.evaluator\n"
				+ "FROM Evaluation e\n"
				+ "WHERE e.status = 'BEFORE_EVALUATING'\n"
				+ "      AND e.company = :company\n"
				+ "GROUP BY e.evaluator\n"
				+ "HAVING count(e) > 1"
				, User.class
				)
			.setParameter("company", getSelectedUser().getCompany())
			.setMaxResults(1)
			.getSingleResult()
		;
		
		setSelectedUser(checkedUser);
		mockAuthService();
		
		// Lekérdezem az általa értékelhető értékelésekete
		List<Evaluation> evaulations = evaluationRepository.findAllByEvaluatorAndStatusOrderById(checkedUser, EvaluationStatusEnum.BEFORE_EVALUATING);

		Optional<Period> optActivePeriod = periodRepository.findByCompanyAndStatus(getSelectedUser().getCompany(), PeriodStatusEnum.ACTIVE);
		Optional<Task> optAutomaticTask = taskRepository.findByOwnerAndPeriodAndTaskType(getSelectedUser(), optActivePeriod.get(), TaskTypeEnum.AUTOMATIC);
		int oldAutomaticEvalCnt = optAutomaticTask.get().getEvaluations().size();

		
		// Értékelem őket
		for(Evaluation eval: evaulations) {
			eval.setStatus(EvaluationStatusEnum.EVALUATING);
			evaluationRepository.save(eval);
			
			ResponseEntity<EvaluationModel> respOldModel = 
					evaluationService.getEvaluation(eval.getId());
			
			EvaluationModel oldModel = respOldModel.getBody();
			EvaluationSelectionModel firstFactor = oldModel.getFactors().get(0);
			FactorOptionModel firstOption = firstFactor.getFactor().getOptions().get(0);
			firstFactor.setSelected(firstOption);
			oldModel.setNote(NOTE);

			// Értékelem
			evaluationService.modifyAndSendEvaluation(oldModel, eval.getId());
		}
		
		// Elvárom, hogy az értékelőhoz létrejöjjön annyi értékelés az automatikus taskok között, ahány értékelés volt
		optAutomaticTask = taskRepository.findByOwnerAndPeriodAndTaskType(getSelectedUser(), optActivePeriod.get(), TaskTypeEnum.AUTOMATIC);
		int newAutomaticEvalCnt = optAutomaticTask.get().getEvaluations().size();
		
		Assert.assertEquals(oldAutomaticEvalCnt + evaulations.size(), newAutomaticEvalCnt);
	}
}
