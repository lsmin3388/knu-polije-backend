package com.knu_polije.project.global.security.dto;

import java.util.Map;

public class GoogleResponseDto implements Oauth2ResponseDto {

    private final Map<String, Object> attributes;

    public GoogleResponseDto(Map<String, Object> attribute) {
        this.attributes = attribute;
    }

    @Override
    public String getProvider() {

        return "google";
    }

    @Override
    public String getProviderId() {

        return attributes.get("sub").toString();
    }

    @Override
    public String getEmail() {
        return attributes.get("email").toString();
    }

    @Override
    public String getName() {

        return attributes.get("name").toString();
    }
}
