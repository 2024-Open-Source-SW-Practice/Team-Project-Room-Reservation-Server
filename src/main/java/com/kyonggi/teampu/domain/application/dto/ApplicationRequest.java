package com.kyonggi.teampu.domain.application.dto;

import com.kyonggi.teampu.domain.application.domain.Application;
import com.kyonggi.teampu.domain.application.domain.ApplicationStatus;
import com.kyonggi.teampu.domain.member.domain.Member;
import com.kyonggi.teampu.domain.member.domain.MemberType;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@NoArgsConstructor
public class ApplicationRequest {
    private String name;
    private MemberType type;
    private Long memberId;
    private String loginId;
    private String phoneNumber;
    private String email;
    private Integer participantCount;
    private List<String> coParticipantNames;
    private Boolean privacyAgreement;
    private LocalDateTime startTime;
    private LocalDateTime endTime;

    public Application toEntity(Member member) {
        return Application.builder()
                .member(member)
                .participantCount(participantCount)
                .coParticipantNames(coParticipantNames)
                .privacyAgreement(privacyAgreement)
                .startTime(startTime)
                .endTime(endTime)
                .appliedDate(LocalDate.now())
                .status(ApplicationStatus.PENDING)
                .build();
    }
}