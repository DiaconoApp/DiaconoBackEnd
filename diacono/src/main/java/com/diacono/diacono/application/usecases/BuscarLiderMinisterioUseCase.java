package com.diacono.diacono.application.usecases;

import com.diacono.diacono.infrastructure.extractor.JwtClaimsExtractor;
import com.diacono.diacono.infrastructure.persistence.MembroMinisterioRepository;
import com.diacono.diacono.presentation.dto.MinisterioSuperSimplificadoDTO;

import java.util.List;
import java.util.UUID;

@Service
public class BuscarLiderMinisterioUseCase {

    private final JwtClaimsExtractor jwtClaimsExtractor;
    private final MembroMinisterioRepository repository;

    public BuscarLiderMinisterioUseCase(JwtClaimsExtractor jwtClaimsExtractor, MembroMinisterioRepository repository) {
        this.jwtClaimsExtractor = jwtClaimsExtractor;
        this.repository = repository;
    }

    public List<MinisterioSuperSimplificadoDTO> buscarMinisteriosLiderMinisterio() {

        UUID idExternoMembro = jwtClaimsExtractor.getSubject();
        UUID idIgreja = jwtClaimsExtractor.getIgrejaId();

        List<MinisterioSuperSimplificadoDTO> ministerios =  repository.buscarMinisterioLider(idExternoMembro, idIgreja);

        return ministerios;
    }

}
