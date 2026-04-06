package com.diacono.diacono.usecases.ministerio;

import com.diacono.diacono.applications.dtos.membro.MembroMinisterioInfoMembroDTO;
import com.diacono.diacono.applications.mappers.membro.MembroMinisterioMapper;
import com.diacono.diacono.domain.entity.MembroMinisterio;
import com.diacono.diacono.domain.repository.MembroMinisterioRepository;
import com.diacono.diacono.global.error.exceptions.ObjectNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public class BuscarMembroMinisterioLiderMinisterioSemFiltroUseCase {

    private final MembroMinisterioRepository membroMinisterioRepository;
    private final MembroMinisterioMapper mapper;

        public BuscarMembroMinisterioLiderMinisterioSemFiltroUseCase(MembroMinisterioRepository membroMinisterioRepository, MembroMinisterioMapper mapper) {
            this.membroMinisterioRepository = membroMinisterioRepository;
            this.mapper = mapper;
        }

    public Page<MembroMinisterioInfoMembroDTO> execute(UUID idMinisterio, Pageable pageable){

        Page<MembroMinisterio> page = membroMinisterioRepository.buscarPorMembroMinisterioSemFiltro(pageable, idMinisterio);

        if(page.isEmpty()){
            throw new ObjectNotFoundException("Nenhum membro_ministerio encontrado com os filtros informados.");
        }

        //fazer o mapper para MembroMinisterioDTO
        Page<MembroMinisterioInfoMembroDTO> response = page.map(mapper::paraMembroMinisterioInfoMembroDTO);

        return response;
    }
}
