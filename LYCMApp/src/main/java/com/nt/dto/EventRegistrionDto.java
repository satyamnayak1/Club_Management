package com.nt.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EventRegistrionDto {
	
	private String eventId;   // which event user is registering for
	
    private String name;      // person name (can be guest also)
    
    private String email;     // email for confirmation
    
    private String mobileNo; 

}
