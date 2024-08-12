package com.knu_polije.project.infra.detect.dto;

import java.util.List;

import lombok.Getter;

@Getter
public class BreedDetectResponse {
	private String image_url;
	private List<ResultDto> results;

	@Getter
	public static class ResultDto {
		private String label;

		@Override
		public String toString() {
			return "ResultDto{" +
				"label='" + label + '\'' +
				'}';
		}
	}

	@Override
	public String toString() {
		return "BreedDetectResponse{" +
			"image_url='" + image_url + '\'' +
			", results=" + results +
			'}';
	}
}
