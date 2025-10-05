package com.diacono.diacono.global.mapper;

import com.diacono.diacono.global.model.dto.response.IgrejaSimplificadoDTO;
import com.diacono.diacono.global.model.entity.Igreja;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface IgrejaMapper {

    IgrejaSimplificadoDTO paraIgrejaSimplificadoDTO(Igreja igreja);

}
