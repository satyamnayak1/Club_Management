package com.nt.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.nt.security.JwtFilter;
import com.nt.service.MyUserDetailService;

import lombok.RequiredArgsConstructor;
@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {
	
	@Autowired
	private JwtFilter jwtFilter;

	@Autowired
	private MyUserDetailService myUserDetailService;
	
	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{
		
		http.csrf(csrf->csrf.disable())
		.authorizeHttpRequests(auth-> {
			auth.requestMatchers("/login", "/refresh","guest").permitAll()
					.anyRequest().authenticated();
		})//.httpBasic(Customizer.withDefaults())
		.addFilterBefore(jwtFilter,UsernamePasswordAuthenticationFilter.class)
		.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
	
		return http.build();
		
	}
	
	
	@Bean
	public AuthenticationProvider authenticationProvider(MyUserDetailService userDetailService,PasswordEncoder encoder) {
		
		DaoAuthenticationProvider provider =new DaoAuthenticationProvider();
		provider.setUserDetailsService(userDetailService);
		provider.setPasswordEncoder(encoder);
		
		return provider;
		
	}
	
	@Bean
	public AuthenticationManager authManager(AuthenticationConfiguration config) throws Exception {	
		return config.getAuthenticationManager();	
	}
	
	
	@Bean
	public PasswordEncoder passwordEncoder() {
	    return new BCryptPasswordEncoder();
	}
	
	

}
