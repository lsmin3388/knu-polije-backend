package com.knu_polije.project.domain.cow.service;

import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.knu_polije.project.domain.cow.dto.CreateOrUpdateCowDto;
import com.knu_polije.project.domain.cow.entity.Cow;
import com.knu_polije.project.domain.cow.repository.CowRepository;
import com.knu_polije.project.domain.member.entity.Member;

import lombok.RequiredArgsConstructor;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CowService {
	private final CowRepository cowRepository;

	public Cow createOrUpdateCow(Member member, Long cowNumber, CreateOrUpdateCowDto createOrUpdateCowDto) {
		Optional<Cow> optionalCow = cowRepository.findByMemberAndCowNumber(member, cowNumber);

		if (optionalCow.isPresent()) {
			Cow cow = optionalCow.get();
			cow.updateCow(createOrUpdateCowDto.cowBreed(), createOrUpdateCowDto.cowWeight());
			return cow;
		} else {
			Cow newCow = Cow.builder()
				.cowNumber(cowNumber)
				.member(member)
				.cowBreed(createOrUpdateCowDto.cowBreed())
				.cowWeight(createOrUpdateCowDto.cowWeight())
				.build();

			return cowRepository.save(newCow);
		}
	}



}
