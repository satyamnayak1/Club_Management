package com.nt.security;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.nt.service.MyUserDetailService;

import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtFilter extends OncePerRequestFilter {
	@Autowired
	private JwtService jwtService;
	@Autowired
	private MyUserDetailService myUserDetailService;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		String authHader=request.getHeader("Authorization");
		
		String token=null;
		String userName=null;
		
		if(authHader!=null && authHader.startsWith("Bearer ")) {
			token=authHader.substring(7);
			userName=jwtService.extractUserName(token);
		}
		
		if(userName != null && SecurityContextHolder.getContext().getAuthentication()== null ) {
			Claims claims = jwtService.extractAllClaims(token);
            List<String> roles = (List<String>) claims.get("role");

            if (roles.contains("GUEST")) {
                // ✅ Create Guest authentication without hitting DB
            	
            	
            	System.out.println("Guest JWT roles: " + roles);
            	System.out.println("UserName from token: " + userName);

                UserDetails guestUser =User.withUsername(userName)

                        .password("") // no password
                        .authorities(new SimpleGrantedAuthority("ROLE_GUEST"))
                        .build();

                UsernamePasswordAuthenticationToken authToken =
                        new UsernamePasswordAuthenticationToken(guestUser, null, guestUser.getAuthorities());

                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authToken);

            } 
		    else {
		        // Normal user → validate against DB
		        UserDetails userDetails = myUserDetailService.loadUserByUsername(userName);
		        if (jwtService.validateToken(token, userDetails)) {
		            UsernamePasswordAuthenticationToken authToken =
		                    new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
		            authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
		            SecurityContextHolder.getContext().setAuthentication(authToken);
		        }
			
			
			
		}
		}
		filterChain.doFilter(request, response);
	}
}
