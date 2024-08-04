package com.knu_polije.project.infra.detect.dto;

import java.util.List;

import lombok.Getter;

@Getter
public class BreedDetectResponse {
	private String imageUrl;
	private List<ResultDto> results;

	@Getter
	public static class ResultDto {
		private String label;

	}
}
