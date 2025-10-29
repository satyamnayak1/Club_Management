package com.nt.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import com.nt.dto.EventCreateDto;
import com.nt.dto.EventRegistrionDto;
import com.nt.dto.EventResponseDto;
import com.nt.entity.Event;
import com.nt.entity.EventRegistration;
import com.nt.entity.User;
import com.nt.entity.UserPrinciple;
import com.nt.mapper.IUserMapper;
import com.nt.repository.IEventRegistrationRepository;
import com.nt.repository.IEventRepository;
import com.nt.repository.IUserRepository;

public class EventImplService implements IEventMgmtService {
	@Autowired
	private IEventRepository eventRepo;
	
	@Autowired
	private IEventRegistrationRepository registrationRepo;
	
	@Autowired
	private IUserRepository userRepo;
	
	@Autowired
	private IUserMapper mapper;

	@Override
	public EventResponseDto addEvent(EventCreateDto eventDto) {
		
		// Get the Authentication object from the security context
	    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

	    // The principal is the UserPrinciple object you created
	    UserPrinciple userPrinciple = (UserPrinciple) authentication.getPrincipal();

	    // Get the userId directly from the principal
	    String userId = userPrinciple.getUserId();
	    
	    User user=userRepo.findById(userId).orElseThrow(()-> new IllegalArgumentException("Invalid User"));
	    
	    Event event=mapper.toEntity(eventDto);
		event.setUser(user);
		
		Event events=eventRepo.save(event);
	
		EventResponseDto eventResponse=mapper.toDto(events);
		eventResponse.setCreatedBy(events.getUser().getName());
		
		System.out.println(eventResponse);
		
		
		return eventResponse;
	}

	@Override
	public String registerEvent(EventRegistrionDto registrationDto) {
		// TODO Auto-generated method stub
		return null;
	}
	
//	@Override
//	public String registerEvent(EventRegistrionDto registrationDto) {
//		
//		// Get the Authentication object from the security context
//	    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//
//	    // The principal is the UserPrinciple object you created
//	    UserPrinciple userPrinciple = (UserPrinciple) authentication.getPrincipal();
//
//	    // Get the userId directly from the principal
//	    String userId = userPrinciple.getUserId();
//		
//		EventRegistration registration=new EventRegistration();
//		
//		registration.setEmail(registrationDto.getEmail());
//		registration.setName(registrationDto.getName());
//		registration.setMobileNo(registrationDto.getMobileNo());
//		
//		
//	}
	
	
	
}
