package com.diacono.diacono.evento.mapper;

import com.diacono.diacono.evento.model.dto.request.EventoUpdateDTO;
import com.diacono.diacono.evento.model.entity.Evento;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface EventoUpdateMapper {

    void updateEventoDTO(EventoUpdateDTO request, @MappingTarget Evento evento);

}
