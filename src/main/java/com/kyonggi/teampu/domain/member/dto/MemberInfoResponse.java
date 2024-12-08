package com.kyonggi.teampu.domain.member.dto;

import com.kyonggi.teampu.domain.member.domain.Member;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class MemberInfoResponse {
    private final Long userId;
    private final String loginId;
    private final String name;
    private final String college;
    private final String department;
    private final String phoneNumber;
    private final String email;
    private final String type;
    private final boolean isAdmin;

    public MemberInfoResponse(Member member) {
        this.userId = member.getId();
        this.loginId = member.getLoginId();
        this.name = member.getName();
        this.college = member.getCollege();
        this.department = member.getDepartment();
        this.phoneNumber = member.getPhoneNumber();
        this.email = member.getEmail();
        this.type = member.getType().name();
        this.isAdmin = member.getIsAdmin();
    }

}
