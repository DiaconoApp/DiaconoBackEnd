package com.diacono.diacono.evento.mapper;

import com.diacono.diacono.evento.model.dto.request.EnderecoEventoDTO;
import com.diacono.diacono.evento.model.dto.response.EnderecoEventoSimplificadoDTO;
import com.diacono.diacono.evento.model.entity.EnderecoEvento;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface EnderecoEventoMapper {

    EnderecoEventoSimplificadoDTO paraEnderecoEventoSimplificadoDTO(EnderecoEvento enderecoEvento);

    @Mapping(target = "idInterno", ignore = true)
    @Mapping(target = "idExterno", ignore = true)
    EnderecoEvento paraEndereco(EnderecoEventoDTO request);

}
