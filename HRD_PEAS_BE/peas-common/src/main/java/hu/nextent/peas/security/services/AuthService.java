package hu.nextent.peas.security.services;

import java.util.Optional;

import hu.nextent.peas.jpa.entity.TokenCache;
import hu.nextent.peas.jpa.entity.User;
import hu.nextent.peas.model.AuthenticationRequestModel;

public interface AuthService {

	public String login(AuthenticationRequestModel body);
	
	public void logout();
	
	public Optional<TokenCache> currentTokenCacheInfo();
	
	public String virtualUserLogin(String  userToken);

	public User currentUser();

}
