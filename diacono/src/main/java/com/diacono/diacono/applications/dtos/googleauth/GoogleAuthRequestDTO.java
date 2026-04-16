package com.diacono.diacono.applications.dtos.googleauth;

public record GoogleAuthRequestDTO(
        String idToken,
        String refreshToken
) {
}
