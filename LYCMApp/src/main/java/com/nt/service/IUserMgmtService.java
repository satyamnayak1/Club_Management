package com.nt.service;

import java.util.Map;
import com.nt.dto.AdminCreateDto;
import com.nt.dto.AdminUpdateDto;
import com.nt.dto.FundAddDto;
import com.nt.dto.FundResponseDto;
import com.nt.dto.LoginDto;
import com.nt.dto.PageResponseDto;
import com.nt.dto.TransactionDetailsDto;
import com.nt.dto.UserDeleteDto;
import com.nt.dto.UserRegisterDto;
import com.nt.dto.UserResponseDto;
import com.nt.dto.UserUpdateDto;

import jakarta.servlet.http.HttpServletRequest;


public interface IUserMgmtService {
	
	public UserResponseDto registerUser(UserRegisterDto registerDto);
	
	//public UserResponseDto addMember(AdminCreateDto adminDto);
	
	public PageResponseDto<UserResponseDto> findAllUser(int no);
	
	//public UserResponseDto updateMember(AdminUpdateDto updateDto);

	public UserDeleteDto deleteMember(String userId);
	
	public String verifyUser(LoginDto loginDto);

	FundResponseDto performTransaction(FundAddDto dto);

	UserResponseDto updateMember(AdminUpdateDto updateDto, String userId);

	UserResponseDto updateUser(UserUpdateDto updateDto);

	//UserResponseDto addMember(AdminCreateDto adminDto, String userId);

	UserResponseDto addMember(AdminCreateDto adminDto);
	
	public Map<String,Object> loginAsGuest(HttpServletRequest request);

	public UserResponseDto getProfile(String name);

	public FundResponseDto getTheFundDetail();

	PageResponseDto<TransactionDetailsDto> getAllTransactions(int page, int size);

	

}
