package com.diacono.diacono.membros.model.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;


public record MembrosUpdateDTO(
        @Email String email,
        @Pattern(regexp = "\\d{5}-\\d{3}") String cep,
        @Positive Integer numeroCasa,
        String senha
) {
}
