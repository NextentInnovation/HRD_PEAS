package hu.nextent.peas.batch;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.batch.test.context.SpringBatchTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;
import org.springframework.transaction.annotation.Transactional;

@ActiveProfiles("test")
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBatchTest
@ContextConfiguration(classes = { TestBachConfig.class }, loader = AnnotationConfigContextLoader.class)
@Transactional
@DirtiesContext
public class TestTestBatchConfig {

	@SuppressWarnings("unused")
	@Autowired
	private BatchStarterService batchStarterService;
	
	@SuppressWarnings("unused")
	@Autowired
	private JobLauncherTestUtils jobLauncherTestUtils;
	
	@Test
	@Rollback
	public void testConfig() {
		
	}
	
}
