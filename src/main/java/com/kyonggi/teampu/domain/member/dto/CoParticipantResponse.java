package com.kyonggi.teampu.domain.member.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class CoParticipantResponse {
    private final Long userId;
    private final String loginId;
    private final String name;

}
