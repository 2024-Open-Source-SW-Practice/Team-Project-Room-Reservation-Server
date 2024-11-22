package com.kyonggi.teampu.domain.auth.service;

import com.kyonggi.teampu.domain.auth.domain.CustomMemberDetails;
import com.kyonggi.teampu.domain.member.domain.Member;
import com.kyonggi.teampu.domain.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CustomMemberDetailsService implements UserDetailsService {
	private final MemberRepository memberRepository;

	@Override
	public UserDetails loadUserByUsername(String loginId) throws UsernameNotFoundException {
		Optional<Member> member = memberRepository.findByLoginId(loginId);

		if (member.isEmpty()) {
			throw new UsernameNotFoundException("해당 유저를 찾을 수 없습니다: " + loginId);
			//UserDetails에 담아서 return하면 AutneticationManager가 검증 함
		}

		System.out.println("**************Found user***************");
		System.out.println("   loginId : " + member.get().getLoginId());
		System.out.println("   password : " + member.get().getPassword());
		System.out.println("***************************************");

		return new CustomMemberDetails(member.get());
	}
}