package com.kyonggi.teampu.domain.member.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
@AllArgsConstructor
public class CoParticipantRequest {
    // CoParticipantRequest: 공동 참여자 정보를 서버로 전송할 때 사용하는 객체
    private String name; // 이름
    private String phoneNumber; // 전화번호

    // CoParticipantRequest에서 이름과 전화번호를 리스트로 변환하려면, CoParticipantRequest 클래스에 변환 메서드를 추가
    public static List<String> toNameList(List<CoParticipantRequest> coParticipantRequests) {
        return coParticipantRequests.stream()
                .map(CoParticipantRequest::getName)
                .toList();
    }

}
