package hu.nextent.peas.scheduler;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Profile;

import hu.nextent.peas.config.WebMvcConfiguration;
import hu.nextent.peas.jpa.config.HibernateTestConfig;
import hu.nextent.peas.mail.TestDummyMailConfig;

@Profile("test")
@Configuration
@ComponentScan(
		basePackages = {
				// Hogy betöltse az ütemező komponenseket
				"hu.nextent.peas.scheduler",
				"hu.nextent.peas.facades", 
				"hu.nextent.peas.events",
				"hu.nextent.peas.cache"
		},
		excludeFilters = 
				@ComponentScan.Filter(type = FilterType.ANNOTATION, classes = Configuration.class)
		)
@Import(
		{
		WebMvcConfiguration.class,
		HibernateTestConfig.class,
		//PeasBachConfig.class,
		TestDummyMailConfig.class
		}
		)
public class TestSchedulerConfig {

}
