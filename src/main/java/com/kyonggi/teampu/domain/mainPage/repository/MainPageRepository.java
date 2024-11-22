package com.kyonggi.teampu.domain.mainPage.repository;

import com.kyonggi.teampu.domain.application.domain.Application;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface MainPageRepository  extends JpaRepository<Application, Long> {

    //Query문 대신 이름으로
    List<Application> findByAppliedDateBetween(LocalDate startDate, LocalDate endDate);
}
