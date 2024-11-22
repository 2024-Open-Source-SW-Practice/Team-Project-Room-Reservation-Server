package com.kyonggi.teampu.domain.application.dto;

import com.kyonggi.teampu.domain.application.domain.Application;
import com.kyonggi.teampu.domain.member.domain.MemberType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
@AllArgsConstructor
public class ApplicationResponse {
    private String loginId;
    private String name;
    private MemberType type;
    private Long memberId;
    private String phoneNumber;
    private String email;
    private Integer participantCount;
    private List<String> coParticipantNames;
    private Boolean privacyAgreement;
    private LocalDateTime startTime;
    private LocalDateTime endTime;

    public static ApplicationResponse of(Application application) {
        return ApplicationResponse.builder()
                .loginId(application.getMember().getLoginId())
                .name(application.getMember().getName())
                .type(application.getMember().getType())
                .memberId(application.getMember().getId())
                .phoneNumber(application.getMember().getPhoneNumber())
                .email(application.getMember().getEmail())
                .participantCount(application.getParticipantCount())
                .coParticipantNames(application.getCoParticipantNames())
                .privacyAgreement(application.getPrivacyAgreement())
                .startTime(application.getStartTime())
                .endTime(application.getEndTime())
                .build();
    }
}
