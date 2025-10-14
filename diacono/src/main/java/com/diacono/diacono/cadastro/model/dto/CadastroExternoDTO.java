package com.diacono.diacono.cadastro.model.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record CadastroExternoDTO(

        @NotNull(message = "Você deve estar associado a uma Igreja para criar um evento.")
        UUID fkIgreja,
        @NotBlank(message = "Você precisa escrever seu nome inteiro.")
        String nome


) {
}
