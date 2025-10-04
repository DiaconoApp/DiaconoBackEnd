package com.diacono.diacono.evento.mapper;

import com.diacono.diacono.evento.model.dto.response.EventoSimplificadoDTO;
import com.diacono.diacono.evento.model.dto.response.EventoUnicoSimplificadoDTO;
import com.diacono.diacono.evento.model.entity.Evento;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface EventoMapper {

    EventoUnicoSimplificadoDTO paraEventoUnicoSimplificadoDTO(Evento evento);

    List<EventoUnicoSimplificadoDTO> paraListaEventoUnicoSimplificadoDTO(List<Evento> eventos);

    default EventoSimplificadoDTO paraEventoSimplificado(List<Evento> eventos, int totalSemana, int totalMes, int totalAno) {

        List<EventoUnicoSimplificadoDTO> listaMapeada = paraListaEventoUnicoSimplificadoDTO(eventos);

        return new EventoSimplificadoDTO(
                listaMapeada,
                totalSemana,
                totalMes,
                totalAno
        );
    }
}
