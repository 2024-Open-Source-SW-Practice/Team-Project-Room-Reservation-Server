package com.kyonggi.teampu.domain.member.service;

import com.kyonggi.teampu.domain.member.domain.Member;
import com.kyonggi.teampu.domain.member.domain.MemberType;
import com.kyonggi.teampu.domain.member.dto.JoinRequest;
import com.kyonggi.teampu.domain.member.dto.MemberInfoResponse;
import com.kyonggi.teampu.domain.member.dto.MyPageRequest;
import com.kyonggi.teampu.domain.member.dto.MyPageResponse;
import com.kyonggi.teampu.domain.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.kyonggi.teampu.global.exception.ErrorCode.MEMBER_NOT_FOUND;
import static com.kyonggi.teampu.global.exception.ErrorCode.MEMBER_NOT_FOUND_BY_LOGIN_ID;

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

    public MemberInfoResponse findById(Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException(MEMBER_NOT_FOUND.getMessage()));

        return new MemberInfoResponse(member);
    }

    // 프로필 조회
    public MyPageResponse.MyPageDTO findByLoginId(String loginId) {
        Member member = memberRepository.findByLoginId(loginId)
                .orElseThrow(() -> new IllegalArgumentException(MEMBER_NOT_FOUND_BY_LOGIN_ID.getMessage()));

        return new MyPageResponse.MyPageDTO(
                member.getLoginId(),
                member.getName(),
                member.getPhoneNumber(),
                member.getEmail()
        );
    }

    @Transactional
    public MyPageResponse.MyPageDTO updateProfile(String loginId, MyPageRequest.UpdateProfileDTO updateRequest) {
        Member member = memberRepository.findByLoginId(loginId)
                .orElseThrow(() -> new IllegalArgumentException(MEMBER_NOT_FOUND_BY_LOGIN_ID.getMessage()));

        member.updateProfile(updateRequest.getName(), updateRequest.getPhoneNumber(), updateRequest.getEmail());
        memberRepository.save(member);

        return new MyPageResponse.MyPageDTO(
                member.getLoginId(),
                member.getName(),
                member.getPhoneNumber(),
                member.getEmail()
        );
    }
}
