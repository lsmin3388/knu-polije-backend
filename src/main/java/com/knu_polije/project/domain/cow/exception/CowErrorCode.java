package com.knu_polije.project.domain.cow.exception;

import org.springframework.http.HttpStatus;

import com.knu_polije.project.global.exception.ErrorCode;

public enum CowErrorCode implements ErrorCode {
	NOT_FOUND_COW(HttpStatus.NOT_FOUND, "Not found Cow");

	private final HttpStatus status;
	private final String message;

	CowErrorCode(HttpStatus status, String message) {
		this.status = status;
		this.message = message;
	}

	public String message() {
		return this.message;
	}

	public String code() {
		return this.name();
	}

	public HttpStatus status() {
		return this.status;
	}
}
