package hu.nextent.peas.security.model;

import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import hu.nextent.peas.jpa.entity.User;
import lombok.Builder;
import lombok.Singular;
import lombok.Value;

/*
 * Mock-olt UserDetail az authentikáciü miatt
 * Ezt majd LDAP esetben ki kell vezetni
 */


@Value
@Builder
public class MockedUserDetails implements UserDetails {


	private static final long serialVersionUID = -5994867149475278569L;
	
	private String id;
	private String username;
	private String companyName;
	private String password;
	private boolean accountNonExpired;
	private boolean accountNonLocked;
	private boolean enabled;
	
	@Singular("grantedAuthority")
	private List<GrantedAuthority> grantedAuthorities;
	
	private User user;
	
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return grantedAuthorities;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}



}
