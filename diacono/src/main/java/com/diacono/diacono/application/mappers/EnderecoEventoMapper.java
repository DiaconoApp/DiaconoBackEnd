package com.diacono.diacono.application.mappers;

import com.diacono.diacono.presentation.dto.request.EnderecoEventoDTO;
import com.diacono.diacono.presentation.dto.response.EnderecoEventoSimplificadoDTO;
import com.diacono.diacono.domain.entities.EnderecoEvento;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface EnderecoEventoMapper {

    EnderecoEventoSimplificadoDTO paraEnderecoEventoSimplificadoDTO(EnderecoEvento enderecoEvento);

    @Mapping(target = "idInterno", ignore = true)
    @Mapping(target = "idExterno", ignore = true)
    EnderecoEvento paraEndereco(EnderecoEventoDTO request);

}
