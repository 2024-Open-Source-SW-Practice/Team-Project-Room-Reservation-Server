package com.kyonggi.teampu.domain.admin.controller;


import com.kyonggi.teampu.domain.admin.dto.request.ApproveRequest;
import com.kyonggi.teampu.domain.admin.service.AdminService;
import com.kyonggi.teampu.domain.application.dto.ApplicationResponse;
import com.kyonggi.teampu.domain.auth.domain.CustomMemberDetails;
import com.kyonggi.teampu.global.response.ApiResponse;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Validated
@RequestMapping("/api/admin")
@Slf4j
public class AdminController {

    private final AdminService adminService;

    @GetMapping("/applications")
    public ApiResponse<List<ApplicationResponse>> findApplications(){
        return ApiResponse.ok(adminService.findApplications());
    }

    @PostMapping("/applications/status")
    public ApiResponse<Void> updateStatus(@RequestBody ApproveRequest request,
                                    @AuthenticationPrincipal CustomMemberDetails customMemberDetails){
        adminService.updateStatus(request, customMemberDetails.getMember());

        return ApiResponse.ok();
    }
}
