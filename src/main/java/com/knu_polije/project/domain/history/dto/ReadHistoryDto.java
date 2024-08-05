package com.knu_polije.project.domain.history.dto;

import com.knu_polije.project.domain.history.entity.History;

public record ReadHistoryDto(
	Long historyId, String detectType,
	String inputImgName, String outputImgName,
	String outputData
) {

	public static ReadHistoryDto of(History history) {
		return new ReadHistoryDto(
			history.getId(), history.getDetectType().name(),
			history.getInputImgName(), history.getOutputImgName(),
			history.getOutputData()
		);
	}
}
