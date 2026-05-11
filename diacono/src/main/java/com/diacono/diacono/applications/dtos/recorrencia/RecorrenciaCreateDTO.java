package com.diacono.diacono.applications.dtos.recorrencia;

import com.diacono.diacono.domain.enums.TipoRecorrencia;
import jakarta.validation.constraints.AssertTrue;
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

    @AssertTrue(message = "É necessário preencher os campos de início e término da recorrência")
    public boolean isDatasObrigatoriasQuandoRecorrente() {
        if (tipoRecorrencia == null || TipoRecorrencia.NAO_REPETE.equals(tipoRecorrencia)) {
            return true;
        }
        return dataInicioRecorrencia != null && dataTerminoRecorrencia != null;
    }

    @AssertTrue(message = "A data final da recorrência precisa ser maior que a data de início, para eventos com recorrência.")
    public boolean isDataTerminoMaiorQueInicioQuandoRecorrente() {
        if (tipoRecorrencia == null || TipoRecorrencia.NAO_REPETE.equals(tipoRecorrencia)) {
            return true;
        }
        if (dataInicioRecorrencia == null || dataTerminoRecorrencia == null) {
            return true;
        }
        return dataTerminoRecorrencia.isAfter(dataInicioRecorrencia);
    }
}
