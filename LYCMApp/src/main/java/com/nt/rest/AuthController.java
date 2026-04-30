package com.nt.rest;

import com.nt.dto.LoginResponseDto;
import com.nt.dto.TokenResponseDto;
import com.nt.dto.UserResponseDto;
import com.nt.entity.*;
import com.nt.enums.Role;
import com.nt.repository.IRoleRepository;
import com.nt.repository.IUserRepository;
import com.nt.repository.RefreshTokenRepository;
import com.nt.service.IUserMgmtService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.nt.dto.LoginDto;
import com.nt.service.CookieService;
import com.nt.service.JwtService;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

import java.time.Duration;
import java.time.Instant;
import java.util.*;

@RestController
@Slf4j
public class AuthController {

    @Autowired
    private IRoleRepository roleRepository;
    @Autowired
    private IUserRepository userRepository;
	@Autowired
	private AuthenticationManager authManager;
	@Autowired
	private JwtService jwtService;
	@Autowired
	private CookieService cookieService;
	@Autowired
	private RefreshTokenRepository refreshTokenRepository;

	@Value("${jwt.refresh-token.ttl}")
	private Duration refreshTokenTtl;

	@Value("${jwt.access-token.ttl}")
	private Duration accessTokenTtl;
	
	
	
	@PostMapping("/login")
	public ResponseEntity<LoginResponseDto> login(@RequestBody LoginDto loginDto, HttpServletResponse response){
		//before generating token check user is authenticated or not
		Authentication authentication=authManager.authenticate(new UsernamePasswordAuthenticationToken(loginDto.getEmail(), loginDto.getPassword()));
			log.info("The authentication object is {}",authentication);

		String accessToken = null;
		String refreshToken = null;
		//extract the principal object
		UserPrinciple userPrinciple = (UserPrinciple) authentication.getPrincipal();
		User user = userPrinciple.getUser();

		//generate the acesstoken
		accessToken = jwtService.generateAccessToken(user, accessTokenTtl.getSeconds());
		//generate refresh token
		refreshToken = jwtService.generateRefreshToken(user, refreshTokenTtl.getSeconds());

		//store refresh token in cookie
		cookieService.attachRefersTokenToCookie(refreshToken, refreshTokenTtl.getSeconds(), response);

		return ResponseEntity.ok(new LoginResponseDto(accessToken,new UserResponseDto(user.getEmail(),user.getName(),user.getMobileNo(),user.getRole().stream().map(role->role.getName().name()).toList())));
	}

    @PostMapping("/guest")
    public ResponseEntity<LoginResponseDto> guestLogin(HttpServletRequest request,HttpServletResponse response){
        //create a user with random name;
        String guestemail="Guest_"+UUID.randomUUID().toString().substring(0,4);

        //get the role from the database
        RoleEntity guestrole =roleRepository.findByName(Role.ROLE_GUEST).orElse(null);
        User user=User.builder()
                .isEnabled(true)
                .isGuest(true)
                .email(guestemail)
                .role(Set.of(guestrole))
                .build();
        User regUser=userRepository.save(user);

        //generate access and refreshToken
        String accessToken=jwtService.generateAccessToken(regUser,accessTokenTtl.getSeconds());
        String refreshToken= jwtService.generateRefreshToken(regUser,10000);

        //store refresh token in cookie
        cookieService.attachRefersTokenToCookie(refreshToken,10000,response);

        return ResponseEntity.status(HttpStatus.CREATED).body(new LoginResponseDto(accessToken,new UserResponseDto(regUser.getEmail(),null,null,regUser.getRole().stream().map(role->role.getName().name()).toList())));

    }

	@PostMapping("/refresh")
	public ResponseEntity<TokenResponseDto> refresh(@RequestBody(required = false)RefreshTokenBody body, HttpServletRequest request, HttpServletResponse response) {
		System.out.println("inside refresh");
		String refreshToken = extractRefreshToken(body, request).orElseThrow(()->new IllegalArgumentException("Token is not available"));


		//get the jti
		String jti = jwtService.extractJti(refreshToken);
		String userId = jwtService.extractUserId(refreshToken);

		//extract the refresh token for forther check
		RefreshToken storeToken = refreshTokenRepository.findByJti(jti).orElseThrow(() -> new BadCredentialsException("Refresh Token is not found"));

		if (!storeToken.getUser().getUserId().toString().equals(userId)) {
			throw new BadCredentialsException("Invalid refresh token");
		}

		//check refresh token is revoked or not
		if (storeToken.isRevoked()) {
			throw new BadCredentialsException("Refresh token is revoked");
		}

		//check token is expired or not
		if (storeToken.getExpiresAt().isBefore(Instant.now())) {
			throw new BadCredentialsException("Refresh token is expired");
		}
		//now rotate the refresh token
		storeToken.setRevoked(true);
		String newJti = UUID.randomUUID().toString();
		storeToken.setReplacedByToken(newJti);
		refreshTokenRepository.save(storeToken);

		//generate new access and refresh token
		User user=storeToken.getUser();
		String accessToken=jwtService.generateAccessToken(user, accessTokenTtl.getSeconds());
		String newRefreshToken=jwtService.generateRefreshToken(user, refreshTokenTtl.getSeconds());

		//store refresh token in cookie
		cookieService.attachRefersTokenToCookie(newRefreshToken,refreshTokenTtl.getSeconds(),response);

		return ResponseEntity.ok(new TokenResponseDto(accessToken));

	}

	private Optional<String> extractRefreshToken(RefreshTokenBody body, HttpServletRequest request){
		Cookie [] cookies=request.getCookies();
		System.out.println("Cookie name and value is "+cookies[0].getName()+"---"+cookies[0].getValue());

            Optional<String> refreshToken= Arrays.stream(cookies)
					.filter(cookie -> "refreshToken".equals(cookie.getName()))
					.map(cookie -> cookie.getValue())
					.filter(val-> !val.isBlank())
					.findFirst();
            if(refreshToken.isPresent())
                return refreshToken;

		//this is too check if refresh token comes from boody
		if(body!=null && body.getRefreshToken()!=null && !body.getRefreshToken().isBlank()){
			return Optional.of(body.getRefreshToken());
		}

		//check refresh token comes with the header
		String authHeader=request.getHeader("Authorization");

		if(authHeader!=null && authHeader.startsWith("Bearer ")){
			String token=authHeader.substring(7);
			//check it is refresh token or not
			if(jwtService.isRefreshToken(token))
				return Optional.of(token);

        }
		return Optional.empty();

	}

	@PostMapping("/logout")
	public String logout(HttpServletRequest request,HttpServletResponse response){
		//extract refresh token from header
		extractRefreshToken(null,request).ifPresent(token->{
			//check refresh token or not
			if(jwtService.isRefreshToken(token)){
				String jti= jwtService.extractJti(token);
				refreshTokenRepository.findByJti(jti).ifPresent(refreshToken -> {
					refreshToken.setRevoked(true);
					refreshTokenRepository.save(refreshToken);
				});
			}

				}
		);

		//remove from the cookie
		cookieService.clearRefreshToken(response);
		return "logout Sucessful";
	}
	


}
