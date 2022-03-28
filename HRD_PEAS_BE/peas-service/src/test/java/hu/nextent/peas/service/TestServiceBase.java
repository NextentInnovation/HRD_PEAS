package hu.nextent.peas.service;

import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;

import hu.nextent.peas.TestConstant;
import hu.nextent.peas.jpa.dao.CompanyRepository;
import hu.nextent.peas.jpa.dao.UserRepository;
import hu.nextent.peas.jpa.entity.User;
import hu.nextent.peas.jpa.utils.LoggedPlacholder;
import hu.nextent.peas.security.services.AuthService;
import lombok.Getter;
import lombok.Setter;
import lombok.val;

@Getter
@Setter
public abstract class TestServiceBase {

	private User selectedUser;

	// Mocked AuthService
	@Autowired
    protected AuthService authService;
	
	@Autowired
	protected CompanyRepository companyRepository;
	
	@Autowired
	protected UserRepository userRepository;
	
	protected void givenUser(String username) {
		val userOpt = userRepository.findByUserNameAndCompany_Name(username, TestConstant.DEFAULT_COMPANY);
		selectedUser = userOpt.get();
	}
	
	protected void mockAuthService() {
		Mockito.when(authService.currentUser()).thenReturn(selectedUser);
		LoggedPlacholder.login(selectedUser.getCompany().getId(), selectedUser.getUserName());
	}
}
