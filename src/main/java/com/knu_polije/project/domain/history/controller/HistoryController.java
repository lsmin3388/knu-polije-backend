package com.knu_polije.project.domain.history.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.knu_polije.project.domain.history.dto.ReadHistoryDto;
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

	@GetMapping()
	@PreAuthorize("isAuthenticated()")
	public ResponseEntity<ApiSuccessResult<List<ReadHistoryDto>>> readMyAll(
		@AuthenticationPrincipal PrincipalDetails principalDetails
	) {
		List<ReadHistoryDto> response = historyService.readHistories(principalDetails.getMember());
		return ResponseEntity
			.status(HttpStatus.OK)
			.body(ApiUtil.success(HttpStatus.OK, response));
	}
}
