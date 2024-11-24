package com.kyonggi.teampu.domain.admin.dto.response;

import com.kyonggi.teampu.domain.application.domain.ApplicationStatus;
import java.time.LocalDate;
import lombok.Builder;
import lombok.Getter;


@Getter
@Builder
public class AppliedInfoResponse {
        private String name;
        private LocalDate appliedDate;
        private ApplicationStatus status;

        public AppliedInfoResponse(String name, LocalDate appliedDate, ApplicationStatus status) {
                this.name = name;
                this.appliedDate = appliedDate;
                this.status = status;
        }
}
