package com.diacono.diacono.applications.dtos.recorrencia;

import com.diacono.diacono.domain.enums.TipoRecorrencia;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record RecorrenciaCreateDTO (

        @NotNull(message = "O evento precisa ter algum tipo de recorrência.")
        TipoRecorrencia tipoRecorrencia,

        @FutureOrPresent(message = "A data precisa ser no presente ou futuro.")
        LocalDate dataInicioRecorrencia,
        @FutureOrPresent(message = "A data precisa ser no presente ou futuro.")
        LocalDate dataTerminoRecorrencia

){
}
