package com.kyonggi.teampu.admin.service;

import com.kyonggi.teampu.admin.dto.response.AdminAppliedInfoResponse;
import com.kyonggi.teampu.domain.application.domain.Application;
import com.kyonggi.teampu.domain.application.repository.ApplicationRepository;
import jakarta.transaction.Transactional;
import java.util.ArrayList;
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

    public List<AdminAppliedInfoResponse> getHome(){
        List<Application> applicationList = applicationRepository.findAllBy();

        List<AdminAppliedInfoResponse> adminAppliedInfoResponseList = new ArrayList<>();

        for (Application application : applicationList) {
            adminAppliedInfoResponseList.add(new AdminAppliedInfoResponse(
                application.getMember().getName(), application.getAppliedDate(), application.getStatus()
            ));
        }

        return adminAppliedInfoResponseList;
    }




}
