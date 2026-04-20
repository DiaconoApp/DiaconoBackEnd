package com.diacono.diacono.infrastructure.persistence.MinisteriosMembros;

import com.diacono.diacono.applications.dtos.ministerio.MinisterioSuperSimplificadoDTO;
import com.diacono.diacono.domain.repository.FindMinisteriosMembrosRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public class FindMinisteriosMembrosRepositoryImpl implements FindMinisteriosMembrosRepository {

    private final FindMinisteriosMembrosJpaRepository jpaRepository;

    public FindMinisteriosMembrosRepositoryImpl(FindMinisteriosMembrosJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public List<MinisterioSuperSimplificadoDTO> buscarMinisteriosMembro(UUID idExternoMembro, UUID idExternoIgreja) {
        return jpaRepository.buscarMinisteriosMembro(idExternoMembro, idExternoIgreja);
    }
}

