package com.diacono.diacono.applications.dtos.evento;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public record EventoUpdateDTO(

        List<UUID> fkMinisterios,

        @Valid
        EnderecoEventoDTO endereco,

        String nome,

        String descricao,

        String publicoAlvo,

        @FutureOrPresent(message = "Não é possível cadastrar eventos com datas passadas.")
        LocalDateTime dataHoraInicio,

        @FutureOrPresent(message = "Não é possível cadastrar eventos com datas passadas.")
        LocalDateTime dataHoraFim,

        BigDecimal custo

) {
}
