package com.diacono.diacono.usecases;

import com.diacono.diacono.applications.dtos.membro.MembroDetalheResponseDTO;
import com.diacono.diacono.applications.mappers.membro.MembroMapper;
import com.diacono.diacono.domain.entity.Membro;
import com.diacono.diacono.domain.repository.MembroRepository;
import com.diacono.diacono.global.error.exceptions.ObjectNotFoundException;
import com.diacono.diacono.global.util.JwtUtils;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class BuscarPerfilUseCase {

    private final MembroRepository membroRepository;
    private final MembroMapper membroMapper;
    private final JwtUtils jwtUtils;

    public BuscarPerfilUseCase(MembroRepository membroRepository, MembroMapper membroMapper, JwtUtils jwtUtils) {
        this.membroRepository = membroRepository;
        this.membroMapper = membroMapper;
        this.jwtUtils = jwtUtils;
    }

    public MembroDetalheResponseDTO execute() {
        UUID membroId = jwtUtils.getSubject();
        UUID igrejaId = jwtUtils.getIgrejaId();

        Membro membro = membroRepository.findByIdExterno(membroId)
                .filter(m -> m.getIgreja() != null && igrejaId.equals(m.getIgreja().getIdExterno()))
                .orElseThrow(() -> new ObjectNotFoundException("Perfil do membro não encontrado"));

        return membroMapper.paraMembroDetalheResponseDTO(membro);
    }
}

