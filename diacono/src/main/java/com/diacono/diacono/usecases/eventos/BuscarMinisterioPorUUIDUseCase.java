package com.diacono.diacono.usecases.eventos;

import com.diacono.diacono.domain.entity.Ministerio;
import com.diacono.diacono.domain.repository.MinisteriosRepository;
import com.diacono.diacono.global.error.exceptions.ObjectNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.UUID;

@Service
public class BuscarMinisterioPorUUIDUseCase {

    private static final Logger logger = LoggerFactory.getLogger(BuscarMinisterioPorUUIDUseCase.class);

    private final MinisteriosRepository ministeriosRepository;

    public BuscarMinisterioPorUUIDUseCase(MinisteriosRepository ministeriosRepository) {
        this.ministeriosRepository = ministeriosRepository;
    }

    public Set<Ministerio> execute(List<UUID> idExterno, UUID igrejaId) {
        if (idExterno == null || idExterno.isEmpty()) {
            logger.debug("Busca de ministerbios com lista vazia. igrejaId=[{}]", igrejaId);
            throw new ObjectNotFoundException("Ministérios não encontrados");
        }

        Set<Ministerio> ministerios = this.ministeriosRepository.findAllByIdExternoInAndIgrejaId(idExterno, igrejaId);

        if (ministerios.isEmpty()) {
            logger.warn("Nenhum ministério encontrado para IDs informados. quantidade=[{}], igrejaId=[{}]", idExterno.size(), igrejaId);
            throw new ObjectNotFoundException("Ministérios não encontrados");
        }

        logger.info("Ministérios recuperados com sucesso. quantidade=[{}], igrejaId=[{}]", ministerios.size(), igrejaId);

        return ministerios;
    }
}
