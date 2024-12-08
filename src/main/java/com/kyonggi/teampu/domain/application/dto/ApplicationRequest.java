package com.kyonggi.teampu.domain.application.dto;

import com.kyonggi.teampu.domain.application.domain.Application;
import com.kyonggi.teampu.domain.member.domain.Member;
import com.kyonggi.teampu.domain.member.dto.CoApplicantRequest;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 예약 제출 페이지에서
 * 사용자의 정보(이름, 학번, 전화번호, 이메일)를 자동으로 받아옴 >> 로그인 정보에서 가져옴
 * 날짜 >> 사용자가 직접 입력
 * 공동 참여자 목록(이름, 전화번호) >> 사용자가 직접 입력
 * 인원 >> 공동 참여자 목록 + 1으로 신청자 포함한 사용 인원 자동 계산
 * 개인정보 동의 여부 >> 사용자가 직접 체크
 */

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ApplicationRequest {
    // 로그인 정보를 통해 이름, 학번, 전화번호, 이메일 받아옴
    private LocalDateTime startTime; // 시작 시간
    private LocalDateTime endTime; // 종료 시간
    private List<CoApplicantRequest> coApplicants; // 공동 참여자 목록 (이름, 전화번호)

    public Application toEntity(Member member) {
        return Application.builder()
                .appliedDate(LocalDate.now()) // 날짜
                .startTime(startTime.withSecond(0).withNano(0)) // 시작 시간
                .endTime(endTime.withSecond(0).withNano(0)) // 종료 시간
                .applicantCount(coApplicants.size() + 1) // 신청자 포함 사용 인원 수
                .applicant(member) // Member 객체만
                .build();
    }
}