package com.kyonggi.teampu.domain.myPage.service;

import com.kyonggi.teampu.domain.member.domain.Member;
import com.kyonggi.teampu.domain.myPage.dto.MyPageResponse.MyPageDTO;
import com.kyonggi.teampu.domain.myPage.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MyPageService {
    private final MemberRepository memberRepository;

    @Transactional(readOnly = true)
    public MyPageDTO findByLoginId(String loginId) {
        Member member = memberRepository.findByLoginId(loginId)
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));

        return new MyPageDTO(
                member.getLoginId(),
                member.getName(),
                member.getPhoneNumber(),
                member.getEmail()
        );
    }
}
