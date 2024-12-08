package com.kyonggi.teampu.domain.auth.controller;

import com.kyonggi.teampu.domain.auth.service.ReissueService;
import com.kyonggi.teampu.global.response.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class ReissueController {
	private final ReissueService reissueService;
	@PostMapping("/reissue")
	ApiResponse reissue(HttpServletRequest request, HttpServletResponse response){
		String newAccess = reissueService.createNewAccessToken(request, response);
		response.addHeader("Authorization", "Bearer " + newAccess);

		return ApiResponse.ok();
	}
}
