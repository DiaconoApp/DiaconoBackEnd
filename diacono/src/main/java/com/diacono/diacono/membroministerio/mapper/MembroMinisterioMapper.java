package com.diacono.diacono.membroministerio.mapper;

import com.diacono.diacono.membroministerio.model.dto.response.MembroMinisterioDTO;
import com.diacono.diacono.membroministerio.model.entity.MembroMinisterio;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface MembroMinisterioMapper {

    @Mapping(source = "ministerio.nome", target = "nomeMinisterio")
    MembroMinisterioDTO paraMembroMinisterioDTO(MembroMinisterio membroMinisterio);

}
