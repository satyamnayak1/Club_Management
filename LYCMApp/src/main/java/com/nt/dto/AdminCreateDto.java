package com.nt.dto;

import io.micrometer.common.lang.NonNull;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AdminCreateDto {
	
	@NotBlank(message = "Name can not be blank")
	@Pattern(regexp = "^[A-Z]\\w{6,}\\d$",message = "User name start with capital letter and end with no")
    private String userName;
	
	@NotBlank(message = "Name can not be blank")
	@Pattern(regexp = "^[A-Z][a-zA-Z]*[a-z]\\\\s[A-Z][a-zA-Z]*[a-z]$")
	private String name;
	
	@NotBlank(message = "Name can not be blank")
	@Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=]).{8,}$")
	private String password;
	
	
	@NonNull
	@Pattern(regexp = "^\\d{10}$")
	private Long mobileNo;
	
	@NotBlank(message = "Name can not be blank")
	@Email
	private String email;

}
