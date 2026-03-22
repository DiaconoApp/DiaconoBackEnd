package com.diacono.diacono.presentation.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public record EventoCreateDTO(

        @NotNull
        @Size(min = 1, message = "O evento deve ter ao menos um ministério participando.")
        List<UUID> fkMinisterios,

        @NotNull(message = "É necessário que o evento tenha um endereço vinculado a ele.")
        @Valid
        EnderecoEventoDTO endereco,

        @NotNull(message = "É necessário que o evento tenha algum tipo de recorrência.")
        @Valid
        RecorrenciaCreateDTO recorrencia,

        @NotBlank
        @Size(max = 100, message = "O nome deve ter no máximo 100 caracteres.")
        @Pattern(regexp = "^[A-Za-zÀ-ÿ ]+$", message = "Deve conter apenas letras e espaços")
        String nome,

        @NotBlank
        @Size(max = 400, message = "A descrição deve ter no máximo 400 caracteres.")
        @Pattern(regexp = "^[A-Za-zÀ-ÿ ]+$", message = "Deve conter apenas letras e espaços")
        String descricao,

        @NotBlank(message = "O evento precisa ter um público-alvo.")
        @Pattern(regexp = "^[A-Za-zÀ-ÿ ]+$", message = "Deve conter apenas letras e espaços")
        String publicoAlvo,

        @NotNull(message = "É necessário que o evento tenha um horário de início.")
        @FutureOrPresent(message = "Não é possível cadastrar eventos com datas passadas.")
        LocalDateTime dataHoraInicio,

        @NotNull(message = "É necessário que o evento tenha um horário de finalização.")
        @FutureOrPresent(message = "Não é possível cadastrar eventos com datas passadas.")
        LocalDateTime dataHoraFim,

        @NotNull
        @PositiveOrZero(message = "O valor para participar do evento não deve ser negativo.")
        BigDecimal custo
) {
}