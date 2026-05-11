package com.diacono.diacono.usecases.membro;

import com.diacono.diacono.applications.dtos.membro.MembroResponseDTO;
import com.diacono.diacono.applications.mappers.membro.MembroMapper;
import com.diacono.diacono.domain.entity.Membro;
import com.diacono.diacono.domain.repository.MembroRepository;
import com.diacono.diacono.global.error.exceptions.ObjectNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class BuscarTodosSemFiltroUseCase {

    private final MembroRepository membroRepository;
    private final MembroMapper membroMapper;

    public BuscarTodosSemFiltroUseCase(MembroRepository membroRepository, MembroMapper membroMapper) {
        this.membroRepository = membroRepository;
        this.membroMapper = membroMapper;
    }

    @Transactional(readOnly = true)
    public Page<MembroResponseDTO> execute(Pageable pageable, UUID igrejaId) {

        Page<Membro> membrosPage = membroRepository.findByIgrejaIdExterno(igrejaId, pageable);

        validarMembrosEncontradosPage(membrosPage);

        return membrosPage.map(membroMapper::paraMembroResponseDTO);
    }

    private void validarMembrosEncontradosPage(Page<Membro> membros) {
        if (membros.isEmpty()) {
            throw new ObjectNotFoundException("Nenhum membro encontrado");
        }
    }
}