package com.knu_polije.project.jwt.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
public record Token(
        @NotNull String grantType,
		@NotNull TokenType tokenType,
        @NotNull String token
) {}
