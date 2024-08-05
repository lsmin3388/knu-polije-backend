package com.knu_polije.project.infra.detect.dto.flask;

import java.util.List;

import lombok.Getter;

@Getter
public class WeightDetectResponse {
	private String imageUrl;
	private List<ResultDto> results;
	private double totalWeight;

	@Getter
	public static class ResultDto {
		private List<Integer> bbox;
		private String label;
		private double weight;

	}
}
