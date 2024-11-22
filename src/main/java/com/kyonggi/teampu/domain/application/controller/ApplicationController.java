package com.kyonggi.teampu.domain.application.controller;

import com.kyonggi.teampu.domain.application.dto.ApplicationRequest;
import com.kyonggi.teampu.domain.application.dto.ApplicationResponse;
import com.kyonggi.teampu.domain.application.service.ApplicationService;
import com.kyonggi.teampu.global.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<ApiResponse<ApplicationResponse>> createApplication(@RequestBody ApplicationRequest applicationRequest) {
        // 400 에러 처리
        if (applicationRequest.getMemberId() == null || applicationRequest.getStartTime() == null || applicationRequest.getEndTime() == null) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(new ApiResponse<>(
                            new ApiResponse.Status(HttpStatus.BAD_REQUEST, "필수 값이 누락되었습니다"),
                            null
                    ));
        }

        try {
            ApplicationResponse applicationResponse = applicationService.createApplication(applicationRequest);
            return ResponseEntity.ok(new ApiResponse<>(
                    new ApiResponse.Status(HttpStatus.CREATED, "신청이 완료되었습니다"),
                    applicationResponse
            ));
        } catch (IllegalArgumentException e) {
            // 401 에러 처리
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body(new ApiResponse<>(
                            new ApiResponse.Status(HttpStatus.UNAUTHORIZED, "권한이 없습니다"),
                            null
                    ));
        }
    }
}