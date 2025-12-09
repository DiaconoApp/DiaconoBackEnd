package com.diacono.diacono.membro.model.dto.response;

public record MembroKpiResponseDTO(
        long membrosAtivos,
        long membrosInativos,
        long totalAnoInicio,
        long totalAnoFim
) {
}
