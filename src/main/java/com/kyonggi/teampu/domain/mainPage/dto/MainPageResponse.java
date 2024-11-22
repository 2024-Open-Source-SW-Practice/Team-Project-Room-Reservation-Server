package com.kyonggi.teampu.domain.mainPage.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
public class MainPageResponse {

    @Getter
    @AllArgsConstructor
    public static class CalendarResponseDTO {
        private int year;
        private int month;
        private List<DayDTO> days; // 일별 정보 담은 리스트
    }

    @Getter
    @AllArgsConstructor
    public static class DayDTO {
        private int day;
        private int reservationCount; //해당 날짜의 예약 수
    }
}
