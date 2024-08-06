package com.knu_polije.project.domain.history.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.knu_polije.project.domain.cow.entity.Cow;
import com.knu_polije.project.domain.history.dto.CreateHistoryDto;
import com.knu_polije.project.domain.history.dto.ReadHistoryDto;
import com.knu_polije.project.domain.history.entity.History;
import com.knu_polije.project.domain.history.repository.HistoryRepository;
import com.knu_polije.project.domain.member.entity.Member;

import lombok.RequiredArgsConstructor;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class HistoryService {
	private final HistoryRepository historyRepository;

	@Transactional
	public ReadHistoryDto createHistory(Member member, Cow cow, CreateHistoryDto createHistoryDto) {
		History newHistory = History.builder()
			.member(member)
			.cow(cow)
			.detectType(createHistoryDto.detectType())
			.inputImgName(createHistoryDto.inputImgUrl())
			.outputImgName(createHistoryDto.outputImgUrl())
			.outputData(createHistoryDto.outputData())
			.build();

		History savedHistory = historyRepository.save(newHistory);
		return ReadHistoryDto.of(savedHistory);
	}

	public List<ReadHistoryDto> getHistoriesByMember(Member member) {
		return ReadHistoryDto.of(historyRepository.findAllByMember(member));
	}

	public List<ReadHistoryDto> getAllHistories() {
		return ReadHistoryDto.of(historyRepository.findAll());
	}
}
