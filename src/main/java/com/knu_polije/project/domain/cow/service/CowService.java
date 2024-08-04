package com.knu_polije.project.domain.cow.service;

import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.knu_polije.project.domain.cow.dto.CreateOrUpdateCowDto;
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
	public Cow updateCow(Member member, Long cowNumber, CreateOrUpdateCowDto createOrUpdateCowDto) {
		Optional<Cow> optionalCow = cowRepository.findByMemberAndCowNumber(member, cowNumber);

		if (optionalCow.isPresent()) {
			Cow cow = optionalCow.get();
			cow.updateCow(createOrUpdateCowDto.cowBreed(), createOrUpdateCowDto.cowWeight());
			return cow;
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
