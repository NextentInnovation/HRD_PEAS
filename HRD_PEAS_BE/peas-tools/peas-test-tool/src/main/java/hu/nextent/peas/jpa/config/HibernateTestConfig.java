package hu.nextent.peas.jpa.config;

import java.sql.Connection;
import java.util.Properties;

import javax.annotation.PostConstruct;
import javax.naming.NamingException;
import javax.sql.DataSource;

import org.hibernate.SessionFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.dao.annotation.PersistenceExceptionTranslationPostProcessor;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.TransactionManagementConfigurer;

import liquibase.exception.LiquibaseException;
import liquibase.integration.spring.SpringLiquibase;
import lombok.extern.slf4j.Slf4j;

@Profile("test")
@Configuration
@EnableJpaRepositories(
		basePackages = {
				"hu.nextent.peas.jpa.dao"
		}
		, excludeFilters = 
				@ComponentScan.Filter(type = FilterType.ANNOTATION, classes = Configuration.class)
		, transactionManagerRef = "txManager"
		)
@EnableTransactionManagement
@Slf4j
public class HibernateTestConfig implements TransactionManagementConfigurer {
	
    private Properties hibernateProperties = new Properties();

    private LocalContainerEntityManagerFactoryBean lcemfb;
    
    @PostConstruct
    public void init() {
        hibernateProperties.put("hibernate.dialect", "org.hibernate.dialect.MySQL8Dialect");
        hibernateProperties.put("hibernate.show_sql", true);
        hibernateProperties.put("hibernate.format_sql", true);
        hibernateProperties.put("hibernate.id.new_generator_mappings", true);
		hibernateProperties.put("hibernate.allow_update_outside_transaction", "true");
		hibernateProperties.put("hibernate.connection.autocommit", "false");
		hibernateProperties.put("hibernate.hbm2ddl.auto", "validate");
		//isolation level
		hibernateProperties.put("hibernate.connection.isolation", String.valueOf(Connection.TRANSACTION_READ_COMMITTED));
		// JPA jpa.properties.hibernate.jdbc.time_zone=UTC
		hibernateProperties.put("jpa.properties.hibernate.jdbc.time_zone", "UTC");
		
		hibernateProperties.put("hibernate.jdbc.batch_size", "5");
		hibernateProperties.put("hibernate.order_inserts", "true");

    }
    
	@Bean
	public SpringLiquibase liquibase() throws LiquibaseException {
		SpringLiquibase liquibase = new SpringLiquibase();
		liquibase.setChangeLog("classpath:liquibase-changeLog.xml");
		liquibase.setDataSource(dataSource());
		liquibase.setContexts("memory");
		liquibase.setShouldRun(true);
		liquibase.setDropFirst(true);
		//liquibase.afterPropertiesSet();
		return liquibase;
	}


    @Bean
    @Primary
    @DependsOn("liquibase")
    public LocalContainerEntityManagerFactoryBean entityManagerFactory() {
    	if (log.isDebugEnabled()) {
    		log.debug("create entityManagerFactory");
    	}
    	if (lcemfb != null) {
    		return lcemfb;
    	}
    	
        lcemfb = new LocalContainerEntityManagerFactoryBean();
        lcemfb.setDataSource(dataSource());
        lcemfb.setJpaVendorAdapter(new HibernateJpaVendorAdapter());
        lcemfb.setPersistenceUnitName("myJpaPersistenceUnit");
        lcemfb.setPackagesToScan(
        		new String[] { 
        				"hu.nextent.peas.jpa.entity"
        				, "hu.nextent.peas.jpa.view"
        				});
        lcemfb.setJpaProperties(hibernateProperties);
        return lcemfb;
    }

    @Bean
    public PlatformTransactionManager txManager() {
    	if (log.isDebugEnabled()) {
    		log.debug("create transactionManager");
    	}
        return new JpaTransactionManager(entityManagerFactory().getObject());
    }

    @Bean
    public PersistenceExceptionTranslationPostProcessor exceptionTranslation() {
    	if (log.isDebugEnabled()) {
    		log.debug("create exceptionTranslation");
    	}
    	
        return new PersistenceExceptionTranslationPostProcessor();
    }
    
    @Bean
    public DataSource dataSource() {
    	DriverManagerDataSource driverManagerDataSource = new DriverManagerDataSource();
        driverManagerDataSource.setUrl("jdbc:h2:mem:peas;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE;MODE=MySQL;DATABASE_TO_LOWER=TRUE;CASE_INSENSITIVE_IDENTIFIERS=TRUE");
        driverManagerDataSource.setDriverClassName("org.h2.Driver");
        driverManagerDataSource.setUsername("sa");
        driverManagerDataSource.setPassword("");
        
        return driverManagerDataSource;
    }

    @Bean
	public SessionFactory getSessionFactory() throws NamingException {
	    if (entityManagerFactory().getObject().unwrap(SessionFactory.class) == null) {
	        throw new NullPointerException("factory is not a hibernate factory");
	    }
	    return entityManagerFactory().getObject().unwrap(SessionFactory.class);
	}

	@Override
	public PlatformTransactionManager annotationDrivenTransactionManager() {
		// TODO Auto-generated method stub
		return txManager();
	}

    
}
