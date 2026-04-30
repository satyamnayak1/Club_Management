package com.nt.advice;

import com.nt.advice.Exception.ApiError;
import io.jsonwebtoken.JwtException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;


@RestControllerAdvice
public class LYCMAppErrorHandler {

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ApiError> handelAuthenticationException(BadCredentialsException exception){
        ApiError apiError=new ApiError(LocalDateTime.now(), HttpStatus.UNAUTHORIZED.value(), "Unauthorized","Invalid credential");

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(apiError);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiError> handelAuthenticationException(IllegalArgumentException exception){
        ApiError apiError=new ApiError(LocalDateTime.now(), HttpStatus.BAD_REQUEST.value(), "Bad Request", exception.getMessage());

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(apiError);
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ApiError> handelAuthenticationException(AuthenticationException authenticationException){
        ApiError apiError=new ApiError(LocalDateTime.now(), HttpStatus.UNAUTHORIZED.value(), "Unauthorized","Invalid credential");

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(apiError);
    }

    @ExceptionHandler(JwtException.class)
    public ResponseEntity<ApiError> handelJwtException(JwtException jwtException){
        ApiError apiError=new ApiError(LocalDateTime.now(),HttpStatus.UNAUTHORIZED.value(), "Jwt Exception","Invalid token or expire");
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(apiError);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiError> handelException(Exception exception){
        ApiError apiError=new ApiError(LocalDateTime.now(),HttpStatus.INTERNAL_SERVER_ERROR.value(),"Server Error","In server some problem");
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(apiError);
    }

}
