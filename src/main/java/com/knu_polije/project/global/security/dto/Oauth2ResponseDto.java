package com.knu_polije.project.global.security.dto;

public interface Oauth2ResponseDto {
    String getProvider();
    String getProviderId();
    String getEmail();
    String getName();

}
