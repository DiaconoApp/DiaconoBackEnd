package com.diacono.diacono.applications.dtos.recorrencia;

import com.diacono.diacono.domain.enums.TipoRecorrencia;

import java.time.LocalDate;
import java.util.UUID;

public record RecorrenciaSimplificadaDTO(
        TipoRecorrencia tipoRecorrencia,
        LocalDate dataInicioRecorrencia,
        LocalDate dataTerminoRecorrencia,
        UUID idExterno
) {
}
