package com.nt.service;


import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import javax.crypto.SecretKey;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import com.nt.entity.RefreshToken;
import com.nt.entity.User;
import com.nt.repository.RefreshTokenRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;


@Service
@Slf4j
public class JwtService {
	@Autowired
	private RefreshTokenRepository refreshTokenRepository;
	
	@Value("${jwt.secret}")
    private String secret;

	private SecretKey getKey(){

		return Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
	}


	public String generateAccessToken(User user, long seconds) {
		
		List<String> role=user.getRole().stream().map(roles->roles.getName().name()).toList();
		Instant now=Instant.now();
		
		return Jwts.builder()
				.subject(user.getEmail())
				.claims(Map.of(
						"userId",user.getUserId(),
						"roles",role,
						"type","access"
						))
				.issuedAt(Date.from(now))
				.expiration(Date.from(now.plusSeconds(seconds)))
				.signWith(getKey())
				.compact();

	}

	public String extractUserName(String token) {

		return extractClaims(token).getSubject();
	}

	public Claims extractClaims(String token){

		return Jwts.parser()
				.verifyWith(getKey())
				.build()
				.parseSignedClaims(token)
				.getPayload();
	}

	public boolean validateToken(String token, UserDetails userDetails) {
		
		final String username = extractUserName(token);
	    return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
	}
	
	// Check whether token is expired
    private Boolean isTokenExpired(String token) {

		return extractExpiration(token).isBefore(Instant.now());
    }

    private Instant extractExpiration(String token) {

		return extractClaims(token).getExpiration().toInstant();
    }

	public String generateRefreshToken(User user, long seconds) {
		//create refresh token object
		String jti=UUID.randomUUID().toString();
		Instant now=Instant.now();
		RefreshToken refreshToken=RefreshToken.builder()
				.jti(jti)
				.user(user)
				.createdAt(now)
				.expiresAt(now.plusSeconds(seconds))
				.build();
		
		//save the refresh token for to validate
		RefreshToken token=refreshTokenRepository.save(refreshToken);
		
		//creating refresh token			
		return Jwts.builder()
				.id(jti)
				.subject(token.getId().toString())
				.issuedAt(Date.from(now))
				.expiration(Date.from(now.plusSeconds(seconds)))
				.signWith(getKey())
				.claim("type","refresh")
				.compact();
	}

	public String extractJti(String refreshToken) {
		return extractRefreshTokenClaim(refreshToken).getId();
	}

	public Claims extractRefreshTokenClaim(String refrehToken){

		try {
			return Jwts.parser()
					.verifyWith(getKey())
					.build()
					.parseSignedClaims(refrehToken)
					.getPayload();
		} catch (io.jsonwebtoken.ExpiredJwtException e) {
			return e.getClaims(); // ✅ FIX
		}
	}

	public String extractUserId(String refreshToken) {
		return extractRefreshTokenClaim(refreshToken).getSubject();
	}

	public boolean isRefreshToken(String refreshToken) {
		return "refresh".equals(extractRefreshTokenClaim(refreshToken).get("typ"));
	}
}
