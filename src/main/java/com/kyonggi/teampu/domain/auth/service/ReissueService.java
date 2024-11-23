package com.kyonggi.teampu.domain.auth.service;

import com.kyonggi.teampu.domain.auth.domain.RefreshToken;
import com.kyonggi.teampu.domain.auth.repository.RefreshTokenRepository;
import com.kyonggi.teampu.global.util.JwtUtil;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Service;

import java.util.Arrays;

import static com.kyonggi.teampu.global.exception.ErrorCode.*;

@Service
@RequiredArgsConstructor
public class ReissueService {
	private final JwtUtil jwtUtil;
	private final RefreshTokenRepository refreshTokenRepository;

	public String createNewAccessToken(HttpServletRequest request, HttpServletResponse response) {
		String refreshToken = getRefreshTokenFromCookies(request);
		String loginId = validateAndGetLoginId(refreshToken);

		refreshTokenRepository.deleteById(refreshToken);
		String newAccessJwt = jwtUtil.createJwt("access", loginId, 30 * 60 * 1000L);
		String newRefreshJwt = jwtUtil.createJwt("refresh", "fakeLoginId", 24 * 60 * 60 * 1000L);
		RefreshToken newRefreshToken = new RefreshToken(newRefreshJwt, loginId);
		refreshTokenRepository.save(newRefreshToken);

		response.addHeader("Set-Cookie", createCookie("refresh", newRefreshJwt).toString());

		return newAccessJwt;
	}

	private String getRefreshTokenFromCookies(HttpServletRequest request) {
		return Arrays.stream(request.getCookies())
				.filter(cookie -> cookie.getName().equals("refresh"))
				.map(Cookie::getValue)
				.findAny()
				.orElseThrow(() -> new IllegalArgumentException(NOT_FOUND_REFRESH_TOKEN.getMessage()));
	}

	private String validateAndGetLoginId(String refreshToken) {
		if (!jwtUtil.validateToken(refreshToken)) {
			throw new IllegalStateException(INVALID_REFRESH_TOKEN.getMessage());
		}
		if (jwtUtil.isExpired(refreshToken)) {
			throw new IllegalStateException(EXPIRED_REFRESH_TOKEN.getMessage());
		}
		if (!jwtUtil.getCategory(refreshToken).equals("refresh")) {
			throw new IllegalStateException(INVALID_REFRESH_TOKEN.getMessage());
		}

		RefreshToken refreshTokenEntity = refreshTokenRepository.findById(refreshToken)
				.orElseThrow(() -> new IllegalStateException(INVALID_REFRESH_TOKEN.getMessage()));

		return refreshTokenEntity.getLoginId();
	}

	private ResponseCookie createCookie(String key, String value) {
		return ResponseCookie.from(key, value)
			.path("/") //쿠키 경로 설정(=도메인 내 모든경로)
			.sameSite("None") //sameSite 설정 (크롬에서 사용하려면 해당 설정이 필요함)
			.httpOnly(false) //JS에서 쿠키 접근 가능하도록함
			.secure(true) // HTTPS 연결에서만 쿠키 사용 sameSite 설정시 필요
			.maxAge(24 * 60 * 60)// 쿠키 유효기간 설정 (=refresh 토큰 만료주기)
			.build();
	}

}
