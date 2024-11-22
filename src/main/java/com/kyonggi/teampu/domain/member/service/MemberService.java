package com.kyonggi.teampu.domain.member.service;

import com.kyonggi.teampu.domain.auth.dto.LoginResponse;
import com.kyonggi.teampu.domain.member.domain.Member;
import com.kyonggi.teampu.domain.member.domain.MemberType;
import com.kyonggi.teampu.domain.member.dto.JoinRequest;
import com.kyonggi.teampu.domain.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Transactional
    public void join(JoinRequest request) {
        String encodedPassword = bCryptPasswordEncoder.encode(request.getPassword());
        Member member = Member.builder()
                .loginId(request.getLoginId())
                .password(encodedPassword)
                .name(request.getName())
                .college(request.getCollege())
                .department(request.getDepartment())
                .phoneNumber(request.getPhoneNumber())
                .email(request.getEmail())
                .type(MemberType.valueOf(request.getType()))
                .build();

        memberRepository.save(member);
    }

    public LoginResponse findById(Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("해당하는 회원이 없습니다."));

        return new LoginResponse(member);
    }
}
