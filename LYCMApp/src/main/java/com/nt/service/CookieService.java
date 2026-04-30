package com.nt.service;

import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Service;
import jakarta.servlet.http.HttpServletResponse;
;

@Service
public class CookieService {
	
	
	public void attachRefersTokenToCookie(String refreshToken, long seconds, HttpServletResponse response) {
		
		//create cookie object
		ResponseCookie cookie=ResponseCookie.from("refreshToken", refreshToken)
				.maxAge(seconds)
				.httpOnly(true)
				.secure(false)
				.path("/")
				.build();

		response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());
	}

	public void clearRefreshToken(HttpServletResponse response) {
		ResponseCookie cookie=ResponseCookie.from("refreshToken","")
				.maxAge(0)
				.httpOnly(true)
				.secure(false)
				.maxAge(500000)
				.build();

		response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());
	}
}
