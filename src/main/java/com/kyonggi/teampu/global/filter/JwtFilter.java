package com.kyonggi.teampu.global.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kyonggi.teampu.domain.auth.domain.CustomMemberDetails;
import com.kyonggi.teampu.domain.member.domain.Member;
import com.kyonggi.teampu.domain.member.repository.MemberRepository;
import com.kyonggi.teampu.global.exception.ErrorCode;
import com.kyonggi.teampu.global.response.ApiResponse;
import com.kyonggi.teampu.global.util.JwtUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Optional;

import static com.kyonggi.teampu.global.exception.ErrorCode.*;

@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {
    private final JwtUtil jwtUtil;
    private final MemberRepository memberRepository;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {
        String authorizationHeader = request.getHeader("Authorization");
        String accessToken = null;

        // 헤더에서 토큰 추출
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            accessToken = authorizationHeader.substring(7);
        }
        if (!StringUtils.hasText(accessToken)) {
            filterChain.doFilter(request, response);
            return;
        }
        if (!jwtUtil.validateToken(accessToken)) {
            createExceptionResponse(response, INVALID_AUTH_TOKEN);
            return;
        }
        if (jwtUtil.isExpired(accessToken)) {
            createExceptionResponse(response, EXPIRED_AUTH_TOKEN);
            return;
        }
        if (!jwtUtil.getCategory(accessToken).equals("access")) {
            createExceptionResponse(response, INVALID_AUTH_TOKEN);
            return;
        }

        String loginId = jwtUtil.getLoginId(accessToken);
        Optional<Member> member = memberRepository.findByLoginId(loginId);

        if (member.isEmpty()) {
            createExceptionResponse(response, MEMBER_NOT_FOUND);
            return;
        }

        CustomMemberDetails customMemberDetails = new CustomMemberDetails(member.get());
        //스프링 시큐리티 인증 토큰 생성
        Authentication authToken = new UsernamePasswordAuthenticationToken(customMemberDetails, null,
                customMemberDetails.getAuthorities());
        //SecurityContextHolder 에 사용자 등록 (=인가 절차 완료)
        SecurityContextHolder.getContext().setAuthentication(authToken);

        filterChain.doFilter(request, response);
    }

    private void createExceptionResponse(HttpServletResponse response, ErrorCode errorCode) throws IOException {
        ApiResponse apiResponse = ApiResponse.exception(errorCode);
        response.setStatus(errorCode.getHttpStatus().value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("UTF-8");
        ObjectMapper mapper = new ObjectMapper();
        mapper.writeValue(response.getWriter(), apiResponse);
    }
}
