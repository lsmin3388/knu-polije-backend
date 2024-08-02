package com.knu_polije.project.domain.member.exception;

import org.springframework.http.HttpStatus;

import com.knu_polije.project.global.exception.ErrorCode;

public enum MemberErrorCode implements ErrorCode {
	NOT_FOUND_MEMBER(HttpStatus.NOT_FOUND, "Not found Member");

	private final HttpStatus status;
	private final String message;

	MemberErrorCode(HttpStatus status, String message) {
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
