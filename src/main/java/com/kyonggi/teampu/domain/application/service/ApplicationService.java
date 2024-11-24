package com.kyonggi.teampu.domain.application.service;

import com.kyonggi.teampu.domain.application.domain.Application;
import com.kyonggi.teampu.domain.application.dto.ApplicationRequest;
import com.kyonggi.teampu.domain.application.repository.ApplicationRepository;
import com.kyonggi.teampu.domain.auth.domain.CustomMemberDetails;
import com.kyonggi.teampu.domain.member.domain.CoParticipant;
import com.kyonggi.teampu.domain.member.domain.Member;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ApplicationService {
    private final ApplicationRepository applicationRepository;

    @Transactional
    public void createApplication(ApplicationRequest applicationRequest, CustomMemberDetails customMemberDetails) {
        // GET 메서드를 별도로 구현할 예정이라 void 처리

        /**
         * 1. 사용자의 정보(이름, 학번, 전화번호, 이메일) 추출
         * 2. 사용자가 직접 정보 입력(날짜, 명단: 이름, 전화번호, 개인정보 동의)
         * 3. 사용 인원 계산
         */

        Member member = customMemberDetails.getMember();
        List<CoParticipant> coParticipants = applicationRequest.getCoParticipants().stream()
                .map(CoParticipant::from)
                .collect(Collectors.toList());

        // Application.builder()를 사용하여 로그인한 사용자 정보와 입력받은 정보를 결합
        Application application = Application.builder()
                .member(member) // memberId
                .applicantName(customMemberDetails.getMember().getName()) // 이름
                .applicantLoginId(customMemberDetails.getMember().getLoginId()) // 학번
                .applicantPhone(customMemberDetails.getMember().getPhoneNumber()) // 전화번호
                .applicantEmail(customMemberDetails.getMember().getEmail()) // 이메일

                .appliedDate(applicationRequest.getAppliedDate()) // 날짜
                .coParticipants(coParticipants) // 명단(이름, 전화번호)
                .participantCount(applicationRequest.getCoParticipants().size() + 1) // 사용 인원 자동 계산
                .privacyAgreement(applicationRequest.getPrivacyAgreement()) // 개인정보 동의
                .status(applicationRequest.getStatus())
                .build();

        applicationRepository.save(application);
    }


    @Transactional
    public void deleteApplication(Long id, CustomMemberDetails customMemberDetails){
        Application application = applicationRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("신청서를 찾을 수 없습니다."));

        applicationRepository.delete(application);
    }
}