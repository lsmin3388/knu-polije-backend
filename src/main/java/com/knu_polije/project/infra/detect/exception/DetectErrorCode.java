package com.knu_polije.project.infra.detect.exception;

import org.springframework.http.HttpStatus;

import com.knu_polije.project.global.exception.ErrorCode;

public enum DetectErrorCode implements ErrorCode {
    FILE_DOWNLOAD_ERROR(HttpStatus.NOT_FOUND, "Failed to download output image"),
    FILE_REQUEST_FAILED(HttpStatus.NOT_FOUND, "Failed to request image");


    private final HttpStatus status;
    private final String message;

    DetectErrorCode(HttpStatus status, String message) {
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
