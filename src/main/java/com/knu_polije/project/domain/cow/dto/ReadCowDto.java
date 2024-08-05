package com.knu_polije.project.domain.cow.dto;

import com.knu_polije.project.domain.cow.entity.Cow;

public record ReadCowDto(
	Long cowId, Long cowNumber,
	String cowBreed, String cowWeight
) {
	public static ReadCowDto of(Cow cow) {
		return new ReadCowDto(
			cow.getId(), cow.getCowNumber(),
			cow.getCowBreed(), cow.getCowWeight()
		);
	}
}
