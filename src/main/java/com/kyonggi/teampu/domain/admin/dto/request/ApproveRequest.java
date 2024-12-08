package com.kyonggi.teampu.domain.admin.dto.request;

import lombok.Getter;

@Getter
public class ApproveRequest {
    private Long applicationId;
    private String status;
}
