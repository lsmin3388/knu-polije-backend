package com.knu_polije.project.infra.detect.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.knu_polije.project.global.util.ApiUtil;
import com.knu_polije.project.global.util.ApiUtil.ApiSuccessResult;
import com.knu_polije.project.infra.detect.dto.BreedDetectResponse;
import com.knu_polije.project.infra.detect.dto.DetectResponse;
import com.knu_polije.project.infra.detect.dto.WeightDetectResponse;
import com.knu_polije.project.infra.detect.service.DetectService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/v1/detects")
@RequiredArgsConstructor
public class DetectController {
	private final DetectService detectService;

	@PostMapping("/breed")
	// @PreAuthorize("isAuthenticated()")
	public ResponseEntity<ApiSuccessResult<DetectResponse<BreedDetectResponse>>> detectBreed(
		@RequestPart("image") MultipartFile image
	) {
		DetectResponse<BreedDetectResponse> response = detectService.handleBreedDetection(image);
		return ResponseEntity
			.status(HttpStatus.OK)
			.body(ApiUtil.success(HttpStatus.OK, response));
	}

	@PostMapping("/weight")
	// @PreAuthorize("isAuthenticated()")
	public ResponseEntity<ApiSuccessResult<DetectResponse<WeightDetectResponse>>> detectWeight(
		@RequestParam("image") MultipartFile image
	) {
		DetectResponse<WeightDetectResponse> response = detectService.handleWeightDetection(image);
		return ResponseEntity
			.status(HttpStatus.OK)
			.body(ApiUtil.success(HttpStatus.OK, response));
	}
}
