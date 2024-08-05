package com.knu_polije.project.domain.cow.service;

import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.knu_polije.project.domain.cow.dto.CreateOrUpdateCowDto;
import com.knu_polije.project.domain.cow.dto.ReadCowDto;
import com.knu_polije.project.domain.cow.entity.Cow;
import com.knu_polije.project.domain.cow.exception.CowErrorCode;
import com.knu_polije.project.domain.cow.repository.CowRepository;
import com.knu_polije.project.domain.member.entity.Member;
import com.knu_polije.project.global.exception.GlobalException;

import lombok.RequiredArgsConstructor;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CowService {
	private final CowRepository cowRepository;

	@Transactional
	public ReadCowDto updateCowBreed(Member member, Long cowNumber, String cowBreed) {
		Optional<Cow> optionalCow = cowRepository.findByMemberAndCowNumber(member, cowNumber);

		if (optionalCow.isPresent()) {
			Cow cow = optionalCow.get();
			cow.updateCowBreed(cowBreed);
			return ReadCowDto.of(cow);
		} else {
			throw new GlobalException(CowErrorCode.NOT_FOUND_COW);
		}
	}

	@Transactional
	public ReadCowDto updateCowWeight(Member member, Long cowNumber, String cowWeight) {
		Optional<Cow> optionalCow = cowRepository.findByMemberAndCowNumber(member, cowNumber);

		if (optionalCow.isPresent()) {
			Cow cow = optionalCow.get();
			cow.updateCowWeight(cowWeight);
			return ReadCowDto.of(cow);
		} else {
			throw new GlobalException(CowErrorCode.NOT_FOUND_COW);
		}
	}

	@Transactional
	public Cow getCowByCowNumberOrCreateCow(Member member, Long cowNumber) {
		Optional<Cow> optionalCow = cowRepository.findByMemberAndCowNumber(member, cowNumber);

		if (optionalCow.isEmpty()) {
			Cow cow = Cow.builder()
				.member(member)
				.cowNumber(cowNumber)
				.build();
			return cowRepository.save(cow);
		} else {
			return optionalCow.get();
		}
	}
}
