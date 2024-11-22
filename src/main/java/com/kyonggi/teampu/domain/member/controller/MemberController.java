package com.kyonggi.teampu.domain.member.controller;

import com.kyonggi.teampu.domain.auth.domain.CustomMemberDetails;
import com.kyonggi.teampu.domain.auth.dto.LoginResponse;
import com.kyonggi.teampu.domain.member.dto.JoinRequest;
import com.kyonggi.teampu.domain.member.service.MemberService;
import com.kyonggi.teampu.global.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class MemberController {
    private final MemberService memberService;

    @PostMapping("/join")
    public ApiResponse<Void> join(@RequestBody JoinRequest request) {
        memberService.join(request);

        return ApiResponse.ok();
    }

    @GetMapping("/profile")
    public ApiResponse<LoginResponse> getProfile(@AuthenticationPrincipal CustomMemberDetails customMemberDetails) {
        Long memberId = customMemberDetails.getMember().getId();

        return ApiResponse.ok(memberService.findById(memberId));
    }
}
