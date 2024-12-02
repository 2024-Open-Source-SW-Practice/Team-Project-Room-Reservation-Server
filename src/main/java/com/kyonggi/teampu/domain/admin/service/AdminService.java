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

@Slf4j
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

    public void approve(ApproveRequest request, Member connectedMember){
        if (!connectedMember.getIsAdmin()) throw new RuntimeException(NOT_ADMIN.getMessage());

        Application application = applicationRepository.findById(request.getApplicationId()).get();
        application.updateStatus(request.getStatus());
    }




}
