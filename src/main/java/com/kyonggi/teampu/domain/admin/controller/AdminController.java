package com.kyonggi.teampu.domain.admin.controller;


import com.kyonggi.teampu.domain.admin.dto.response.AppliedInfoResponse;
import com.kyonggi.teampu.domain.admin.service.AdminService;
import com.kyonggi.teampu.global.response.ApiResponse;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Validated
@RequestMapping("/api/admin")
@Slf4j
public class AdminController {

    private final AdminService adminService;

    @GetMapping("/home")
    public ApiResponse<List<AppliedInfoResponse>> getHome(){
        return ApiResponse.ok(adminService.getHome());
    }

}