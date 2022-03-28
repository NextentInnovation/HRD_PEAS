package hu.nextent.peas.security.filter;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;
import org.springframework.web.filter.OncePerRequestFilter;

import hu.nextent.peas.jpa.utils.LoggedPlacholder;

public class JWTTokenAuthenticationFilter extends OncePerRequestFilter {

	@Autowired
    private TokenAuthentivationFilterHelper tokenAuthentivationFilterHelper;
	
    public JWTTokenAuthenticationFilter() {
    	super();
    }

    @Override
    protected void doFilterInternal(
			HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException
    {
    	
    	if (tokenAuthentivationFilterHelper == null) {
        	SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);
    	}
    	
    	logger.debug("doFilterInternal");
    	
    	try {
    		tokenAuthentivationFilterHelper.filter(request);
    	} finally {
    		filterChain.doFilter(request, response);
    		SecurityContextHolder.getContext().setAuthentication(null);
    	    LoggedPlacholder.logout();  
		}
    	
    }

}