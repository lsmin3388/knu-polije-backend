package com.knu_polije.project.infra.detect.service;

import org.springframework.core.io.Resource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.client.MultipartBodyBuilder;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import com.knu_polije.project.domain.storage.exception.StorageErrorCode;
import com.knu_polije.project.domain.storage.service.StorageService;
import com.knu_polije.project.global.config.EndpointProperties;
import com.knu_polije.project.global.exception.GlobalException;
import com.knu_polije.project.infra.detect.dto.BreedDetectResponse;
import com.knu_polije.project.infra.detect.dto.WeightDetectResponse;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DetectService {
	private final RestTemplate restTemplate;
	private final StorageService storageService;
	private final EndpointProperties endpointProperties;

	public BreedDetectResponse sendImageToServerForBreedDetect(String fileName) {
		return sendImageToServer(fileName, endpointProperties.getBreedDetect(), BreedDetectResponse.class);
	}

	public WeightDetectResponse sendImageToServerForWeightDetect(String fileName) {
		return sendImageToServer(fileName, endpointProperties.getWeightDetect(), WeightDetectResponse.class);
	}

	private <T> T sendImageToServer(String fileName, String serverUrl, Class<T> responseType) {
		Resource resource = storageService.loadAsResource(fileName);
		if (resource == null) {
			throw new GlobalException(StorageErrorCode.NOT_FOUND_FILE);
		}

		MultipartBodyBuilder builder = new MultipartBodyBuilder();
		builder.part("image", resource);

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.MULTIPART_FORM_DATA);

		HttpEntity<MultiValueMap<String, HttpEntity<?>>> requestEntity = new HttpEntity<>(builder.build(), headers);

		return restTemplate.exchange(serverUrl, HttpMethod.POST, requestEntity, responseType).getBody();
	}
}
