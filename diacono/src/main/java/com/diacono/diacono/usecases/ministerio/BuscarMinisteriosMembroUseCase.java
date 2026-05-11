package com.diacono.diacono.usecases.ministerio;

import com.diacono.diacono.applications.dtos.ministerio.MinisterioSuperSimplificadoDTO;
import com.diacono.diacono.domain.repository.MinisteriosRepository;
import com.diacono.diacono.global.error.exceptions.ObjectNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
public class BuscarMinisteriosMembroUseCase {

    private static final Logger logger = LoggerFactory.getLogger(BuscarMinisteriosMembroUseCase.class);

    private final MinisteriosRepository ministeriosRepository;

    public BuscarMinisteriosMembroUseCase(MinisteriosRepository ministeriosRepository) {
        this.ministeriosRepository = ministeriosRepository;
    }

    @Transactional(readOnly = true)
    public List<MinisterioSuperSimplificadoDTO> execute(UUID igrejaId, UUID membroId) {

        List<MinisterioSuperSimplificadoDTO> ministerios = ministeriosRepository.buscarMinisteriosMembro(membroId, igrejaId);

        if (ministerios.isEmpty()) {
            logger.warn("Nenhum ministério encontrado para o membro. membroId=[{}] igrejaId=[{}]", membroId, igrejaId);
            throw new ObjectNotFoundException("Nenhum ministério encontrado para o membro informado.");
        }

        return ministerios;

    }

}