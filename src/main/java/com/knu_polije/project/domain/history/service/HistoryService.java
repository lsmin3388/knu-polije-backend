package com.knu_polije.project.domain.history.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.knu_polije.project.domain.cow.entity.Cow;
import com.knu_polije.project.domain.history.dto.CreateHistoryDto;
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
	public void createHistory(Member member, Cow cow, CreateHistoryDto createHistoryDto) {
		historyRepository.save(History.builder()
			.member(member)
			.cow(cow)
			.detectType(createHistoryDto.detectType())
			.resultValue(createHistoryDto.resultValue())
			.resultPath(createHistoryDto.resultPath())
			.build());

	}

}
