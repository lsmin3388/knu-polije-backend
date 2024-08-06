package com.knu_polije.project.infra.detect.service;

import java.io.IOException;

import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import com.knu_polije.project.domain.storage.exception.StorageErrorCode;
import com.knu_polije.project.domain.storage.service.StorageService;
import com.knu_polije.project.global.config.EndpointProperties;
import com.knu_polije.project.global.exception.GlobalException;
import com.knu_polije.project.global.util.ImageDownloadUtil;
import com.knu_polije.project.infra.detect.dto.BreedDetectResponse;
import com.knu_polije.project.infra.detect.dto.DetectResponse;
import com.knu_polije.project.infra.detect.dto.WeightDetectResponse;
import com.knu_polije.project.infra.detect.exception.DetectErrorCode;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class DetectService {
	private final RestTemplate restTemplate;
	private final StorageService storageService;
	private final EndpointProperties endpointProperties;

	public DetectResponse handleDetection(MultipartFile image, boolean isBreedDetection) {
		/* Store Image in Local Repository */
		String storedFileName = storageService.storeImage(image);

		/* Send Image to Server for Detection */
		String outputImageUrl;
		if (isBreedDetection) {
			BreedDetectResponse breedDetectResponse = sendImageToServerForBreedDetect(storedFileName);
			outputImageUrl = breedDetectResponse.getImage_url();
		} else {
			WeightDetectResponse weightDetectResponse = sendImageToServerForWeightDetect(storedFileName);
			outputImageUrl = weightDetectResponse.getImage_url();
		}

		/* Download the Output Image */
		String outputImgUri;
		try {
			outputImgUri = ImageDownloadUtil.downloadImage(outputImageUrl);
		} catch (IOException e) {
			throw new GlobalException(DetectErrorCode.FILE_DOWNLOAD_ERROR);
		}

		/* Response */
		return new DetectResponse(storedFileName, outputImgUri, null);
	}

	public BreedDetectResponse sendImageToServerForBreedDetect(String fileName) {
		return sendImageToServer(fileName, endpointProperties.getBreedDetect(), BreedDetectResponse.class);
	}

	public WeightDetectResponse sendImageToServerForWeightDetect(String fileName) {
		return sendImageToServer(fileName, endpointProperties.getWeightDetect(), WeightDetectResponse.class);
	}

	private <T> T sendImageToServer(String fileName, String serverUrl, Class<T> responseType) {
		try {
			Resource resource = storageService.loadAsResource(fileName);
			if (resource == null) {
				throw new GlobalException(StorageErrorCode.NOT_FOUND_FILE);
			}

			FileSystemResource fileSystemResource = new FileSystemResource(resource.getFile());
			MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
			body.add("image", fileSystemResource);

			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.MULTIPART_FORM_DATA);

			HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);

			return restTemplate.exchange(serverUrl, HttpMethod.POST, requestEntity, responseType).getBody();
		} catch (IOException exception) {
			log.error("Failed to send image to server", exception);
			throw new GlobalException(DetectErrorCode.FILE_REQUEST_FAILED);
		}
	}
}
