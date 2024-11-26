package com.kyonggi.teampu.domain.admin.service;

import static com.kyonggi.teampu.global.exception.ErrorCode.NOT_ADMIN;

import com.kyonggi.teampu.domain.admin.dto.request.ApproveRequest;
import com.kyonggi.teampu.domain.admin.dto.response.AppliedInfoResponse;
import com.kyonggi.teampu.domain.application.domain.Application;
import com.kyonggi.teampu.domain.application.repository.ApplicationRepository;
import com.kyonggi.teampu.domain.member.domain.Member;
import com.kyonggi.teampu.global.response.ApiResponse;
import jakarta.transaction.Transactional;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class AdminService {

    private final ApplicationRepository applicationRepository;

    public List<AppliedInfoResponse> getHome(){
        List<Application> applicationList = applicationRepository.findAll();

        return applicationList.stream()
            .map(application -> new AppliedInfoResponse(
                application.getMember().getName(),
                application.getAppliedDate(),
                application.getStatus()
            ))
            .toList();
    }

    public void approve(ApproveRequest request, Member connectedMember){
        if (!connectedMember.getIsAdmin()) throw new RuntimeException(NOT_ADMIN.getMessage());

        Application application = applicationRepository.findById(request.getApplicationId()).get();
        application.updateStatus(request.getStatus());
    }




}
