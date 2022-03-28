package hu.nextent.peas.batch;

import org.junit.After;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobInstance;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.batch.test.JobRepositoryTestUtils;
import org.springframework.batch.test.StepScopeTestExecutionListener;
import org.springframework.batch.test.context.SpringBatchTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;
import org.springframework.transaction.annotation.Transactional;

import hu.nextent.peas.jpa.dao.BatchEventRepository;
import hu.nextent.peas.jpa.dao.NotificationRepository;
import hu.nextent.peas.jpa.entity.BatchEventTypeEnum;
import hu.nextent.peas.utils.MockMailSender;

@ActiveProfiles("test")
@SpringBatchTest
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { TestBachConfig.class })
//@DirtiesContext
@TestExecutionListeners({ 
	DependencyInjectionTestExecutionListener.class, 
	DirtiesContextTestExecutionListener.class,
	StepScopeTestExecutionListener.class,
	TransactionalTestExecutionListener.class
	})
@DirtiesContext(classMode = ClassMode.AFTER_CLASS)
@Transactional
public class TestSendMailNotifications {
	@Autowired
	private BatchEventGeneratorService batchEventGeneratorService;
	
    @Autowired
    private JobLauncherTestUtils jobLauncherTestUtils;

	@Autowired
	@Qualifier("jobSendNotification")
    private Job job;
	
	@Autowired
	private BatchEventRepository batchEventRepository;
	
	@Autowired
	private NotificationRepository notificationRepository;

    @Autowired
    private JobRepositoryTestUtils jobRepositoryTestUtils;
    
    @Autowired
    private TestSendMailNotifications _self;

    @After
    public void cleanUp() {
        jobRepositoryTestUtils.removeJobExecutions();
    }
    
	@Test
	@Rollback
	public void testMe() throws Exception {
		MockMailSender.setCnt(0L);
		Long LIMIT = 10l;
		
		batchEventGeneratorService.generateNotificationSend(LIMIT.intValue());
		Long cntBatchEvt = batchEventRepository.count();
		
		Assert.assertEquals(LIMIT, cntBatchEvt);
		
		jobLauncherTestUtils.setJob(job);
		JobExecution jobExecution = jobLauncherTestUtils.launchJob(defaultJobParameters());

		Assert.assertTrue(jobExecution.getAllFailureExceptions().isEmpty());

		JobInstance actualJobInstance = jobExecution.getJobInstance();
	    ExitStatus actualJobExitStatus = jobExecution.getExitStatus();
	    Assert.assertEquals(actualJobInstance.getJobName(),"jobSendNotification");
	    Assert.assertEquals(actualJobExitStatus.getExitCode(), "COMPLETED");
        
		Assert.assertEquals(LIMIT, MockMailSender.getCnt());
	}
	
	private JobParameters defaultJobParameters() {
		JobParameters jobParameters = new JobParametersBuilder()
				.addLong("companyId", null)
				.addString("batchEventType", BatchEventTypeEnum.NOTIFICATION_SEND.name())
				.toJobParameters();
		return jobParameters;
   }
	

	
}
