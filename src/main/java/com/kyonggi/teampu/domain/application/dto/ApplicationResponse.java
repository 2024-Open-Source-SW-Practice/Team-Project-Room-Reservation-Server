package com.kyonggi.teampu.domain.application.dto;


import com.kyonggi.teampu.domain.application.domain.Application;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@Builder
public class ApplicationResponse {
    private LocalDate appliedDate; // 날짜
    private String name; // 이름
    private Integer participantCount; // 참여 인원(신청자 제외)

    public static ApplicationResponse fromEntity(Application application){
        return ApplicationResponse.builder()
                .appliedDate(application.getAppliedDate()) // 날짜
                .name(application.getMember().getName()) // 이름
                .participantCount(application.getCoParticipants().size()) // 참여 인원(신청자 제외)
                .build();
    }

}
