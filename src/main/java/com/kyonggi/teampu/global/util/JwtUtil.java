package com.kyonggi.teampu.global.util;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Component
public class JwtUtil {
	private final SecretKey secretKey;

	public JwtUtil(@Value("${spring.jwt.secret}") String secret) {
		secretKey = new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8),
			Jwts.SIG.HS256.key().build().getAlgorithm());
	}

	//claim 에서 email 정보 추출
	public String getLoginId(String token) {
		return Jwts.parser()
			.verifyWith(secretKey)
			.build()
			.parseSignedClaims(token)
			.getPayload()
			.get("loginId", String.class);
	}

	//claim 에서 토큰 종류 정보 추출
	public String getCategory(String token) {
		return Jwts.parser()
			.verifyWith(secretKey)
			.build()
			.parseSignedClaims(token)
			.getPayload()
			.get("category", String.class);
	}

	//claim 에서 토큰 만료 여부 추출
	public boolean isExpired(String token) {
		try {
			Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token);
			return false;
		} catch (ExpiredJwtException e) {
			return true;
		}
	}

	//JWT 토큰 생성 Claim 에는 토큰 종류와 email 만 담음
	public String createJwt(String category, String loginId, Long expiredMs) {
		return Jwts.builder()
			.claim("category", category)
			.claim("loginId", loginId)
			.issuedAt(new Date(System.currentTimeMillis()))
			.expiration(new Date(System.currentTimeMillis() + expiredMs))
			.signWith(secretKey)
			.compact();
	}

	// 토큰 검증 로직
	public boolean validateToken(String token) {
		try {
			Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token);
			return true;
		} catch (ExpiredJwtException e) {
			// 토큰이 만료된 경우
			return true;
		} catch (Exception e) {
			// 서명이 잘못됬거나 형식이 잘못됐을경우
			return false;
		}
	}
}
