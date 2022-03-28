package hu.nextent.peas.service;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Profile;

import hu.nextent.peas.config.WebMvcConfiguration;
import hu.nextent.peas.jpa.config.HibernateTestConfig;
import hu.nextent.peas.mail.TestDummyMailConfig;

@Profile("test")
@ComponentScan(
		excludeFilters = 
				@ComponentScan.Filter(type = FilterType.ANNOTATION, classes = Configuration.class)
		)
@Import(
		{
		WebMvcConfiguration.class,
		HibernateTestConfig.class,
		TestAuthServiceConfig.class,
		TestDummyMailConfig.class
		}
		)
public class TestServiceConfig {

}
