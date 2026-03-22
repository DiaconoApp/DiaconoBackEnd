package com.diacono.diacono.presentation.dto.response;

public record MembroKpiResponseDTO(
        long membrosAtivos,
        long membrosInativos,
        long totalAnoInicio,
        long totalAnoFim
) {
}
