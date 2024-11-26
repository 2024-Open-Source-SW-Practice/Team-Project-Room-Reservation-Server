package com.kyonggi.teampu.domain.application.repository;

import com.kyonggi.teampu.domain.application.domain.Application;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface ApplicationRepository extends JpaRepository<Application, Long> {
    List<Application> findByAppliedDateBetween(LocalDate startDate, LocalDate endDate);
}