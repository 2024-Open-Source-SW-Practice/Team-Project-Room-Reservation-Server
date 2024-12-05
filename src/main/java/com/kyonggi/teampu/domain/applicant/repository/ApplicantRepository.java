package com.kyonggi.teampu.domain.applicant.repository;

import com.kyonggi.teampu.domain.applicant.domain.Applicant;
import com.kyonggi.teampu.domain.member.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ApplicantRepository extends JpaRepository<Applicant, Long> {
    @Query("SELECT ca.member FROM Applicant ca JOIN ca.application a WHERE a.id = :applicationId")
    List<Member> findMembersByApplicationId(@Param("applicationId") Long applicationId);
