package com.kyonggi.teampu.domain.member.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

public class MyPageRequest {

    @Getter
    @AllArgsConstructor
    public static class UpdateProfileDTO {
        private String name;
        private String phoneNumber;
        private String email;
    }
}

