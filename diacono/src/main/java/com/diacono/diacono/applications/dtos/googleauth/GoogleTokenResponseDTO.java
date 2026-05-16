package com.diacono.diacono.applications.dtos.googleauth;

import com.fasterxml.jackson.annotation.JsonProperty;

public record GoogleTokenResponseDTO(
        @JsonProperty("access_token")
        String accessToken,
        @JsonProperty("id_token")
        String idToken,
        @JsonProperty("refresh_token")
        String refreshToken,
        @JsonProperty("expires_in")
        Long expiresIn,
        @JsonProperty("token_type")
        String tokenType,
        String scope
) {
}
