package com.diacono.diacono.application.usecases;

import com.diacono.diacono.infrastructure.persistence.MembroMinisterioRepository;
import com.diacono.diacono.presentation.dto.response.MembroMinisterioInfoMembroDTO;

import java.util.UUID;

@Service
public class BuscarPorLiderMinisterioUseCase {

    private final MembroMinisterioRepository repository;

    public BuscarPorLiderMinisterioUseCase(MembroMinisterioRepository repository) {
        this.repository = repository;
    }

    public Page<MembroMinisterioInfoMembroDTO> buscarMembroMinisterioLiderMinisterio(UUID idMinisterio, Pageable page) {
        return repository.buscarPorMembroMinisterioSemFiltro(idMinisterio, page);
    }

}
