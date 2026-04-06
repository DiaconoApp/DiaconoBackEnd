package com.diacono.diacono.usecases.eventos;

import com.diacono.diacono.domain.entity.Ministerio;
import com.diacono.diacono.domain.repository.MinisteriosRepository;
import com.diacono.diacono.global.error.exceptions.ObjectNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.UUID;

@Service
public class BuscarMinisterioPorUUIDUseCase {

    private final MinisteriosRepository ministeriosRepository;

    public BuscarMinisterioPorUUIDUseCase(MinisteriosRepository ministeriosRepository) {
        this.ministeriosRepository = ministeriosRepository;
    }

    public Set<Ministerio> execute(List<UUID> idExterno) {
        Set<Ministerio> ministerios = this.ministeriosRepository.findAllByIdExternoIn(idExterno);

        if (ministerios.isEmpty()) {
            throw new ObjectNotFoundException("Ministérios não encontrados");
        }

        return ministerios;
    }
}
