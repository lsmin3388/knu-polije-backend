package com.knu_polije.project.domain.history.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.knu_polije.project.domain.history.dto.ReadHistoryDto;
import com.knu_polije.project.domain.history.entity.History;
import com.knu_polije.project.domain.history.service.HistoryService;
import com.knu_polije.project.global.security.details.PrincipalDetails;
import com.knu_polije.project.global.util.ApiUtil;
import com.knu_polije.project.global.util.ApiUtil.ApiSuccessResult;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/history")
@RequiredArgsConstructor
public class HistoryController {
	private final HistoryService historyService;

	@GetMapping
	@PreAuthorize("isAuthenticated()")
	public ResponseEntity<ApiSuccessResult<List<ReadHistoryDto>>> readAllByMember(
		@AuthenticationPrincipal PrincipalDetails principalDetails
	) {
		List<ReadHistoryDto> histories = historyService.getHistoriesByMember(principalDetails.getMember());
		return ResponseEntity
			.status(HttpStatus.OK)
			.body(ApiUtil.success(HttpStatus.OK, histories));
	}


	// todo: remove this endpoint after testing
	@GetMapping("/all")
	public ResponseEntity<ApiSuccessResult<List<ReadHistoryDto>>> readAll(
		@AuthenticationPrincipal PrincipalDetails principalDetails
	) {
		List<ReadHistoryDto> histories = historyService.getAllHistories();
		return ResponseEntity
			.status(HttpStatus.OK)
			.body(ApiUtil.success(HttpStatus.OK, histories));
	}
}
