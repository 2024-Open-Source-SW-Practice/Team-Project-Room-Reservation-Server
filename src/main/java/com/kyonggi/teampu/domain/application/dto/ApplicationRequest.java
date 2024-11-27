package com.kyonggi.teampu.domain.application.dto;
import com.kyonggi.teampu.domain.application.domain.ApplicationStatus;
import com.kyonggi.teampu.domain.member.dto.CoParticipantRequest;
import lombok.AllArgsConstructor;
import lombok.Getter;

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
@AllArgsConstructor
public class ApplicationRequest {
    // 로그인 정보를 통해 이름, 학번, 전화번호, 이메일 받아옴
    private LocalDateTime startTime; // 시작 시간
    private LocalDateTime endTime; // 종료 시간
    private LocalDate appliedDate; // 날짜
    private List<CoParticipantRequest> coParticipants; // 공동 참여자 목록 (이름, 전화번호)
    private Boolean privacyAgreement; // 개인정보 동의 여부
    private ApplicationStatus status;

}