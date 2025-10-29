package com.nt.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDeleteDto {
	
	
	@NotBlank
	private String message;
	@NotBlank
    private String deletedMemberId;
	@NotBlank
    private String deletedMemberName;

}
