package com.kyonggi.teampu.domain.admin.controller;


import com.kyonggi.teampu.domain.admin.dto.response.AdminAppliedInfoResponse;
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
@RequestMapping("/admin")
@Slf4j
public class AdminController {

    private final AdminService adminService;

    @GetMapping("/home")
    public ApiResponse<List<AdminAppliedInfoResponse>> getHome(){
        return ApiResponse.ok(adminService.getHome());
    }

    /**
     * 고민 중인 사항이
     * admin 패키지를 따로 만들자 vs member 패키지에 다 넣어버리자
     * 인데 어떻게 가는 것이 좋을까요??
     * + URI에 admin이 들어가는 것이 어떤지? 참고한 우테코 레포는 admin이라는 URI가 있더라구요.
     *
     * */
}
