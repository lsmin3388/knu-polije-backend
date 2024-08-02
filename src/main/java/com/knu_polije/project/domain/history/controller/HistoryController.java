package com.knu_polije.project.domain.history.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.knu_polije.project.domain.history.entity.History;
import com.knu_polije.project.domain.history.service.HistoryService;
import com.knu_polije.project.global.security.details.PrincipalDetails;
import com.knu_polije.project.global.util.ApiUtil;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/history")
@RequiredArgsConstructor
public class HistoryController {
	private final HistoryService historyService;

	@PostMapping
	@PreAuthorize("isAuthenticated()")
	public ResponseEntity<ApiUtil.ApiSuccessResult<List<History>>> readAllByMember(
		@AuthenticationPrincipal PrincipalDetails principalDetails
	) {
		List<History> histories = historyService.getHistoriesByMember(principalDetails.getMember());
		return ResponseEntity
			.status(HttpStatus.OK)
			.body(ApiUtil.success(HttpStatus.OK, histories));
	}
}
