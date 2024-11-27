package com.kyonggi.teampu.domain.application.repository;

import com.kyonggi.teampu.domain.application.domain.Application;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface ApplicationRepository extends JpaRepository<Application, Long> {
    List<Application> findByAppliedDateBetween(LocalDate startDate, LocalDate endDate);

    @EntityGraph(attributePaths = {"member"}) // fetch join 사용
    Optional<Application> findById(Long id);

}