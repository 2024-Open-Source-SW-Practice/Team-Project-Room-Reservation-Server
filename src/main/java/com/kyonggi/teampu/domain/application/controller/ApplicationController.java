package com.kyonggi.teampu.domain.application.controller;

import com.kyonggi.teampu.domain.application.dto.ApplicationRequest;
import com.kyonggi.teampu.domain.application.dto.ApplicationResponse;
import com.kyonggi.teampu.domain.application.service.ApplicationService;
import com.kyonggi.teampu.global.exception.ErrorCode;
import com.kyonggi.teampu.global.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/apply")
public class ApplicationController {
    private final ApplicationService applicationService;

    @PostMapping
    public ApiResponse<ApplicationResponse> createApplication(@RequestBody ApplicationRequest applicationRequest) {
        // 400 에러 처리
        if (applicationRequest.getMemberId() == null ||
                applicationRequest.getStartTime() == null ||
                applicationRequest.getEndTime() == null) {
            return ApiResponse.exception(ErrorCode.INVALID_REQUEST);
        }
        try {
            // 201 응답 처리
            ApplicationResponse aplApplicationResponse = applicationService.createApplication(applicationRequest);
            return ApiResponse.ok(aplApplicationResponse);
        }catch (IllegalArgumentException e){
            // 401 에러 처리
            return ApiResponse.exception(ErrorCode.NOT_ENOUGH_PERMISSION);
        }
    }
}