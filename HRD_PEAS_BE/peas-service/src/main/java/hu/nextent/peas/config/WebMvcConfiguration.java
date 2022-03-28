package hu.nextent.peas.config;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.springframework.cache.CacheManager;
import org.springframework.cache.caffeine.CaffeineCache;
import org.springframework.cache.support.SimpleCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.converter.ConverterRegistry;
import org.springframework.format.FormatterRegistry;
import org.springframework.format.datetime.standard.DateTimeFormatterRegistrar;
import org.springframework.format.support.DefaultFormattingConversionService;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.github.benmanes.caffeine.cache.Caffeine;

import hu.nextent.peas.constant.CacheNames;
import hu.nextent.peas.utils.RFC3339DateFormat;


@Configuration
@ComponentScan(
		excludeFilters = 
				@ComponentScan.Filter(type = FilterType.ANNOTATION, classes = Configuration.class)
		)
public class WebMvcConfiguration
//extends WebMvcConfigurationSupport
implements WebMvcConfigurer 
{
    
	private DefaultFormattingConversionService conversionService = new DefaultFormattingConversionService();

	/*
    @Override
    public void configureDefaultServletHandling(DefaultServletHandlerConfigurer configurer) {
        configurer.enable();
    }
    */

	@Bean
    public ConversionService conversionService() {
        return conversionService;
    }
	
	@Bean
    public ConverterRegistry converterRegistry() {
        return conversionService;
    }

    @Override
    public void addFormatters(FormatterRegistry registry) {
        DateTimeFormatterRegistrar registrar = new DateTimeFormatterRegistrar();
        registrar.setUseIsoFormat(true);
        registrar.registerFormatters(registry);
    }
   
    @Bean
    public PasswordEncoder passwordEncoder() {
    	return new BCryptPasswordEncoder();
    }
    
    @Bean
    public Jackson2ObjectMapperBuilder builder() {
  	  //log.debug("builder");
      Jackson2ObjectMapperBuilder builder = new Jackson2ObjectMapperBuilder()
          .indentOutput(true)
          .featuresToDisable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
          .dateFormat(new RFC3339DateFormat());
      return builder;
    }

    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
      //log.debug("configureMessageConverters");
      converters.add(new MappingJackson2HttpMessageConverter(objectMapper()));
      //super.configureMessageConverters(converters);
      //this.addDefaultHttpMessageConverters(converters);
    }

    @Bean
    public ObjectMapper objectMapper(){
  	  //log.debug("objectMapper");
  	  return builder().build();
    }
    
    @Bean
    public CacheManager cacheManager() {
    	SimpleCacheManager cacheManager =  new SimpleCacheManager();
    	cacheManager.setCaches(Arrays.asList(
    			new CaffeineCache(CacheNames.SYSTEM_PARAMETER_CACHE, 
    					Caffeine.newBuilder()
    						.maximumSize(100)
    						.expireAfterWrite(30, TimeUnit.MINUTES)
    						.build()
    				    ),
    		    new CaffeineCache(CacheNames.COMPANY_PARAMETER_CACHE, 
    		    		Caffeine.newBuilder()
							.maximumSize(1000)
							.expireAfterWrite(30, TimeUnit.MINUTES)
							.build()
    		    		),
    		    new CaffeineCache(CacheNames.LABEL_CACHE, 
    		    		Caffeine.newBuilder()
							.maximumSize(1000)
							.expireAfterWrite(30, TimeUnit.MINUTES)
							.build()
    		    		),
    		    new CaffeineCache(CacheNames.NOTIFICATION_ACTION_CACHE, 
    		    		Caffeine.newBuilder()
							.maximumSize(100)
							.expireAfterWrite(30, TimeUnit.MINUTES)
							.build()
    		    		)
    			));
    	return cacheManager;
    }
}
