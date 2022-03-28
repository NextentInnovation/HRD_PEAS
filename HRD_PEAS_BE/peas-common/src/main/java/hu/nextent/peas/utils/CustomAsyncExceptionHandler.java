package hu.nextent.peas.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;

import java.lang.reflect.Method;

public class CustomAsyncExceptionHandler implements AsyncUncaughtExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(CustomAsyncExceptionHandler.class);

    @Override
    public void handleUncaughtException(
            Throwable throwable, Method method, Object... obj) {

        log.error("Exception message - " + throwable.getMessage());
        log.error("Method name - " + method.getName());
        if (obj != null && obj.length > 0) {
            log.error("Parameter value - " + obj[0]);
        }
    }

}
