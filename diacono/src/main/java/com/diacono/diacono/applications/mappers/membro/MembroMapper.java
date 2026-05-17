package com.diacono.diacono.applications.mappers.membro;

import com.diacono.diacono.applications.dtos.CadastroExternoDTO;
import com.diacono.diacono.applications.dtos.membro.MembroCreateDTO;
import com.diacono.diacono.applications.dtos.membro.MembroDetalheResponseDTO;
import com.diacono.diacono.applications.dtos.membro.MembroResponseDTO;
import com.diacono.diacono.applications.dtos.membro.MembroSimplificadoDTO;
import com.diacono.diacono.applications.mappers.endereco.EnderecoMembroMapper;
import com.diacono.diacono.applications.mappers.igreja.IgrejaMapper;
import com.diacono.diacono.applications.mappers.ministerio.MinisterioMapper;
import com.diacono.diacono.domain.entity.Membro;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring",
        uses = {EnderecoMembroMapper.class, IgrejaMapper.class, MinisterioMapper.class, MembroMinisterioMapper.class})
public interface MembroMapper {

    @Mapping(source = "cargoMembro", target = "cargo")
    MembroResponseDTO paraMembroResponseDTO(Membro membro);

    @Mapping(source = "cargoMembro", target = "cargo")
    @Mapping(source = "igreja.idExterno", target = "fkIgreja")
    @Mapping(source = "enderecoMembro", target = "membroEnderecoDTO")
    @Mapping(target = "ministerios", source = "ministerios")
    MembroDetalheResponseDTO paraMembroDetalheResponseDTO(Membro membro);

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