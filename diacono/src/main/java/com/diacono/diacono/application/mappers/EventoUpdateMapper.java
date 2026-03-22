package com.diacono.diacono.application.mappers;

import com.diacono.diacono.presentation.dto.request.EventoUpdateDTO;
import com.diacono.diacono.domain.entities.Evento;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface EventoUpdateMapper {

    void updateEventoDTO(EventoUpdateDTO request, @MappingTarget Evento evento);

}
