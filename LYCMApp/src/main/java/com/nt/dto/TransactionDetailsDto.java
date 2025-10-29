package com.nt.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import com.nt.enums.TransactionType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TransactionDetailsDto {
	
	private String name;
	  
	private BigDecimal amount;
	
	private TransactionType type;
	
	private String reason;
	
	private LocalDateTime createdAt;
	
}
