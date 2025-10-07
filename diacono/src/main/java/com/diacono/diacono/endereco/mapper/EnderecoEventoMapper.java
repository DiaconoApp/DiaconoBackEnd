package com.diacono.diacono.endereco.mapper;

import com.diacono.diacono.endereco.model.dto.request.EnderecoEventoDTO;
import com.diacono.diacono.endereco.model.dto.response.EnderecoEventoSimplificadoDTO;
import com.diacono.diacono.endereco.model.entity.EnderecoEvento;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface EnderecoEventoMapper {

    EnderecoEventoSimplificadoDTO paraEnderecoSimplificadoDTO(EnderecoEvento enderecoEvento);

    @Mapping(target = "idInterno", ignore = true)
    @Mapping(target = "idExterno", ignore = true)
    EnderecoEvento paraEndereco(EnderecoEventoDTO request);

}
