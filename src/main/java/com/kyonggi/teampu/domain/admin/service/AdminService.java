package com.kyonggi.teampu.domain.admin.service;

import com.kyonggi.teampu.domain.admin.dto.response.AppliedInfoResponse;
import com.kyonggi.teampu.domain.application.domain.Application;
import com.kyonggi.teampu.domain.application.repository.ApplicationRepository;
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




}
