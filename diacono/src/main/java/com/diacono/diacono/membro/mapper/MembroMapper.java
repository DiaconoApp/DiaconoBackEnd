package com.diacono.diacono.membro.mapper;

import com.diacono.diacono.Igreja.mapper.IgrejaMapper;
import com.diacono.diacono.membro.model.dto.request.MembroCreateDTO;
import com.diacono.diacono.membro.model.dto.response.MembroResponseDTO;
import com.diacono.diacono.membro.model.dto.response.MembroSimplificadoDTO;
import com.diacono.diacono.membro.model.entity.Membro;
import com.diacono.diacono.membroministerio.mapper.MembroMinisterioMapper;
import com.diacono.diacono.ministerio.mapper.MinisterioMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring",
        uses = {EnderecoMembroMapper.class, IgrejaMapper.class, MinisterioMapper.class, MembroMinisterioMapper.class})
public interface MembroMapper {


    MembroResponseDTO paraMembroResponseDTO(Membro membro);

    MembroSimplificadoDTO paraMembroSimplificadoDTO(Membro membro);

    List<MembroResponseDTO> paraMembrosResponseDTO(List<Membro> membros);



    @Mapping(target = "idInterno", ignore = true)
    @Mapping(target = "idExterno", ignore = true)
    @Mapping(target = "ministerios", ignore = true)
    @Mapping(target = "status", ignore = true)
    Membro paraMembro(MembroSimplificadoDTO membroDTO);

    @Mapping(target = "idInterno", ignore = true)
    @Mapping(target = "idExterno", ignore = true)
    @Mapping(target = "status", ignore = true)
    Membro paraMembro(MembroCreateDTO membroDTO);

}