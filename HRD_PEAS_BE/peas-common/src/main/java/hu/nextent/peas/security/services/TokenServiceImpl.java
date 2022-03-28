package hu.nextent.peas.security.services;

import java.time.OffsetDateTime;
import java.util.Date;
import java.util.Optional;

import javax.validation.constraints.NotNull;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;

import com.google.common.primitives.Longs;

import hu.nextent.peas.jpa.dao.TokenCacheRespository;
import hu.nextent.peas.jpa.dao.UserXRoleRepository;
import hu.nextent.peas.jpa.entity.TokenCache;
import hu.nextent.peas.security.constant.SecurityConstants;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.val;

@Service
@Transactional
@Validated
public class TokenServiceImpl 
implements TokenService {
    
    @Autowired
    SecurityConfigService securityConfigService;
    
    @Autowired
    TokenCacheRespository tokenCacheRespository;
    
    @Autowired
    UserXRoleRepository userRoleRepository;

    public TokenServiceImpl() {
    }
	
    @Override
    public String createToken(@NotNull TokenCache tokenCache) {
    	val builder = Jwts.builder()
    			.setId(tokenCache.getId().toString())
    			.setSubject(tokenCache.getSub())
    			.setIssuer(tokenCache.getIss())
    			.setExpiration(convertTo(tokenCache.getExp()))
    			.setNotBefore(convertTo(tokenCache.getNbf()))
    			.setIssuedAt(convertTo(tokenCache.getIat()))
    			//.claim(SecurityConstants.CLAIM_ROLES, roles(tokenCache))
    			.signWith(SignatureAlgorithm.HS512, securityConfigService.secret())
    	;
    	
    	if (securityConfigService.remoteAddressCheck()) {
    		builder.claim(SecurityConstants.CLAIM_REMOTE_ADDRESS, tokenCache.getRemoteAddress());
    	}
    	return builder.compact();
    }

    private Date convertTo(OffsetDateTime odt) {
    	return odt == null ? null : Date.from(odt.toInstant());
    }
    
    public TokenCache parseTokenCache(@NotNull String token) {
    	Jws<Claims> claims = null;
    	try {
    		claims = getClaims(token);
    	} catch (ExpiredJwtException e) {
    		// Destroy old token
    		Optional<TokenCache> opTokenCache = tokenCacheRespository.findById(Longs.tryParse(e.getClaims().getId()));
    		if (opTokenCache.isPresent()) {
    			tokenCacheRespository.delete(opTokenCache.get());
    		}
			throw e;
    	}
    	String id = claims.getBody().getId();
    	if (StringUtils.isEmpty(id)) {
    		throw new UsernameNotFoundException("");
    	}
    	Optional<TokenCache> optTokenCache = tokenCacheRespository.findById(Longs.tryParse(id));
    	if (!optTokenCache.isPresent()) {
    		throw new UsernameNotFoundException(""); // TODO nevesíteni
    	}
    	val tokenCache = optTokenCache.get();
    	
    	if (securityConfigService.remoteAddressCheck() 
    			&& claims.getBody().containsKey(SecurityConstants.REMOTE_ADDRESS_CHECK)
    			&& !tokenCache.getRemoteAddress().equalsIgnoreCase(claims.getBody().get(SecurityConstants.CLAIM_REMOTE_ADDRESS, String.class))
    			) {
    		throw new UsernameNotFoundException("Remote Addres problem");
    	}
    	
    	checkTokenCache(tokenCache);
    	
    	return tokenCache;
    }

    
    private Jws<Claims> getClaims(String token) {
		return Jwts.parser().setSigningKey(securityConfigService.secret()).parseClaimsJws(token);
    }
   
    public void checkTokenCache(TokenCache tokenCache) {
    	if (tokenCache == null) {
    		return;
    	}
    	
		val user = tokenCache.getUser();
		if (user.getActive() == null
				|| !user.getActive()
				|| user.getCompany() == null
				|| user.getCompany().getActive() == null
				|| !user.getCompany().getActive()) {
			throw new UsernameNotFoundException("Not Active User");
		}
			
    	
    }

}