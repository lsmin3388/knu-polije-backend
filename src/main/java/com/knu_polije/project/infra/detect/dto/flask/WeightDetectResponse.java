package com.knu_polije.project.infra.detect.dto.flask;

import java.util.List;

import lombok.Getter;

@Getter
public class WeightDetectResponse {
	private String image_url;
	private List<ResultDto> results;
	private double total_weight;

	@Getter
	public static class ResultDto {
		private List<Integer> bbox;
		private String label;
		private double weight;

	}
}
