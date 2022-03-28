package hu.nextent.peas.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;
import org.springframework.core.env.Environment;

import hu.nextent.peas.constant.CommonConstants;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.ParameterBuilder;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.ApiKey;
import springfox.documentation.service.Contact;
import springfox.documentation.service.SecurityScheme;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.service.Parameter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Configuration
@ComponentScan(
		basePackages = "hu.nextent.peas"
		, excludeFilters = @ComponentScan.Filter(type = FilterType.ANNOTATION, classes = Configuration.class)
		)
public class SwaggerDocumentationConfig {


	@Autowired
    private Environment env;
	
    ApiInfo apiInfo() {
       return new ApiInfoBuilder()
                .title(env.getProperty("swagger.appName", "PEAS Blabla API"))
                .description(env.getProperty("swagger.appDescription", "PEAS Blabla modul API"))
                .license(env.getProperty("swagger.license", ""))
                .licenseUrl(env.getProperty("swagger.licenseUrl", ""))
                .termsOfServiceUrl(env.getProperty("swagger.infoUrl", "http://nextent.hu"))
                .version(env.getProperty("swagger.appVersion", "1.0.0"))
                .contact(new Contact("","", env.getProperty("swagger.infoEmail", "apiteam@nextent.hu")))
                .build();
    }

    @Bean
    public Docket customImplementation(){
    	// Adding Language Header
    	ParameterBuilder aParameterBuilder = new ParameterBuilder();
        aParameterBuilder.name(CommonConstants.LANGUAGE_HEADER).modelRef(new ModelRef("string")).parameterType("header").required(false).build();
        List<Parameter> aParameters = new ArrayList<Parameter>();
        aParameters.add(aParameterBuilder.build());
         
    	Docket docket = new Docket(DocumentationType.SWAGGER_2)
                .select()
                    .apis(RequestHandlerSelectors.basePackage("hu.nextent.peas"))
                    .build()
                .directModelSubstitute(java.time.LocalDate.class, java.sql.Date.class)
                .directModelSubstitute(java.time.OffsetDateTime.class, java.util.Date.class)
                .genericModelSubstitutes(Optional.class)
                .apiInfo(apiInfo())
                .securitySchemes(securitySchemes())
                // Adding Language Header
                .globalOperationParameters(aParameters)
                ;
    	return docket;
    }
    
    private List<? extends SecurityScheme> securitySchemes() {
        return Arrays.asList(new ApiKey("Bearer %token", "Authorization", "Header"));
    }    


}
