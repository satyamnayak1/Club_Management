package com.nt.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginDto {
	
	@NotBlank
	@Pattern(regexp = "^[A-Z]\\w{6,}\\d$",message="User name should be unique")
	private String userName;
	
	@NotBlank
	@Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=]).{8,}$",message="Password Contain")
	private String password;
	
	

}
