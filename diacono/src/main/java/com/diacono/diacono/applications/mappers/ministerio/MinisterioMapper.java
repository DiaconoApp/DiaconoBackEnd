package com.diacono.diacono.applications.mappers.ministerio;

import com.diacono.diacono.applications.dtos.ministerio.MinisterioSimplificadoDTO;
import com.diacono.diacono.domain.entity.Ministerio;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface MinisterioMapper {

    MinisterioSimplificadoDTO paraMinisterioSimplificadoDTO(Ministerio ministerio);


}
