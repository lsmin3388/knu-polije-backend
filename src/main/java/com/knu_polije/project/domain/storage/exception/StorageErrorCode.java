package com.knu_polije.project.domain.storage.exception;

import org.springframework.http.HttpStatus;

import com.knu_polije.project.global.exception.ErrorCode;

public enum StorageErrorCode implements ErrorCode {
    NOT_FOUND_FILE(HttpStatus.NOT_FOUND, "존재하지 않는 파일"),
    FAILED_FILE_STORE(HttpStatus.BAD_REQUEST, "파일 저장 실패"),
    UNSUPPORTED_FILE_EXTENSION(HttpStatus.BAD_REQUEST, "허용되지 않는 확장자"),
    INVALID_FILE_PATH(HttpStatus.BAD_REQUEST, "잘못된 파일 경로");


    private final HttpStatus status;
    private final String message;

    StorageErrorCode(HttpStatus status, String message) {
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
