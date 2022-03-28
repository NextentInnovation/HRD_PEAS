package hu.nextent.peas.jpa.config;

import static hu.nextent.peas.jpa.constans.HibernateConstant.HIBERNATE_DATASOURCE_URL;
import static hu.nextent.peas.jpa.constans.HibernateConstant.HIBERNATE_DIALECT;
import static hu.nextent.peas.jpa.constans.HibernateConstant.HIBERNATE_FORMAT_SQL;
import static hu.nextent.peas.jpa.constans.HibernateConstant.HIBERNATE_HBM2DDL_AUTO;
import static hu.nextent.peas.jpa.constans.HibernateConstant.HIBERNATE_ID_NEW_GENERATOR;
import static hu.nextent.peas.jpa.constans.HibernateConstant.HIBERNATE_SHOW_SQL;

import java.sql.Connection;
import java.util.Properties;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.dao.annotation.PersistenceExceptionTranslationPostProcessor;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.repository.query.QueryLookupStrategy.Key;
import org.springframework.jdbc.datasource.lookup.JndiDataSourceLookup;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.TransactionManagementConfigurer;
import org.springframework.util.StringUtils;

import lombok.extern.slf4j.Slf4j;

@Profile("!test")
@Configuration
@PropertySource("classpath:application.properties")
@EnableJpaRepositories(
		basePackages = {
				"hu.nextent.peas.jpa.dao"
		}
		, excludeFilters = @ComponentScan.Filter(type = FilterType.ANNOTATION, classes = Configuration.class)
		, queryLookupStrategy = Key.CREATE_IF_NOT_FOUND
		, transactionManagerRef = "txManager"
		)
@EnableTransactionManagement
@Slf4j
public class HibernateConfig implements TransactionManagementConfigurer {

    private String dialect;
    private String show_sql;
    private String format_sql;
    private String id_new_generator_mappings;
    private String hbm2ddl_auto;
    
    private String jndi;

    private Properties hibernateProperties = new Properties();

    @Autowired
    private Environment env;

    private DataSource dataSource = null;
    
    private LocalContainerEntityManagerFactoryBean lcemfb;
    
    @PostConstruct
    public void init() {
		dialect = env.getProperty(HIBERNATE_DIALECT);
		show_sql = env.getProperty(HIBERNATE_SHOW_SQL);
		format_sql = env.getProperty(HIBERNATE_FORMAT_SQL);
		id_new_generator_mappings = env.getProperty(HIBERNATE_ID_NEW_GENERATOR);
		hbm2ddl_auto = env.getProperty(HIBERNATE_HBM2DDL_AUTO);
		
		jndi = env.getProperty(HIBERNATE_DATASOURCE_URL);
		
        log.debug(HIBERNATE_HBM2DDL_AUTO + ": " + hbm2ddl_auto);
        log.debug(HIBERNATE_DIALECT + ": " + dialect);
        log.debug(HIBERNATE_SHOW_SQL + ": " + show_sql);
        log.debug(HIBERNATE_FORMAT_SQL + ": " + format_sql);
        log.debug(HIBERNATE_ID_NEW_GENERATOR + ": " + id_new_generator_mappings);
        log.debug("datasource " + HIBERNATE_DATASOURCE_URL + ":" + jndi);

		if (!StringUtils.isEmpty(hbm2ddl_auto)) {
		 	hibernateProperties.setProperty(HIBERNATE_HBM2DDL_AUTO, hbm2ddl_auto);
		}
		hibernateProperties.put(HIBERNATE_DIALECT, dialect);
		hibernateProperties.put(HIBERNATE_SHOW_SQL, show_sql);
		hibernateProperties.put(HIBERNATE_FORMAT_SQL, format_sql);
		hibernateProperties.put(HIBERNATE_ID_NEW_GENERATOR, id_new_generator_mappings);
		hibernateProperties.put("hibernate.allow_update_outside_transaction", "true");
		hibernateProperties.put("hibernate.connection.autocommit", "false");
        //isolation level
		hibernateProperties.put("hibernate.connection.isolation", String.valueOf(Connection.TRANSACTION_READ_COMMITTED));
		// JPA jpa.properties.hibernate.jdbc.time_zone=UTC
		hibernateProperties.put("jpa.properties.hibernate.jdbc.time_zone", "UTC");
		
		hibernateProperties.put("hibernate.jdbc.batch_size", "5");
		hibernateProperties.put("hibernate.order_inserts", "true");
		
    }

    @Bean
    @Primary
    public LocalContainerEntityManagerFactoryBean entityManagerFactory() {
    	if (log.isDebugEnabled()) {
    		log.debug("create entityManagerFactory");
    	}
    	if (lcemfb != null) {
    		return lcemfb;
    	}
    	
        lcemfb = new LocalContainerEntityManagerFactoryBean();
        lcemfb.setDataSource(dataSource());
        lcemfb.setJpaVendorAdapter(getJpaVendorAdapter());
        lcemfb.setPersistenceUnitName("myJpaPersistenceUnit");
        lcemfb.setPackagesToScan(
        		new String[] { 
        				"hu.nextent.peas.jpa.entity"
        				, "hu.nextent.peas.jpa.view"
        				});
        lcemfb.setJpaProperties(hibernateProperties);
        return lcemfb;
    }

    public JpaVendorAdapter getJpaVendorAdapter() {
        return new HibernateJpaVendorAdapter();
    }
    
    @Bean
    public PlatformTransactionManager txManager() {
    	if (log.isDebugEnabled()) {
    		log.debug("create transactionManager");
    	}
    	JpaTransactionManager jpaTransactionManager = new JpaTransactionManager();
    	jpaTransactionManager.setEntityManagerFactory(entityManagerFactory().getObject());
    	
        return jpaTransactionManager;
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
        if (dataSource != null) {
    		return dataSource;
    	}
        
        log.info("create jndi DataSource");
		JndiDataSourceLookup lookup = new JndiDataSourceLookup();
		dataSource = lookup.getDataSource(jndi);
    	return dataSource;
    }

    
    
    @Bean
	public SessionFactory getSessionFactory() {
	    if (entityManagerFactory().getObject().unwrap(SessionFactory.class) == null) {
	        throw new NullPointerException("factory is not a hibernate factory");
	    }
	    SessionFactory sessionFactory = entityManagerFactory().getObject().unwrap(SessionFactory.class);
	    return sessionFactory;
	}

	@Override
	public PlatformTransactionManager annotationDrivenTransactionManager() {
		return txManager();
	}

}
