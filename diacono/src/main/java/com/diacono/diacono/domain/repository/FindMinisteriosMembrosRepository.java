package com.diacono.diacono.domain.repository;

import com.diacono.diacono.applications.dtos.ministerio.MinisterioSuperSimplificadoDTO;

import java.util.List;
import java.util.UUID;

public interface FindMinisteriosMembrosRepository {
    List<MinisterioSuperSimplificadoDTO> buscarMinisteriosMembro(UUID idExternoMembro, UUID idExternoIgreja);
}

