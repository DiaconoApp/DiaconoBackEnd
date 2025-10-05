package com.diacono.diacono.membro.mapper;

import com.diacono.diacono.membro.model.dto.response.MembroSimplificadoDTO;
import com.diacono.diacono.membro.model.entity.Membro;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface MembroMapper {

    MembroSimplificadoDTO paraMembroSimplificadoDTO(Membro membro);

}
