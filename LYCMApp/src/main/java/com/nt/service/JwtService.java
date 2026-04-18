package com.nt.service;

import java.security.NoSuchAlgorithmException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.nt.entity.RefreshToken;
import com.nt.entity.RoleEntity;
import com.nt.entity.User;
import com.nt.enums.Role;
import com.nt.repository.RefreshTokenRepository;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;

@Service

public class JwtService {
	@Autowired
	private RefreshTokenRepository refreshTokenRepository;
	
	
    private String secrateKey;
	
	public JwtService() {
		try {
			KeyGenerator keyGen=KeyGenerator.getInstance("HmacSHA256");
			SecretKey sk=keyGen.generateKey();
			 secrateKey=Base64.getEncoder().encodeToString(sk.getEncoded());
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public String generateAccessToken(User user) {
		
		List<String> role=user.getRole().stream().map(roles->roles.getName().name()).toList();
		Instant now=Instant.now();
		
		return Jwts.builder()
				.subject(user.getEmail())
				.claims(Map.of(
						"userId",user.getUserId(),
						"roles",role,
						"typ","access"
						))
				.issuedAt(Date.from(now))
				.expiration(Date.from(now.plus(5,ChronoUnit.MINUTES)))
				.signWith(getKey())
				.compact();

	}
	private SecretKey getKey() {
		byte [] keyBytes=Decoders.BASE64.decode(secrateKey);
		
		return Keys.hmacShaKeyFor(keyBytes);
	}

	public String extractUserName(String token) {
		// TODO Auto-generated method stub
		return extractClaim(token, Claims::getSubject);
	}
	
	// Generic method to extract any claim
    private <T> T extractClaim(String token, java.util.function.Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    public Claims extractAllClaims(String token) {
        return Jwts.parser()
                .verifyWith(getKey())
                .build()// validate signature
                .parseSignedClaims(token)
                .getPayload();
    }


	public boolean validateToken(String token, UserDetails userDetails) {
		
		final String username = extractUserName(token);
	    return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
	}
	
	// Check whether token is expired
    private Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

	public String generateRefreshToken(User user) {
		//create refresh token object
		String jti=UUID.randomUUID().toString();
		Instant now=Instant.now();
		RefreshToken refreshToken=RefreshToken.builder()
				.jti(jti)
				.user(user)
				.createdAt(now)
				.expiresAt(now.plus(5, ChronoUnit.HOURS))
				.build();
		
		//save the refresh token for to validate
		RefreshToken token=refreshTokenRepository.save(refreshToken);
		
		//creating refresh token			
		return Jwts.builder()
				.id(jti)
				.subject(token.getId().toString())
				.issuedAt(Date.from(now))
				.expiration(Date.from(now.plus(5,ChronoUnit.HOURS)))
				.signWith(getKey())
				.claim("typ","refresh")
				.compact();
	}

}
