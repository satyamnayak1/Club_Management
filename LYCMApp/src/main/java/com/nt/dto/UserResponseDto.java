package com.nt.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserResponseDto {
	
	private String userName;
	
	private String email;
	
    private String name;
    
    private String mobileNo;
    
    private String dob;
	

}
