package com.diacono.diacono.usecases.ministerio;

import com.diacono.diacono.applications.dtos.ministerio.MinisterioSuperSimplificadoDTO;
import com.diacono.diacono.domain.repository.MembroMinisterioRepository;
import com.diacono.diacono.global.error.exceptions.ObjectNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
public class BuscarMinisteriosLiderMinisterioUseCase {

    private static final Logger logger = LoggerFactory.getLogger(BuscarMinisteriosLiderMinisterioUseCase.class);

    private final MembroMinisterioRepository membroMinisterioRepository;

    public BuscarMinisteriosLiderMinisterioUseCase(MembroMinisterioRepository membroMinisterioRepository) {
        this.membroMinisterioRepository = membroMinisterioRepository;
    }

    @Transactional(readOnly = true)
    public List<MinisterioSuperSimplificadoDTO> execute(UUID igrejaId, UUID membroId) {

        List<MinisterioSuperSimplificadoDTO> ministerios = membroMinisterioRepository.buscarMinisterioLider(membroId, igrejaId);

        if (ministerios.isEmpty()) {
            logger.warn("Nenhum ministério encontrado para o líder. membroId=[{}] igrejaId=[{}]", membroId, igrejaId);
            throw new ObjectNotFoundException("Nenhum ministério encontrado para o líder informado.");
        }

        return ministerios;

    }

}