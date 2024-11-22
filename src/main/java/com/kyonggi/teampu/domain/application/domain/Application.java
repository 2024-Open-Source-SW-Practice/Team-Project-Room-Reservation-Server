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

    @Column(name = "start_time", nullable = false)
    private LocalDateTime startTime;

    @Column(name = "end_time", nullable = false)
    private LocalDateTime endTime;

    @Column(name = "applied_date", nullable = false)
    private LocalDate appliedDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private ApplicationStatus status;

    @Column(name = "participant_count")
    private Integer participantCount;

    @ElementCollection // JPA에서 값 타입 컬렉션을 매핑할 때 사용하는 어노테이션
    @Column(name = "co_participant_names")
    private List<String> coParticipantNames = new ArrayList<>();

    @Column(name = "privacy_agreement")
    private Boolean privacyAgreement;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;
}
