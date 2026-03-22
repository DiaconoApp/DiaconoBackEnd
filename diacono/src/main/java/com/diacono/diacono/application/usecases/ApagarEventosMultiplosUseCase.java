package com.diacono.diacono.application.usecases;

import com.diacono.diacono.domain.entities.Evento;
import com.diacono.diacono.application.exceptions.ObjectNotFoundException;
import com.diacono.diacono.infrastructure.extractor.JwtClaimsExtractor;
import com.diacono.diacono.infrastructure.persistence.EventoRepository;
import com.diacono.diacono.presentation.dto.response.RestResponseMessage;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

import static com.diacono.diacono.application.validators.EventoValidator.validarIdExternoPreenchido;

@Service
public class ApagarEventosMultiplosUseCase {

    private final EventoRepository repository;
    private final JwtClaimsExtractor jwtClaimsExtractor;

    public ApagarEventosMultiplosUseCase(EventoRepository repository, JwtClaimsExtractor jwtClaimsExtractor) {
        this.repository = repository;
        this.jwtClaimsExtractor = jwtClaimsExtractor;
    }

    @Transactional
    public RestResponseMessage apagarEventosMultiplos(UUID idEvento){

        validarIdExternoPreenchido(idEvento);

        Evento evento = repository.findByIdExterno(idEvento);
        if (evento == null) {
            throw new ObjectNotFoundException("Não foi possível apagar o evento, verifique se o evento existe");
        }

        List<Evento> eventos = repository.findByPeriodoAndRecorrencia(evento.getRecorrencia(), evento.getDataHoraInicio(), jwtClaimsExtractor.getIgrejaId());
        System.out.println(eventos);
        if (!eventos.contains(evento)) {
            eventos.add(evento);
        }

        repository.deleteAll(eventos);

        RestResponseMessage message = new RestResponseMessage(HttpStatus.OK, "Evento apagado com sucesso");

        return message;

    }

}
