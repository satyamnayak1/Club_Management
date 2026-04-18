package com.nt.runner;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.nt.entity.RoleEntity;

import com.nt.enums.Role;
import com.nt.repository.IRoleRepository;

@Component
public class MyRunner implements CommandLineRunner {
	
	@Autowired
	private IRoleRepository roleRepository;

	@Override
	public void run(String... args) throws Exception {
		//add the role in the database
		roleRepository.findByName(Role.ROLE_ADMIN).ifPresentOrElse(role->{System.out.println("role is already exist");},()->{
			
			roleRepository.save(RoleEntity.builder().name(Role.ROLE_ADMIN).build());
		});
		
        roleRepository.findByName(Role.ROLE_USER).ifPresentOrElse(role->{System.out.println("role is already exist");},()->{
			
			roleRepository.save(RoleEntity.builder().name(Role.ROLE_USER).build());
		});
		

	}

}
