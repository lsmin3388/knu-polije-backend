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
import com.knu_polije.project.infra.detect.dto.DetectResponse;
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
	public ResponseEntity<?> detectBreed(@RequestPart("image") MultipartFile image) {
		DetectResponse response = detectService.handleDetection(image, true);
		return ResponseEntity
			.status(HttpStatus.OK)
			.body(ApiUtil.success(HttpStatus.OK, response));
	}

	@PostMapping("/weight")
	// @PreAuthorize("isAuthenticated()")
	public ResponseEntity<?> detectWeight(@RequestParam("image") MultipartFile image) {
		DetectResponse response = detectService.handleDetection(image, false);
		return ResponseEntity
			.status(HttpStatus.OK)
			.body(ApiUtil.success(HttpStatus.OK, response));
	}
}
