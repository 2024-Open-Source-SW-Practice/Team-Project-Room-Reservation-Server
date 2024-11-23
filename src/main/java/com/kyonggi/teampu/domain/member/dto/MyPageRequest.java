package com.kyonggi.teampu.domain.member.dto;

import lombok.Getter;

public class MyPageRequest {

    @Getter
    public static class UpdateProfileDTO {
        private String name;
        private String phoneNumber;
        private String email;
    }
}

