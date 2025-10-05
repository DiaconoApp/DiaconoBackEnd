package com.diacono.diacono.Igreja.mapper;

import com.diacono.diacono.Igreja.model.dto.response.IgrejaSimplificadoDTO;
import com.diacono.diacono.Igreja.model.entity.Igreja;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface IgrejaMapper {

    IgrejaSimplificadoDTO paraIgrejaSimplificadoDTO(Igreja igreja);

}
