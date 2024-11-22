package com.kyonggi.teampu.domain.member.repository;

import com.kyonggi.teampu.domain.member.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long> {
}
