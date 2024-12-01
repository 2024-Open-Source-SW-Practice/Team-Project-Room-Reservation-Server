package com.kyonggi.teampu.domain.coApplicant.repository;

import com.kyonggi.teampu.domain.coApplicant.domain.CoApplicant;
import com.kyonggi.teampu.domain.member.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CoApplicantRepository extends JpaRepository<CoApplicant, Long> {
    @Query("SELECT ca.member FROM CoApplicant ca JOIN ca.application a WHERE a.id = :applicationId")
    List<Member> findCoApplicantsByApplicationId(@Param("applicationId") Long applicationId);
}
