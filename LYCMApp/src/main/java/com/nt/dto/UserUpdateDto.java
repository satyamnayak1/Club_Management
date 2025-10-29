package com.nt.dto;

import java.time.LocalDate;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserUpdateDto {
	
	
	
	private String userName;
	
	
	private String name;
	
	
	private String fatherName;
	
	
	private String password;
	
	
	private String mobileNo;
	
	
	private LocalDate dob;
	
	
	private String email;
	
	
	private String address;

}
