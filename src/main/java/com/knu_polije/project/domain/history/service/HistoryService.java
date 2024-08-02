package com.knu_polije.project.domain.history.service;

import java.util.List;

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
	public History createHistory(Member member, Cow cow, CreateHistoryDto createHistoryDto) {
		History newHistory = History.builder()
			.member(member)
			.cow(cow)
			.detectType(createHistoryDto.detectType())
			.resultValue(createHistoryDto.resultValue())
			.resultPath(createHistoryDto.resultPath())
			.build();

		return historyRepository.save(newHistory);
	}

	public List<History> getHistoriesByMember(Member member) {
		return historyRepository.findAllByMember(member);
	}




}
