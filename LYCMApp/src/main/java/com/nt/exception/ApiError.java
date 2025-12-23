package com.nt.exception;

import java.time.Instant;
import java.util.Arrays;
import java.util.List;

import org.springframework.http.HttpStatus;
import lombok.Data;



@Data
public class ApiError {
	
	private int status;
    private String error;
    private String message;
    private List<String> errors;
    private String path;
    private Instant timestamp;

    public ApiError(HttpStatus status, String message,
                    List<String> errors, String path) {
        this.status = status.value();
        this.error = status.name();
        this.message = message;
        this.errors = errors;
        this.path = path;
        this.timestamp = Instant.now();
    }
    
   
}
