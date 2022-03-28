package hu.nextent.peas.security.services;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.NotImplementedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import hu.nextent.peas.jpa.dao.TokenCacheRespository;
import hu.nextent.peas.jpa.entity.TokenCache;
import hu.nextent.peas.jpa.entity.User;
import hu.nextent.peas.jpa.utils.LoggedPlacholder;
import hu.nextent.peas.model.AuthenticationRequestModel;
import hu.nextent.peas.security.constant.SecurityConstants;
import hu.nextent.peas.security.model.MockedUserDetails;
import hu.nextent.peas.security.model.UserNameHolder;
import hu.nextent.peas.security.util.QueryClientAddress;
import lombok.val;
import lombok.extern.slf4j.Slf4j;

@Service
@Transactional
@Slf4j
public class AuthServiceImpl implements AuthService {
	
	@Autowired
	private SecurityUserService securityUserService;
	
	@Autowired
	private AuthenticationManager authenticationManager;
	
	@Autowired
	private TokenCacheRespository tokenCacheRespository;
	
	@Autowired
	private TokenService tokenService;
	
	@Autowired
	private SecurityConfigService securityConfigService;
	
	@Autowired 
	private ObjectMapper objectMapper;
	
	@Autowired(required = false)
	Optional<HttpServletRequest> optRequest;
	
	@Override
	public String login(AuthenticationRequestModel body) {
		val company = body.getCompany();
		val userName = body.getUsername();
		val password = body.getPassword();
		
		val fixedCompany = Optional.ofNullable(company).orElse(securityConfigService.defaultCompany());
		
		String useNameHolderStr = null;
		
		try {
			useNameHolderStr = objectMapper.writeValueAsString(UserNameHolder.builder().userName(userName).company(fixedCompany).build());
		} catch (JsonProcessingException e) {
			throw new RuntimeException(e);
		}
		
		val userDetail = securityUserService.loadUserByUsername(useNameHolderStr);

		UsernamePasswordAuthenticationToken authentication = 
        		new UsernamePasswordAuthenticationToken(
        				userDetail, 
        				password, 
        				userDetail.getAuthorities()
        				);
        // Do Login
		authenticationManager.authenticate(authentication);
		TokenCache tokenCache = createTokenCache(((MockedUserDetails)userDetail).getUser(), getClientIpAddress());
		val token = tokenService.createToken(tokenCache);
		
		return token;
	}
	
	
	private TokenCache createTokenCache(User user, String remoteAddress) {
		log.debug("createWebTokenCache " + user);
		
		OffsetDateTime now = OffsetDateTime.now(ZoneOffset.UTC);
		OffsetDateTime exp = now.plusMinutes(securityConfigService.expireMinute());
		
		val tokenCacheBuilder = 
				TokenCache.builder()
				.user(user)
				.iss(SecurityConstants.CLAIM_ISSUER_SOURCE)
				.exp(exp)
				.nbf(now)
				.iat(now)
				.sub(user.getUserName());
		
		if (securityConfigService.remoteAddressCheck()) {
			tokenCacheBuilder.remoteAddress(remoteAddress);
		}
		
		TransactionSynchronizationManager.isActualTransactionActive();
		val tokenCache = tokenCacheBuilder.build();
		tokenCacheRespository.saveAndFlush(tokenCache);
		return tokenCache;
	}
	

	@Override
	public void logout() {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		TokenCache tokenCache = (TokenCache)auth.getPrincipal();
		tokenCacheRespository.delete(tokenCache);
		SecurityContextHolder.clearContext();
		LoggedPlacholder.logout();
	}

	@Override
	public Optional<TokenCache> currentTokenCacheInfo() {
		// TODO Megoldani másképpen, hogy ne függjön a security modultól
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (auth == null  || auth.getPrincipal() == null) {
			return Optional.empty();
		}
		return Optional.ofNullable((TokenCache)auth.getPrincipal());
	}

	private String getClientIpAddress() {
		return QueryClientAddress.getClientIpAddress(optRequest.get());
	}
	
	@Override	
	public String virtualUserLogin(String  userToken) {
		throw new NotImplementedException("NotImplemented: virtualUserLogin");
	}

	@Override
	public User currentUser() {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (auth == null  || auth.getPrincipal() == null) {
			return null;
		}
		val tokenCache = (TokenCache)auth.getPrincipal();
		return tokenCache.getUser();
	}
	
	

}
