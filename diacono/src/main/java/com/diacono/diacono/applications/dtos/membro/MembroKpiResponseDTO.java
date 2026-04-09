package com.diacono.diacono.applications.dtos.membro;

public record MembroKpiResponseDTO(
        long membrosAtivos,
        long membrosInativos,
        long totalAnoInicio,
        long totalAnoFim
) {
}
