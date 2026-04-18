package com.nt.service;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Service;

import jakarta.servlet.http.HttpServletResponse;

@Service
public class CookieService {
	
	
	public void attachRefersTokenToCookie(String refreshToken,HttpServletResponse response) {
		
		//create cookie object
		ResponseCookie cookie=ResponseCookie.from("refreshToken", refreshToken)
				.maxAge(7000)
				.secure(true)
				.path("/")
				.build();
		
		response.addHeader(HttpHeaders.SET_COOKIE, refreshToken.toString());
		
	}

}
