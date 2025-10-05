package com.diacono.diacono.endereco.mapper;

import com.diacono.diacono.endereco.model.dto.request.EnderecoDTO;
import com.diacono.diacono.endereco.model.dto.response.EnderecoSimplificadoDTO;
import com.diacono.diacono.endereco.model.entity.Endereco;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface EnderecoMapper {

    EnderecoSimplificadoDTO paraEnderecoSimplificadoDTO(Endereco endereco);

    @Mapping(target = "idInterno", ignore = true)
    @Mapping(target = "idExterno", ignore = true)
    Endereco paraEndereco(EnderecoDTO request);

}
