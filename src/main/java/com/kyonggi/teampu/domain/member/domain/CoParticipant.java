package com.kyonggi.teampu.domain.member.domain;

import com.kyonggi.teampu.domain.member.dto.CoParticipantRequest;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 공동 사용자 정보를 별도의 엔티티로 관리함
 */
@Embeddable
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CoParticipant {
    @Column(name = "name")
    private String name;

    @Column(name = "phone_number")
    private String phoneNumber;

    // DTO를 값 타입으로 변환하는 메서드
    public static CoParticipant from(CoParticipantRequest request) {
        return new CoParticipant(
                request.getName(),
                request.getPhoneNumber()
        );
    }

}