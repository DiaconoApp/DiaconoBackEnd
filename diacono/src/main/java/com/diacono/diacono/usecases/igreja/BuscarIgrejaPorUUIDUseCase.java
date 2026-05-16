package com.diacono.diacono.usecases.igreja;

import com.diacono.diacono.domain.entity.Igreja;
import com.diacono.diacono.domain.repository.IgrejaRepository;
import com.diacono.diacono.global.error.exceptions.FieldInvalidException;
import com.diacono.diacono.global.error.exceptions.ObjectNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class BuscarIgrejaPorUUIDUseCase {

    private static final Logger logger = LoggerFactory.getLogger(BuscarIgrejaPorUUIDUseCase.class);

    private final IgrejaRepository igrejaRepository;

    public BuscarIgrejaPorUUIDUseCase(IgrejaRepository igrejaRepository) {
        this.igrejaRepository = igrejaRepository;
    }

    /*ESSE MÉTODO SE RELACIONA COM EVENTO*/
    public Igreja execute(UUID idExterno){
        if (idExterno == null) {
            logger.warn("Tentativa de buscar igreja sem idExterno informado");
            throw new FieldInvalidException("O Id da igreja é obrigatório");
        }

        return igrejaRepository.findByIdExterno(idExterno)
                .orElseThrow(() -> {
                    logger.warn("Igreja nao encontrada para idExterno={}", idExterno);
                    return new ObjectNotFoundException("Igreja não encontrada");
                });
    }
}
