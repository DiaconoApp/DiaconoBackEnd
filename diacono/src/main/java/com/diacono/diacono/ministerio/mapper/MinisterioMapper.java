package com.diacono.diacono.ministerio.mapper;

import com.diacono.diacono.ministerio.model.dto.response.MinisterioSimplificadoDTO;
import com.diacono.diacono.ministerio.model.entity.Ministerio;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface MinisterioMapper {

    MinisterioSimplificadoDTO paraMinisterioSimplificadoDTO(Ministerio ministerio);


}
