package com.diacono.diacono.applications.mappers.endereco;

import com.diacono.diacono.applications.dtos.membro.EnderecoMembroDTO;
import com.diacono.diacono.domain.entity.EnderecoMembro;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface EnderecoMembroMapper {

    EnderecoMembro paraEnderecoMembro(EnderecoMembroDTO enderecoMembroDto);
}
