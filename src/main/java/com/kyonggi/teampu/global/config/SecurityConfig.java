package com.kyonggi.teampu.global.config;

import com.kyonggi.teampu.domain.auth.repository.RefreshTokenRepository;
import com.kyonggi.teampu.domain.member.repository.MemberRepository;
import com.kyonggi.teampu.global.filter.CustomEntryPoint;
import com.kyonggi.teampu.global.filter.CustomLogoutFilter;
import com.kyonggi.teampu.global.filter.JwtFilter;
import com.kyonggi.teampu.global.filter.LoginFilter;
import com.kyonggi.teampu.global.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutFilter;
import org.springframework.web.cors.CorsConfiguration;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    private final AuthenticationConfiguration authenticationConfiguration;
    private final JwtUtil jwtUtil;
    private final MemberRepository memberRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final CustomEntryPoint customEntryPoint;

    private static final String[] PUBLIC_URLS = {
            "/**"
    };

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.cors((cors) -> cors
                .configurationSource(request -> {
                    CorsConfiguration configuration = new CorsConfiguration();
                    configuration.setAllowedOrigins(Arrays.asList("http://localhost:3000", "http://localhost:8080"));
                    configuration.setAllowedMethods(Collections.singletonList("*"));
                    configuration.setAllowCredentials(true);
                    configuration.setAllowedHeaders(Collections.singletonList("*"));
                    configuration.setMaxAge(3600L);
                    configuration.setExposedHeaders(List.of("Authorization"));
                    return configuration;
                }));

        http.csrf(AbstractHttpConfigurer::disable);
        http.formLogin(AbstractHttpConfigurer::disable);
        http.httpBasic(AbstractHttpConfigurer::disable);

        http.authorizeHttpRequests(auth -> auth
                .requestMatchers(PUBLIC_URLS).permitAll()
                .anyRequest().authenticated());

        http.exceptionHandling(exceptionHandling -> exceptionHandling
                .authenticationEntryPoint(customEntryPoint));


        http.addFilterAfter(new JwtFilter(jwtUtil, memberRepository), UsernamePasswordAuthenticationFilter.class)
                .addFilterAt(
                        new LoginFilter(
                                authenticationManager(authenticationConfiguration),
                                refreshTokenRepository,
                                jwtUtil,
                                memberRepository,
                                "/api/login"),
                        //소셜 로그인시 무한 루프 문제 해결을 위해 인가 검증필터는 로그인 필터 이후에 삽입
                        UsernamePasswordAuthenticationFilter.class
                )
                .addFilterBefore(new CustomLogoutFilter(jwtUtil, refreshTokenRepository), LogoutFilter.class);

        http.sessionManagement((session) -> session
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        return http.build();
    }
}
