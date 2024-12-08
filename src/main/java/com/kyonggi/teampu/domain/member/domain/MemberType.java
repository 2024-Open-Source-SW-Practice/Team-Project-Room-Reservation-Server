package com.kyonggi.teampu.domain.member.domain;

import java.util.Arrays;

public enum MemberType {
    UNDERGRADUATE("학부생"),
    GRADUATE("대학원생"),
    PROFESSOR("교수");

    MemberType(String name) {
        this.name = name;
    }

    private final String name;

    public static MemberType findByName(String name) {
        return Arrays.stream(MemberType.values())
                .filter(memberType -> memberType.name.equals(name))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("해당하는 회원 타입이 없습니다."));
    }
}
