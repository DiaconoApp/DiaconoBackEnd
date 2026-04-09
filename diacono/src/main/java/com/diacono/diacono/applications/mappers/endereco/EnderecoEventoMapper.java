package com.diacono.diacono.applications.mappers.endereco;

import com.diacono.diacono.applications.dtos.evento.EnderecoEventoDTO;
import com.diacono.diacono.applications.dtos.evento.EnderecoEventoSimplificadoDTO;
import com.diacono.diacono.domain.entity.EnderecoEvento;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface EnderecoEventoMapper {

    EnderecoEventoSimplificadoDTO paraEnderecoEventoSimplificadoDTO(EnderecoEvento enderecoEvento);

    @Mapping(target = "idInterno", ignore = true)
    @Mapping(target = "idExterno", ignore = true)
    EnderecoEvento paraEndereco(EnderecoEventoDTO request);

}
