package com.kyonggi.teampu.domain.mainPage.controller;

import com.kyonggi.teampu.domain.mainPage.dto.MainPageResponse;
import com.kyonggi.teampu.domain.mainPage.service.MainPageService;
import com.kyonggi.teampu.global.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/apply")
@RequiredArgsConstructor
public class MainPageController {
    private final MainPageService mainService;

    // 로그인 전 전체 예약 정보 확인
    @GetMapping("/home")
    public ApiResponse<MainPageResponse.CalendarResponseDTO> getCalendarData(
            @RequestParam(required = false) Integer year,
            @RequestParam(required = false) Integer month
    ) {
        MainPageResponse.CalendarResponseDTO response = mainService.getCalendarData(year, month);
        return ApiResponse.ok(response);
    }
}
