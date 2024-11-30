package com.kyonggi.teampu.domain.application.domain;

import com.kyonggi.teampu.domain.member.domain.CoParticipant;
import com.kyonggi.teampu.domain.member.domain.Member;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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

    @Column(name = "count_cp_with_applicant") // 신청자 포함 사용 인원 수
    private Integer countCpWithApplicant;

    @Column(name = "count_cp_only") // 신청자 제외 사용 인원 수
    private Integer countCpOnly;

    @ElementCollection // JPA에서 값 타입 컬렉션을 매핑할 때 사용하는 어노테이션
    @CollectionTable(
            name = "application_co_participants",
            joinColumns = @JoinColumn(name = "application_id")
    )
    private List<CoParticipant> coParticipants = new ArrayList<>();

    @Column(name = "privacy_agreement")
    private Boolean privacyAgreement;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    public void updateStatus(ApplicationStatus status){
        this.status = status;
    }

}
