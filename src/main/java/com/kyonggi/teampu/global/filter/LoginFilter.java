package com.kyonggi.teampu.global.filter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kyonggi.teampu.domain.auth.domain.RefreshToken;
import com.kyonggi.teampu.domain.auth.dto.LoginResponse;
import com.kyonggi.teampu.domain.auth.repository.RefreshTokenRepository;
import com.kyonggi.teampu.domain.member.domain.Member;
import com.kyonggi.teampu.domain.member.repository.MemberRepository;
import com.kyonggi.teampu.global.exception.ErrorCode;
import com.kyonggi.teampu.global.response.ApiResponse;
import com.kyonggi.teampu.global.util.JwtUtil;
import com.sun.jdi.request.InvalidRequestStateException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseCookie;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Map;

import static com.kyonggi.teampu.global.exception.ErrorCode.*;

public class LoginFilter extends UsernamePasswordAuthenticationFilter {
    private final AuthenticationManager authenticationManager;
    private final RefreshTokenRepository refreshTokenRepository;
    private final JwtUtil jwtUtil;
    private final MemberRepository memberRepository;

    public LoginFilter(
            AuthenticationManager authenticationManager,
            RefreshTokenRepository refreshTokenRepository,
            JwtUtil jwtUtil,
            MemberRepository memberRepository,
            String url
    ) {
        this.authenticationManager = authenticationManager;
        this.refreshTokenRepository = refreshTokenRepository;
        this.jwtUtil = jwtUtil;
        this.memberRepository = memberRepository;
        setFilterProcessesUrl(url);
    }

    //로그인 시도시 작동
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws
            AuthenticationException {
        //request 받은 json 값을 추출함
        Map<String, String> loginInfo = getLoginInfoFromJson(request);
        String loginId = loginInfo.get("loginId");
        String password = loginInfo.get("password");
        //해당 값이 없을시 예외처리
        if (loginId == null) {
            throw new InvalidRequestStateException("이메일을 입력해 주세요");
        }
        if (password == null) {
            throw new InvalidRequestStateException("비밀번호를 입력해 주세요");
        }

        //스프링 시큐리티에서 username과 password를 검증하기 위해서는 token에 담아야 함
        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(loginId, password);
        //token에 담은 검증을 위한 AuthenticationManager로 전달
        return authenticationManager.authenticate(authToken);
    }

    //로그인 성공시 작동 - 토큰을 생성하고 해당 내용을 프론ㅌ
    @Override
    protected void successfulAuthentication(
			HttpServletRequest request,
			HttpServletResponse response,
			FilterChain chain,
			Authentication authResult
	) throws IOException, ServletException {
        //인증에 성공한 loginId 받아오기
        String loginId = authResult.getName();
        Member member = memberRepository.findByLoginId(loginId)
				.orElseThrow(() -> new InvalidRequestStateException("존재하지 않는 사용자입니다."));

        //토큰 생성 - access 토큰 유효기간 30분
        String accessToken = jwtUtil.createJwt("access", loginId, 30 * 60 * 1000L);
        //토큰 생성 - refresh 토큰 유효기간 1일 (refresh 토큰에서는 사용자 정보를 포함하지 않음)
        String refresh = jwtUtil.createJwt("refresh", "fakeEmail", 24 * 60 * 60 * 1000L);
        response.addHeader("Authorization", "Bearer " + accessToken);// 헤더에 access 토큰 넣기
        response.addHeader("Set-Cookie", createCookie("refresh", refresh).toString()); //쿠키 생성밒 추가
        // refresh 토큰 객체 생성 및 Redis 저장
        RefreshToken refreshToken = new RefreshToken(refresh, loginId);
        refreshTokenRepository.save(refreshToken);

        //API 응답 생성
        // createAPIResponse(response, REQUEST_OK);
        createUserResponse(response, member);
    }

    //로그인 실패시 작동 - 실패시 json 응답을 작성해 보내는것이 목적
    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response,
                                              AuthenticationException failed) throws IOException, ServletException {

        //API 응답 생성 - failed 예외는 BadCredentialsException 하나로만 처리함
        createAPIResponse(response, INCORRECT_AUTH_INFO);
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

    private void createAPIResponse(HttpServletResponse response, ErrorCode errorCode) throws IOException {
        ApiResponse apiResponse = ApiResponse.exception(errorCode);
        response.setStatus(errorCode.getHttpStatus().value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("UTF-8");
        ObjectMapper mapper = new ObjectMapper();
        mapper.writeValue(response.getWriter(), apiResponse);
    }

    @Transactional
    protected void createUserResponse(HttpServletResponse response, Member member) throws IOException {
        LoginResponse loginResponse = new LoginResponse(member);
        ApiResponse<LoginResponse> apiResponse = ApiResponse.ok(loginResponse);
        response.setStatus(200);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("UTF-8");
        ObjectMapper mapper = new ObjectMapper();
        mapper.writeValue(response.getWriter(), apiResponse);
    }

    // JSON 파일을 읽고 반환하는 역할
    private Map<String, String> getLoginInfoFromJson(HttpServletRequest request) {
        if (!"application/json".equals(request.getContentType())) {
            throw new InvalidRequestStateException("JSON 형식이 아닙니다");
        }
        StringBuilder jsonString = new StringBuilder();
        try (BufferedReader reader = request.getReader()) {
            String line;
            while ((line = reader.readLine()) != null) {
                jsonString.append(line);
            }
        } catch (IOException e) {
            throw new InvalidRequestStateException("JSON 파일 읽기 실패");
        }
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.readValue(jsonString.toString(), new TypeReference<>() {
            });
        } catch (JsonProcessingException e) {
            throw new InvalidRequestStateException("JSON 형식에 맞지 않는 포맷");
        }
    }
}
