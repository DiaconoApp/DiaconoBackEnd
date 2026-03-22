package com.diacono.diacono.application.service;

import com.diacono.diacono.domain.entities.Igreja;
import com.diacono.diacono.application.exceptions.ObjectNotFoundException;
import com.diacono.diacono.infrastructure.persistence.IgrejaRepository;

import java.util.UUID;

public class IgrejaFetcher {

    private final IgrejaRepository repository;

    public IgrejaFetcher(IgrejaRepository repository) {
        this.repository = repository;
    }

    public Igreja buscarUUID(UUID idExterno){
        //adicionar validação da existência da Igreja -- SE DER ERRO LANÇAR EXCEÇÃO
        Igreja igreja = repository.findByIdExterno(idExterno);
        if(igreja == null){
            throw new ObjectNotFoundException("Igreja não encontrada");
        }

        return igreja;

    }

}
