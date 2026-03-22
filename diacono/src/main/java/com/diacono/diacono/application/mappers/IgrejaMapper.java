package com.diacono.diacono.application.mappers;

import com.diacono.diacono.presentation.dto.response.IgrejaSemiCompletoDTO;
import com.diacono.diacono.presentation.dto.response.IgrejaSimplificadoDTO;
import com.diacono.diacono.domain.entities.Igreja;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface IgrejaMapper {

    IgrejaSimplificadoDTO paraIgrejaSimplificadoDTO(Igreja igreja);

    List<IgrejaSemiCompletoDTO> paraListaIgrejaSemiCompletoDTO(List<Igreja> igrejas);

}
