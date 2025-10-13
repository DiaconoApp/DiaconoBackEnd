package com.diacono.diacono.evento.model.dto.request;

import com.diacono.diacono.evento.model.entity.DiasSemanaRecorrencia;
import com.diacono.diacono.evento.model.entity.TipoRecorrencia;
import com.diacono.diacono.endereco.model.dto.request.EnderecoEventoDTO;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.UUID;

public record EventoCreateDTO(

        @NotNull
        UUID fkIgreja,

        @NotNull
        UUID fkOrganizador,

        @NotNull
        @Size(min = 1, message = "O evento deve ter ao menos um ministério participando.")
        List<UUID> fkMinisterios,

        @NotBlank
        @Size(max = 100, message = "O nome deve ter no máximo 100 caracteres.")
        String nome,

        @NotBlank
        @Size(max = 400, message = "A descrição deve ter no máximo 400 caracteres.")
        String descricao,

        @NotBlank(message = "O evento precisa ter um público-alvo.")
        String publicoAlvo,

        @NotNull
        @FutureOrPresent(message = "Não é possível cadastrar eventos com datas passadas.")
        LocalDate data,

        @NotNull(message = "É necessário que o evento tenha um horário de início.")
        LocalTime horaInicio,

        @NotNull(message = "É necessário que o evento tenha um horário de finalização.")
        LocalTime horaFim,

        @NotNull
        @PositiveOrZero(message = "O valor para participar do evento não deve ser negativo.")
        BigDecimal custo,



        @NotNull(message = "Mesmo que o evento não seja recorrente, é necessário selecionar.")
        TipoRecorrencia tipoRecorrencia,

        @FutureOrPresent
        LocalDate dataInicioRecorrencia,
        @FutureOrPresent
        LocalDate dataTerminoRecorrencia,

        // Sem @NotNull, para ser validado no Service
        List<DiasSemanaRecorrencia> diasSemana,

        LocalTime horarioRecorrencia,

        @PositiveOrZero
        int intervaloRecorrencia,

        @NotNull(message = "É necessário que o evento tenha um endereço vinculado a ele.")
        EnderecoEventoDTO endereco
) {
}