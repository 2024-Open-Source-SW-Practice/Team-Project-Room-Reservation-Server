package com.kyonggi.teampu.domain.application.domain;

import com.fasterxml.jackson.annotation.JsonValue;

public enum ApplicationStatus {
    PENDING("pending"),
    APPROVED("approved"),
    REJECTED("rejected");

    private final String name;

    ApplicationStatus(String name) {
        this.name = name;
    }

    public boolean isUpdatable() {
        return this == PENDING;
    }

    @JsonValue
    public String getName() {
        return this.name;
    }

}
