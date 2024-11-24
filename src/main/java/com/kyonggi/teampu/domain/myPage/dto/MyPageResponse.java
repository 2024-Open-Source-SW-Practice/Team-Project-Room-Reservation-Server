package com.kyonggi.teampu.domain.myPage.dto;

import lombok.*;

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
