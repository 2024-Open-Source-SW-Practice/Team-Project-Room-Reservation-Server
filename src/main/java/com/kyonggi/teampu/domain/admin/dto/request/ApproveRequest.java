package com.kyonggi.teampu.domain.admin.dto.request;

import com.kyonggi.teampu.domain.application.domain.ApplicationStatus;
import lombok.Getter;

@Getter
public class ApproveRequest {
    private Long applicationId;
    private ApplicationStatus status;
}
