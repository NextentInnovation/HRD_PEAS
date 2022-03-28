package hu.nextent.peas.security.services;

import java.io.IOException;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.ObjectMapper;

import hu.nextent.peas.jpa.dao.UserRepository;
import hu.nextent.peas.jpa.dao.UserXRoleRepository;
import hu.nextent.peas.jpa.entity.Role;
import hu.nextent.peas.jpa.entity.RoleEnum;
import hu.nextent.peas.jpa.entity.User;
import hu.nextent.peas.jpa.entity.UserXRole;
import hu.nextent.peas.security.model.MockedUserDetails;
import hu.nextent.peas.security.model.UserNameHolder;
import lombok.val;

@Service
@Transactional
public class SecurityUserServiceImpl implements SecurityUserService {
	
	@Autowired
	UserRepository userRepository;

	@Autowired
	UserXRoleRepository userRoleRepository;
	
	@Autowired
	PasswordEncoder passwordEncoder;
	
	@Autowired
	ObjectMapper objectMapper;

    @Override
    public UserDetails loadUserByUsername(String username) {
    	if (username == null) {
    		return null;
    	}
    	
    	val jpaUser = getJpaUser(username);
    	
    	if (jpaUser == null) {
    		return null;
    	}
    	
    	
    	
    	return buildMockedUserDetails(jpaUser);
    }


    User getJpaUser(String username) {
    	UserNameHolder parsedUserName = readUserNameHolder(username);
    	
    	if (parsedUserName == null) {
    		return null;
    	}
    	
    	Optional<User> jpaUser = 
    			userRepository.findByUserNameAndCompany_Name(
    					parsedUserName.getUserName(), parsedUserName.getCompany()
    			);
    	
    	if (jpaUser.isPresent()) {
    		User user = jpaUser.get();
	    	if (user.getActive() == null 
	    			|| !user.getActive()) {
	    		return null;
	    	}
	    	
	    	if (user.getCompany() == null
	    			|| user.getCompany().getActive() == null
	    			|| !user.getCompany().getActive()
	    			) {
	    		return null;
	    	}
    	}
    	
    	return jpaUser.orElse(null);
    }
    
    UserNameHolder readUserNameHolder(String userName) {
    	try {
			return objectMapper.readValue(userName, UserNameHolder.class);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
    }
    
    String writeUserNameHolder(String userName, String companyName) {
    	try {
    		return objectMapper.writeValueAsString(UserNameHolder.builder().userName(userName).company(companyName).build());
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
    }
    
    
	// Mockolt User
    MockedUserDetails buildMockedUserDetails(User jpaUser) {
    	return MockedUserDetails
				.builder()
				.id(jpaUser.getId().toString())
				.username(writeUserNameHolder(jpaUser.getUserName(), jpaUser.getCompany().getName()))
				.companyName(jpaUser.getCompany().getName())
				.password(jpaUser.getCalculatesPasswdHash())
				.accountNonExpired(true)
				.accountNonLocked(true)
				.enabled(true)
				.grantedAuthorities(
						userRoleRepository
							.findAllByUser(jpaUser)
							.stream()
							.map(UserXRole::getRole)
							.map(Role::getName)
							.map(RoleEnum::name)
							.map(String::toLowerCase)
							.distinct()
							.map(SimpleGrantedAuthority::new)
							.collect(Collectors.toList())
						)
				.user(jpaUser)
				.build();
    }
   
}
