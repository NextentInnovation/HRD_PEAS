package hu.nextent.peas.scheduler;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Optional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;
import org.springframework.transaction.annotation.Transactional;

import hu.nextent.peas.jpa.dao.CompanyRepository;
import hu.nextent.peas.jpa.dao.EvaluationRepository;
import hu.nextent.peas.jpa.dao.NotificationRepository;
import hu.nextent.peas.jpa.dao.PeriodRepository;
import hu.nextent.peas.jpa.dao.TaskRepository;
import hu.nextent.peas.jpa.dao.ToDoRepository;
import hu.nextent.peas.jpa.entity.Company;
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

@ActiveProfiles("test")
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { TestSchedulerConfig.class }, loader = AnnotationConfigContextLoader.class)
@Transactional
@DirtiesContext
public class TestCloseExpiredEvaluationScheduler {
	
	protected final String DEFAULT_COMPANY = "nextent";
	
	@Autowired
	private CloseExpiredEvaluationScheduler testedScheduler;
	
	@Autowired
	private EvaluationRepository evaluationRepository;
	
	@Autowired
	private CompanyRepository companyRepository;
	
	@PersistenceContext
	private EntityManager entityManager;
	
	@Autowired
	private ToDoRepository toDoRepository;
	
	@Autowired
	private PeriodRepository periodRepository;
	
	@Autowired
	private TaskRepository taskRepository;
	
	@Autowired
	private NotificationRepository notificationRepository;
	
	@Test
	@Rollback
	public void testNotExpiredEvaluation() {
		testedScheduler.scheduleCloseExpiredEvaluation();
	}
	
	@Test
	@Rollback
	public void testCloseExpiredEvaluation() {
		// Egy adott ??rt??kel??st deadline ??rt??k??t a kurrens d??tum al?? ??ll??tok
		// Ekkor elv??rom, hogy 
		// - lez??rja az ??rt??kel??st
		// - notifik??ci??t k??ldj??n
		// - automatikus taskba rossz ??rt??kel??st adjon
		
		// Given
		Optional<Company> optCompany = companyRepository.findByName(DEFAULT_COMPANY);
		Company company = optCompany.get();
		List<Evaluation> evaluations = evaluationRepository.findAllByCompanyAndStatus(company, EvaluationStatusEnum.EVALUATING);
		Evaluation evaluation = evaluations.get(0);
		OffsetDateTime now = OffsetDateTime.now(ZoneOffset.UTC);
		OffsetDateTime expiredDate = now.minusMinutes(10); // 10 perce lej??rt
		evaluation.setDeadline(expiredDate);
		evaluationRepository.save(evaluation);
		evaluationRepository.flush();
		
		// When
		testedScheduler.scheduleCloseExpiredEvaluation();
		
		// Assert
		evaluationRepository.flush();
		entityManager.refresh(evaluation);
		// ??rt??kel??s st??tusza lej??rt
		Assert.assertEquals(EvaluationStatusEnum.EXPIRED, evaluation.getStatus());
		// Pontoz??s Evaluation (Mivel a Bad Status 0, ez??rt 0-nak kell lennie)
		Assert.assertTrue(BigDecimal.ZERO.compareTo(evaluation.getScore()) == 0);
		// Pontoz??s Task (Mivel a Bad Status 0, ez??rt 0-nak kell lennie)
		Task task = evaluation.getTask();
		Assert.assertTrue(BigDecimal.ZERO.compareTo(task.getScore()) == 0);
		Assert.assertEquals(Integer.valueOf(0), task.getEvaluatedCount());
		Assert.assertNotNull(task.getEvaluationPercentage());
		Assert.assertTrue(BigDecimal.ZERO.compareTo(task.getEvaluationPercentage()) == 0);
		Assert.assertTrue(evaluation.getScore().compareTo(task.getScore()) == 0);
		// Automatikus task kap egy ??rt??kel??set
		Optional<Period> optActivePeriod = periodRepository.findByCompanyAndStatus(company, PeriodStatusEnum.ACTIVE);
		Assert.assertTrue(optActivePeriod.isPresent());
		Period activePeriod = optActivePeriod.get();
		Optional<Task> optAutomaticTask = taskRepository.findByOwnerAndPeriodAndTaskType(evaluation.getEvaluator(), activePeriod, TaskTypeEnum.AUTOMATIC);
		Assert.assertTrue(optAutomaticTask.isPresent());
		Task automaticTask = optAutomaticTask.get();
		Assert.assertTrue(!automaticTask.getEvaluations().isEmpty());
		Assert.assertEquals(2, automaticTask.getEvaluations().size());
		// Notifik??ci?? k??ld??se a lej??ratr??l
		List<Notification> notificationEnds = notificationRepository.findAllByToUserAndEvaluationAndNotificationType(
				evaluation.getEvaluator(), evaluation, NotificationTypeEnum.EVALUATION_EXPIRED
				);
		Assert.assertEquals(1, notificationEnds.size());
		// Lez??rja az ??rt??kel??shez kapcsol??d?? ToDo-t
		List<ToDo> optTodo = toDoRepository.findAllByToUserAndEvaluationAndToDoType(evaluation.getEvaluator(), evaluation, ToDoTypeEnum.EVALUATION);
		Assert.assertTrue(!optTodo.isEmpty());
		ToDo todo = optTodo.get(0);
		Assert.assertEquals(ToDoStatusEnum.EXPIRED, todo.getStatus());
	}
	
	
	@Test
	@Rollback
	public void testExpiredTaskAllEvaluation() {
		// Az ??sszes ??rt??kel??s lej??rt
		Optional<Company> optCompany = companyRepository.findByName(DEFAULT_COMPANY);
		Company company = optCompany.get();
		List<Evaluation> evaluations = evaluationRepository.findAllByCompanyAndStatus(company, EvaluationStatusEnum.EVALUATING);
		for(Evaluation evaluation: evaluations) {
			OffsetDateTime now = OffsetDateTime.now(ZoneOffset.UTC);
			OffsetDateTime expiredDate = now.minusMinutes(10); // 10 perce lej??rt
			evaluation.setDeadline(expiredDate);
			evaluationRepository.save(evaluation);
		}
		evaluationRepository.flush();
		
		// When
		testedScheduler.scheduleCloseExpiredEvaluation();
		
		// Assert
		evaluationRepository.flush();
		
		// Els?? ??rt??kel??s viszg??lata
		Evaluation evaluation = evaluations.get(0);
		entityManager.refresh(evaluation);
		// ??rt??kel??s st??tusza lej??rt
		Assert.assertEquals(EvaluationStatusEnum.EXPIRED, evaluation.getStatus());
		
		// Pontoz??s Evaluation (Mivel a Bad Status 0, ez??rt 0-nak kell lennie)
		Assert.assertTrue(BigDecimal.ZERO.compareTo(evaluation.getScore()) == 0);
		// Pontoz??s Task (Mivel a Bad Status 0, ez??rt 0-nak kell lennie)
		Task task = evaluation.getTask();
		Assert.assertTrue(BigDecimal.ZERO.compareTo(task.getScore()) == 0);
		Assert.assertEquals(Integer.valueOf(0), task.getEvaluatedCount());
		Assert.assertNotNull(task.getEvaluationPercentage());
		// Mindenki lek??rt??kelt
		Assert.assertTrue(BigDecimal.ZERO.compareTo(task.getEvaluationPercentage()) == 0);
		Assert.assertTrue(evaluation.getScore().compareTo(task.getScore()) == 0);
		// Task st??tusz
		Assert.assertEquals(TaskStatusEnum.EVALUATED, task.getStatus());
		
	}
}
	
