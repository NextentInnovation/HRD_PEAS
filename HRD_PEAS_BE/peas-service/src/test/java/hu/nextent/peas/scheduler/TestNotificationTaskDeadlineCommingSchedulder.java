package hu.nextent.peas.scheduler;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
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

import hu.nextent.peas.constant.ServiceConstant;
import hu.nextent.peas.jpa.dao.CompanyParameterRepository;
import hu.nextent.peas.jpa.dao.CompanyRepository;
import hu.nextent.peas.jpa.dao.NotificationRepository;
import hu.nextent.peas.jpa.dao.TaskRepository;
import hu.nextent.peas.jpa.entity.Company;
import hu.nextent.peas.jpa.entity.Notification;
import hu.nextent.peas.jpa.entity.NotificationTypeEnum;
import hu.nextent.peas.jpa.entity.Task;
import hu.nextent.peas.jpa.entity.TaskStatusEnum;
import hu.nextent.peas.jpa.entity.TaskTypeEnum;
import lombok.val;


@ActiveProfiles("test")
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { TestSchedulerConfig.class }, loader = AnnotationConfigContextLoader.class)
@Transactional
@DirtiesContext
public class TestNotificationTaskDeadlineCommingSchedulder {

	private final String DEFAULT_COMPANY = "nextent";
	
	@Autowired
	private CompanyParameterRepository companyParameterRepository;
	
	@Autowired
	private CompanyRepository companyRepository;

	@Autowired
	private TaskRepository taskRepository;

	@Autowired
	private NotificationRepository notificationRepository;

	@Autowired 
	private NotificationTaskDeadlineCommingSchedulder testedSchedulder;
	
	
	private Company defaultCompany;
	private String companyParameter;
	private List<Integer> deadlineDays;
	private Task preparedTask;
	
	@Test
	@Rollback
	public void testGetDeadlineDays() {
		givenDefaultCompany();
		givenCompanyParameterDeadlineStr();
		
		Assert.assertNotNull(companyParameter);
	}
	
	private void givenDefaultCompany() {
		defaultCompany = companyRepository.findByName(DEFAULT_COMPANY).get();
	}
	
	private void givenCompanyParameterDeadlineStr() {
		companyParameter = companyParameterRepository.findByCompanyAndCode(defaultCompany, ServiceConstant.DEADLINE_TASK_NOTIFICATION_WARNING).get().getValue();
	}
	
	@Test
	@Rollback
	public void testNotHaveDeadline() {
		testedSchedulder.scheduleNotificationTaskDeadlineComming();
	}
	
	@Test
	@Rollback
	public void testHaveLeastOneDeadlinedTask() {
		// Preparálom az értékelést, hogy elgyen deadline
		givenDefaultCompany();
		givenCompanyParameterDeadlineStr();
		givenDeadlineDays();
		preparedOneTask();
		
		// WHEN
		testedSchedulder.scheduleNotificationTaskDeadlineComming();
		
		// ASSERT
		// Megviszgálom, hogy elkészült-e a notifikáció
		preparedTask = taskRepository.getOne(preparedTask.getId());
		List<Notification> notifications = notificationRepository.findAllByTaskAndNotificationType(
				preparedTask, NotificationTypeEnum.TASK_DEADLINE
				);
		// Egy elemre számítok
		Assert.assertEquals(1, notifications.size());
		
	}
	
	void givenDeadlineDays() {
		val deadlineArray = companyParameter;
		val days = StringUtils.split(deadlineArray, ",");
		deadlineDays = new ArrayList<Integer>();
		for(String day: days) {
			deadlineDays.add(Integer.valueOf(day));
		}
		Collections.sort(deadlineDays);
	}
	
	void preparedOneTask() {
		List<Task> tasks = taskRepository.findAllByCompanyAndTaskTypeAndStatus(defaultCompany, TaskTypeEnum.NORMAL, TaskStatusEnum.PARAMETERIZATION);
		Assert.assertTrue(!tasks.isEmpty());
		Integer days = deadlineDays.get(deadlineDays.size() - 1);
		OffsetDateTime now = OffsetDateTime.now(ZoneOffset.UTC);
		OffsetDateTime deadlinedDate = now.minusDays(days);
		preparedTask = tasks.get(0);
		preparedTask.setDeadline(deadlinedDate);
		taskRepository.save(preparedTask);
		taskRepository.flush();
	}
	
	
	@Test
	@Rollback
	public void testHaveLeastOneDeadlinedEvaluationAndNotDuplicate() {
		// Preparálom az értékelést, hogy elgyen deadline
		givenDefaultCompany();
		givenCompanyParameterDeadlineStr();
		givenDeadlineDays();
		preparedOneTask();
		
		testedSchedulder.scheduleNotificationTaskDeadlineComming();
		notificationRepository.flush();
		// WHEN
		testedSchedulder.scheduleNotificationTaskDeadlineComming();
		
		// ASSERT
		// Megviszgálom, hogy elkészült-e, de csak egy notifikáció az adott napra
		preparedTask = taskRepository.getOne(preparedTask.getId());
		List<Notification> notifications = notificationRepository.findAllByTaskAndNotificationType(
				preparedTask, NotificationTypeEnum.TASK_DEADLINE
				);
		// Egy elemre számítok
		Assert.assertEquals(1, notifications.size());
		
	}
}
