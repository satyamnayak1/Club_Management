package com.nt.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserRegisterDto {
	
	private String email;

	private String name;
	
	private String password;
	
	private String mobileNo;
	
}
