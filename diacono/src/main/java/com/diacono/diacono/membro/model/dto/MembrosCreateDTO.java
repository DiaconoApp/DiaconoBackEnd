package com.diacono.diacono.membro.model.dto;

import jakarta.validation.constraints.*;
import org.hibernate.validator.constraints.br.CPF;

import java.time.LocalDate;

public record MembrosCreateDTO(

        @NotBlank String nome,
        @Email @NotBlank String email,
        @PastOrPresent @NotNull LocalDate dataNascimento,
        @CPF String cpf,
        @Pattern(regexp = "\\d{5}-\\d{3}") @NotBlank String cep,
        @Positive @NotNull Integer numeroCasa,
        @NotBlank String senhaTemporaria,
        @NotBlank String senha

) {}
