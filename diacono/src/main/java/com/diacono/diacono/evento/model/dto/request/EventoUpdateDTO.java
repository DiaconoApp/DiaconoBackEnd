package com.diacono.diacono.evento.model.dto.request;

import com.diacono.diacono.endereco.model.dto.request.EnderecoEventoDTO;
import com.diacono.diacono.evento.model.entity.DiasSemanaRecorrencia;
import com.diacono.diacono.evento.model.entity.TipoRecorrencia;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.PositiveOrZero;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.UUID;

public record EventoUpdateDTO(

        List<UUID> fkMinisterios,
        
        String nome,

        String descricao,
        String publicoAlvo,

        @FutureOrPresent(message = "A data do evento deve ser hoje ou uma data futura.")
        LocalDate data,


        LocalTime horaInicio,
        LocalTime horaFim,

        @PositiveOrZero(message = "O custo deve ser um valor positivo.")
        BigDecimal custo,

        TipoRecorrencia tipoRecorrencia,
        LocalDate dataInicioRecorrencia,
        LocalDate dataTerminoRecorrencia,
        List<DiasSemanaRecorrencia> diasSemana,
        LocalTime horarioRecorrencia,
        int intervaloRecorrencia,

        EnderecoEventoDTO endereco

) {
}
