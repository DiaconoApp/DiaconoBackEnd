package com.diacono.diacono.membroministerio.model.dto.response;

import java.time.LocalDate;

public record MinisterioDashEvolucaoDTO (
        long quantidadeMembros,
        LocalDate data
){
}
