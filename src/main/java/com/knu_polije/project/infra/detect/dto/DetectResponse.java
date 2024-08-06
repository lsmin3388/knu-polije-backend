package com.knu_polije.project.infra.detect.dto;

public record DetectResponse<T>(String inputImgName, String outputImgName, T result) {
}
