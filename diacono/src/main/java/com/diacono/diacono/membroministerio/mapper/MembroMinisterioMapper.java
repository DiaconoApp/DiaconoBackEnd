package com.diacono.diacono.membroministerio.mapper;

import com.diacono.diacono.membroministerio.model.dto.response.MembroMinisterioDTO;
import com.diacono.diacono.membroministerio.model.entity.MembroMinisterio;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface MembroMinisterioMapper {


    @Mapping(target = "nome", source = "membro.nome")
    @Mapping(target = "email", source = "membro.email")
    @Mapping(target = "telefone", source = "membro.celular")
    @Mapping(target = "status", source = "membro.status")
    @Mapping(target = "dataNascimento", source = "membro.dataNascimento")
    @Mapping(target = "cargo", source = "cargoMembro")
    MembroMinisterioDTO paraMembroMinisterioDTO(MembroMinisterio membroMinisterio);

}
