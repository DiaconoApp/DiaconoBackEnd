package com.diacono.diacono.usecases.escalasevento;

import com.diacono.diacono.applications.dtos.RestResponseMessageDTO;
import com.diacono.diacono.applications.dtos.escalasevento.EscalaEventoEscaladoDTO;
import com.diacono.diacono.domain.entity.Evento;
import com.diacono.diacono.domain.repository.EventoRepository;
import com.diacono.diacono.global.error.exceptions.FieldInvalidException;
import com.diacono.diacono.global.error.exceptions.ObjectNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Service
public class AtualizarEscalaEventoPorEventoIdUseCase {
    // TODO: Revisar codigo, talvez possivel excesso de regras. Tambem testar diferentes cenarios do gerar escalas

    private final EventoRepository eventoRepository;
    private final GerarEscalaEventoUseCase gerarEscalaEventoUseCase;

    public AtualizarEscalaEventoPorEventoIdUseCase(
            EventoRepository eventoRepository,
            GerarEscalaEventoUseCase gerarEscalaEventoUseCase
    ) {
        this.eventoRepository = eventoRepository;
        this.gerarEscalaEventoUseCase = gerarEscalaEventoUseCase;
    }

    @Transactional
    public RestResponseMessageDTO execute(UUID igrejaId, UUID eventoId, List<EscalaEventoEscaladoDTO> listaEscalaEvento) {
        validarListaEscalaEvento(listaEscalaEvento);

        Evento evento = buscarEvento(igrejaId, eventoId);

        List<UUID> ministeriosEscaladosId = listaEscalaEvento.stream()
                .filter(item -> Boolean.TRUE.equals(item.isMinisterioEscalado()))
                .map(EscalaEventoEscaladoDTO::idExternoMinisterio)
                .filter(Objects::nonNull)
                .distinct()
                .toList();

        evento.setEscalaEvento(gerarEscalaEventoUseCase.executeParaAtualizacao(
                evento,
                evento.getEscalaEvento(),
                ministeriosEscaladosId
        ));

        eventoRepository.save(evento);

        return new RestResponseMessageDTO(HttpStatus.OK, "Escala do evento atualizada com sucesso");
    }

    private void validarListaEscalaEvento(List<EscalaEventoEscaladoDTO> listaEscalaEvento) {
        if (listaEscalaEvento == null || listaEscalaEvento.isEmpty()) {
            throw new FieldInvalidException("Lista de escalas do evento nao pode estar vazia");
        }
    }

    private Evento buscarEvento(UUID igrejaId, UUID eventoId) {
        Evento evento = eventoRepository.findByIdExterno(eventoId)
                .orElseThrow(() -> new ObjectNotFoundException("Evento nao encontrado"));

        if (evento.getIgreja() == null || !igrejaId.equals(evento.getIgreja().getIdExterno())) {
            throw new ObjectNotFoundException("Evento nao encontrado");
        }

        return evento;
    }
}

