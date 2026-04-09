package com.diacono.diacono.applications.mappers.evento;

import com.diacono.diacono.applications.dtos.evento.EventoUpdateDTO;
import com.diacono.diacono.domain.entity.Evento;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface EventoUpdateMapper {

    void updateEventoDTO(EventoUpdateDTO request, @MappingTarget Evento evento);

}
