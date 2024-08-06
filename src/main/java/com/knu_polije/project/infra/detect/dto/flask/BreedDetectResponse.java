package com.knu_polije.project.infra.detect.dto.flask;

import java.util.List;

import lombok.Getter;

@Getter
public class BreedDetectResponse {
	private String image_url;
	private List<ResultDto> results;

	@Getter
	public static class ResultDto {
		private String label;

	}
}
