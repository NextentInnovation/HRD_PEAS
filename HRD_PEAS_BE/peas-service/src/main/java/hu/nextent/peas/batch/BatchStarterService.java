package hu.nextent.peas.batch;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionException;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import hu.nextent.peas.jpa.entity.BatchEventTypeEnum;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@Transactional
public class BatchStarterService {
	
	private final Integer LIMIT_SEND = 1000;
	
	@Autowired
	private BatchEventGeneratorService batchEventGeneratorService;
	
	@Autowired
	@Qualifier("peasJobLauncher")
    private JobLauncher peasJobLuncher;

	@Autowired
	@Qualifier("jobSendNotification")
    private Job jobSendNotification;

    @Transactional(propagation = Propagation.REQUIRES_NEW)
	public void startNotificationSend(boolean generateEvents) {
		if (generateEvents) {
			batchEventGeneratorService.generateNotificationSend(LIMIT_SEND);
		}
		
		JobParameters jobParameters = new JobParametersBuilder()
				.addLong("companyId", null)
				.addString("batchEventType", BatchEventTypeEnum.NOTIFICATION_SEND.name())
				.addLong("time", System.currentTimeMillis())
				.toJobParameters();
		
		try {
            JobExecution execution = peasJobLuncher.run(jobSendNotification, jobParameters);
            log.info("jobCreateRating, Batch job ends with status as {}", execution.getStatus().name());
        } catch (JobExecutionException e) {
            log.error("jobCreateRating, batch job error: ", e);
        }
	}
	
}
