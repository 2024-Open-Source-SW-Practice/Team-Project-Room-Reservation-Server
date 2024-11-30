package com.kyonggi.teampu.domain.application.service;

import com.kyonggi.teampu.domain.application.domain.ApplicationStatus;
import com.kyonggi.teampu.domain.application.dto.ApplicationRequest;
import com.kyonggi.teampu.domain.application.dto.ApplicationResponse;
import com.kyonggi.teampu.domain.application.dto.MainPageResponse;
import com.kyonggi.teampu.domain.application.repository.ApplicationRepository;
import com.kyonggi.teampu.domain.member.domain.Member;
import com.kyonggi.teampu.domain.member.domain.MemberType;
import com.kyonggi.teampu.domain.member.dto.CoParticipantRequest;
import com.kyonggi.teampu.domain.member.repository.MemberRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@ActiveProfiles("test")
@DisplayName("ApplicationService 테스트")
public class ApplicationServiceTest {

    @Autowired
    private ApplicationService applicationService;
    @Autowired
    private ApplicationRepository applicationRepository;
    @Autowired
    private MemberRepository memberRepository;
    private Member member;

    @BeforeEach
    void setUp() {
        // 빌더 패턴을 사용해 Member 객체 생성
        member = Member.builder()
                .loginId("loginId1")
                .password("password1")
                .name("member1")
                .college("소프트웨어경영대학")
                .department("AI컴퓨터공학부")
                .phoneNumber("010-1234-5678")
                .email("member1@kyonggi.ac.kr")
                .type(MemberType.UNDERGRADUATE)
                .build();
        memberRepository.save(member);
    }

    @Test
    @DisplayName("신청서를 테스트한다.")
    void testCreateApplication() {
        // ApplicationRequest 생성
        ApplicationRequest applicationRequest = new ApplicationRequest(
                LocalDateTime.now(),
                LocalDateTime.now(),
                LocalDate.of(2024, 12, 25), // appliedDate
                List.of(new CoParticipantRequest("Jane Doe", "010-2345-6789")), // coParticipants
                true, // privacyAgreement
                ApplicationStatus.PENDING // status
        );

        // 신청서 생성
        applicationService.createApplication(applicationRequest, member);

        // 생성된 신청서 조회
        assertNotNull(applicationRepository.findAll());
        assertEquals(1, applicationRepository.findAll().size());
    }

    @Test
    @DisplayName("존재하지 않는 신청을 조회할 경우, 예외를 반환한다.")
    void testGetNonExistentApplication() {
        // 존재하지 않는 신청서 ID로 조회 시도
        Long nonExistentId = 999L;

        // 예외 발생 확인
        assertThrows(IllegalArgumentException.class, () -> applicationService.getDetailApplication(nonExistentId));
    }

    @Test
    @DisplayName("신청서를 조회한다.")
    void testGetDetailApplication() {
        // ApplicationRequest 생성 후 신청서 생성
        ApplicationRequest applicationRequest = new ApplicationRequest(
                LocalDateTime.now(),
                LocalDateTime.now(),
                LocalDate.of(2024, 12, 25),
                List.of(new CoParticipantRequest("Jane Doe", "010-2345-6789")),
                true,
                ApplicationStatus.PENDING
        );
        applicationService.createApplication(applicationRequest, member);

        // 방금 생성된 신청서 조회
        Long applicationId = applicationRepository.findAll().get(0).getId();
        ApplicationResponse response = applicationService.getDetailApplication(applicationId);

        // 응답 값 검증
        assertNotNull(response);
        assertEquals("2024-12-25", response.getAppliedDate().toString());
        assertEquals(member.getName(), response.getName());
    }

