package com.kyonggi.teampu.domain.myPage.controller;

import com.kyonggi.teampu.global.response.ApiResponse;
import com.kyonggi.teampu.domain.myPage.dto.MyPageResponse;
import com.kyonggi.teampu.domain.myPage.service.MyPageService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/profile")
public class MyPageController {
    private final MyPageService myPageService;

    @GetMapping("/{login_id}")
    public ApiResponse<MyPageResponse.MyPageDTO> findByLoginId(@PathVariable("login_id") String loginId) {
        MyPageResponse.MyPageDTO memberDTO = myPageService.findByLoginId(loginId);
        return ApiResponse.ok(memberDTO);
    }
}
