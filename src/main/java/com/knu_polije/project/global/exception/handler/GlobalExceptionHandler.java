package com.knu_polije.project.global.exception.handler;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.knu_polije.project.global.exception.GlobalException;
import com.knu_polije.project.global.util.ApiUtil;

//@RestControllerAdvice
@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(GlobalException.class)
    public ResponseEntity<ApiUtil.ApiErrorResult> businessLogicException(final GlobalException globalException) {
        HttpStatus status = globalException.getStatus();
        String code = globalException.getCode();
        String message = globalException.getMessage();

        return ResponseEntity
                .status(status)
                .contentType(MediaType.APPLICATION_JSON)
                .body(ApiUtil.error(status, code, message));
    }
}
