package com.diacono.diacono.applications.dtos.googleauth;

public record GoogleAuthorizationCodeRequestDTO(
        String authorizationCode,
        String redirectUri,
        String codeVerifier
) {
}
