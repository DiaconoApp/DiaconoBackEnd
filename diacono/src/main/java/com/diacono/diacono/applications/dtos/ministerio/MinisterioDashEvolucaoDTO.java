package com.diacono.diacono.applications.dtos.ministerio;

import java.time.LocalDate;

public record MinisterioDashEvolucaoDTO (
        long quantidadeMembros,
        LocalDate data
){
}
