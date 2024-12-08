package com.kyonggi.teampu.global.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kyonggi.teampu.domain.auth.domain.RefreshToken;
import com.kyonggi.teampu.domain.auth.repository.RefreshTokenRepository;
import com.kyonggi.teampu.global.exception.ErrorCode;
import com.kyonggi.teampu.global.response.ApiResponse;
import com.kyonggi.teampu.global.util.JwtUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.filter.GenericFilterBean;

import java.io.IOException;
import java.util.Arrays;
import java.util.Optional;

import static com.kyonggi.teampu.global.exception.ErrorCode.*;

@RequiredArgsConstructor
public class CustomLogoutFilter extends GenericFilterBean {
	private final JwtUtil jwtUtil;
	private final RefreshTokenRepository refreshTokenRepository;

	@Override
	public void doFilter(
			ServletRequest servletRequest,
			ServletResponse servletResponse,
			FilterChain filterChain
	) throws IOException, ServletException {
		doFilter((HttpServletRequest)servletRequest, (HttpServletResponse)servletResponse, filterChain);
	}

	private void doFilter(
			HttpServletRequest request,
			HttpServletResponse response,
			FilterChain filterChain
	) throws IOException, ServletException {
		String requestUri = request.getRequestURI();
		if (!requestUri.matches("^\\/api/logout$")) {
			filterChain.doFilter(request, response);
			return;
		}
		String requestMethod = request.getMethod();
		if (!requestMethod.equals("POST")) {
			filterChain.doFilter(request, response);
			return;
		}
		String refreshToken = getRefreshTokenFromCookies(request);
		if (!isTokenValid(refreshToken, response)) {
			return;
		}

		refreshTokenRepository.deleteById(refreshToken);
		Cookie cookie = new Cookie("refresh", null);
		cookie.setMaxAge(0);
		cookie.setPath("/");
		cookie.setHttpOnly(false);
		response.addCookie(cookie);

		createSuccessResponse(response);
	}

	private String getRefreshTokenFromCookies(HttpServletRequest request) {
		return Arrays.stream(request.getCookies())
				.filter(cookie -> cookie.getName().equals("refresh"))
				.map(Cookie::getValue)
				.findAny()
				.orElseThrow(() -> new IllegalArgumentException(NOT_FOUND_REFRESH_TOKEN.getMessage()));
	}

	private boolean isTokenValid(String refreshToken, HttpServletResponse response) throws IOException {
		if (!jwtUtil.validateToken(refreshToken)) {
			createExceptionResponse(response, INVALID_REFRESH_TOKEN);
			return false;
		}
		if (jwtUtil.isExpired(refreshToken)) {
			createExceptionResponse(response, EXPIRED_REFRESH_TOKEN);
			return false;
		}
		if (!jwtUtil.getCategory(refreshToken).equals("refresh")) {
			createExceptionResponse(response, INVALID_REFRESH_TOKEN);
			return false;
		}

		Optional<RefreshToken> refreshTokenEntity = refreshTokenRepository.findById(refreshToken);
		if (refreshTokenEntity.isEmpty()) {
			createExceptionResponse(response, INVALID_REFRESH_TOKEN);
			return false;
		}

		return true;
	}

	private void createExceptionResponse(HttpServletResponse response, ErrorCode errorCode) throws IOException {
		ApiResponse<Object> apiResponse = ApiResponse.exception(errorCode);
		response.setStatus(errorCode.getHttpStatus().value());
		response.setContentType(MediaType.APPLICATION_JSON_VALUE);
		response.setCharacterEncoding("UTF-8");
		ObjectMapper mapper = new ObjectMapper();
		mapper.writeValue(response.getWriter(), apiResponse);
	}

	private void createSuccessResponse(HttpServletResponse response) throws IOException {
		ApiResponse<Object> apiResponse = ApiResponse.ok();
		response.setStatus(HttpStatus.OK.value());
		response.setContentType(MediaType.APPLICATION_JSON_VALUE);
		response.setCharacterEncoding("UTF-8");
		ObjectMapper mapper = new ObjectMapper();
		mapper.writeValue(response.getWriter(), apiResponse);
	}
}
