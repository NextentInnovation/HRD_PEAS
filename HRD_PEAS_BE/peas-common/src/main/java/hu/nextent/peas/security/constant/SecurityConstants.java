package hu.nextent.peas.security.constant;

import java.util.Arrays;
import java.util.List;

import com.google.common.net.HttpHeaders;

public final class SecurityConstants {
	
	
	public static final String[] PUBLIC_URLS = new String[] {
			// Auth
         	"/healt_v1", 
     		"/login_v1", 
     		"/module_v1",
     		// Resource
     		"/label_v1",
     		"/csrf",
     		// Swagger
     		// TODO ki kell szedni produkciós módnál
        	"/swagger-ui.html", 
        	"/webjars/**", 
        	"/swagger-resources/**", 
        	"/api-docs",
        	"/api-docs/**",
        	"/v2/api-docs",
        	"/v2/api-docs/**"
	};


	
	
    public static final String TOKEN_HEADER_AUTHORIZATION = HttpHeaders.AUTHORIZATION;
    public static final String TOKEN_HEADER_X_AUTHORIZATION = "X-Authorization";
    public static final String TOKEN_HEADER_X_AUTH_TOKEN = "x-auth-token";
    
    public static final List<String> VALID_TOKEN_HEADERS = 
    		Arrays.asList(
    				TOKEN_HEADER_AUTHORIZATION, 
    				TOKEN_HEADER_X_AUTHORIZATION,
    				TOKEN_HEADER_X_AUTH_TOKEN
    				);
    
    
    public static final String TOKEN_PREFIX = "Bearer ";
    
    public final static String CLAIM_ISSUER_SOURCE = "Nextent";
    public final static String CLAIM_ROLES = "roles";
    public final static String CLAIM_REMOTE_ADDRESS = "remoteAddress";

    public static final String USER_EXPIRE_MINUTE = "secret.user_expire_minute";
    public static final String SECRET = "secret.secret";
    public static final String REMOTE_ADDRESS_CHECK = "secret.remote_address_check";
    public static final String DEFAULT_COMPANY = "secret.default_company";
    

}