    @Test
    @DisplayName("신청서를 수정한다.")
    void testUpdateApplication() {
        // 기존 신청 생성
        ApplicationRequest request = new ApplicationRequest(
                LocalDateTime.now(),
                LocalDateTime.now(),
                LocalDate.of(2024, 12, 25),
                List.of(new CoParticipantRequest("Jane Doe", "010-2345-6789")),
                true,
                ApplicationStatus.PENDING
        );
        applicationService.createApplication(request, member);
        Long applicationId = applicationRepository.findAll().get(0).getId();

        // 수정 요청 생성 (공동 참여자와 날짜만 수정)
        ApplicationRequest updateRequest = new ApplicationRequest(
                LocalDateTime.now(),
                LocalDateTime.now(),
                LocalDate.of(2025, 1, 15),
                List.of(new CoParticipantRequest("John Doe", "010-9876-5432")),
                true,
                ApplicationStatus.PENDING
        );

        // 신청 수정
        applicationService.updateApplication(applicationId, member, updateRequest);

        // 수정된 신청서 확인
        ApplicationResponse response = applicationService.getDetailApplication(applicationId);
        assertEquals("2025-01-15", response.getAppliedDate().toString());
        assertEquals(2, response.getCountCpOnly() + 1); // 신청자 본인 + 공동 참여자
        assertEquals(member.getName(), response.getName());
    }

    @Test
    @DisplayName("달력 상의 신청 정보를 확인한다.")
    void testGetCalendarData() {
        // 여러 신청 생성
        applicationService.createApplication(
                new ApplicationRequest(
                        LocalDateTime.now(),
                        LocalDateTime.now(),
                        LocalDate.of(2024, 12, 10), List.of(),
                        true,
                        ApplicationStatus.PENDING
                ),
                member
        );
        applicationService.createApplication(
                new ApplicationRequest(
                        LocalDateTime.now(),
                        LocalDateTime.now(),
                        LocalDate.of(2024, 12, 15), List.of(),
                        true,
                        ApplicationStatus.PENDING
                ),
                member
        );

        // 달력 데이터 조회
        MainPageResponse.CalendarResponseDTO calendarData = applicationService.getCalendarData(2024, 12);

        // 검증
        assertEquals(2024, calendarData.getYear());
        assertEquals(12, calendarData.getMonth());

        // 12월 10일과 15일 예약 개수 확인
        List<MainPageResponse.DayDTO> days = calendarData.getDays();
        assertEquals(1, days.stream().filter(day -> day.getDay() == 10).findFirst().get().getReservationCount());
        assertEquals(1, days.stream().filter(day -> day.getDay() == 15).findFirst().get().getReservationCount());
    }

    @Test
    @DisplayName("존재하지 않는 신청을 수정할 경우, 예외를 반환한다.")
    void testUpdateNonExistentApplication() {
        // 존재하지 않는 신청서 ID로 수정 시도
        Long nonExistentId = 999L;

        ApplicationRequest updateRequest = new ApplicationRequest(
                LocalDateTime.now(),
                LocalDateTime.now(),
                LocalDate.of(2025, 1, 15),
                List.of(new CoParticipantRequest("John Doe", "010-9876-5432")),
                true,
                ApplicationStatus.PENDING
        );

        // 예외 발생 확인
        assertThrows(IllegalArgumentException.class, () -> applicationService.updateApplication(nonExistentId, member, updateRequest));
    }

    @Test
    void testDeleteApplication() {
        // 신청서 생성
        ApplicationRequest applicationRequest = new ApplicationRequest(
                LocalDateTime.now(),
                LocalDateTime.now(),
                LocalDate.of(2024, 12, 25),
                List.of(new CoParticipantRequest("Jane Doe", "010-2345-6789")),
                true,
                ApplicationStatus.PENDING
        );
        applicationService.createApplication(applicationRequest, member);

        Long applicationId = applicationRepository.findAll().get(0).getId();

        // 신청서 삭제
        applicationService.deleteApplication(applicationId);

        // 삭제된 신청서 조회 시 예외 발생 확인
        assertThrows(IllegalArgumentException.class, () -> applicationService.getDetailApplication(applicationId));
    }
}
