package com.nt.mapper;

import org.mapstruct.Mapper;

import com.nt.dto.AdminCreateDto;
import com.nt.dto.EventCreateDto;
import com.nt.dto.EventResponseDto;
import com.nt.dto.TransactionDto;
import com.nt.dto.UserRegisterDto;
import com.nt.dto.UserResponseDto;
import com.nt.entity.Event;
import com.nt.entity.FundTransaction;
import com.nt.entity.User;

@Mapper(componentModel = "spring")
public interface IUserMapper {
	
	User toEntity(UserRegisterDto userDto);
	
	User toEntity(AdminCreateDto adminDto);
	
	Event toEntity(EventCreateDto dto);
	
	UserResponseDto toDto(User user);
	
	
	
	EventResponseDto toDto(Event event);
	TransactionDto toDto(FundTransaction txn);
	

}
