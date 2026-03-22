package com.diacono.diacono.application.mappers;

import com.diacono.diacono.presentation.dto.request.CadastroExternoDTO;
import com.diacono.diacono.presentation.dto.request.MembroCreateDTO;
import com.diacono.diacono.presentation.dto.response.MembroResponseDTO;
import com.diacono.diacono.presentation.dto.response.MembroSimplificadoDTO;
import com.diacono.diacono.domain.entities.Membro;
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

    @Mapping(target = "idInterno", ignore = true)
    @Mapping(target = "idExterno", ignore = true)
    @Mapping(target = "status", ignore = true)
    Membro paraMembro(CadastroExternoDTO membroDTO);

}