package com.kyonggi.teampu.global;


import com.kyonggi.teampu.global.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Validated
@RequestMapping("/")
@Slf4j
public class TestController {

    @GetMapping("/health")
    public ApiResponse<Void> healthCheck(){
        return ApiResponse.ok();
    }

}
