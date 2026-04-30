package com.nt.security;

import java.io.IOException;
import java.util.List;
import java.util.logging.Handler;

import io.jsonwebtoken.JwtException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.filter.OncePerRequestFilter;

import com.nt.service.JwtService;
import com.nt.service.MyUserDetailService;

import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.servlet.HandlerExceptionResolver;

@Component
public class JwtFilter extends OncePerRequestFilter {
	@Autowired
	private JwtService jwtService;

	@Autowired
	private MyUserDetailService myUserDetailService;

	@Autowired
	@Qualifier("handlerExceptionResolver")
	private HandlerExceptionResolver handlerResolver;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		System.out.println("Inside the dofilter method");
		String authHader=request.getHeader("Authorization");

//		String type= jwtService.extractClaims(authHader.substring(7)).get("typ").toString();
		
		String token=null;
		String userName=null;
		try {

			if (authHader != null && authHader.startsWith("Bearer ")) {

				if(!isAccessToken(authHader.substring(7))){
					filterChain.doFilter(request,response);
					return;
				}

				token = authHader.substring(7);
				userName = jwtService.extractUserName(token);
			}

			if (userName != null && SecurityContextHolder.getContext().getAuthentication() == null) {

				//validate with data base
				UserDetails userDetails = myUserDetailService.loadUserByUsername(userName);
				if (jwtService.validateToken(token, userDetails)) {
					//authentication object
					UsernamePasswordAuthenticationToken authToken =
							new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
					authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
					//store authentication object in security context holder
					SecurityContextHolder.getContext().setAuthentication(authToken);
				}

			}
			filterChain.doFilter(request, response);
		}
		catch (JwtException ex) {
			handlerResolver.resolveException(request, response, null, ex);
		}
		catch (Exception ex){
			//sending exception to the global exception handeler
			handlerResolver.resolveException(request,response,null,ex);
		}

	}

	private boolean isAccessToken(String token) {
		String type =jwtService.extractClaims(token).get("type",String.class);
		return "access".equalsIgnoreCase(type);
	}
}
