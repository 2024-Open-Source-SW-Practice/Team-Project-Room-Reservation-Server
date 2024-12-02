package com.kyonggi.teampu.domain.member.repository;

import com.kyonggi.teampu.domain.member.domain.Member;

import org.springframework.data.repository.Repository;

import java.util.Optional;

public interface MemberRepository extends Repository<Member, Long> {
    Member save(Member member);

    Optional<Member> findByLoginId(String loginId);

    Optional<Member> findById(Long id);

    Optional<Member> findByNameAndPhoneNumber(String name, String phoneNumber);

    void delete(Member member);
}
