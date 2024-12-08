package com.kyonggi.teampu.domain.application.controller;

import com.kyonggi.teampu.domain.application.dto.MainPageResponse;
import com.kyonggi.teampu.domain.application.dto.ApplicationRequest;
import com.kyonggi.teampu.domain.application.dto.ApplicationResponse;
import com.kyonggi.teampu.domain.application.service.ApplicationService;
import com.kyonggi.teampu.domain.auth.domain.CustomMemberDetails;
import com.kyonggi.teampu.domain.member.domain.Member;
import com.kyonggi.teampu.global.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/apply")
@RequiredArgsConstructor
public class ApplicationController {
    private final ApplicationService applicationService;

    // 로그인 전 전체 예약 정보 확인
    @GetMapping("/home")
    public ApiResponse<MainPageResponse.CalendarResponseDTO> getCalendarData(
            @RequestParam(required = false) Integer year,
            @RequestParam(required = false) Integer month
    ) {
        MainPageResponse.CalendarResponseDTO response = applicationService.getCalendarData(year, month);
        return ApiResponse.ok(response);
    }

    /**
     * 신청서 작성 API
     */
    @PostMapping
    public ApiResponse<Void> createApplication(
            @RequestBody ApplicationRequest applicationRequest,
            @AuthenticationPrincipal CustomMemberDetails customMemberDetails
    ) {
        // @AuthenticationPrincipal 스프링 시큐리티 활용해서 사용자 정보 받아옴
        applicationService.createApplication(applicationRequest, customMemberDetails.getMember());

        return ApiResponse.ok(); // POST에 대해서 리턴 값 필요없음 >> Void
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> deleteApplication(@PathVariable Long id) {
        applicationService.deleteApplication(id);

        return ApiResponse.ok();
    }

    @GetMapping("/{id}")
    public ApiResponse<ApplicationResponse> getDetailApplication(@PathVariable Long id) {
        ApplicationResponse applicationResponse = applicationService.getDetailApplication(id);

        return ApiResponse.ok(applicationResponse);
    }

    @GetMapping("/my")
    public ApiResponse<List<ApplicationResponse>> findApplicationsByMember(
            @AuthenticationPrincipal CustomMemberDetails customMemberDetails
    ) {
        return ApiResponse.ok(applicationService.findByMember(customMemberDetails.getMember()));
    }

    @PatchMapping("/{id}")
    public ApiResponse<Void> updateApplication(
            @PathVariable Long id,
            Member member,
            @RequestBody ApplicationRequest applicationRequest
    ) {
        applicationService.updateApplication(id, member, applicationRequest);
        return ApiResponse.ok();
    }
}