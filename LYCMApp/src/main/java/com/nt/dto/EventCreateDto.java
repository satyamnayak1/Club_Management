package com.nt.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EventCreateDto {
	
	
	private String eventName;
	
	
	private LocalDate eventStartDate;
	
	private LocalDate eventEndDate;
	

	private LocalDateTime eventTime;
	
	
	private String description;
	

	private String location;
	
	
}
