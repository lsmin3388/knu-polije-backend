package com.knu_polije.project.domain.member.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.knu_polije.project.domain.member.entity.Member;
import com.knu_polije.project.global.security.details.PrincipalDetails;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/member")
@RequiredArgsConstructor
public class MemberController {

	@GetMapping
	@PreAuthorize("isAuthenticated()")
	public Member read(@AuthenticationPrincipal PrincipalDetails principalDetails) {
		return principalDetails.getMember();
	}
}
