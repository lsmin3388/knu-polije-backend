package com.knu_polije.project.domain.member.dto;

import com.knu_polije.project.domain.member.entity.Member;

public record ReadMemberDto(
	Long id,
	String email,
	String role,
	String name
) {
	public static ReadMemberDto of(Member member) {
		return new ReadMemberDto(
			member.getId(),
			member.getEmail(),
			member.getRole().name(),
			member.getName()
		);
	}
}
