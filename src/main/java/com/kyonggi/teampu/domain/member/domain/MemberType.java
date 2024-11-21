package com.kyonggi.teampu.domain.member.domain;

public enum MemberType {
    UNDERGRADUATE("학부생"),
    GRADUATE("대학원생"),
    PROFESSOR("교수");

    MemberType(String name) {
        this.name = name;
    }

    private final String name;
}
