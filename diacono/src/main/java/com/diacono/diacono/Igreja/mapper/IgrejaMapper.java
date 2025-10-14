package com.diacono.diacono.Igreja.mapper;

import com.diacono.diacono.Igreja.model.dto.response.IgrejaSemiCompletoDTO;
import com.diacono.diacono.Igreja.model.dto.response.IgrejaSimplificadoDTO;
import com.diacono.diacono.Igreja.model.entity.Igreja;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface IgrejaMapper {

    IgrejaSimplificadoDTO paraIgrejaSimplificadoDTO(Igreja igreja);

    List<IgrejaSemiCompletoDTO> paraListaIgrejaSemiCompletoDTO(List<Igreja> igrejas);

}
