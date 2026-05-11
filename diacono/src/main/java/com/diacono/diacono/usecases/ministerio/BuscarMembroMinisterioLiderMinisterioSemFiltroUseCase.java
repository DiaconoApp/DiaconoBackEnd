package com.diacono.diacono.usecases.ministerio;

import com.diacono.diacono.applications.dtos.membro.MembroMinisterioInfoMembroDTO;
import com.diacono.diacono.applications.mappers.membro.MembroMinisterioMapper;
import com.diacono.diacono.domain.entity.MembroMinisterio;
import com.diacono.diacono.domain.repository.MembroMinisterioRepository;
import com.diacono.diacono.global.error.exceptions.ObjectNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class BuscarMembroMinisterioLiderMinisterioSemFiltroUseCase {

    private static final Logger logger = LoggerFactory.getLogger(BuscarMembroMinisterioLiderMinisterioSemFiltroUseCase.class);

    private final MembroMinisterioRepository membroMinisterioRepository;
    private final MembroMinisterioMapper mapper;

    public BuscarMembroMinisterioLiderMinisterioSemFiltroUseCase(MembroMinisterioRepository membroMinisterioRepository, MembroMinisterioMapper mapper) {
        this.membroMinisterioRepository = membroMinisterioRepository;
        this.mapper = mapper;
    }

    public Page<MembroMinisterioInfoMembroDTO> execute(UUID idMinisterio, Pageable pageable) {

        Page<MembroMinisterio> page = membroMinisterioRepository.buscarPorMembroMinisterioSemFiltro(pageable, idMinisterio);

        if (page.isEmpty()) {
            logger.warn("Nenhum membro encontrado no ministério sem filtros. idMinisterio=[{}]", idMinisterio);
            throw new ObjectNotFoundException("Nenhum membro_ministerio encontrado com os filtros informados.");
        }

        Page<MembroMinisterioInfoMembroDTO> response = page.map(mapper::paraMembroMinisterioInfoMembroDTO);

        return response;
    }
}