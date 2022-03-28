package hu.nextent.peas.security.filter;

import java.io.IOException;
import java.util.Arrays;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

import hu.nextent.peas.model.ErrorMessageModel;

/**
 * The RestAccessDeniedHandler is called by the ExceptionTranslationFilter to handle all AccessDeniedExceptions.
 * These exceptions are thrown when the authentication is valid but access is not authorized.
 *
 */
@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON)
public class RestAccessDeniedHandler implements AccessDeniedHandler {

	@Autowired
	ObjectMapper objectMapper;
	
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException {
    	String[] exceptionList = ExceptionUtils.getStackFrames(accessDeniedException);
    	ResponseEntityWriter.writeTo(
    			response, 
    			objectMapper, 
    			ResponseEntity
    				.status(HttpStatus.FORBIDDEN)
    				.body(new ErrorMessageModel()
    						.code(Integer.toString(HttpStatus.FORBIDDEN.value()))
    						.message(HttpStatus.FORBIDDEN.getReasonPhrase())
    						.stacktrace(Arrays.asList(exceptionList))
    						)
    			);
    	
        response.getWriter().flush();
    }
}
