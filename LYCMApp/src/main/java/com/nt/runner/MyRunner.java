package com.nt.runner;

import com.nt.dto.UserRegisterDto;
import com.nt.entity.User;
import com.nt.service.IUserMgmtService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.nt.entity.RoleEntity;

import com.nt.enums.Role;
import com.nt.repository.IRoleRepository;

@Component
@Slf4j
public class MyRunner implements CommandLineRunner {
	
	@Autowired
	private IRoleRepository roleRepository;

	@Autowired
	private IUserMgmtService userMgmtService;

	@Override
	public void run(String... args) throws Exception {
		//add the role in the database
		roleRepository.findByName(Role.ROLE_ADMIN).ifPresentOrElse(role->{System.out.println("role is already exist");},()->{
			
			roleRepository.save(RoleEntity.builder().name(Role.ROLE_ADMIN).build());
		});
		roleRepository.findByName(Role.ROLE_GUEST).ifPresentOrElse(role->{System.out.println("role is already exist");},()->{

			roleRepository.save(RoleEntity.builder().name(Role.ROLE_GUEST).build());
		});
		
        roleRepository.findByName(Role.ROLE_USER).ifPresentOrElse(role->{System.out.println("role is already exist");},()->{
			
			roleRepository.save(RoleEntity.builder().name(Role.ROLE_USER).build());
		});


		//store the user

		UserRegisterDto user= UserRegisterDto.builder().email("test1@gmail.com").password("12345").mobileNo("9999988888").name("user").build();
		UserRegisterDto admin=UserRegisterDto.builder().email("test2@gmail.com").password("12345").mobileNo("8888877777").name("admin").build();

		log.info("{}",userMgmtService.register(user));
		log.info("{}",userMgmtService.register(admin));

	}

}
