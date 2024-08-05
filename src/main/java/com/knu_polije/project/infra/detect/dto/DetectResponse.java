package com.knu_polije.project.infra.detect.dto;

import com.knu_polije.project.domain.cow.dto.ReadCowDto;
import com.knu_polije.project.domain.history.dto.ReadHistoryDto;

public record DetectResponse(
	ReadCowDto cow,
	ReadHistoryDto history
) { }
