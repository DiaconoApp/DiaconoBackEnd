package com.diacono.diacono.application.usecases;

import com.diacono.diacono.domain.enums.EnumStatusMembro;
import com.diacono.diacono.infrastructure.persistence.MembroMinisterioRepository;
import com.diacono.diacono.presentation.dto.response.MembroMinisterioInfoMembroDTO;

import java.util.UUID;

@Service
public class BuscarMinisterioLiderComFiltroUseCase {

    private final MembroMinisterioRepository repository;

    public BuscarMinisterioLiderComFiltroUseCase(MembroMinisterioRepository repository) {
        this.repository = repository;
    }

    public Page<MembroMinisterioInfoMembroDTO> buscarMembroMinisterioLiderMinisterioComFiltro(UUID idMinisterio, Pageable page, String texto, EnumStatusMembro status) {
        return repository.buscarPorMembroMinisterioComFiltro(idMinisterio, page, texto, status);
    }

}
