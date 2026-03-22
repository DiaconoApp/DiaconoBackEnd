package com.diacono.diacono.application.service;

import com.diacono.diacono.domain.entities.Ministerio;
import com.diacono.diacono.application.exceptions.ObjectNotFoundException;
import com.diacono.diacono.infrastructure.persistence.MinisteriosRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.UUID;

@Service
public class MinisteriosFetcher {

    private final MinisteriosRepository  repository;

    public MinisteriosFetcher(MinisteriosRepository repository) {
        this.repository = repository;
    }

    public Set<Ministerio> buscarPorUUID(List<UUID> idExterno) {
        Set<Ministerio> ministerios = repository.findAllByIdExternoIn(idExterno);

        if (ministerios.isEmpty()) {
            throw new ObjectNotFoundException("Ministérios não encontrados");
        }

        return ministerios;
    }

    public Ministerio buscarPorUUID(UUID idExterno) {
        Ministerio ministerios = repository.findByIdExterno(idExterno);

        if (ministerios == null) {
            throw new ObjectNotFoundException("Ministérios não encontrados");
        }

        return ministerios;
    }
}
