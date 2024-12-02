package com.kyonggi.teampu.domain.application.dto;


import com.kyonggi.teampu.domain.application.domain.Application;
import com.kyonggi.teampu.domain.member.domain.Member;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
public class ApplicationResponse {
    private LocalDateTime startTime; // 시작 시간
    private LocalDateTime endTime; // 종료 시간
    private LocalDate appliedDate; // 날짜
    private Integer applicantCount; // 동반 참가자 수만 저장하는 필드
    private ApplicantResponse applicant; // 이름
    private List<ApplicantResponse> coApplicants; // 공동 참여자 목록 (이름, 전화번호)


    public static ApplicationResponse fromEntity(Application application, List<Member> coApplicants){
        List<ApplicantResponse> coApplicantResponses = coApplicants.stream()
                .map(ApplicantResponse::fromEntity)
                .toList();

        return ApplicationResponse.builder()
                .startTime(application.getStartTime()) // 시작 시간
                .endTime(application.getEndTime()) // 종료 시간
                .appliedDate(application.getAppliedDate()) // 날짜
                .applicant(ApplicantResponse.fromEntity(application.getApplicant())) // 이름
                .applicantCount(application.getApplicantCount()) // 참여 인원(신청자 제외)
                .coApplicants(coApplicantResponses) // 공동 참여자 목록
                .build();
    }

    @Getter
    @Builder
    public static class ApplicantResponse {
        private final Long id;
        private final String loginId;
        private final String name;
        private final String college;
        private final String department;
        private final String phoneNumber;
        private final String email;
        private final String type;
        private final boolean isAdmin;

        public static ApplicantResponse fromEntity(Member member){
            return ApplicantResponse.builder()
                    .id(member.getId())
                    .loginId(member.getLoginId())
                    .name(member.getName())
                    .college(member.getCollege())
                    .department(member.getDepartment())
                    .phoneNumber(member.getPhoneNumber())
                    .email(member.getEmail())
                    .type(member.getType().name())
                    .isAdmin(member.getIsAdmin())
                    .build();
        }
    }
}
