package com.nt.dto;

import io.micrometer.common.lang.NonNull;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AdminUpdateDto {
	
	
	@NotBlank
    private String userName;
	@NotBlank
	
	private String name;
	@NotBlank
	
	private String password;
	@NonNull
	
	private String mobileNo;
	@NotBlank
	
	private String email;

}
