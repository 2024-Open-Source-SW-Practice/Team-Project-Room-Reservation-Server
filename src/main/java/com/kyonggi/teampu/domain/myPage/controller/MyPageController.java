package com.kyonggi.teampu.domain.myPage.controller;

import com.kyonggi.teampu.domain.myPage.dto.MyPageRequest;
import com.kyonggi.teampu.global.response.ApiResponse;
import com.kyonggi.teampu.domain.myPage.dto.MyPageResponse;
import com.kyonggi.teampu.domain.myPage.service.MyPageService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;


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

    @PatchMapping("/{login_id}")
    public ApiResponse<MyPageResponse.MyPageDTO> updateProfile(
            @PathVariable("login_id") String loginId,
            @RequestBody MyPageRequest.UpdateProfileDTO updateRequest) {
        MyPageResponse.MyPageDTO updatedMember = myPageService.updateProfile(loginId, updateRequest);
        return ApiResponse.ok(updatedMember);
    }
}
