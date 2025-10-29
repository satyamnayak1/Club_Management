package com.nt.service;

import com.nt.dto.EventCreateDto;
import com.nt.dto.EventRegistrionDto;
import com.nt.dto.EventResponseDto;

public interface IEventMgmtService {
	
	public EventResponseDto addEvent(EventCreateDto eventDto);

	String registerEvent(EventRegistrionDto registrationDto);

}
