package com.kyonggi.teampu.domain.application.dto;


import com.kyonggi.teampu.domain.application.domain.Application;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Builder
public class ApplicationResponse {
    private LocalDateTime startTime; // 시작 시간
    private LocalDateTime endTime; // 종료 시간
    private LocalDate appliedDate; // 날짜
    private String name; // 이름
    private Integer countCpOnly; // 동반 참가자 수만 저장하는 필드


    public static ApplicationResponse fromEntity(Application application){
        return ApplicationResponse.builder()
                .startTime(application.getStartTime()) // 시작 시간
                .endTime(application.getEndTime()) // 종료 시간
                .appliedDate(application.getAppliedDate()) // 날짜
                .name(application.getMember().getName()) // 이름
                .countCpOnly(application.getCoParticipants().size()) // 참여 인원(신청자 제외)
                .build();
    }

}
