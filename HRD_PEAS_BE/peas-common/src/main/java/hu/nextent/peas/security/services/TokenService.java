package hu.nextent.peas.security.services;


import hu.nextent.peas.jpa.entity.TokenCache;

public interface TokenService {
	
	String createToken(TokenCache tokenCache);
	
	TokenCache parseTokenCache(String token);
	
	void checkTokenCache(TokenCache tokenCache);
	
	
}
