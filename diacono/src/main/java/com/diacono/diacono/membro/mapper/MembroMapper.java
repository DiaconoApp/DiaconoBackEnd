package com.diacono.diacono.membro.mapper;

import com.diacono.diacono.Igreja.mapper.IgrejaMapper;
import com.diacono.diacono.membro.model.dto.request.MembroCreateDTO;
import com.diacono.diacono.membro.model.dto.response.MembroResponseDTO;
import com.diacono.diacono.membro.model.dto.response.MembroSimplificadoDTO;
import com.diacono.diacono.membro.model.entity.Membro;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.data.domain.Page;

import java.util.List;

@Mapper(componentModel = "spring",
        uses = {EnderecoMembroMapper.class, IgrejaMapper.class})
public interface MembroMapper {

    MembroSimplificadoDTO paraMembroSimplificadoDTO(Membro membro);

    @Mapping(target = "idInterno", ignore = true)
    @Mapping(target = "idExterno", ignore = true)
    @Mapping(target = "ministerios", ignore = true)
    @Mapping(target = "status", ignore = true)
    Membro paraMembro(MembroSimplificadoDTO membroDTO);

    @Mapping(target = "idInterno", ignore = true)
    @Mapping(target = "idExterno", ignore = true)
    @Mapping(target = "status", ignore = true)
    Membro paraMembro(MembroCreateDTO membroDTO);


    List<MembroResponseDTO> paraMembrosResponseDTO(Page<Membro> membros);

    List<MembroResponseDTO> paraMembrosResponseDTO(List<Membro> membros);
}
