//package hu.nextent.peas.config;
//
//import java.util.concurrent.Executor;
//
//import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.scheduling.annotation.AsyncConfigurer;
//import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
//
//import hu.nextent.peas.utils.CustomAsyncExceptionHandler;
//
//
//@Configuration
//public class PeasAsyncConfigurator 
//implements AsyncConfigurer {
//
//	@Bean("threadPoolTaskExecutor")
//	public Executor taskExecutor() {
//	    ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
//	    executor.setCorePoolSize(2);
//	    executor.setMaxPoolSize(2);
//	    executor.setQueueCapacity(500);
//	    executor.setThreadNamePrefix("peas-async-");
//	    executor.initialize();
//	    return executor;
//	}
//	
//	@Override
//	public Executor getAsyncExecutor() {
//	    return taskExecutor();
//	}
//	
//	@Override
//	public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler() {
//	    return new CustomAsyncExceptionHandler();
//	}
//
//}
