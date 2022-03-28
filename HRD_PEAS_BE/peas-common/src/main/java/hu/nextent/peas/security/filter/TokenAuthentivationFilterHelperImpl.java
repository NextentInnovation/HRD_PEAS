package hu.nextent.peas.security.filter;

import java.io.IOException;
import java.util.AbstractMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import hu.nextent.peas.jpa.dao.UserXRoleRepository;
import hu.nextent.peas.jpa.entity.Role;
import hu.nextent.peas.jpa.entity.RoleEnum;
import hu.nextent.peas.jpa.entity.TokenCache;
import hu.nextent.peas.jpa.entity.UserXRole;
import hu.nextent.peas.jpa.utils.LoggedPlacholder;
import hu.nextent.peas.security.constant.SecurityConstants;
import hu.nextent.peas.security.services.TokenService;
import lombok.val;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@Transactional
@Validated
public class TokenAuthentivationFilterHelperImpl implements TokenAuthentivationFilterHelper {
	
	@Autowired
    TokenService tokenService;
	
	@Autowired
	UserXRoleRepository userRoleRepository;
	
	static List<RequestMatcher> publicRequestMatchers = null;
	static {
    	publicRequestMatchers = 
    			Stream.of(SecurityConstants.PUBLIC_URLS)
    				.map(AntPathRequestMatcher::new)
    				.collect(Collectors.toList());
	}
    
    
	@Override
	public void filter(HttpServletRequest request) throws ServletException, IOException {
		log.debug("start TokenAuthentivationFilterHelperImpl");
		if (request == null) {
			return;
		}
		try {
			innerFilter(request);
		} catch (Exception e) {
			log.warn("unwanted exception", e);
		}
		log.debug("end TokenAuthentivationFilterHelperImpl");
	}

		
	public void innerFilter(HttpServletRequest request) throws ServletException, IOException {
    	log.debug("innerFilter");
    	    	
    	if (isPublicUrl(request)) {
        	return;
    	}
    	
    	if (!hasAuthenticationToken(request)) {
    		log.warn("Token cannot be null or empty");
            return;  
    	}
        
        String token = decodeTokenFromHeaderValue(request);
        // TODO Itt kell majd token lejártát ellenőrizni 
        // pl. assertNotExpired(jwt);
    	log.debug("Parse token");
        TokenCache tokenCache = tokenService.parseTokenCache(token);
        if (tokenCache != null) {
            log.debug("founded tokenCache: " + tokenCache.getId());
            // Contexus bejegyzés
            SecurityContextHolder.getContext().setAuthentication(buildAuthenticationFromJwt(request, tokenCache));
            LoggedPlacholder.login(getCompanyId(tokenCache), getUserName(tokenCache));   
        } else {
        	log.debug("Token User Not Founded");
        }
	}

    boolean isPublicUrl(HttpServletRequest request) {
    	boolean foundedMatch = publicRequestMatchers.stream().anyMatch(p -> p.matches(request));
    	if (foundedMatch) {
    		log.debug("Public Url");
    	}
        return foundedMatch;
    }
    
    private Map.Entry<String, String> getHeaderValue(HttpServletRequest request) {
    	for(String header: SecurityConstants.VALID_TOKEN_HEADERS) {
    		String headerValue = request.getHeader(header);
    		if (StringUtils.isNotBlank(headerValue)) {
    			return new AbstractMap.SimpleEntry<String, String>(header, headerValue);
    		}
    	}
    	return new AbstractMap.SimpleEntry<String, String>(SecurityConstants.TOKEN_HEADER_AUTHORIZATION, null);
    }
    
    boolean hasAuthenticationHeader(HttpServletRequest request) {
        if (StringUtils.isBlank(getHeaderValue(request).getValue())) {
        	log.debug("don't have header from: " + SecurityConstants.VALID_TOKEN_HEADERS);
            return false;
        }
        
        return true;
    }
    
    boolean hasAuthenticationToken(HttpServletRequest request) {
    	val headerValue = getHeaderValue(request);
    	if (StringUtils.isBlank(headerValue.getValue())) {
        	log.debug("don't have token from: " + SecurityConstants.VALID_TOKEN_HEADERS);
            return false;
    	}
    	
    	if (headerValue.getKey().equals(SecurityConstants.TOKEN_HEADER_X_AUTH_TOKEN)) {
    		return true;
    	}
    	
        if (!headerValue.getValue().startsWith(SecurityConstants.TOKEN_PREFIX)) {
        	log.debug("don't have token: " + SecurityConstants.VALID_TOKEN_HEADERS);
            return false;
        }
        
        String token = headerValue.getValue().substring(SecurityConstants.TOKEN_PREFIX.length()).trim();
        if (StringUtils.isBlank(token) || StringUtils.isEmpty(token)) {
        	log.debug("token is blank");
            return false;
        }
        
        return true;
    }
    
    String decodeTokenFromHeaderValue(HttpServletRequest request) {
    	val headerValue = getHeaderValue(request);
    	if (headerValue.getKey().equals(SecurityConstants.TOKEN_HEADER_X_AUTH_TOKEN)) {
        	log.debug("decodeTokenFromHeaderValue: {}", headerValue.getValue());
    		return headerValue.getValue();
    	} else if (headerValue.getValue() == null) {
        	log.debug("decodeTokenFromHeaderValue: null");
    		return null;
    	} else {
            String token = headerValue.getValue().substring(SecurityConstants.TOKEN_PREFIX.length()).trim();
        	log.debug("decodeTokenFromHeaderValue: {}", token);
        	return token;
    	}
    }
    
    String getUserName(TokenCache tokenCache) {
    	return tokenCache.getUser().getUserName();
    }
    
    Long getCompanyId(TokenCache tokenCache) {
    	return tokenCache.getUser().getCompany().getId();
    }

    Authentication buildAuthenticationFromJwt(HttpServletRequest request, TokenCache tokenCache) {
    	log.debug("buildAuthenticationFromJwt");
    	List<GrantedAuthority> authorities = null;
		authorities = userRoleRepository
				.findAllByUser(tokenCache.getUser())
				.stream()
					.map(UserXRole::getRole)
					.map(Role::getName)
					.map(RoleEnum::name)
					.map(String::toLowerCase)
					.distinct()
					.map(SimpleGrantedAuthority::new)
					.collect(Collectors.toList())
					;
        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(tokenCache, null, authorities);
        authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        return authentication;
    }
    

	
}
