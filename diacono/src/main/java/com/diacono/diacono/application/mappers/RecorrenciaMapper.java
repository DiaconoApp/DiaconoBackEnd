package com.diacono.diacono.application.mappers;


import com.diacono.diacono.presentation.dto.request.RecorrenciaCreateDTO;
import com.diacono.diacono.domain.entities.Recorrencia;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface RecorrenciaMapper {

    @Mapping(target = "idInterno", ignore = true)
    @Mapping(target = "idExterno", ignore = true)
    Recorrencia paraRecorrencia(RecorrenciaCreateDTO recorrencia);

}
