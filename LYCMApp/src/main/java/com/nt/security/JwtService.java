package com.nt.security;

import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.nt.enums.Role;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtService {
	
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

	public String generateToken(String userName,Set<Role> roles,String userId) {
		
		
		Map<String, Object> claims=new HashMap<>();
		
		claims.put("role", roles.stream().map(Role::name).toList());
		claims.put("userId", userId);
		
		return Jwts.builder()
				.claims()
				.add(claims)
				.subject(userName)
				.issuedAt(new Date(System.currentTimeMillis()))
				.expiration(new Date(System.currentTimeMillis()+1000 * 60 * 60 * 30))
				.and()
				.signWith(getKey())
				.compact()
				;
		
		
		
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

}
