package com.diacono.diacono.usecases.eventos;

import com.diacono.diacono.applications.dtos.RestResponseMessageDTO;
import com.diacono.diacono.domain.entity.Evento;
import com.diacono.diacono.domain.repository.EventoRepository;
import com.diacono.diacono.global.error.exceptions.ObjectNotFoundException;
import com.diacono.diacono.global.util.JwtUtils;
import com.diacono.diacono.usecases.eventos.validation.ValidarIdExternoPreenchido;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
public class ApagarEventosMultiplosUseCase {

    private final EventoRepository eventoRepository;
    private final ValidarIdExternoPreenchido validarIdExternoPreenchido;
    private final JwtUtils jwtUtils;

    public ApagarEventosMultiplosUseCase(
            EventoRepository eventoRepository,
            ValidarIdExternoPreenchido validarIdExternoPreenchido,
            JwtUtils jwtUtils
    ) {
        this.eventoRepository = eventoRepository;
        this.validarIdExternoPreenchido = validarIdExternoPreenchido;
        this.jwtUtils = jwtUtils;
    }

    @Transactional
    public RestResponseMessageDTO execute(UUID idEvento) {

        validarIdExternoPreenchido.validarIdExternoPreenchido(idEvento);

        Evento evento = eventoRepository.findByIdExterno(idEvento)
                .orElseThrow(() -> new ObjectNotFoundException("Não foi possível apagar o evento, verifique se o evento existe"));

        List<Evento> eventos = eventoRepository.findByPeriodoAndRecorrencia(
                evento.getRecorrencia(),
                evento.getDataHoraInicio(),
                jwtUtils.getIgrejaId()
        );

        if (!eventos.contains(evento)) {
            eventos.add(evento);
        }

        eventoRepository.deleteAll(eventos);

        return new RestResponseMessageDTO(HttpStatus.OK, "Eventos apagados com sucesso");
    }
}