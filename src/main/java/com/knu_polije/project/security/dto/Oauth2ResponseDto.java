package com.knu_polije.project.security.dto;

public interface Oauth2ResponseDto {
    String getProvider();
    String getProviderId();
    String getEmail();
    String getName();

}
