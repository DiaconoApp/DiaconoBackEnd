package com.diacono.diacono.application.usecases;

import com.diacono.diacono.infrastructure.persistence.MembroMinisterioRepository;
import com.diacono.diacono.presentation.dto.response.RestResponseMessage;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class RemoverLiderMinisterioUseCase {

    private final MembroMinisterioRepository repository;

    public RemoverLiderMinisterioUseCase(MembroMinisterioRepository repository) {
        this.repository = repository;
    }

    @Transactional
    public RestResponseMessage removerMembroMinisterioLiderMinisterio(UUID idMinisterio, UUID idMembroMinisterio) {

        repository.deleteByMembroIdExternoAndMinisterioIdExterno(idMembroMinisterio, idMinisterio);
        return new RestResponseMessage(HttpStatus.OK, "Membro removido do ministério com sucesso");
    }
}
