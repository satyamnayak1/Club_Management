package com.nt.entity;

import java.util.Collection;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

public class UserPrinciple implements UserDetails {
	
	
   

	
    private User user;
	
	public UserPrinciple(User user) {
		this.user=user;
		
	}
	
	
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		
		return user.getRoles().stream()
	            .map(role -> new SimpleGrantedAuthority("ROLE_" + role.name()))
	            .collect(Collectors.toSet());
	}

	@Override
	public String getPassword() {
		// TODO Auto-generated method stub
		return user.getPassword();
	}

	@Override
	public String getUsername() {
		// TODO Auto-generated method stub
		return user.getUserName();
	}
	
	public String getUserId() {
		return user.getUserId();
	}
	
	@Override
    public boolean isAccountNonExpired() { return true; }

    @Override
    public boolean isAccountNonLocked() { return true; }

    @Override
    public boolean isCredentialsNonExpired() { return true; }

    @Override
    public boolean isEnabled() { return true; }

}
