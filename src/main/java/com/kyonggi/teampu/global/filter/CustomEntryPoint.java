package com.kyonggi.teampu.global.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kyonggi.teampu.global.exception.ErrorCode;
import com.kyonggi.teampu.global.response.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

import static com.kyonggi.teampu.global.exception.ErrorCode.MEMBER_NOT_AUTHENTICATED;

@Slf4j
@Component
public class CustomEntryPoint implements AuthenticationEntryPoint {
	@Override
	public void commence(
			HttpServletRequest request,
			HttpServletResponse response,
			AuthenticationException e
	) throws IOException {
		createAPIResponse(response, MEMBER_NOT_AUTHENTICATED);
	}

	private void createAPIResponse(HttpServletResponse response, ErrorCode errorCode) throws IOException {
		ApiResponse<Object> apiResponse = ApiResponse.exception(errorCode);
		response.setStatus(errorCode.getHttpStatus().value());
		response.setContentType(MediaType.APPLICATION_JSON_VALUE);
		response.setCharacterEncoding("UTF-8");
		ObjectMapper mapper = new ObjectMapper();
		mapper.writeValue(response.getWriter(), apiResponse);
	}
}
