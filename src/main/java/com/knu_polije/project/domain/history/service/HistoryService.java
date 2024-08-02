package com.knu_polije.project.domain.history.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.knu_polije.project.domain.history.repository.HistoryRepository;

import lombok.RequiredArgsConstructor;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class HistoryService {
	private final HistoryRepository historyRepository;
}
