package com.diacono.diacono.usecases.ministerio;

import com.diacono.diacono.applications.mappers.membro.MembroMinisterioMapper;
import com.diacono.diacono.domain.entity.MembroMinisterio;
import com.diacono.diacono.domain.repository.MembroMinisterioRepository;
import com.diacono.diacono.global.error.exceptions.ObjectNotFoundException;
import com.diacono.diacono.domain.enums.EnumStatusMembro;
import com.diacono.diacono.applications.dtos.membro.MembroMinisterioInfoMembroDTO;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.*;

@Service
public class BuscarMembroMinisterioLiderMinisterioComFiltroUseCase {

    private final MembroMinisterioRepository membroMinisterioRepository;
    private final MembroMinisterioMapper mapper;

    public BuscarMembroMinisterioLiderMinisterioComFiltroUseCase(MembroMinisterioRepository membroMinisterioRepository, MembroMinisterioMapper mapper) {
        this.membroMinisterioRepository = membroMinisterioRepository;
        this.mapper = mapper;
    }

    public Page<MembroMinisterioInfoMembroDTO> execute(UUID idMinisterio, Pageable pageable, String texto, EnumStatusMembro status){

        String textoFormatado = null;
        if (texto != null && !texto.isBlank()) {
            textoFormatado = "%" + texto + "%";
        }

        Page<MembroMinisterio> page = membroMinisterioRepository.buscarPorMembroMinisterioComFiltro(pageable,idMinisterio, textoFormatado, status);

        if(page.isEmpty()){
            throw new ObjectNotFoundException("Nenhum membro_ministerio encontrado com os filtros informados.");
        }

        //fazer o mapper para MembroMinisterioDTO
        Page<MembroMinisterioInfoMembroDTO> response = page.map(mapper::paraMembroMinisterioInfoMembroDTO);

        return response;
    }

}
