package com.diacono.diacono.usecases.eventos;

import com.diacono.diacono.applications.dtos.RestResponseMessageDTO;
import com.diacono.diacono.domain.entity.Evento;
import com.diacono.diacono.domain.repository.EventoRepository;
import com.diacono.diacono.global.error.exceptions.ObjectNotFoundException;
import com.diacono.diacono.usecases.eventos.validation.ValidarIdExternoPreenchido;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class ApagarEventoUnicoUseCase {

    private static final Logger logger = LoggerFactory.getLogger(ApagarEventoUnicoUseCase.class);

    private final EventoRepository eventoRepository;
    private final ValidarIdExternoPreenchido validarIdExternoPreenchido;

    public ApagarEventoUnicoUseCase(
            EventoRepository eventoRepository,
            ValidarIdExternoPreenchido validarIdExternoPreenchido
    ) {
        this.eventoRepository = eventoRepository;
        this.validarIdExternoPreenchido = validarIdExternoPreenchido;
    }

    @Transactional
    public RestResponseMessageDTO execute(UUID id, UUID igrejaId) {

        validarIdExternoPreenchido.validarIdExternoPreenchido(id);

        if (igrejaId != null) {
            Evento evento = eventoRepository.findByIdExterno(id)
                    .orElseThrow(() -> new ObjectNotFoundException("Não foi possível apagar o evento, verifique se o evento existe"));

            if (evento.getIgreja() == null || evento.getIgreja().getIdExterno() == null || !igrejaId.equals(evento.getIgreja().getIdExterno())) {
                logger.warn("Tentativa de exclusão de evento fora do escopo da igreja autenticada. eventoId=[{}] igrejaId=[{}]", id, igrejaId);
                throw new ObjectNotFoundException("Não foi possível apagar o evento, verifique se o evento existe");
            }
        }

        long deleteCount = eventoRepository.deleteByIdExterno(id);

        if (deleteCount == 0) {
            throw new ObjectNotFoundException("Não foi possível apagar o evento, verifique se o evento existe");
        }

        logger.info("Evento apagado com sucesso. eventoId=[{}] igrejaId=[{}]", id, igrejaId);

        return new RestResponseMessageDTO(HttpStatus.OK, "Evento apagado com sucesso");
    }
}