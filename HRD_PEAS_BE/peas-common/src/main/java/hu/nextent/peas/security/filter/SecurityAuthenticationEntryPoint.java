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
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

import hu.nextent.peas.model.ErrorMessageModel;

/**
 * SecurityAuthenticationEntryPoint is called by ExceptionTranslationFilter to handle all AuthenticationException.
 * These exceptions are thrown when authentication failed : wrong login/password, authentication unavailable, invalid token
 * authentication expired, etc.
 * <p>
 * For problems related to access (roles), see RestAccessDeniedHandler.
 */
@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON)
public class SecurityAuthenticationEntryPoint implements AuthenticationEntryPoint {

	@Autowired
	ObjectMapper objectMapper;

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException {

    	String[] exceptionList = ExceptionUtils.getStackFrames(authException);

    	ResponseEntityWriter.writeTo(
    			response, 
    			objectMapper, 
    			ResponseEntity
    				.status(HttpStatus.UNAUTHORIZED)
    				.body(new ErrorMessageModel()
    						.code(Integer.toString(HttpStatus.UNAUTHORIZED.value()))
    						.message(HttpStatus.UNAUTHORIZED.getReasonPhrase())
    						.stacktrace(Arrays.asList(exceptionList))
    						)
    			);

        response.getWriter().flush();
    }
}
