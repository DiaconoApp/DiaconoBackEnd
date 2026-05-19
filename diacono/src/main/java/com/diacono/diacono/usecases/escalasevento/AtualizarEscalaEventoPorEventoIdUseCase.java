package com.diacono.diacono.usecases.escalasevento;

import com.diacono.diacono.applications.dtos.RestResponseMessageDTO;
import com.diacono.diacono.applications.dtos.escalasevento.EscalaEventoEscaladoDTO;
import com.diacono.diacono.domain.entity.EscalaEvento;
import com.diacono.diacono.domain.entity.Evento;
import com.diacono.diacono.domain.repository.EscalaMinisterioRepository;
import com.diacono.diacono.domain.repository.EventoRepository;
import com.diacono.diacono.domain.service.EscalaStatusDomainService;
import com.diacono.diacono.global.error.exceptions.FieldInvalidException;
import com.diacono.diacono.global.error.exceptions.ObjectNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

@Service
public class AtualizarEscalaEventoPorEventoIdUseCase {
    // TODO: Revisar codigo, talvez possivel excesso de regras. Tambem testar diferentes cenarios do gerar escalas
    private static final Logger logger = LoggerFactory.getLogger(AtualizarEscalaEventoPorEventoIdUseCase.class);

    private final EventoRepository eventoRepository;
    private final EscalaMinisterioRepository escalaMinisterioRepository;
    private final GerarEscalaEventoUseCase gerarEscalaEventoUseCase;
    private final EscalaStatusDomainService escalaStatusDomainService;

    public AtualizarEscalaEventoPorEventoIdUseCase(
            EventoRepository eventoRepository,
            EscalaMinisterioRepository escalaMinisterioRepository,
            GerarEscalaEventoUseCase gerarEscalaEventoUseCase,
            EscalaStatusDomainService escalaStatusDomainService
    ) {
        this.eventoRepository = eventoRepository;
        this.escalaMinisterioRepository = escalaMinisterioRepository;
        this.gerarEscalaEventoUseCase = gerarEscalaEventoUseCase;
        this.escalaStatusDomainService = escalaStatusDomainService;
    }

    @Transactional
    public RestResponseMessageDTO execute(UUID igrejaId, UUID eventoId, List<EscalaEventoEscaladoDTO> listaEscalaEvento) {
        validarIds(igrejaId, eventoId);
        validarListaEscalaEvento(listaEscalaEvento);

        Evento evento = buscarEvento(igrejaId, eventoId);

        List<UUID> ministeriosEscaladosId = listaEscalaEvento.stream()
                .filter(item -> Boolean.TRUE.equals(item.isMinisterioEscalado()))
                .map(EscalaEventoEscaladoDTO::idExternoMinisterio)
                .filter(Objects::nonNull)
                .distinct()
                .toList();

        logger.info("Atualizacao de escalas do evento: igrejaId=[{}], eventoId=[{}], ministeriosEscalados=[{}]",
                igrejaId, eventoId, ministeriosEscaladosId.size());

        List<UUID> escalasRemovidas = identificarEscalasRemovidas(evento.getEscalaEvento(), ministeriosEscaladosId);
        excluirEscalasMinisterioRemovidas(igrejaId, escalasRemovidas);

        evento.setEscalaEvento(gerarEscalaEventoUseCase.executeParaAtualizacao(
                evento,
                evento.getEscalaEvento(),
                ministeriosEscaladosId,
                igrejaId
        ));

        eventoRepository.save(evento);
        escalaStatusDomainService.recalcularStatusEvento(eventoId);

        return new RestResponseMessageDTO(HttpStatus.OK, "Escala do evento atualizada com sucesso");
    }

    private List<UUID> identificarEscalasRemovidas(Set<EscalaEvento> escalasAtuais, List<UUID> ministeriosEscaladosId) {
        if (escalasAtuais == null || escalasAtuais.isEmpty()) {
            return List.of();
        }

        return escalasAtuais.stream()
                .filter(escala -> escala.getMinisterio() != null)
                .filter(escala -> escala.getMinisterio().getIdExterno() != null)
                .filter(escala -> !ministeriosEscaladosId.contains(escala.getMinisterio().getIdExterno()))
                .map(EscalaEvento::getIdExterno)
                .filter(Objects::nonNull)
                .toList();
    }

    private void excluirEscalasMinisterioRemovidas(UUID igrejaId, List<UUID> escalasRemovidas) {
        for (UUID escalaEventoId : escalasRemovidas) {
            escalaMinisterioRepository.deleteByEscalaEventoIdAndIgrejaId(igrejaId, escalaEventoId);
        }
    }

    private void validarListaEscalaEvento(List<EscalaEventoEscaladoDTO> listaEscalaEvento) {
        if (listaEscalaEvento == null || listaEscalaEvento.isEmpty()) {
            logger.warn("Atualizacao de escalas com lista vazia");
            throw new FieldInvalidException("Lista de escalas do evento nao pode estar vazia");
        }

        for (EscalaEventoEscaladoDTO escalaEvento : listaEscalaEvento) {
            if (escalaEvento == null) {
                logger.warn("Atualizacao de escalas com item nulo na lista");
                throw new FieldInvalidException("Item da lista de escalas do evento nao pode ser nulo");
            }

            if (Boolean.TRUE.equals(escalaEvento.isMinisterioEscalado()) && escalaEvento.idExternoMinisterio() == null) {
                logger.warn("Atualizacao de escalas com ministerio escalado sem idExternoMinisterio");
                throw new FieldInvalidException("Ministerio escalado deve ter idExternoMinisterio");
            }
        }
    }

    private void validarIds(UUID igrejaId, UUID eventoId) {
        if (igrejaId == null || eventoId == null) {
            logger.warn("Atualizacao de escalas com ids invalidos: igrejaIdPresente=[{}], eventoIdPresente=[{}]",
                    igrejaId != null, eventoId != null);
            throw new FieldInvalidException("Ids de igreja e evento sao obrigatorios");
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
