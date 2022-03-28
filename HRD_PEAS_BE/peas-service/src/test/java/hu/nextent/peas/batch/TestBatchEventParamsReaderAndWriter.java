package hu.nextent.peas.batch;

import java.util.Arrays;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.batch.test.MetaDataInstanceFactory;
import org.springframework.batch.test.StepScopeTestUtils;
import org.springframework.batch.test.context.SpringBatchTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;
import org.springframework.transaction.annotation.Transactional;

import hu.nextent.peas.jpa.entity.BatchEvent;
import hu.nextent.peas.jpa.entity.BatchEventTypeEnum;

@ActiveProfiles("test")
@SpringBatchTest
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { TestBachConfig.class }, loader = AnnotationConfigContextLoader.class)
@Transactional
@DirtiesContext
public class TestBatchEventParamsReaderAndWriter {

	@SuppressWarnings("unused")
	@Autowired
	private BatchStarterService batchStarterService;
	
	@SuppressWarnings("unused")
	@Autowired
	private JobLauncherTestUtils jobLauncherTestUtils;
	
	@Autowired
	BatchEventParamsReader batchEventParamsReader;
	
	@Autowired
	BatchEventItemWriter batchEventItemWriter;
	
	@Test
	@Rollback
	public void testReader() throws Exception {
		
		
		 // given
        StepExecution stepExecution = MetaDataInstanceFactory.createStepExecution(defaultJobParameters());

        // when
        StepScopeTestUtils.doInStepScope(stepExecution, () -> {
        	batchEventParamsReader.beforeStep(stepExecution);
            @SuppressWarnings("unused")
			BatchEvent evt = null;
            while ((evt = batchEventParamsReader.read()) != null) {
            	
            }
            return null;
        });
	}
	
	
	private JobParameters defaultJobParameters() {
		JobParameters jobParameters = new JobParametersBuilder()
				.addLong("companyId", null)
				.addString("batchEventType", BatchEventTypeEnum.NOTIFICATION_SEND.name())
				.toJobParameters();
		return jobParameters;
   }
	
	
	@Test
	@Rollback
	public void testWriter() throws Exception {
		// given
        StepExecution stepExecution = MetaDataInstanceFactory.createStepExecution(defaultJobParameters());

        BatchEvent evt = BatchEvent.builder()
        		.batchEventType(BatchEventTypeEnum.NOTIFICATION_SEND)
        		.parameterId(1l)
        		.build();
        
        // when
        StepScopeTestUtils.doInStepScope(stepExecution, () -> {
        		batchEventItemWriter.write(Arrays.asList(evt));
        		return null;
        	}
        );
        
        Assert.assertNotNull(evt.getId());
	}
}
