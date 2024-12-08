package com.kyonggi.teampu.domain.member.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CoApplicantRequest {
    private String name;
    private String phoneNumber;
}
