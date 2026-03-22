package com.diacono.diacono.application.mappers;

import com.diacono.diacono.presentation.dto.MinisterioSimplificadoDTO;
import com.diacono.diacono.domain.entities.Ministerio;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface MinisterioMapper {

    MinisterioSimplificadoDTO paraMinisterioSimplificadoDTO(Ministerio ministerio);


}
