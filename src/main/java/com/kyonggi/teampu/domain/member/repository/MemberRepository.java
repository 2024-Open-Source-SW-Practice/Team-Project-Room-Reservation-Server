package com.kyonggi.teampu.domain.member.repository;

import com.kyonggi.teampu.domain.member.domain.Member;
import org.springframework.data.repository.Repository;

public interface MemberRepository extends Repository<Member, Long> {
    Member save(Member member);
    Member findByLoginId(String loginId);
    Member findById(Long id);
    void delete(Member member);
}
