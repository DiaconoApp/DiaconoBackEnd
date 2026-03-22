package com.diacono.diacono.application.mappers;

import com.diacono.diacono.presentation.dto.request.EventoCreateDTO;
import com.diacono.diacono.presentation.dto.request.EventoUpdateDTO;
import com.diacono.diacono.presentation.dto.response.EventoCompletoDTO;
import com.diacono.diacono.presentation.dto.response.EventoSimplificadoDTO;
import com.diacono.diacono.presentation.dto.response.EventoUnicoSimplificadoDTO;
import com.diacono.diacono.domain.entities.Evento;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring",
        uses = {MembroMapper.class, MinisterioMapper.class, EnderecoEventoMapper.class, IgrejaMapper.class})
public interface EventoMapper {

    EventoUnicoSimplificadoDTO paraEventoUnicoSimplificadoDTO(Evento evento);

    List<EventoUnicoSimplificadoDTO> paraListaEventoUnicoSimplificadoDTO(List<Evento> eventos);

    default EventoSimplificadoDTO paraEventoSimplificado(List<Evento> eventos) {

        List<EventoUnicoSimplificadoDTO> listaMapeada = paraListaEventoUnicoSimplificadoDTO(eventos);

        return new EventoSimplificadoDTO(
                listaMapeada
        );
    }

    EventoCompletoDTO paraEventoCompletoDTO(Evento evento);

    @Mapping(target = "idInterno", ignore = true)
    @Mapping(target = "idExterno", ignore = true)
    @Mapping(target = "igreja", ignore = true)
    @Mapping(target = "organizador", ignore = true)
    @Mapping(target = "ministerios", ignore = true)
    @Mapping(target = "recorrencia", ignore = true)
    Evento paraEvento(EventoCreateDTO request);

    @Mapping(target = "idInterno", ignore = true)
    @Mapping(target = "idExterno", ignore = true)
    @Mapping(target = "igreja", ignore = true)
    @Mapping(target = "organizador", ignore = true)
    @Mapping(target = "ministerios", ignore = true)
    Evento paraEvento(EventoUpdateDTO request);
}
