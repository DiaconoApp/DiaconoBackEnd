package com.diacono.diacono.applications.mappers.recorrencia;


import com.diacono.diacono.applications.dtos.recorrencia.RecorrenciaCreateDTO;
import com.diacono.diacono.domain.entity.Recorrencia;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface RecorrenciaMapper {

    @Mapping(target = "idInterno", ignore = true)
    @Mapping(target = "idExterno", ignore = true)
    Recorrencia paraRecorrencia(RecorrenciaCreateDTO recorrencia);

}
