package com.kyonggi.teampu.domain.member.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

public class MyPageResponse {

    @Getter
    @ToString
    @AllArgsConstructor
    public static class MyPageDTO {
        private String loginId;
        private String name;
        private String phoneNumber;
        private String email;
    }
}
