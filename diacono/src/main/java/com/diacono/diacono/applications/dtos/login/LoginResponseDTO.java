package com.diacono.diacono.applications.dtos.login;

public record LoginResponseDTO(
        String acessToken,
        Long expiresIn
) {
}
