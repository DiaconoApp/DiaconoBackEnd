package com.diacono.diacono.global.config;

import jakarta.validation.constraints.NotBlank;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

@ConfigurationProperties(prefix = "google.oauth")
@Validated
public record GoogleOAuthProperties(
        @NotBlank String clientId,
        @NotBlank String clientSecret
) {
}

