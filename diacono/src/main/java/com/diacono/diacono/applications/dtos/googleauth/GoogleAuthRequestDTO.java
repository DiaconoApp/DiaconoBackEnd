package com.diacono.diacono.applications.dtos.googleauth;

import jakarta.validation.constraints.NotBlank;

public record GoogleAuthRequestDTO(
        @NotBlank(message = "idToken é obrigatório.")
        String idToken,

        String refreshToken
) {
}
