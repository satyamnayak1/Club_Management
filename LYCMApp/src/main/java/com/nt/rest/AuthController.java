package com.nt.rest;

import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpServerErrorException;

import com.nt.dto.LoginDto;
import com.nt.entity.User;
import com.nt.entity.UserPrinciple;
import com.nt.service.AuthService;
import com.nt.service.CookieService;
import com.nt.service.JwtService;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequiredArgsConstructor
@Slf4j
public class AuthController {
	
	private final AuthenticationManager authManager;
	
	private final JwtService jwtService;
	
	private final CookieService cookieService;
	
	
	
	@PostMapping("/login")
	public ResponseEntity<String> login(@RequestBody LoginDto loginDto,HttpServletResponse response){
		//before generating token check user is authenticated or not
		Authentication authentication=authManager.authenticate(new UsernamePasswordAuthenticationToken(loginDto.getUserName(), loginDto.getPassword()));
		log.info("The authentication object is {}",authentication);
		
		
		String accessToken=null;
		String refreshToken=null;
		
		if(authentication.isAuthenticated()) {
			
			//extract the principal object
			UserPrinciple userPrinciple=(UserPrinciple)authentication.getPrincipal();
			User user=userPrinciple.getUser();
			//generate the acesstoken
		    accessToken=jwtService.generateAccessToken(user);
		    //generate refresh token
		    refreshToken=jwtService.generateRefreshToken(user);
		    
		    //store refresh token in cookie
		    
		    cookieService.attachRefersTokenToCookie(refreshToken, response);
		}
		return ResponseEntity.ok(accessToken);
	}
	


}
