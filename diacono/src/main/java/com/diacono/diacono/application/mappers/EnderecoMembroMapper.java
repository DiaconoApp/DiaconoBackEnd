package com.diacono.diacono.application.mappers;

import com.diacono.diacono.presentation.dto.request.EnderecoMembroDTO;
import com.diacono.diacono.domain.entities.EnderecoMembro;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface EnderecoMembroMapper {

    EnderecoMembro paraEnderecoMembro(EnderecoMembroDTO enderecoMembroDto);
}
