package com.nt.dto;

import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TotalFundResponseDto {
	
	 
	    private String fundId;
	    
	    
	    private BigDecimal amount;
	    

}
