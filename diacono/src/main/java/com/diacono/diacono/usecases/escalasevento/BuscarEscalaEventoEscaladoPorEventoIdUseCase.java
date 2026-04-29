package com.diacono.diacono.usecases.escalasevento;

import com.diacono.diacono.applications.dtos.escalasevento.EscalaEventoEscaladoDTO;
import com.diacono.diacono.domain.repository.EscalaEventoRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class BuscarEscalaEventoEscaladoPorEventoIdUseCase {
    private final EscalaEventoRepository escalaEventoRepository;

    public BuscarEscalaEventoEscaladoPorEventoIdUseCase(
            EscalaEventoRepository escalaEventoRepository
    ) {
        this.escalaEventoRepository = escalaEventoRepository;
    }

    public List<EscalaEventoEscaladoDTO> execute(UUID idIgreja, UUID eventoId) {
        return escalaEventoRepository
                .findEscalaEventoEscaladoByEventoId(idIgreja, eventoId);
    }
}
