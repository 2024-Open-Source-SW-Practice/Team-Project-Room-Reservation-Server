package com.kyonggi.teampu.domain.application.domain;

import com.fasterxml.jackson.annotation.JsonValue;

public enum ApplicationStatus {
    PENDING("pending"),
    APPROVED("approved"),
    REJECTED("rejected");

    private final String code;

    ApplicationStatus(String code) {
        this.code = code;
    }

    @JsonValue
    public String getCode() {
        return this.code;
    }

}
