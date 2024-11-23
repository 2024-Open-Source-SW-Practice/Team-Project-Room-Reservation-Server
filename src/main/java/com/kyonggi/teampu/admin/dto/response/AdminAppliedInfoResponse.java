package com.kyonggi.teampu.admin.dto.response;

import com.kyonggi.teampu.domain.application.domain.ApplicationStatus;
import java.time.LocalDate;
import lombok.Builder;
import lombok.Getter;


@Getter
@Builder
public class AdminAppliedInfoResponse {
        private String name;
        private LocalDate appliedDate;
        private ApplicationStatus status;

        public AdminAppliedInfoResponse(String name, LocalDate appliedDate, ApplicationStatus status) {
                this.name = name;
                this.appliedDate = appliedDate;
                this.status = status;
        }

        // 뭔가 그냥 static class 쓰기 싫어서 이렇게 바꿔봤습니다.
        // 기존 코드 대비 폭풍, 냉정, 객관적, 파괴적 피드백 주시면 감사하겠습니다.
        /**
         * 기존 코드
         * public class AdminHomeResponse {
         *         @Builder
         *         @Getter
         *         public static class AppliedInfoList {
         *                 private final List<AppliedInfo> appliedInfoList;
         *         }
         *
         *         @Builder
         *         @Getter
         *         public static class AppliedInfo {
         *                 private final String name;
         *                 private final LocalDate appliedDate;
         *         }
         * }
         *
         * */


}
