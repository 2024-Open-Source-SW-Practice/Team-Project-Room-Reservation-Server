package com.kyonggi.teampu.domain.mainPage.service;

import com.kyonggi.teampu.domain.application.domain.Application;
import com.kyonggi.teampu.domain.mainPage.dto.MainPageResponse;
import com.kyonggi.teampu.domain.mainPage.repository.MainPageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MainPageService {

    private final MainPageRepository mainPageRepository;

    public MainPageResponse.CalendarResponseDTO getCalendarData(Integer year, Integer month) {
        LocalDate now = LocalDate.now();

        int targetYear = (year != null) ? year : now.getYear();
        int targetMonth = (month != null) ? month : now.getMonthValue();

        YearMonth yearMonth = YearMonth.of(targetYear, targetMonth); // 선택한 월의 마지막 일
        LocalDate startDate; // 시작일

        if (targetYear == now.getYear() && targetMonth == now.getMonthValue()) {
            //선택한 월이 현재 월과 같다면 시작일을 지금으로
            startDate = now;
        } else if (targetYear < now.getYear() || (targetYear == now.getYear() && targetMonth < now.getMonthValue())) {
            // 과거 월이면 빈 리스트
            return new MainPageResponse.CalendarResponseDTO(targetYear, targetMonth, new ArrayList<>());
        } else {
            // 미래 월이면 1일부터
            startDate = yearMonth.atDay(1);
        }

        LocalDate endDate = yearMonth.atEndOfMonth();

        // 예약 정보 DB 조회
        List<Application> applications = mainPageRepository.findByAppliedDateBetween(startDate, endDate);

        Map<LocalDate, Integer> reservationCountByDate = new HashMap<>();

        // reservationCountByDate에 일별로 몇개의 예약이 있는지 확인 후 put
        for (Application app : applications) {
            LocalDate appDate = app.getAppliedDate();
            int currentCount = reservationCountByDate.getOrDefault(appDate, 0);
            reservationCountByDate.put(appDate, currentCount + 1);
        }

        // 날짜별 DTO 생성
        List<MainPageResponse.DayDTO> days = new ArrayList<>();
        for (int day = startDate.getDayOfMonth(); day <= endDate.getDayOfMonth(); day++) {
            LocalDate date = LocalDate.of(targetYear, targetMonth, day);
            int count = reservationCountByDate.getOrDefault(date, 0);
            days.add(new MainPageResponse.DayDTO(day, count));
        }

        return new MainPageResponse.CalendarResponseDTO(targetYear, targetMonth, days);
    }
}