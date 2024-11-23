package com.kyonggi.teampu.admin.controller;


import com.kyonggi.teampu.admin.dto.response.AdminAppliedInfoResponse;
import com.kyonggi.teampu.admin.service.AdminService;
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
@RequestMapping("/admin")
@Slf4j
public class AdminController {

    private final AdminService adminService;

    @GetMapping("/home")
    public ApiResponse<List<AdminAppliedInfoResponse>> getHome(){
        return ApiResponse.ok(adminService.getHome());
    }
}
