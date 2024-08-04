package com.knu_polije.project.infra.detect.controller;

import java.io.IOException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.knu_polije.project.domain.cow.entity.Cow;
import com.knu_polije.project.domain.cow.service.CowService;
import com.knu_polije.project.domain.history.dto.CreateHistoryDto;
import com.knu_polije.project.domain.history.entity.DetectType;
import com.knu_polije.project.domain.history.service.HistoryService;
import com.knu_polije.project.domain.member.entity.Member;
import com.knu_polije.project.domain.member.repository.MemberRepository;
import com.knu_polije.project.domain.storage.service.StorageService;
import com.knu_polije.project.global.exception.GlobalException;
import com.knu_polije.project.global.security.details.PrincipalDetails;
import com.knu_polije.project.global.util.ApiUtil;
import com.knu_polije.project.global.util.ImageDownloadUtil;
import com.knu_polije.project.infra.detect.dto.BreedDetectResponse;
import com.knu_polije.project.infra.detect.dto.WeightDetectResponse;
import com.knu_polije.project.infra.detect.exception.StorageErrorCode;
import com.knu_polije.project.infra.detect.service.DetectService;

import lombok.RequiredArgsConstructor;


@RestController
@RequestMapping("/api/v1/detects")
@RequiredArgsConstructor
public class DetectController {
	private final StorageService storageService;
	private final DetectService detectService;
	private final HistoryService historyService;
	private final CowService cowService;

	private final MemberRepository memberRepository;
	// @PostMapping("/images")
	// public ResponseEntity<ApiUtil.ApiSuccessResult<String>> uploadImage(
	// 	@RequestPart MultipartFile image
	// ) {

	@PostMapping("/breed")
	// @PreAuthorize("isAuthenticated()")
	public ResponseEntity<?> detectBreed(
		@RequestPart("image") MultipartFile image,
		@RequestParam Long cowNumber
		// @AuthenticationPrincipal PrincipalDetails principalDetails
	) {
		// Member member = principalDetails.getMember();
		Member member = memberRepository.findById(1L).get();
		Cow cow = cowService.getCowByCowNumberOrCreateCow(member, cowNumber);

		String storedFileName = storageService.storeImage(image);
		BreedDetectResponse response = detectService.sendImageToServerForBreedDetect(storedFileName);

		String outputImageUrl = response.getImageUrl();
		String outputData = response.getResults().stream()
			.map(BreedDetectResponse.ResultDto::getLabel)
			.reduce((label1, label2) -> label1 + ", " + label2)
			.orElse("");

		String outputImgUri;
		try {
			outputImgUri = ImageDownloadUtil.downloadImage(outputImageUrl);
		} catch (IOException e) {
			throw new GlobalException(StorageErrorCode.FILE_DOWNLOAD_ERROR);
		}

		CreateHistoryDto createHistoryDto = new CreateHistoryDto(DetectType.BREED, storedFileName, outputImgUri, outputData);
		historyService.createHistory(member, cow, createHistoryDto);

		return ResponseEntity
			.status(HttpStatus.OK)
			.body(ApiUtil.success(HttpStatus.OK, response));
	}

	@PostMapping("/weight")
	@PreAuthorize("isAuthenticated()")
	public ResponseEntity<?> detectWeight(@RequestParam("image") MultipartFile image,
		@RequestParam Long cowId,
		@AuthenticationPrincipal PrincipalDetails principalDetails) {
		Member member = principalDetails.getMember();
		// Cow cow = cowService.getCowById(cowId);
		Cow cow = null;

		String storedFileName = storageService.storeImage(image);
		WeightDetectResponse response = detectService.sendImageToServerForWeightDetect(storedFileName);

		String outputImageUrl = response.getImageUrl();
		String outputData = String.valueOf(response.getTotalWeight());

		String outputImgUri;
		try {
			outputImgUri = ImageDownloadUtil.downloadImage(outputImageUrl);
		} catch (IOException e) {
			throw new GlobalException(StorageErrorCode.FILE_DOWNLOAD_ERROR);
		}

		CreateHistoryDto createHistoryDto = new CreateHistoryDto(DetectType.WEIGHT, storedFileName, outputImgUri, outputData);
		historyService.createHistory(member, cow, createHistoryDto);

		return ResponseEntity
			.status(HttpStatus.OK)
			.body(ApiUtil.success(HttpStatus.OK, response));
	}
}
