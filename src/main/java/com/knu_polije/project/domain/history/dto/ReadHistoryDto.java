package com.knu_polije.project.domain.history.dto;

import java.time.LocalDateTime;
import java.util.List;

import com.knu_polije.project.domain.history.entity.History;

public record ReadHistoryDto(
	Long id,
	String inputFileName,
	String outputFileName,
	String data,
	LocalDateTime createdAt
) {
	public static ReadHistoryDto of(History history) {
		return new ReadHistoryDto(
			history.getId(),
			history.getInputFileName(),
			history.getOutputFileName(),
			history.getData(),
			history.getCreatedAt()
		);
	}

	public static List<ReadHistoryDto> of(List<History> histories) {
		return histories.stream()
			.map(ReadHistoryDto::of)
			.toList();
	}
}
