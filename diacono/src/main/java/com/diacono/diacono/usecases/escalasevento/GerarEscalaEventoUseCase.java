package com.diacono.diacono.usecases.escalasevento;

import com.diacono.diacono.domain.entity.EscalaEvento;
import com.diacono.diacono.domain.entity.Evento;
import com.diacono.diacono.domain.entity.Ministerio;
import com.diacono.diacono.global.error.exceptions.FieldInvalidException;
import com.diacono.diacono.usecases.eventos.BuscarMinisterioPorUUIDUseCase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.HashMap;
import java.util.stream.Collectors;

@Service
public class GerarEscalaEventoUseCase {
    // NOTE: Validacoes de acesso/scoping por Igreja sao responsabilidade do chamador (controller/usecases acima)

    private static final Logger logger = LoggerFactory.getLogger(GerarEscalaEventoUseCase.class);
    private final BuscarMinisterioPorUUIDUseCase buscarMinisterioPorUUIDUseCase;

    public GerarEscalaEventoUseCase(BuscarMinisterioPorUUIDUseCase buscarMinisterioPorUUIDUseCase) {
        this.buscarMinisterioPorUUIDUseCase = buscarMinisterioPorUUIDUseCase;
    }

    public Set<EscalaEvento> executeParaCriacao(Evento evento, List<UUID> ministeriosId, UUID igrejaId) {
        validarEvento(evento, "criacao");
        validarListaMinisterios(ministeriosId, "criacao", evento.getIdExterno());

        Set<Ministerio> ministerios = buscarMinisterioPorUUIDUseCase.execute(ministeriosId, igrejaId);
        Set<EscalaEvento> escalasEvento = new HashSet<>();

        for (Ministerio ministerio : ministerios) {
            EscalaEvento escala = new EscalaEvento();
            escala.setEvento(evento);
            escala.setMinisterio(ministerio);
            escalasEvento.add(escala);
        }

        logger.info("Escalas de evento geradas na criacao: quantidadeEscalas=[{}], eventoId=[{}]", escalasEvento.size(), evento.getIdExterno());
        return escalasEvento;
    }

    public Set<EscalaEvento> executeParaAtualizacao(Evento evento, Set<EscalaEvento> escalasEventoOrigem, List<UUID> ministeriosId, UUID igrejaId) {
        validarEvento(evento, "atualizacao");

        if (ministeriosId == null || ministeriosId.isEmpty()) {
            logger.debug("Atualizacao de escalas requerida com lista vazia de ministérios: eventoId=[{}]", evento.getIdExterno());
            return new HashSet<>();
        }

        validarListaMinisterios(ministeriosId, "atualizacao", evento.getIdExterno());
        Set<Ministerio> ministerios = buscarMinisterioPorUUIDUseCase.execute(ministeriosId, igrejaId);
        Set<EscalaEvento> escalasAtualizadas = new HashSet<>();

        Map<UUID, EscalaEvento> escalasPorMinisterio = new HashMap<>();
        if (escalasEventoOrigem != null) {
            escalasPorMinisterio = escalasEventoOrigem.stream()
                    .filter(escalaEvento -> escalaEvento.getMinisterio() != null && escalaEvento.getMinisterio().getIdExterno() != null)
                    .collect(Collectors.toMap(
                            escalaEvento -> escalaEvento.getMinisterio().getIdExterno(),
                            escalaEvento -> escalaEvento,
                            (escalaExistente, escalaDuplicada) -> escalaExistente,
                            HashMap::new
                    ));
        }

        for (Ministerio ministerio : ministerios) {
            EscalaEvento escalaExistente = escalasPorMinisterio.remove(ministerio.getIdExterno());

            if (escalaExistente != null) {
                escalaExistente.setEvento(evento);
                escalaExistente.setMinisterio(ministerio);
                escalasAtualizadas.add(escalaExistente);
                continue;
            }

            EscalaEvento novaEscala = new EscalaEvento();
            novaEscala.setEvento(evento);
            novaEscala.setMinisterio(ministerio);
            novaEscala.setStatusEscalaEvento(com.diacono.diacono.domain.enums.EnumStatusEvento.PENDENTE);
            escalasAtualizadas.add(novaEscala);
        }

        logger.info("Escalas de evento atualizadas: quantidadeTotal=[{}], novasEscalas=[{}], eventoId=[{}]", escalasAtualizadas.size(), escalasAtualizadas.size() - escalasPorMinisterio.size(), evento.getIdExterno());
        return escalasAtualizadas;
    }

    public Set<EscalaEvento> executeParaClonagem(Evento evento, Set<EscalaEvento> escalasEventoOrigem) {
        validarEvento(evento, "clonagem");
        if (escalasEventoOrigem == null) {
            logger.warn("Clonagem de escalas com origem nula: eventoId=[{}]", evento.getIdExterno());
            throw new FieldInvalidException("Lista de escalas de origem nao pode ser nula");
        }

        Set<EscalaEvento> escalasEvento = new HashSet<>();

        for (EscalaEvento escalaOrigem : escalasEventoOrigem) {
            EscalaEvento escala = new EscalaEvento();
            escala.setEvento(evento);
            escala.setMinisterio(escalaOrigem.getMinisterio());
            escala.setStatusEscalaEvento(escalaOrigem.getStatusEscalaEvento());
            escalasEvento.add(escala);
        }

        return escalasEvento;
    }

    private void validarEvento(Evento evento, String operacao) {
        if (evento == null || evento.getIdExterno() == null) {
            logger.warn("Operacao de escala com evento invalido: operacao=[{}], eventoIdPresente=[{}]",
                    operacao, evento != null);
            throw new FieldInvalidException("Evento é obrigatório para gerar escala");
        }
    }

    private void validarListaMinisterios(List<UUID> ministeriosId, String operacao, UUID eventoId) {
        if (ministeriosId == null) {
            logger.warn("Operacao de escala com lista de ministérios nula: operacao=[{}], eventoId=[{}]", operacao, eventoId);
            throw new FieldInvalidException("Lista de ministérios nao pode ser nula");
        }

        if (ministeriosId.stream().anyMatch(java.util.Objects::isNull)) {
            logger.warn("Operacao de escala com ministerioId nulo: operacao=[{}], eventoId=[{}]", operacao, eventoId);
            throw new FieldInvalidException("Lista de ministérios contem id nulo");
        }
    }
}
