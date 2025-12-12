package com.diacono.diacono.evento.model.dto.request;

import com.diacono.diacono.evento.model.entity.TipoRecorrencia;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
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
