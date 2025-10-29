package com.nt.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
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
@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {
	
	@Autowired
	private JwtFilter jwtFilter;
	
	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{
		
		http.csrf(csrf->csrf.disable())
		.formLogin(form -> form.disable())
		.authorizeHttpRequests(auth->
			auth
			.requestMatchers(
                    "/login.html",
                    "/register.html",
                    "/dashboard.html",
                    "/add-member.html",
                    "/update-member.html",
                    "/fund.html",
                    "/transaction.html",
                    "/show.html",
                    "/css/**",
                    "/js/**")
            .permitAll()
			.requestMatchers("/register","/login","/guest","/transaction").permitAll()
			.requestMatchers("/showfund").permitAll()
			.requestMatchers("/update").hasAnyRole("ADMIN","USER")
			.requestMatchers("/fund").hasAnyRole("ADMIN","USER","GUEST")
			.anyRequest().authenticated()
			
		)//.httpBasic(Customizer.withDefaults())
		.addFilterBefore(jwtFilter,UsernamePasswordAuthenticationFilter.class).sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
		
		return http.build();
		
	}
	
	
	@Bean
	public AuthenticationProvider authenticationProvider(MyUserDetailService userDetailService,PasswordEncoder encoder) {
		
		DaoAuthenticationProvider provider =new DaoAuthenticationProvider(userDetailService);
		
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
