package com.diacono.diacono.global.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "google.oauth")
public record GoogleOAuthProperties(String clientId, String clientSecret, String tokenUri) {

    public String resolvedTokenUri() {
        if (tokenUri == null || tokenUri.isBlank()) {
            return "https://oauth2.googleapis.com/token";
        }

        return tokenUri;
    }
}
