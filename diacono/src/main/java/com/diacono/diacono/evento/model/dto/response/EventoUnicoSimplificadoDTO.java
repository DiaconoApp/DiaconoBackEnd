package com.diacono.diacono.evento.model.dto.response;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

public record EventoUnicoSimplificadoDTO(UUID idExterno, String nome, LocalDate data, LocalTime horaInicio) {
}
