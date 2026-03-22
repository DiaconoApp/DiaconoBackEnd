package com.diacono.diacono.presentation.dto.response;

public record KpisMembrosDTO(
        long membrosAtivos,
        long membrosNovos,
        double retencao
) {
}
