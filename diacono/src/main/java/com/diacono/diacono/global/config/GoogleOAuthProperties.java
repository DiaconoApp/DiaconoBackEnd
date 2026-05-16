package com.diacono.diacono.global.config;

import jakarta.validation.constraints.NotBlank;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

@ConfigurationProperties(prefix = "google.oauth")
public record GoogleOAuthProperties(String clientId, String clientSecret, String tokenUri) {

    public String resolvedTokenUri() {
        if (tokenUri == null || tokenUri.isBlank()) {
            return "https://oauth2.googleapis.com/token";
        }

        return tokenUri;
    }
}
