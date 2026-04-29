package com.diacono.diacono.usecases.ministerio;

import com.diacono.diacono.applications.dtos.ministerio.MinisterioSuperSimplificadoDTO;
import com.diacono.diacono.domain.repository.MinisteriosRepository;
import com.diacono.diacono.global.error.exceptions.ObjectNotFoundException;
import com.diacono.diacono.global.util.JwtUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
public class BuscarMinisteriosMembroUseCase {

    private final MinisteriosRepository ministeriosRepository;
    private final JwtUtils jwtUtils;

    public BuscarMinisteriosMembroUseCase(MinisteriosRepository ministeriosRepository, JwtUtils jwtUtils) {
        this.ministeriosRepository = ministeriosRepository;
        this.jwtUtils = jwtUtils;
    }

    @Transactional(readOnly = true)
    public List<MinisterioSuperSimplificadoDTO> execute() {
        UUID idExternoMembro = jwtUtils.getSubject();
        UUID idIgreja = jwtUtils.getIgrejaId();

        return buscarMinisteriosMembro(idExternoMembro, idIgreja);
    }

    public List<MinisterioSuperSimplificadoDTO> buscarMinisteriosMembro(UUID idMembro, UUID idIgreja) {
        List<MinisterioSuperSimplificadoDTO> ministerios = ministeriosRepository.buscarMinisteriosMembro(idMembro, idIgreja);

        if (ministerios.isEmpty()) {
            throw new ObjectNotFoundException("Nenhum ministério encontrado para o membro informado.");
        }

        return ministerios;
    }
}

