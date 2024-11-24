package com.kyonggi.teampu.domain.application.repository;

import com.kyonggi.teampu.domain.application.domain.Application;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ApplicationRepository extends JpaRepository<Application, Long> {

}
