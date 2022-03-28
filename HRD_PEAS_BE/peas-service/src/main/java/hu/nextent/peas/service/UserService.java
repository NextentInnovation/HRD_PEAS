package hu.nextent.peas.service;

import org.springframework.http.ResponseEntity;

import hu.nextent.peas.model.UserModel;
import hu.nextent.peas.model.UserPageModel;
import hu.nextent.peas.model.UserQueryParameterModel;

public interface UserService {

	public ResponseEntity<UserPageModel> getAllUser(UserQueryParameterModel body);
	
	public ResponseEntity<UserModel> getUser(Long userId);
}
