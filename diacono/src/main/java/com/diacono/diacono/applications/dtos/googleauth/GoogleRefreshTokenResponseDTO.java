package com.diacono.diacono.applications.dtos.googleauth;

public record GoogleRefreshTokenResponseDTO(
        String email,
        String nome,
        String refreshToken
) {
}
