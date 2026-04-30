package com.nt.service;

import java.util.List;
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

import com.nt.entity.User;
import jakarta.servlet.http.HttpServletRequest;


public interface IUserMgmtService {
	
	public UserResponseDto register(UserRegisterDto registerDto);

    List<User> getAll();
}
