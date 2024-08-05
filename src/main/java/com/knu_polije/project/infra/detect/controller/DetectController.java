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

import com.knu_polije.project.domain.cow.dto.ReadCowDto;
import com.knu_polije.project.domain.cow.entity.Cow;
import com.knu_polije.project.domain.cow.service.CowService;
import com.knu_polije.project.domain.history.dto.CreateHistoryDto;
import com.knu_polije.project.domain.history.dto.ReadHistoryDto;
import com.knu_polije.project.domain.history.entity.DetectType;
import com.knu_polije.project.domain.history.service.HistoryService;
import com.knu_polije.project.domain.member.entity.Member;
import com.knu_polije.project.domain.member.repository.MemberRepository;
import com.knu_polije.project.domain.storage.service.StorageService;
import com.knu_polije.project.global.exception.GlobalException;
import com.knu_polije.project.global.security.details.PrincipalDetails;
import com.knu_polije.project.global.util.ApiUtil;
import com.knu_polije.project.global.util.ImageDownloadUtil;
import com.knu_polije.project.infra.detect.dto.DetectResponse;
import com.knu_polije.project.infra.detect.dto.flask.BreedDetectResponse;
import com.knu_polije.project.infra.detect.dto.flask.WeightDetectResponse;
import com.knu_polije.project.infra.detect.exception.DetectErrorCode;
import com.knu_polije.project.infra.detect.service.DetectService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/v1/detects")
@RequiredArgsConstructor
public class DetectController {
	private final StorageService storageService;
	private final DetectService detectService;
	private final HistoryService historyService;
	private final CowService cowService;

	private final MemberRepository memberRepository;

	@PostMapping("/breed")
	@PreAuthorize("isAuthenticated()")
	public ResponseEntity<?> detectBreed(
		@RequestPart("image") MultipartFile image,
		@RequestParam Long cowNumber,
		@AuthenticationPrincipal PrincipalDetails principalDetails
	) {
		/* Get Member Object By Authorization */
		Member member = principalDetails.getMember(); // memberRepository.findById(1L).get();

		/* Get Cow Object By Member And CowNumber */
		Cow cow = cowService.getCowByCowNumberOrCreateCow(member, cowNumber);

		/* Store Image in Local Repository */
		String storedFileName = storageService.storeImage(image);

		/* Send Image to Server for Breed Detect */
		BreedDetectResponse breedDetectResponse = detectService.sendImageToServerForBreedDetect(storedFileName);

		/* Process Response */
		String outputImageUrl = breedDetectResponse.getImageUrl();
		String outputData = breedDetectResponse.getResults().stream()
			.map(BreedDetectResponse.ResultDto::getLabel)
			.reduce((label1, label2) -> label1 + ", " + label2)
			.orElse("");

		String outputImgUri;
		try {
			outputImgUri = ImageDownloadUtil.downloadImage(outputImageUrl);
		} catch (IOException e) {
			throw new GlobalException(DetectErrorCode.FILE_DOWNLOAD_ERROR);
		}

		/* Create History */
		CreateHistoryDto createHistoryDto = new CreateHistoryDto(DetectType.BREED, storedFileName, outputImgUri, outputData);
		ReadHistoryDto readHistoryDto = historyService.createHistory(member, cow, createHistoryDto);

		/* Update Cow Data */
		ReadCowDto readCowDto = cowService.updateCowBreed(member, cowNumber, outputData);

		/* Response */
		DetectResponse response = new DetectResponse(readCowDto, readHistoryDto);
		return ResponseEntity
			.status(HttpStatus.OK)
			.body(ApiUtil.success(HttpStatus.OK, response));
	}

	@PostMapping("/weight")
	@PreAuthorize("isAuthenticated()")
	public ResponseEntity<?> detectWeight(
		@RequestParam("image") MultipartFile image,
		@RequestParam Long cowNumber,
		@AuthenticationPrincipal PrincipalDetails principalDetails
	) {
		/* Get Member Object By Authorization */
		Member member = principalDetails.getMember(); // memberRepository.findById(1L).get();

		/* Get Cow Object By Member And CowNumber */
		Cow cow = cowService.getCowByCowNumberOrCreateCow(member, cowNumber);

		/* Store Image in Local Repository */
		String storedFileName = storageService.storeImage(image);

		/* Send Image to Server for Weight Detect */
		WeightDetectResponse weightDetectResponse = detectService.sendImageToServerForWeightDetect(storedFileName);

		/* Process Response */
		String outputImageUrl = weightDetectResponse.getImageUrl();
		String outputData = String.valueOf(weightDetectResponse.getTotalWeight());

		String outputImgUri;
		try {
			outputImgUri = ImageDownloadUtil.downloadImage(outputImageUrl);
		} catch (IOException e) {
			throw new GlobalException(DetectErrorCode.FILE_DOWNLOAD_ERROR);
		}

		/* Create History */
		CreateHistoryDto createHistoryDto = new CreateHistoryDto(DetectType.WEIGHT, storedFileName, outputImgUri, outputData);
		ReadHistoryDto readHistoryDto = historyService.createHistory(member, cow, createHistoryDto);

		/* Update Cow Data */
		ReadCowDto readCowDto = cowService.updateCowWeight(member, cowNumber, outputData);

		/* Response */
		DetectResponse response = new DetectResponse(readCowDto, readHistoryDto);
		return ResponseEntity
			.status(HttpStatus.OK)
			.body(ApiUtil.success(HttpStatus.OK, response));
	}
}
