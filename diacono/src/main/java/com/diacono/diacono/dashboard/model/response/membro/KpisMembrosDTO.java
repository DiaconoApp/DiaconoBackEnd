package com.diacono.diacono.dashboard.model.response.membro;

public record KpisMembrosDTO(
        long membrosAtivos,
        long membrosNovos,
        long retencao
) {
}
