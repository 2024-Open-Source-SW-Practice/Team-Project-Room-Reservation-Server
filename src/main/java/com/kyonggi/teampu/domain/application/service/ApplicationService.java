package com.kyonggi.teampu.domain.application.service;

import com.kyonggi.teampu.domain.application.domain.Application;
import com.kyonggi.teampu.domain.application.dto.ApplicationRequest;
import com.kyonggi.teampu.domain.application.dto.ApplicationResponse;
import com.kyonggi.teampu.domain.application.dto.MainPageResponse;
import com.kyonggi.teampu.domain.application.repository.ApplicationRepository;
import com.kyonggi.teampu.domain.member.domain.CoParticipant;
import com.kyonggi.teampu.domain.member.domain.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.kyonggi.teampu.domain.application.dto.ApplicationResponse.fromEntity;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ApplicationService {
    private final ApplicationRepository applicationRepository;

    @Transactional
    public void createApplication(ApplicationRequest applicationRequest, Member member) {
        // GET 메서드를 별도로 구현할 예정이라 void 처리

        /**
         * 1. 사용자의 정보(이름, 학번, 전화번호, 이메일) 추출
         * 2. 사용자가 직접 정보 입력(날짜, 명단: 이름, 전화번호, 개인정보 동의)
         * 3. 사용 인원 계산
         */

        List<CoParticipant> coParticipants = applicationRequest.getCoParticipants().stream()
                .map(CoParticipant::from)
                .collect(Collectors.toList());

        // Application.builder()를 사용하여 로그인한 사용자 정보와 입력받은 정보를 결합
        Application application = Application.builder()
                .member(member) // Member 객체만
                .appliedDate(applicationRequest.getAppliedDate()) // 날짜
                .startTime(applicationRequest.getStartTime().withSecond(0).withNano(0)) // 시작 시간
                .endTime(applicationRequest.getEndTime().withSecond(0).withNano(0)) // 종료 시간
                .coParticipants(coParticipants) // 명단(이름, 전화번호)
                .countCpWithApplicant(applicationRequest.getCoParticipants().size()+1) // 신청자 포함 사용 인원 수
                .countCpOnly(applicationRequest.getCoParticipants().size()) // 신청자 제외 사용 인원 수
                .privacyAgreement(applicationRequest.getPrivacyAgreement()) // 개인정보 동의
                .status(applicationRequest.getStatus())
                .startTime(LocalDateTime.now())
                .endTime(LocalDateTime.now())
                .build();

        applicationRepository.save(application);
    }

    @Transactional
    public void deleteApplication(Long id){
        Application application = applicationRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("신청서를 찾을 수 없습니다."));

        applicationRepository.delete(application);
    }

    @Transactional
    public ApplicationResponse getDetailApplication(Long id){
        Application application = applicationRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("신청서를 찾을 수 없습니다."));

        return fromEntity(application);

    }

    @Transactional
    public void updateApplication(Long id, Member member, ApplicationRequest applicationRequest){
        Application application = applicationRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("신청서를 찾을 수 없습니다."));

        // PATCH를 사용했기 떄문에 널이 아닌 필드만 업데이트 하는 로직이 필요함
        LocalDate appliedDate = applicationRequest.getAppliedDate() != null ?
                applicationRequest.getAppliedDate() : application.getAppliedDate();

        List<CoParticipant> coParticipants = applicationRequest.getCoParticipants() != null ?
                applicationRequest.getCoParticipants().stream()
                        .map(CoParticipant::from)
                        .collect(Collectors.toList()) :
                application.getCoParticipants();

        // Builder를 사용한 업데이트
        Application updatedApplication = Application.builder()
                .id(application.getId())
                .member(application.getMember())  // 기존 member 정보 유지
                .appliedDate(appliedDate) // 날짜
                .startTime(applicationRequest.getStartTime().withSecond(0).withNano(0)) // 시작 시간
                .endTime(applicationRequest.getEndTime().withSecond(0).withNano(0)) // 종료 시간
                .coParticipants(coParticipants) // 공동 참여자 명단(이름, 전화번호)
                .countCpWithApplicant(applicationRequest.getCoParticipants().size()+1) // 신청자 포함 사용 인원 수
                .countCpOnly(applicationRequest.getCoParticipants().size()) // 신청자 제외 사용 인원 수
                .privacyAgreement(application.getPrivacyAgreement())
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
}