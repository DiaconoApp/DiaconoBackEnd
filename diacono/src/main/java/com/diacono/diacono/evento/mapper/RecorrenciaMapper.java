package com.diacono.diacono.evento.mapper;


import com.diacono.diacono.evento.model.dto.request.RecorrenciaCreateDTO;
import com.diacono.diacono.evento.model.entity.Recorrencia;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface RecorrenciaMapper {

    @Mapping(target = "idInterno", ignore = true)
    @Mapping(target = "idExterno", ignore = true)
    Recorrencia paraRecorrencia(RecorrenciaCreateDTO recorrencia);

}
