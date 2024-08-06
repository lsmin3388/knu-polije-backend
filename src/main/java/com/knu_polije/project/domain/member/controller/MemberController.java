package com.knu_polije.project.domain.member.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.knu_polije.project.domain.member.dto.ReadMemberDto;
import com.knu_polije.project.domain.member.entity.Member;
import com.knu_polije.project.global.security.details.PrincipalDetails;
import com.knu_polije.project.global.util.ApiUtil;
import com.knu_polije.project.global.util.ApiUtil.ApiSuccessResult;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class MemberController {

	@GetMapping("/member")
	@PreAuthorize("isAuthenticated()")
	public ResponseEntity<ApiSuccessResult<ReadMemberDto>> read(
		@AuthenticationPrincipal PrincipalDetails principalDetails
	) {
		ReadMemberDto response = ReadMemberDto.of(principalDetails.getMember());
		return ResponseEntity
			.status(HttpStatus.OK)
			.body(ApiUtil.success(HttpStatus.OK, response));
	}
}
