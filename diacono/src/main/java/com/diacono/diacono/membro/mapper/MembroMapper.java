package com.diacono.diacono.membro.mapper;

import com.diacono.diacono.Igreja.mapper.IgrejaMapper;
import com.diacono.diacono.membro.model.dto.MembrosCreateDTO;
import com.diacono.diacono.membro.model.dto.response.MembroSimplificadoDTO;
import com.diacono.diacono.membro.model.entity.Membro;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring",
        uses = {EnderecoMembroMapper.class, IgrejaMapper.class})
public interface MembroMapper {

    MembroSimplificadoDTO paraMembroSimplificadoDTO(Membro membro);

    @Mapping(target = "idInterno", ignore = true)
    @Mapping(target = "idExterno", ignore = true)
    @Mapping(target = "ministerio", ignore = true)
    @Mapping(target = "status", ignore = true)
    Membro paraMembro(MembroSimplificadoDTO membroDTO);

    @Mapping(target = "idInterno", ignore = true)
    @Mapping(target = "idExterno", ignore = true)
    @Mapping(target = "ministerio", ignore = true)
    @Mapping(target = "statusMembro", ignore = true)
    @Mapping(target = "enderecoMembro", ignore = true)
    @Mapping(target = "senha", ignore = true)
    Membro paraMembro(MembrosCreateDTO membroDTO);
}
