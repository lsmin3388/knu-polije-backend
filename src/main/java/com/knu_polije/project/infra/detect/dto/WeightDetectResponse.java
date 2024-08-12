package com.knu_polije.project.infra.detect.dto;

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

		@Override
		public String toString() {
			return "ResultDto{" +
				"bbox=" + bbox +
				", label='" + label + '\'' +
				", weight=" + weight +
				'}';
		}
	}

	@Override
	public String toString() {
		return "WeightDetectResponse{" +
			"image_url='" + image_url + '\'' +
			", results=" + results +
			", total_weight=" + total_weight +
			'}';
	}
}
