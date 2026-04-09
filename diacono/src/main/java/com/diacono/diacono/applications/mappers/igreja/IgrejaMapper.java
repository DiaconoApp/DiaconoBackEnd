package com.diacono.diacono.applications.mappers.igreja;

import com.diacono.diacono.applications.dtos.igreja.IgrejaSemiCompletoDTO;
import com.diacono.diacono.applications.dtos.igreja.IgrejaSimplificadoDTO;
import com.diacono.diacono.domain.entity.Igreja;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface IgrejaMapper {

    IgrejaSimplificadoDTO paraIgrejaSimplificadoDTO(Igreja igreja);

    List<IgrejaSemiCompletoDTO> paraListaIgrejaSemiCompletoDTO(List<Igreja> igrejas);

}
