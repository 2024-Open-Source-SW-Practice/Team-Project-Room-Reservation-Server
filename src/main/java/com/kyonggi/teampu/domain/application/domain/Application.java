package com.kyonggi.teampu.domain.application.domain;

import com.kyonggi.teampu.domain.member.domain.Member;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Table(name = "applications")
public class Application {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "application_id")
    private Long id;

    @Column(name = "start_time", nullable = false, columnDefinition = "TIMESTAMP(0)") // 밀리초 자릿수를 0으로 지정
    private LocalDateTime startTime;

    @Column(name = "end_time", nullable = false, columnDefinition = "TIMESTAMP(0)") // 밀리초 자릿수를 0으로 지정
    private LocalDateTime endTime;

    @Column(name = "applied_date", nullable = false)
    private LocalDate appliedDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private ApplicationStatus status = ApplicationStatus.PENDING; // 기본값 설정

    @Column(name = "applicant_count") // 신청자 포함 사용 인원 수
    private Integer applicantCount;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member applicant;

    public void updateStatus(ApplicationStatus status){
        this.status = status;
    }

}
