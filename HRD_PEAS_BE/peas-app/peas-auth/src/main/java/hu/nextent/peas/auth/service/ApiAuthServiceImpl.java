package hu.nextent.peas.auth.service;

import java.util.Map;
import java.util.Optional;
import java.util.Properties;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.NotImplementedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.ConversionService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import hu.nextent.peas.model.AuthenticationRequestModel;
import hu.nextent.peas.model.ModuleListModel;
import hu.nextent.peas.model.ModuleModel;
import hu.nextent.peas.model.UserModel;
import hu.nextent.peas.module.ModuleSigleton;
import hu.nextent.peas.security.constant.SecurityConstants;
import hu.nextent.peas.security.services.AuthService;
import lombok.val;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@Transactional
public class ApiAuthServiceImpl
{
	@Autowired
	protected AuthService authService;
	
	@Autowired
	private ConversionService conversionService;

	public ResponseEntity<String> login(AuthenticationRequestModel body) {
		AuthenticationRequestModel.ModeEnum lmode = Optional.ofNullable(body.getMode()).orElse(AuthenticationRequestModel.ModeEnum.WEB);
		body.setMode(lmode);
		val jwtToken = authService.login(body); 
        return ResponseEntity
        			.status(HttpStatus.OK)
        			.header(SecurityConstants.TOKEN_HEADER_X_AUTHORIZATION, SecurityConstants.TOKEN_PREFIX + jwtToken)
        			.header(SecurityConstants.TOKEN_HEADER_X_AUTH_TOKEN, jwtToken)
        			.body(jwtToken);
	}
	
	
	public ResponseEntity<String> virtualUserLogin(String virtualUserToken) {
		try {
			val jwtToken = authService.virtualUserLogin(virtualUserToken);
			return ResponseEntity
        			.status(HttpStatus.OK)
        			// TODO Itt beszélni kell Csabival, hogy melyik menjen vissza
        			.header(SecurityConstants.TOKEN_HEADER_AUTHORIZATION, SecurityConstants.TOKEN_PREFIX + jwtToken)
        			.header(SecurityConstants.TOKEN_HEADER_X_AUTHORIZATION, SecurityConstants.TOKEN_PREFIX + jwtToken)
        			.header(SecurityConstants.TOKEN_HEADER_X_AUTH_TOKEN, jwtToken)
        			.body(jwtToken);
		} catch (NotImplementedException e) {
			return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).build();
		}
	}

	public ResponseEntity<Void> logout() {
		authService.logout();
		return ResponseEntity.status(HttpStatus.NO_CONTENT).header(HttpHeaders.AUTHORIZATION, "").build();
	}

	public ResponseEntity<UserModel> currentUserModel() {
		val token = authService.currentTokenCacheInfo();
		if (!token.isPresent() || token.get().getUser() == null) {
			return ResponseEntity.noContent().build();
		}
		return ResponseEntity.ok(conversionService.convert(token.get().getUser(), UserModel.class));
	}
	
	
    public ResponseEntity<Void> healthly(HttpServletRequest request) {
        return ResponseEntity.ok().build();
    }
    
    
    public ResponseEntity<ModuleListModel> getAllModule(HttpServletRequest request) {
    	Map<String,Properties> moduleMap = ModuleSigleton.getInstance().getModules();
    	ModuleListModel listModel = new ModuleListModel();
    	moduleMap.forEach((k,v) -> listModel.add(new ModuleModel().name(k).info(v)) );
        return ResponseEntity.ok(listModel);
    }

	
}
