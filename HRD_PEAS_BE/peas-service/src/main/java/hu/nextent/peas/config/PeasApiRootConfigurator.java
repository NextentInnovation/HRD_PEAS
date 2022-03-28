package hu.nextent.peas.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import hu.nextent.peas.jpa.config.HibernateConfig;

@Configuration
@EnableWebMvc
@EnableScheduling
@ComponentScan(
		basePackages = "hu.nextent.peas"
		, excludeFilters = @ComponentScan.Filter(type = FilterType.ANNOTATION, classes = Configuration.class)
		)
@PropertySources(
	{
		//@PropertySource(value = "classpath:application.properties", ignoreResourceNotFound=true),
		@PropertySource(value = "classpath:swagger.properties"),
		@PropertySource(value = "classpath:application.properties"),
	}
)
@Import(
		{
			//PeasAsyncConfigurator.class,
			WebMvcConfiguration.class,
			HibernateConfig.class,
			MailConfig.class,
			PeasBachConfig.class,
			//SwaggerUiConfiguration.class
		}
)
public class PeasApiRootConfigurator {


}
