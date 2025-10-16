package com.diacono.diacono.membro.mapper;

import com.diacono.diacono.membro.model.dto.EnderecoMembroDTO;
import com.diacono.diacono.membro.model.entity.EnderecoMembro;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface EnderecoMembroMapper {

    EnderecoMembro paraEnderecoMembro(EnderecoMembroDTO enderecoMembroDto);
}
