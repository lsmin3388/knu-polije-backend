package com.knu_polije.project.domain.member.service;

import java.util.Optional;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.knu_polije.project.domain.member.entity.Member;
import com.knu_polije.project.domain.member.exception.MemberErrorCode;
import com.knu_polije.project.domain.member.repository.MemberRepository;
import com.knu_polije.project.global.exception.GlobalException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberService {
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    public Member findMemberByEmail(String email) {
        return memberRepository.findByEmail(email)
                .orElseThrow(() -> new GlobalException(MemberErrorCode.NOT_FOUND_MEMBER));
    }

    public boolean existMemberByEmail(String email) {
        return memberRepository.existsByEmail(email);
    }

    @Transactional
    public Member saveOrReturn(Member member) {
        Optional<Member> optionalMember = memberRepository.findByEmail(member.getEmail());
		return optionalMember.orElseGet(() -> memberRepository.save(member));
    }

}
