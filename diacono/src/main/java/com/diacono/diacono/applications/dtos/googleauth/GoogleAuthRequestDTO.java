package com.diacono.diacono.applications.dtos.googleauth;

@Deprecated(forRemoval = false)
public record GoogleAuthRequestDTO(
        String idToken,
        String refreshToken
) {
}
