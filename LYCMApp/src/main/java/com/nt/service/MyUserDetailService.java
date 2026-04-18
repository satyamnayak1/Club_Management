package com.nt.service;



import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.nt.entity.User;
import com.nt.entity.UserPrinciple;
import com.nt.repository.IUserRepository;

@Service
public class MyUserDetailService implements UserDetailsService{
	
	@Autowired
	private IUserRepository userRepo;

	@Override
	public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
		
		User user = userRepo.findByEmail(userName)
                .orElseThrow(() -> new UsernameNotFoundException(
                        "User not found with username: " + userName));
		
		return new UserPrinciple(user);
	}


}
