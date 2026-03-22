package com.diacono.diacono.presentation.dto.response;

import java.time.LocalDate;

public record MinisterioDashEvolucaoDTO (
        long quantidadeMembros,
        LocalDate data
){
}
