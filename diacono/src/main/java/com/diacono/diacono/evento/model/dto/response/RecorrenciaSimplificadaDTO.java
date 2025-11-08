package com.diacono.diacono.evento.model.dto.response;

import com.diacono.diacono.evento.model.entity.TipoRecorrencia;

import java.time.LocalDate;
import java.util.UUID;

public record RecorrenciaSimplificadaDTO(
        TipoRecorrencia tipoRecorrencia,
        LocalDate dataInicioRecorrencia,
        LocalDate dataTerminoRecorrencia,
        UUID idExterno
) {
}
