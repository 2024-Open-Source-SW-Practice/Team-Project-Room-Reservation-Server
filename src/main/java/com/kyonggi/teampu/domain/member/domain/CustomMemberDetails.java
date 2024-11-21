package com.kyonggi.teampu.domain.member.domain;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

public class CustomMemberDetails implements UserDetails {
    private final Member member;

    public CustomMemberDetails(Member member) {
        this.member = member;
    }

    public Member getMember() {
        return member;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    @Override
    public String getPassword() {
        return member.getPassword();
    }

    @Override
    public String getUsername() {
        return member.getEmail();
    }
}
