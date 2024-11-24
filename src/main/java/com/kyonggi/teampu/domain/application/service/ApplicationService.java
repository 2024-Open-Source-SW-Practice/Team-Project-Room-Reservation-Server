package com.kyonggi.teampu.domain.application.service;

import com.kyonggi.teampu.domain.application.domain.Application;
import com.kyonggi.teampu.domain.application.dto.ApplicationRequest;
import com.kyonggi.teampu.domain.application.repository.ApplicationRepository;
import com.kyonggi.teampu.domain.auth.domain.CustomMemberDetails;
import com.kyonggi.teampu.domain.member.dto.CoParticipantRequest;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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

        // Application.builder()를 사용하여 로그인한 사용자 정보와 입력받은 정보를 결합
        Application application = Application.builder()
                .applicantName(customMemberDetails.getMember().getName()) // 이름
                .applicantLoginId(customMemberDetails.getMember().getLoginId()) // 학번
                .applicantPhone(customMemberDetails.getMember().getPhoneNumber()) // 전화번호
                .applicantEmail(customMemberDetails.getMember().getEmail()) // 이메일

                .date(applicationRequest.getDate()) // 날짜
                .coParticipants(CoParticipantRequest.toNameList(applicationRequest.getCoParticipants())) // 명단(이름, 전화번호)
                .participantCount(applicationRequest.getCoParticipants().size() + 1) // 사용 인원 자동 계산
                .privacyAgreement(applicationRequest.getPrivacyAgreement()) // 개인정보 동의
                .build();

        applicationRepository.save(application);
    }

}