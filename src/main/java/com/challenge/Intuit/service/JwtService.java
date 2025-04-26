package com.challenge.Intuit.service;

import com.challenge.Intuit.entity.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.Map;

@Service
public class JwtService {
	@Value("${application.security.jwt.secret-key}")
	private String secretK;
	@Value("${application.security.jwt.expiration}")
	private long expiration;
	@Value("${application.security.jwt.refresh-token.expiration}")
	private long refreshExpiration;

	public String generatedToken(final User user) {
		return this.buildToken(user, this.expiration);
	}

	public String generatedRefreshToken(final User user) {
		return this.buildToken(user, this.refreshExpiration);
	}

	private String buildToken(final User user, final long expiration) {
		return Jwts.builder()
				   .id(user.getId().toString())
				   .claims(Map.of("rol", user.getRol()))
				   .subject(user.getEmail())
				   .issuedAt(new Date(System.currentTimeMillis()))
				   .expiration(new Date(System.currentTimeMillis() + this.expiration))
				   .signWith(this.getSecretKey())
				   .compact();
	}

	private SecretKey getSecretKey() {
		byte[] keyByte = Decoders.BASE64.decode(this.secretK);
		return Keys.hmacShaKeyFor(keyByte);
	}

	private Claims getPayload(String token) {
		return Jwts.parser()
				   .verifyWith(getSecretKey())
				   .build()
				   .parseSignedClaims(token)
				   .getPayload();
	}

	public String extractEmail(String token) {
		final Claims claimsToken = getPayload(token);
		return claimsToken.getSubject();
	}

	private Date extractExpiration(String token) {
		final Claims claimsToken = getPayload(token);
		return claimsToken.getExpiration();
	}

	public boolean isValidToken(String token, User user) {
		final String userEmail = this.extractEmail(token);
		return  (userEmail.equals(user.getEmail())) && !this.isTokenExpired(token);
	}

	private boolean isTokenExpired(String token) {
		return this.extractExpiration(token).before(new Date());
	}
}
