package hu.nextent.peas.batch;

import javax.sql.DataSource;

import org.mockito.Mockito;
import org.springframework.batch.core.configuration.annotation.BatchConfigurer;
import org.springframework.batch.core.configuration.annotation.DefaultBatchConfigurer;
import org.springframework.batch.core.explore.JobExplorer;
import org.springframework.batch.core.explore.support.JobExplorerFactoryBean;
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
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.transaction.PlatformTransactionManager;

import hu.nextent.peas.config.PeasBachConfig;
import hu.nextent.peas.config.WebMvcConfiguration;
import hu.nextent.peas.jpa.config.HibernateTestConfig;
import hu.nextent.peas.mail.TestDummyMailConfig;
import hu.nextent.peas.security.services.AuthService;

@Profile("test")
@Configuration
@ComponentScan(
		basePackages = {
				// Hogy betöltse az ütemező komponenseket
				"hu.nextent.peas.scheduler",
				"hu.nextent.peas.facades", 
				"hu.nextent.peas.events",
				"hu.nextent.peas.cache",
				"hu.nextent.peas.email",
				"hu.nextent.peas.utils",
		},
		excludeFilters = 
				@ComponentScan.Filter(type = FilterType.ANNOTATION, classes = Configuration.class)
		)
@Import(
		{
		WebMvcConfiguration.class,
		HibernateTestConfig.class,
		TestDummyMailConfig.class,
		PeasBachConfig.class
		}
		)
public class TestBachConfig {

	
	@Autowired
	private DataSource dataSource;
	
	@Autowired
	@Qualifier("txManager")
    private PlatformTransactionManager txManager;
	
    @Bean
    public BatchConfigurer batchConfigurer() {
	    return new DefaultBatchConfigurer() {
	    	
	    	
	    	
	        @Override
	        protected JobRepository createJobRepository() throws Exception {
	            JobRepositoryFactoryBean factoryBean = new JobRepositoryFactoryBean();
	            factoryBean.setDatabaseType("MySql");
	            factoryBean.setIsolationLevelForCreate("ISOLATION_REPEATABLE_READ");
	            factoryBean.setDataSource(dataSource);
	            factoryBean.setTransactionManager(txManager);
	            factoryBean.setValidateTransactionState(false);
	            factoryBean.afterPropertiesSet();
	            return factoryBean.getObject();
	        }
	
	        @Override
	        protected JobExplorer createJobExplorer() throws Exception {
	            JobExplorerFactoryBean factoryBean = new JobExplorerFactoryBean();
	            factoryBean.setDataSource(dataSource);
	            factoryBean.afterPropertiesSet();
	            return factoryBean.getObject();
	        }
	
	        @Override
	        protected JobLauncher createJobLauncher() throws Exception {
	            SimpleJobLauncher jobLauncher = new SimpleJobLauncher();
	            jobLauncher.setJobRepository(getJobRepository());
	            //jobLauncher.setTaskExecutor(new SimpleAsyncTaskExecutor());
	            jobLauncher.afterPropertiesSet();
	            return jobLauncher;
	        }
	    };
    }

	@Bean
    @Primary
    public AuthService authService() {
        return Mockito.mock(AuthService.class);
    }
}
