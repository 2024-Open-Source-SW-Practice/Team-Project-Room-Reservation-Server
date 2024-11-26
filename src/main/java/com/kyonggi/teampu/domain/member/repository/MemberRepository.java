package com.kyonggi.teampu.domain.member.repository;

import com.kyonggi.teampu.domain.member.domain.Member;

import org.springframework.data.repository.Repository;

import java.util.Optional;

public interface MemberRepository extends Repository<Member, Long> {
    Member save(Member member);

    Optional<Member> findByLoginId(String loginId);

    Optional<Member> findById(Long id);

    void delete(Member member);


}
