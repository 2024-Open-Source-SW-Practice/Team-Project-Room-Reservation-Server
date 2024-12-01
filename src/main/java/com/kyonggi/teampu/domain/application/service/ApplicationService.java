package com.kyonggi.teampu.domain.application.service;

import com.kyonggi.teampu.domain.application.domain.Application;
import com.kyonggi.teampu.domain.application.dto.ApplicationRequest;
import com.kyonggi.teampu.domain.application.dto.ApplicationResponse;
import com.kyonggi.teampu.domain.application.dto.MainPageResponse;
import com.kyonggi.teampu.domain.application.repository.ApplicationRepository;
import com.kyonggi.teampu.domain.coApplicant.domain.CoApplicant;
import com.kyonggi.teampu.domain.coApplicant.repository.CoApplicantRepository;
import com.kyonggi.teampu.domain.member.domain.Member;
import com.kyonggi.teampu.domain.member.dto.CoApplicantRequest;
import com.kyonggi.teampu.domain.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.kyonggi.teampu.domain.application.dto.ApplicationResponse.fromEntity;
import static com.kyonggi.teampu.global.exception.ErrorCode.*;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ApplicationService {
    private final ApplicationRepository applicationRepository;
    private final CoApplicantRepository coApplicantRepository;
    private final MemberRepository memberRepository;

    @Transactional
    public void createApplication(ApplicationRequest applicationRequest, Member member) {
        Application application = applicationRequest.toEntity(member);
        applicationRepository.save(application);

        applicationRequest.getCoApplicants()
                .stream()
                .map(this::findMemberByNameAndPhoneNumber)
                .map(coApplicantMember -> CoApplicant.builder()
                        .member(coApplicantMember)
                        .application(application)
                        .build()
                ).forEach(coApplicantRepository::save);
    }

    @Transactional
    public void deleteApplication(Long id) {
        Application application = applicationRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException(APPLICATION_NOT_FOUND.getMessage()));

        applicationRepository.delete(application);
    }

    public ApplicationResponse getDetailApplication(Long id) {
        Application application = applicationRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException(APPLICATION_NOT_FOUND.getMessage()));
        List<Member> coApplicants = coApplicantRepository.findCoApplicantsByApplicationId(id);

        return fromEntity(application, coApplicants);
    }

    @Transactional
    public void updateApplication(Long id, Member member, ApplicationRequest applicationRequest) {
        Application application = applicationRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException(APPLICATION_NOT_FOUND.getMessage()));

        if (!application.getStatus().isUpdatable()) {
            throw new IllegalStateException(APPLICATION_ALREADY_APPROVED.getMessage());
        }

        Application updatedApplication = Application.builder()
                .id(application.getId())
                .applicant(application.getApplicant())  // 기존 member 정보 유지
                .appliedDate(applicationRequest.getAppliedDate()) // 날짜
                .startTime(applicationRequest.getStartTime().withSecond(0).withNano(0)) // 시작 시간
                .endTime(applicationRequest.getEndTime().withSecond(0).withNano(0)) // 종료 시간
                .applicantCount(application.getApplicantCount()) // 신청자 포함 사용 인원 수
                .status(application.getStatus()) // 상태
                .build();

        applicationRepository.save(updatedApplication);
    }

    public MainPageResponse.CalendarResponseDTO getCalendarData(Integer year, Integer month) {
        LocalDate now = LocalDate.now();

        int targetYear = (year != null) ? year : now.getYear();
        int targetMonth = (month != null) ? month : now.getMonthValue();

        YearMonth yearMonth = YearMonth.of(targetYear, targetMonth); // 선택한 월의 마지막 일
        LocalDate startDate; // 시작일

        if (targetYear == now.getYear() && targetMonth == now.getMonthValue()) {
            //선택한 월이 현재 월과 같다면 시작일을 지금으로
            startDate = now;
        } else if (targetYear < now.getYear() || (targetYear == now.getYear() && targetMonth < now.getMonthValue())) {
            // 과거 월이면 빈 리스트
            return new MainPageResponse.CalendarResponseDTO(targetYear, targetMonth, new ArrayList<>());
        } else {
            // 미래 월이면 1일부터
            startDate = yearMonth.atDay(1);
        }

        LocalDate endDate = yearMonth.atEndOfMonth();

        // 예약 정보 DB 조회
        List<Application> applications = applicationRepository.findByAppliedDateBetween(startDate, endDate);

        Map<LocalDate, Integer> reservationCountByDate = new HashMap<>();

        // reservationCountByDate에 일별로 몇개의 예약이 있는지 확인 후 put
        for (Application app : applications) {
            LocalDate appDate = app.getAppliedDate();
            int currentCount = reservationCountByDate.getOrDefault(appDate, 0);
            reservationCountByDate.put(appDate, currentCount + 1);
        }

        // 날짜별 DTO 생성
        List<MainPageResponse.DayDTO> days = new ArrayList<>();
        for (int day = startDate.getDayOfMonth(); day <= endDate.getDayOfMonth(); day++) {
            LocalDate date = LocalDate.of(targetYear, targetMonth, day);
            int count = reservationCountByDate.getOrDefault(date, 0);
            days.add(new MainPageResponse.DayDTO(day, count));
        }

        return new MainPageResponse.CalendarResponseDTO(targetYear, targetMonth, days);
    }

    private Member findMemberByNameAndPhoneNumber(CoApplicantRequest coApplicantRequest) {
        return memberRepository.findByNameAndPhoneNumber(
                coApplicantRequest.getName(),
                coApplicantRequest.getPhoneNumber()
        ).orElseThrow(() -> new IllegalArgumentException(MEMBER_NOT_FOUND_BY_NAME_AND_PHONE_NUMBER.getMessage()));
    }
}