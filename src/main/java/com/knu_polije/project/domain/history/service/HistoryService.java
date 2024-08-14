package com.knu_polije.project.domain.history.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.knu_polije.project.domain.history.dto.ReadHistoryDto;
import com.knu_polije.project.domain.history.entity.History;
import com.knu_polije.project.domain.history.repository.HistoryRepository;
import com.knu_polije.project.domain.member.entity.Member;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class HistoryService {
	private final HistoryRepository historyRepository;

	@Transactional
	public void addHistory(Member member, String inputFileName, String outputFileName, String data) {
		historyRepository.save(History.builder()
			.member(member)
			.inputFileName(inputFileName)
			.outputFileName(outputFileName)
			.data(data)
			.build());
	}

	public List<ReadHistoryDto> readHistories(Member member) {
		List<History> histories = historyRepository.findAllByMember(member);
		return ReadHistoryDto.of(histories);
	}
}
