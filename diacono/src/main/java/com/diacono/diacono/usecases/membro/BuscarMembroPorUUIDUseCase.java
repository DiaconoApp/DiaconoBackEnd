package com.diacono.diacono.usecases.membro;

import com.diacono.diacono.applications.dtos.membro.MembroDetalheResponseDTO;
import com.diacono.diacono.applications.mappers.membro.MembroMapper;
import com.diacono.diacono.domain.entity.Membro;
import com.diacono.diacono.domain.repository.MembroRepository;
import com.diacono.diacono.global.error.exceptions.ObjectNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class BuscarMembroPorUUIDUseCase {

    private final MembroRepository membroRepository;
    private final MembroMapper membroMapper;

    public BuscarMembroPorUUIDUseCase(MembroRepository membroRepository, MembroMapper membroMapper) {
        this.membroRepository = membroRepository;
        this.membroMapper = membroMapper;
    }

    @Transactional(readOnly = true)
    public MembroDetalheResponseDTO execute(UUID idExterno, UUID igrejaId) {
        Membro membro = membroRepository.findByIdExterno(idExterno)
                .orElseThrow(() -> new ObjectNotFoundException("Membro não encontrado"));

        if (igrejaId != null) {
            if (membro.getIgreja() == null || !igrejaId.equals(membro.getIgreja().getIdExterno())) {
                throw new ObjectNotFoundException("Membro não encontrado");
            }
        }

        return membroMapper.paraMembroDetalheResponseDTO(membro);
    }
}