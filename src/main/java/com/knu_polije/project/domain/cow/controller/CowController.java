package com.knu_polije.project.domain.cow.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.knu_polije.project.domain.cow.service.CowService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/cow")
@RequiredArgsConstructor
public class CowController {
	private final CowService cowService;
}
