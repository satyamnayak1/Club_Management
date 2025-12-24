package com.nt.advice;

import java.util.List;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import com.nt.exception.ApiError;
import com.nt.exception.FundIsNotAvailableException;
import com.nt.exception.InsufficientFundException;
import com.nt.exception.InvalidAmountException;
import com.nt.exception.UserNameIsAlreadyAvailable;
import com.nt.exception.UserNotFoundException;

@RestControllerAdvice
public class LYCMAppErrorHandler {
	@ExceptionHandler(IllegalArgumentException.class)
	public ResponseEntity<ApiError> handelUserNotFoundException(IllegalArgumentException iae){
		
		ApiError error=new ApiError(null, null, null, null);
		
		return new ResponseEntity<ApiError>(error,HttpStatus.NOT_FOUND);
	}
	
	@ExceptionHandler(UserNameIsAlreadyAvailable.class)
	public ResponseEntity<ApiError> userAlreadyAvailable(UserNameIsAlreadyAvailable ex,WebRequest request){
		
		String path=request.getDescription(false).substring(3);
		System.out.println(path);
		
		ApiError error=new ApiError(HttpStatus.CONFLICT,ex.getMessage(),List.of("Username is already taken"),path);
		
		return new ResponseEntity<ApiError>(error,HttpStatus.CONFLICT);
		
	}
	
	@ExceptionHandler(UserNotFoundException.class)
	public ResponseEntity<ApiError> userIsNotPresent(UserNotFoundException ex,WebRequest request){
		
		String path=request.getDescription(false).substring(3);
		System.out.println(path);
		
		ApiError error=new ApiError(HttpStatus.CONFLICT,ex.getMessage(),List.of("User is not available"),path);
		
		return new ResponseEntity<ApiError>(error,HttpStatus.CONFLICT);
		
	}
	
	@ExceptionHandler(InvalidAmountException.class)
	public ResponseEntity<ApiError> invalinAmount(InvalidAmountException ex,WebRequest request){
		String path=request.getDescription(false).substring(3);
		System.out.println(path);
		
        ApiError error=new ApiError(HttpStatus.BAD_REQUEST,ex.getMessage(),List.of("Amount should be more than 0"),path);		
		return new ResponseEntity<ApiError>(error,HttpStatus.BAD_REQUEST);
		
	}
	
	@ExceptionHandler(InsufficientFundException.class)
	public ResponseEntity<ApiError> insufficientFund(InsufficientFundException ex,WebRequest req){
		
		String path=req.getDescription(false).substring(3);
		
		ApiError error=new ApiError(HttpStatus.BAD_REQUEST,ex.getMessage(),List.of("Balance is more than available"), path);
		
		return new ResponseEntity<ApiError>(error,HttpStatus.BAD_REQUEST);		
	}
	
	@ExceptionHandler(FundIsNotAvailableException.class)
	public ResponseEntity<ApiError> noFundAvailable(FundIsNotAvailableException ex,WebRequest request){	
		String path=request.getDescription(false).substring(3);
		ApiError error=new ApiError(HttpStatus.BAD_REQUEST,ex.getMessage(),List.of("Fund is not created"), path);
		
		return new ResponseEntity<ApiError>(error,HttpStatus.BAD_REQUEST);
	}

}
