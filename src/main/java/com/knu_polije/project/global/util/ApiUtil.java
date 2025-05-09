package com.knu_polije.project.global.util;

import org.springframework.http.HttpStatus;

import com.fasterxml.jackson.annotation.JsonInclude;

public class ApiUtil {

    public static <T> ApiSuccessResult<T> success(HttpStatus httpStatus) {
        return new ApiSuccessResult<>(httpStatus.value(), null);
    }

    public static <T> ApiSuccessResult<T> success(HttpStatus httpStatus, T response) {
        return new ApiSuccessResult<>(httpStatus.value(), response);
    }

    public static ApiErrorResult error(HttpStatus status, String code, String message) {
        return new ApiErrorResult(status.value(), code, message);
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    public record ApiSuccessResult<T>(int status, T response) {
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    public record ApiErrorResult(int status, String code, String message) {
    }

}
