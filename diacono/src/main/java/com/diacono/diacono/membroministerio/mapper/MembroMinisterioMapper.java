package com.diacono.diacono.membroministerio.mapper;

import com.diacono.diacono.membroministerio.model.dto.response.MembroMinisterioDTO;
import com.diacono.diacono.membroministerio.model.entity.MembroMinisterio;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface MembroMinisterioMapper {

    @Mapping(target = "idExternoMinisterio", source = "ministerio.idExterno")
    @Mapping(target = "nomeMinisterio", source = "ministerio.nome")
    @Mapping(target = "nomeLider", source = "ministerio.nomeLider")
    @Mapping(target = "status", source = "ministerio.status")
    MembroMinisterioDTO paraMembroMinisterioDTO(MembroMinisterio membroMinisterio);

}
