package com.knu_polije.project.domain.history.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.knu_polije.project.domain.history.service.HistoryService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/history")
@RequiredArgsConstructor
public class HistoryController {
	private final HistoryService historyService;

}
