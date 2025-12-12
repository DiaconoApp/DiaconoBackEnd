package com.diacono.diacono.evento.model.entity;

import com.diacono.diacono.global.util.IdEntityUtils;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;

@Entity
@Getter
@NoArgsConstructor
@SuperBuilder(toBuilder = true)
public class Recorrencia extends IdEntityUtils {

    @Enumerated(EnumType.STRING)
    private TipoRecorrencia tipoRecorrencia;
    private LocalDate dataInicioRecorrencia;
    private LocalDate dataTerminoRecorrencia;
}
