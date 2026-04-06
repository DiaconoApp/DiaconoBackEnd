package com.diacono.diacono.usecases.ministerio;

import com.diacono.diacono.applications.dtos.ministerio.MinisterioSuperSimplificadoDTO;
import com.diacono.diacono.domain.repository.MembroMinisterioRepository;
import com.diacono.diacono.global.error.exceptions.ObjectNotFoundException;
import com.diacono.diacono.global.util.JwtUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
public class BuscarMinisteriosLiderMinisterioUseCase {

    private final MembroMinisterioRepository membroMinisterioRepository;
    private final JwtUtils jwtUtils;

public BuscarMinisteriosLiderMinisterioUseCase(MembroMinisterioRepository membroMinisterioRepository, JwtUtils jwtUtils) {
        this.membroMinisterioRepository = membroMinisterioRepository;
        this.jwtUtils = jwtUtils;
    }

    @Transactional(readOnly = true)
    public List<MinisterioSuperSimplificadoDTO> execute() {
        UUID idExternoMembro = jwtUtils.getSubject();
        UUID idIgreja = jwtUtils.getIgrejaId();

        return buscarMinisterioLider(idExternoMembro, idIgreja);
    }

    public List<MinisterioSuperSimplificadoDTO> buscarMinisterioLider(UUID idMembro, UUID idIgreja){

        List<MinisterioSuperSimplificadoDTO> ministerios = membroMinisterioRepository.buscarMinisterioLider(idMembro, idIgreja);

        if(ministerios.isEmpty()){
            throw new ObjectNotFoundException("Nenhum ministério encontrado para o líder informado.");
        }

        return ministerios;
    }
}