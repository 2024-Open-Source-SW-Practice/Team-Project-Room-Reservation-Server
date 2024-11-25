package com.kyonggi.teampu.domain.application.controller;

import com.kyonggi.teampu.domain.application.dto.ApplicationRequest;
import com.kyonggi.teampu.domain.application.dto.ApplicationResponse;
import com.kyonggi.teampu.domain.application.service.ApplicationService;
import com.kyonggi.teampu.domain.auth.domain.CustomMemberDetails;
import com.kyonggi.teampu.global.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/apply")
public class ApplicationController {
    private final ApplicationService applicationService;

    /**
     * 신청서 작성 API
     * */
    @PostMapping
    public ApiResponse<Void> createApplication(@RequestBody ApplicationRequest applicationRequest,
                                               @AuthenticationPrincipal CustomMemberDetails customMemberDetails) {
        // @AuthenticationPrincipal 스프링 시큐리티 활용해서 사용자 정보 받아옴

        applicationService.createApplication(applicationRequest, customMemberDetails);

        return ApiResponse.ok(); // POST에 대해서 리턴 값 필요없음 >> Void
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> deleteApplication(@PathVariable Long id,
                                               @AuthenticationPrincipal CustomMemberDetails customMemberDetails) {

        applicationService.deleteApplication(id, customMemberDetails);

        return ApiResponse.ok();
    }

    @GetMapping("/{id}")
    public ApiResponse<ApplicationResponse> getDetailApplication(@PathVariable Long id,
                                                                 @AuthenticationPrincipal CustomMemberDetails customMemberDetails){
        ApplicationResponse applicationResponse = applicationService.getDetailApplication(id, customMemberDetails);

        return ApiResponse.ok(applicationResponse);
    }

    @PatchMapping("/{id}")
    public ApiResponse<Void> updateApplication(@PathVariable Long id,
                                                              @AuthenticationPrincipal CustomMemberDetails customMemberDetails,
                                                              @RequestBody ApplicationRequest applicationRequest){
        applicationService.updateApplication(id, customMemberDetails, applicationRequest);
        return ApiResponse.ok();
    }

}