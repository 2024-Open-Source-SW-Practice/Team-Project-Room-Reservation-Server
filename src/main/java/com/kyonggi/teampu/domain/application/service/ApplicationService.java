package com.kyonggi.teampu.domain.application.service;

import com.kyonggi.teampu.domain.application.domain.Application;
import com.kyonggi.teampu.domain.application.dto.ApplicationRequest;
import com.kyonggi.teampu.domain.application.dto.ApplicationResponse;
import com.kyonggi.teampu.domain.application.repository.ApplicationRepository;
import com.kyonggi.teampu.domain.member.domain.Member;
import com.kyonggi.teampu.domain.member.repository.MemberRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ApplicationService {
    private final ApplicationRepository applicationRepository;
    private final MemberRepository memberRepository;

    @Transactional
    public ApplicationResponse createApplication(String loginId, ApplicationRequest request) {
        Member member = memberRepository.findByLoginId(loginId)
                .orElseThrow(() -> new IllegalArgumentException("회원을 찾을 수 없습니다"));

        Application application = request.toEntity(member);
        Application savedApplication = applicationRepository.save(application);

        return ApplicationResponse.of(savedApplication);
    }
}