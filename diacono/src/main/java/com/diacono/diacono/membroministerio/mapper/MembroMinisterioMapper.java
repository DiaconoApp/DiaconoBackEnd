package com.diacono.diacono.membroministerio.mapper;

import com.diacono.diacono.membroministerio.model.dto.response.MembroMinisterioDTO;
import com.diacono.diacono.membroministerio.model.entity.MembroMinisterio;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface MembroMinisterioMapper {

    MembroMinisterioDTO paraMembroMinisterioDTO(MembroMinisterio membroMinisterio);

}
