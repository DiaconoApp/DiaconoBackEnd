package com.diacono.diacono.usecases.membro;

import com.diacono.diacono.applications.dtos.membro.MembroResponseDTO;
import com.diacono.diacono.applications.mappers.membro.MembroMapper;
import com.diacono.diacono.domain.entity.Membro;
import com.diacono.diacono.domain.repository.MembroRepository;
import com.diacono.diacono.global.error.exceptions.ObjectNotFoundException;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class BuscarMembroPorUUIDUseCase {

    private final MembroRepository membroRepository;
    private final MembroMapper membroMapper;

    public BuscarMembroPorUUIDUseCase(MembroRepository membroRepository, MembroMapper membroMapper) {
        this.membroRepository = membroRepository;
        this.membroMapper = membroMapper;
    }

    public MembroResponseDTO execute(UUID idExterno) {
        Membro membro = membroRepository.findByIdExterno(idExterno)
                .orElseThrow(() -> new ObjectNotFoundException("Membro não encontrado"));

        return membroMapper.paraMembroResponseDTO(membro);
    }
}