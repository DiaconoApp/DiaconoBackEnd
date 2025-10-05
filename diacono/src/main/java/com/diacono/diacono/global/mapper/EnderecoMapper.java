package com.diacono.diacono.global.mapper;

import com.diacono.diacono.global.model.dto.response.EnderecoSimplificadoDTO;
import com.diacono.diacono.global.model.entity.Endereco;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface EnderecoMapper {

    EnderecoSimplificadoDTO paraEnderecoSimplificadoDTO(Endereco endereco);

}
