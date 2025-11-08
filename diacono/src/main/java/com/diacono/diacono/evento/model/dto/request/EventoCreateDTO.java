package com.diacono.diacono.evento.model.dto.request;

import com.diacono.diacono.evento.model.entity.TipoRecorrencia;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.UUID;

public record EventoCreateDTO(

        @NotNull(message = "Você deve estar associado a uma Igreja para criar um evento.")
        UUID fkIgreja,

        @NotNull(message = "Todo evento precisa ter um membro responsável por ele.")
        UUID fkOrganizador,

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
        String nome,

        @NotBlank
        @Size(max = 400, message = "A descrição deve ter no máximo 400 caracteres.")
        String descricao,

        @NotBlank(message = "O evento precisa ter um público-alvo.")
        String publicoAlvo,

        @NotNull(message = "É necessário que o evento tenha uma data.")
        @FutureOrPresent(message = "Não é possível cadastrar eventos com datas passadas.")
        LocalDate data,

        @NotNull(message = "É necessário que o evento tenha um horário de início.")
        LocalTime horaInicio,

        @NotNull(message = "É necessário que o evento tenha um horário de finalização.")
        LocalTime horaFim,

        @NotNull
        @PositiveOrZero(message = "O valor para participar do evento não deve ser negativo.")
        BigDecimal custo
) {
}