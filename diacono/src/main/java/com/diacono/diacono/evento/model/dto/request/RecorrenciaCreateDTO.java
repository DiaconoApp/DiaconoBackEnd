package com.diacono.diacono.evento.model.dto.request;

import com.diacono.diacono.evento.model.entity.TipoRecorrencia;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;

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
