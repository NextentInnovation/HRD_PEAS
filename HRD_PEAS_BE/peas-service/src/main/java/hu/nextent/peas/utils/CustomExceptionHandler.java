package hu.nextent.peas.utils;

import java.util.Arrays;
import java.util.Locale;
import java.util.Map;

import javax.validation.ValidationException;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.security.authentication.AccountStatusException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import org.springframework.web.util.WebUtils;

import hu.nextent.peas.exceptions.BaseResponseException;
import hu.nextent.peas.model.ErrorMessageModel;

@ControllerAdvice
public class CustomExceptionHandler 
extends ResponseEntityExceptionHandler
{

	
	@Autowired
	private MessageSource messageSource;
	
	@Autowired
	@Lazy
	private UserServiceHelper userServiceHelper;
	
    @InitBinder
    public void initBinder(WebDataBinder binder) {
        StringTrimmerEditor stringTrimmer = new StringTrimmerEditor(true);
        binder.registerCustomEditor(String.class, stringTrimmer);
    }
    
    @ExceptionHandler({ BaseResponseException.class })
    public ResponseEntity<ErrorMessageModel> handlePeasException(
    		BaseResponseException ex, 
    		WebRequest request
    		) {
    	HttpStatus exStatus = ex.getStatus() == null ? HttpStatus.BAD_REQUEST : ex.getStatus();
    	String reasonMessage = applyReason(ex);
    	
    	ErrorMessageModel model = 
    			new ErrorMessageModel()
    				.stacktrace(Arrays.asList(ExceptionUtils.getStackFrames(ex)))
    				.status(exStatus.value())
    				.code(exStatus.name())
    				.message(reasonMessage)
    				;
    	
    	return ResponseEntity.status(exStatus).body(model);
    }
    
    private String applyReason(BaseResponseException ex) {
    	Locale langLocale = new Locale(userServiceHelper.getLanguage());
    	return messageSource.getMessage(
    			ex.getReasonCode(), 
    			ex.getArgs(), 
    			ex.getReasonCode(), 
    			langLocale);
    }
    
    
    
    public static Object[][] EXCEPTION_STATUS = {
    		{ ValidationException.class, HttpStatus.BAD_REQUEST },
    		{ AccountStatusException.class, HttpStatus.FORBIDDEN },
    		{ BadCredentialsException.class, HttpStatus.FORBIDDEN },
    		{ InsufficientAuthenticationException.class, HttpStatus.FORBIDDEN },
    		{ UsernameNotFoundException.class, HttpStatus.NOT_FOUND },
    		{ Exception.class, HttpStatus.INTERNAL_SERVER_ERROR },
    };
    
    public static Map<Object, Object> EXCEPTION_STATUS_MAP = ArrayUtils.toMap(EXCEPTION_STATUS);
    
    @ExceptionHandler({
		ValidationException.class,
		// AuthenticationException
		AccountStatusException.class,
    	BadCredentialsException.class,
    	InsufficientAuthenticationException.class,
    	UsernameNotFoundException.class,
    	// Generikus
    	Exception.class
    	})
    public ResponseEntity<Object> handleValidationException(
    		Exception ex, 
    		WebRequest request
    		) {
    	
    	String[] exceptionList = ExceptionUtils.getStackFrames(ex);
    	
    	HttpStatus status = HttpStatus.BAD_REQUEST;
    	if (EXCEPTION_STATUS_MAP.containsKey(ex.getClass())) {
    		status = (HttpStatus)EXCEPTION_STATUS_MAP.get(ex.getClass());
    		return ResponseEntity
    				.status(status)
    				.body(
    						new ErrorMessageModel()
    							.status(status.value())
    							.code("Validation Exception")
    							.message(ex.getClass().getCanonicalName())
    	    					.status(status.value())
    	    					.stacktrace(Arrays.asList(exceptionList))
    						);
    	}
    	HttpHeaders headers = new HttpHeaders();
    	return handleExceptionInternal(ex, null, headers, status, request);
    }
    
    // Átírom az őst, hogy nekem megfelelő legyen
	protected ResponseEntity<Object> handleExceptionInternal(
			Exception ex, 
			@Nullable Object body,
			HttpHeaders headers, 
			HttpStatus status, 
			WebRequest request) {

		if (HttpStatus.INTERNAL_SERVER_ERROR.equals(status)) {
			request.setAttribute(WebUtils.ERROR_EXCEPTION_ATTRIBUTE, ex, WebRequest.SCOPE_REQUEST);
		}
		
		String[] exceptionList = ExceptionUtils.getStackFrames(ex);
		
		ErrorMessageModel model =
			new ErrorMessageModel()
				.status(status.value())
				.code(status.name())
				.message(ex.getClass().getCanonicalName())
				.status(status.value())
				.stacktrace(Arrays.asList(exceptionList));
		
		return new ResponseEntity<>(model, headers, status);
	}
    
}
