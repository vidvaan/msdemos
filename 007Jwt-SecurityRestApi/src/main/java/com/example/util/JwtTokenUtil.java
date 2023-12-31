package com.example.util;

import java.io.Serializable;
import java.time.Instant;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import javax.crypto.SecretKey;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtTokenUtil implements Serializable {

	private static final long serialVersionUID = -2550185165626007488L;

	public static final long JWT_TOKEN_VALIDITY = 60 * 60;

	private String secret = "snjdu438fkdj38fdmcv7dm3ckvhrsnjdu438fkdj38fdmcv7dm3ckvhr";

	// retrieve username from jwt token
	public String getUsernameFromToken(String token) {
		return getClaimFromToken(token, Claims::getSubject);
	}

	// retrieve expiration date from jwt token
	public Date getExpirationDateFromToken(String token) {
		return getClaimFromToken(token, Claims::getExpiration);
	}

	public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
		final Claims claims = getAllClaimsFromToken(token);
		return claimsResolver.apply(claims);
	}

	// for retrieveing any information from token we will need the secret key
	private Claims getAllClaimsFromToken(String token) {
		
		byte[] secretKeyBytes = secret.getBytes();
		SecretKey key = Keys.hmacShaKeyFor(secretKeyBytes);
		// return Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
		JwtParser parser = Jwts.parser().verifyWith(key).build();

		Claims claims = parser.parseSignedClaims(token).getPayload();

		return claims;
	}

	// check if the token has expired
	private Boolean isTokenExpired(String token) {
		final Date expiration = getExpirationDateFromToken(token);
		return expiration.before(new Date());
	}

	// generate token for user
	public String generateToken(UserDetails userDetails) {
		Map<String, Object> claims = new HashMap<>();
		return doGenerateToken(claims, userDetails.getUsername());
	}

	// while creating the token -
	// 1. Define claims of the token, like Issuer, Expiration, Subject, and the ID
	// 2. Sign the JWT using the HS512 algorithm and secret key.
	// 3. According to JWS Compact
	// Serialization(https://tools.ietf.org/html/draft-ietf-jose-json-web-signature-41#section-3.1)
	// compaction of the JWT to a URL-safe string
	private String doGenerateToken(Map<String, Object> claims, String subject) {
		
		 byte[] secretKeyBytes = secret.getBytes();
	        SecretKey secretKey = Keys.hmacShaKeyFor(secretKeyBytes);
	        Instant now = Instant.now();
	        Instant expiration = now.plusSeconds(JWT_TOKEN_VALIDITY); // expires in 1 hour
	        Date expDate = Date.from(expiration);
	        String token = Jwts.builder()
	                .subject(subject)
	                .expiration(expDate)
	                .issuedAt(Date.from(now))
	                .signWith(secretKey)
	                .compact();

		return token;
	}

	// validate token
	public Boolean validateToken(String token, UserDetails userDetails) {
		final String username = getUsernameFromToken(token);
		return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
	}

	public static void main(String[] args) {
		UserDetails userDetails = new UserDetails() {

			@Override
			public boolean isEnabled() {
				return true;
			}

			@Override
			public boolean isCredentialsNonExpired() {
				return true;
			}

			@Override
			public boolean isAccountNonLocked() {
				return true;
			}

			@Override
			public boolean isAccountNonExpired() {
				return true;
			}

			@Override
			public String getUsername() {
				return "balaji1218";
			}

			@Override
			public String getPassword() {
				return new BCryptPasswordEncoder().encode("balaji");
			}

			@Override
			public Collection<? extends GrantedAuthority> getAuthorities() {
				// TODO Auto-generated method stub
				return null;
			}
		};

		JwtTokenUtil jwtTokenUtil = new JwtTokenUtil();
		
		String jwttoken = jwtTokenUtil.generateToken(userDetails);
		System.out.println(jwttoken);
		
		
		
		// Token send
		
		// Get username form token
		
		String username = jwtTokenUtil.getUsernameFromToken(jwttoken);
		
		System.out.println(username);
		
		
		System.out.println(jwtTokenUtil.validateToken(jwttoken, userDetails));
		
	}
}