package com.diacono.diacono.applications.dtos.ministerio;

public record KpisMembrosDTO(
        long membrosAtivos,
        long membrosNovos,
        double retencao
) {
}
