package com.nt.advice;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.nt.exception.ErrorDetails;

@RestControllerAdvice
public class LYCMAppErrorHandler {
	@ExceptionHandler(IllegalArgumentException.class)
	public ResponseEntity<ErrorDetails> handelUserNotFoundException(IllegalArgumentException iae){
		ErrorDetails error=new ErrorDetails(LocalDateTime.now(),iae.getMessage(),"404_User already exist");
		
		return new ResponseEntity<ErrorDetails>(error,HttpStatus.NOT_FOUND);
	}

}
