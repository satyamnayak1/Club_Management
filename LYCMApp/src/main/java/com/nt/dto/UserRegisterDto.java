package com.nt.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserRegisterDto {
	@NotBlank
	@Pattern(regexp = "^[A-Z]\\w{6,}\\d$",message="User name should be unique")
	private String userName;
	
	@NotBlank
	@Pattern(regexp = "^[A-Z][a-zA-Z]*[a-z]\\s[A-Z][a-zA-Z]*[a-z]$")
	private String name;
	
	@NotBlank
	@Pattern(regexp ="^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=]).{8,}$" )
	private String password;
	
	@NotBlank
	@Pattern(regexp = "^\\d{10}$")
	private String mobileNo;
	
	@Email
	@NotBlank
	private String email;
	
	private String secretId;
	
	

}
