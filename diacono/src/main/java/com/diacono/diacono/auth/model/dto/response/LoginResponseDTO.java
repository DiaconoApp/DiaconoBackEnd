package com.diacono.diacono.auth.model.dto.response;

public record LoginResponseDTO(String acessToken, Long expiresIn) {
}
