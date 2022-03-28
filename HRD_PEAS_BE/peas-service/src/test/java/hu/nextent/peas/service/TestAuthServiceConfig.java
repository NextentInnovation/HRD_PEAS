package hu.nextent.peas.service;

import org.mockito.Mockito;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.Primary;

import hu.nextent.peas.security.services.AuthService;

@Configuration
// Vizsgált csomagok
@ComponentScan(
		basePackages = {
				  "hu.nextent.peas.service"
				, "hu.nextent.peas.converters"
				, "hu.nextent.peas.events"
				, "hu.nextent.peas.facades"
				, "hu.nextent.peas.jpa.dao"
				, "hu.nextent.peas.utils"
				, "hu.nextent.peas.cache"
		}
		, excludeFilters = 
				@ComponentScan.Filter(type = FilterType.ANNOTATION, classes = Configuration.class)
		)
public class TestAuthServiceConfig {

	@Bean
    @Primary
    public AuthService authService() {
        return Mockito.mock(AuthService.class);
    }
}
