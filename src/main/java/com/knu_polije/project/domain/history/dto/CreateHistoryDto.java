package com.knu_polije.project.domain.history.dto;

import com.knu_polije.project.domain.history.entity.DetectType;

public record CreateHistoryDto(
	DetectType detectType,
	String resultValue,
	String resultPath
) { }
