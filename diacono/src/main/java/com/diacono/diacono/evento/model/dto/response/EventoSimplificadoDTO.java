package com.diacono.diacono.evento.model.dto.response;


import java.util.ArrayList;

public record EventoSimplificadoDTO(ArrayList<EventoUnicoSimplificadoDTO> eventosMes, int totalSemana, int totalMes, int totalAno) {
}
