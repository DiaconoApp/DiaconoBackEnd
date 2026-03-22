package com.diacono.diacono.application.mappers;

import com.diacono.diacono.presentation.dto.response.MembroMinisterioDTO;
import com.diacono.diacono.presentation.dto.response.MembroMinisterioInfoMembroDTO;
import com.diacono.diacono.domain.entities.MembroMinisterio;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface MembroMinisterioMapper {

    @Mapping(target = "idExternoMinisterio", source = "ministerio.idExterno")
    @Mapping(target = "nomeMinisterio", source = "ministerio.nome")
    @Mapping(target = "nomeLider", source = "ministerio.nomeLider")
    @Mapping(target = "status", source = "ministerio.status")
    MembroMinisterioDTO paraMembroMinisterioDTO(MembroMinisterio membroMinisterio);

    @Mapping(target = "idExternoMembro", source = "membro.idExterno")
    @Mapping(target = "nome", source = "membro.nome")
    @Mapping(target = "email", source = "membro.email")
    @Mapping(target = "numeroCelular", source = "membro.celular")
    @Mapping(target = "dataNascimento", source = "membro.dataNascimento")
    @Mapping(target = "cargo", source = "cargoMembro")
    @Mapping(target = "nomeMinisterio", source = "ministerio.nome")
    @Mapping(target = "status", source = "membro.status")
    MembroMinisterioInfoMembroDTO paraMembroMinisterioInfoMembroDTO(MembroMinisterio membroMinisterio);


}
