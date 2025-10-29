package com.nt.dto;

import java.math.BigDecimal;

import com.nt.enums.TransactionType;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FundAddDto {
	@NotNull(message = "Amount cannot be null")
	//@Pattern(regexp = "^\\d{10}")
	@DecimalMin(value = "0.0", inclusive = false)  //ensures the value is strictly greater than 1 (not equal)
	private BigDecimal amount;  // Amount to add or withdraw
	
	
    private TransactionType type;   // ADD or WITHDRAW
    
    
    @NotBlank(message = "Reason cannot be empty")
    private String reason; 
	
	
	

}
