package com.diacono.diacono.membro.model.dto.response;

public record MembroKpiResponseDTO(
        long membrosAtivos,
        long totalAnoInicio,
        long totalAnoFim
) {
}
