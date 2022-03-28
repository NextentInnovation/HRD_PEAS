package hu.nextent.peas.config;

import javax.sql.DataSource;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.support.SimpleJobLauncher;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.repository.support.JobRepositoryFactoryBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.Import;
import org.springframework.transaction.PlatformTransactionManager;

import hu.nextent.peas.batch.BatchEventItemWriter;
import hu.nextent.peas.batch.BatchEventParamsReader;
import hu.nextent.peas.batch.SendNotificationProcessor;
import hu.nextent.peas.jpa.config.HibernateConfig;
import hu.nextent.peas.jpa.entity.BatchEvent;

@Configuration
@ComponentScan(
		basePackages = "hu.nextent.peas.batch"
		, excludeFilters = 
				@ComponentScan.Filter(type = FilterType.ANNOTATION, classes = Configuration.class)
		)
@Import({HibernateConfig.class})
@EnableBatchProcessing
public class PeasBachConfig {
	
	@Autowired
	@Qualifier("txManager")
    private PlatformTransactionManager txManager;
	
	@Autowired
	private DataSource dataSource;
	
	@Autowired
    private JobBuilderFactory jobBuilderFactory;
 
    @Autowired
    private StepBuilderFactory stepBuilderFactory;
    
    @Autowired
    private BatchEventParamsReader batchEventParamsReader;
    
    @Autowired
    private SendNotificationProcessor sendNotificationProcessor;

    @Autowired
    private BatchEventItemWriter batchEventItemWriter;
    
    @Bean
    public JobRepository getJobRepository() throws Exception {
        JobRepositoryFactoryBean factoryBean = new JobRepositoryFactoryBean();
        factoryBean.setDatabaseType("MySQL");
        factoryBean.setDataSource(dataSource);
        factoryBean.setTransactionManager(txManager);
        factoryBean.setIsolationLevelForCreate("ISOLATION_REPEATABLE_READ");
        factoryBean.setValidateTransactionState(false);
        return factoryBean.getObject();
    }
    
    @Bean("peasJobLauncher")
    public JobLauncher getJobLauncher() throws Exception {
        SimpleJobLauncher jobLauncher = new SimpleJobLauncher();
        jobLauncher.setJobRepository(getJobRepository());
        //jobLauncher.setTaskExecutor(new SimpleAsyncTaskExecutor());
        jobLauncher.afterPropertiesSet();
        return jobLauncher;
    }
    
   
    /*
     * Notifikációs levél küldése
     */
    @Bean
    public Step stepSendNotification() {
    	return stepBuilderFactory
    			.get("stepSendNotification")
    			.listener(batchEventParamsReader)
    			.<BatchEvent, BatchEvent> chunk(BatchEventParamsReader.PAGE_SIZE)
    			.reader(batchEventParamsReader)
    			.processor(sendNotificationProcessor)
    			.writer(batchEventItemWriter)
    			.build();
    }
    
    @Bean(name = "jobSendNotification")
    public Job jobSendNotification(@Qualifier("stepSendNotification") Step stepSendNotification) {
        return jobBuilderFactory
        		.get("jobSendNotification")
        		.start(stepSendNotification)
        		.build();
    }
    
//    @Bean
//    public StepScope stepScope() {
//        final StepScope stepScope = new StepScope();
//        stepScope.setAutoProxy(true);
//        return stepScope;
//    }
}
