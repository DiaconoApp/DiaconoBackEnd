package com.diacono.diacono.evento.model.dto.request;

import com.diacono.diacono.evento.model.entity.DiasSemanaRecorrencia;
import com.diacono.diacono.evento.model.entity.TipoRecorrencia;
import com.diacono.diacono.endereco.model.dto.request.EnderecoEventoDTO;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.UUID;

public record EventoCreateDTO(
        //falta as validações de campo

        UUID fkIgreja,

        UUID fkOrganizador,

        ArrayList<UUID> fkMinisterios,

        String nome,

        String descricao,
        String publicoAlvo,

        LocalDate data,

        LocalTime horaInicio,
        LocalTime horaFim,

        BigDecimal custo,

        TipoRecorrencia tipoRecorrencia,
        LocalDate dataInicioRecorrencia,
        LocalDate dataTerminoRecorrencia,
        ArrayList<DiasSemanaRecorrencia> diasSemana,
        LocalTime horarioRecorrencia,
        int intervaloRecorrencia,

        EnderecoEventoDTO endereco
) {
}
