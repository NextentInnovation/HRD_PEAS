package hu.nextent.peas.config;

import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;

import hu.nextent.peas.security.config.SecurityConfig;

public class WebApplication extends AbstractAnnotationConfigDispatcherServletInitializer {

    @Override
    protected Class<?>[] getRootConfigClasses() {
        return new Class[] { 
        		PeasApiRootConfigurator.class,
        		SecurityConfig.class,
        		SwaggerUiConfiguration.class
        		};
    }

    @Override
    protected Class<?>[] getServletConfigClasses() {
    	return null;
    }

    @Override
    protected String[] getServletMappings() {
        return new String[] { "/" };
    }
}