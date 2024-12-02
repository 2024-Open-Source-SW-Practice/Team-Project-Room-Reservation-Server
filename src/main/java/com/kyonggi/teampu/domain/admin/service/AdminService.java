package com.kyonggi.teampu.domain.admin.service;

import com.kyonggi.teampu.domain.admin.dto.request.ApproveRequest;
import com.kyonggi.teampu.domain.applicant.repository.ApplicantRepository;
import com.kyonggi.teampu.domain.application.domain.Application;
import com.kyonggi.teampu.domain.application.dto.ApplicationResponse;
import com.kyonggi.teampu.domain.application.repository.ApplicationRepository;
import com.kyonggi.teampu.domain.member.domain.Member;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.kyonggi.teampu.global.exception.ErrorCode.APPLICATION_NOT_FOUND;
import static com.kyonggi.teampu.global.exception.ErrorCode.NOT_ADMIN;

@Service
@Transactional
@RequiredArgsConstructor
public class AdminService {
    private final ApplicationRepository applicationRepository;
    private final ApplicantRepository applicantRepository;

    public List<ApplicationResponse> findApplications() {
        return applicationRepository.findAll()
                .stream()
                .map(application -> ApplicationResponse.fromEntity(
                        application,
                        applicantRepository.findCoApplicantsByApplicationId(application.getId())
                )).toList();
    }

    public void updateStatus(ApproveRequest request, Member connectedMember) {
        if (!connectedMember.isAdmin()) throw new IllegalStateException(NOT_ADMIN.getMessage());

        Application application = applicationRepository.findById(request.getApplicationId())
                .orElseThrow(() -> new IllegalArgumentException(APPLICATION_NOT_FOUND.getMessage()));
        application.updateStatus(request.getStatus());
    }


}
