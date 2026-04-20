package com.diacono.diacono.usecases.escalasministerio;

import com.diacono.diacono.applications.dtos.RestResponseMessageDTO;
import com.diacono.diacono.applications.dtos.escalaministerio.EscalaMembroMinisterioDTO;
import com.diacono.diacono.applications.dtos.escalaministerio.EscalaMinisterioSalvarDTO;
import com.diacono.diacono.applications.dtos.ministerio.MinisterioSuperSimplificadoDTO;
import com.diacono.diacono.domain.entity.EscalaEvento;
import com.diacono.diacono.domain.entity.EscalaMinisterio;
import com.diacono.diacono.domain.entity.MembroMinisterio;
import com.diacono.diacono.domain.enums.EnumStatusEscalaMinisterio;
import com.diacono.diacono.domain.repository.EscalaEventoRepository;
import com.diacono.diacono.domain.repository.EscalaMinisterioRepository;
import com.diacono.diacono.domain.repository.MembroMinisterioRepository;
import com.diacono.diacono.domain.service.EscalaStatusDomainService;
import com.diacono.diacono.global.error.exceptions.FieldInvalidException;
import com.diacono.diacono.global.error.exceptions.ObjectNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class SalvarEscalaMinisterioPorEscalaEventoIdUseCase {

    private final EscalaMinisterioRepository escalaMinisterioRepository;
    private final EscalaEventoRepository escalaEventoRepository;
    private final MembroMinisterioRepository membroMinisterioRepository;
    private final EscalaStatusDomainService escalaStatusDomainService;

    public SalvarEscalaMinisterioPorEscalaEventoIdUseCase(
            EscalaMinisterioRepository escalaMinisterioRepository,
            EscalaEventoRepository escalaEventoRepository,
            MembroMinisterioRepository membroMinisterioRepository,
            EscalaStatusDomainService escalaStatusDomainService
    ) {
        this.escalaMinisterioRepository = escalaMinisterioRepository;
        this.escalaEventoRepository = escalaEventoRepository;
        this.membroMinisterioRepository = membroMinisterioRepository;
        this.escalaStatusDomainService = escalaStatusDomainService;
    }

    @Transactional
    public RestResponseMessageDTO execute(
            UUID escalaEventoId,
            UUID igrejaId,
            UUID membroId,
            List<EscalaMinisterioSalvarDTO> escalasMinisterio
    ) {
        validarRequest(escalasMinisterio);
        validarEscalaEventoId(escalaEventoId, igrejaId, membroId);
        validarMembrosDaEscala(escalaEventoId, igrejaId, escalasMinisterio);
        validarConflitoDeEscala(escalaEventoId, igrejaId, escalasMinisterio);

        EscalaEvento escalaEvento = buscarEscalaEvento(igrejaId, escalaEventoId);

        List<UUID> idsMembrosMinisterioDisponiveis = extrairIdsMembrosMinisterioDisponiveis(igrejaId, escalaEventoId);
        Map<UUID, MembroMinisterio> membrosMinisterioPorId = buscarMembrosMinisterioPorId(igrejaId, escalaEventoId, idsMembrosMinisterioDisponiveis);

        List<EscalaMinisterio> escalasParaSalvar = montarEscalasParaSalver(membrosMinisterioPorId, escalaEvento, escalasMinisterio);

        escalaMinisterioRepository.replaceEscalaMinisterioByEscalaEventoId(igrejaId, escalaEventoId, escalasParaSalvar);
        escalaStatusDomainService.recalcularStatusPorEscalaEventoId(escalaEventoId);

        return new RestResponseMessageDTO(HttpStatus.OK, "Escala de membros do ministério atualizada com sucesso");
    }

    private void validarRequest(List<EscalaMinisterioSalvarDTO> escalasMinisterio) {
        if (escalasMinisterio == null) {
            throw new FieldInvalidException("Lista de escala do ministério não pode ser nula");
        }

        boolean possuiIdMembroNulo = escalasMinisterio.stream()
                .anyMatch(item -> item.idExternoMembroMinisterio() == null);

        if (possuiIdMembroNulo) {
            throw new FieldInvalidException("É obrigatório informar o id do membro do ministério");
        }

        long totalMembros = escalasMinisterio.size();

        long membrosDistintos = escalasMinisterio.stream()
                .map(EscalaMinisterioSalvarDTO::idExternoMembroMinisterio)
                .distinct()
                .count();

        if (totalMembros != membrosDistintos) {
            throw new FieldInvalidException("A lista de escalados contém membros duplicados");
        }
    }

    private void validarEscalaEventoId(UUID escalaEventoId, UUID igrejaId, UUID membroId) {
        UUID ministerioId = escalaEventoRepository.findMinisterioIdByEscalaEventoId(igrejaId, escalaEventoId);

        if (ministerioId == null) {
            throw new ObjectNotFoundException("Escala evento não encontrada para a igreja informada.");
        }

        List<UUID> listaMinisteriosLider = membroMinisterioRepository
                .buscarMinisterioLider(membroId, igrejaId)
                .stream()
                .map(MinisterioSuperSimplificadoDTO::idExterno)
                .toList();

        if (!listaMinisteriosLider.contains(ministerioId)) {
            throw new ObjectNotFoundException("O líder informado não possui vínculo com o ministério solicitado.");
        }
    }

    private void validarMembrosDaEscala(
            UUID escalaEventoId,
            UUID igrejaId,
            List<EscalaMinisterioSalvarDTO> escalasMinisterio
    ) {
        Set<UUID> idsMembrosDisponiveis = escalaMinisterioRepository
                .findEscalaMembroMinisterioByEscalaEventoId(igrejaId, escalaEventoId)
                .stream()
                .map(EscalaMembroMinisterioDTO::membroMinisterioId)
                .collect(java.util.stream.Collectors.toSet());

        boolean possuiMembroInvalido = escalasMinisterio.stream()
                .map(EscalaMinisterioSalvarDTO::idExternoMembroMinisterio)
                .anyMatch(idMembroMinisterio -> !idsMembrosDisponiveis.contains(idMembroMinisterio));

        if (possuiMembroInvalido) {
            throw new FieldInvalidException("A lista contém membros que não pertencem ao ministério da escala informada");
        }
    }

    private void validarConflitoDeEscala(
            UUID escalaEventoId,
            UUID igrejaId,
            List<EscalaMinisterioSalvarDTO> escalasMinisterio
    ) {
        Set<UUID> membrosOcupados = Set.copyOf(
                escalaMinisterioRepository.findMembrosMinisterioOcupadosByEscalaEventoId(igrejaId, escalaEventoId)
        );

        boolean existeConflito = escalasMinisterio.stream()
                .map(EscalaMinisterioSalvarDTO::idExternoMembroMinisterio)
                .anyMatch(membrosOcupados::contains);

        if (existeConflito) {
            throw new FieldInvalidException("A lista contém membros com conflito de escala para este horário");
        }
    }

    private EscalaEvento buscarEscalaEvento(UUID igrejaId, UUID escalaEventoId) {
        EscalaEvento escalaEvento = escalaEventoRepository.findEscalaEventoByIdExternoAndIgrejaId(igrejaId, escalaEventoId);

        if (escalaEvento == null) {
            throw new ObjectNotFoundException("Escala evento não encontrada para a igreja informada.");
        }

        return escalaEvento;
    }

    private List<UUID> extrairIdsMembrosMinisterioDisponiveis(UUID igrejaId, UUID escalaEventoId) {
        return escalaMinisterioRepository
                .findEscalaMembroMinisterioByEscalaEventoId(igrejaId, escalaEventoId)
                .stream()
                .map(EscalaMembroMinisterioDTO::membroMinisterioId)
                .toList();
    }

    private Map<UUID, MembroMinisterio> buscarMembrosMinisterioPorId(UUID igrejaId, UUID escalaEventoId, List<UUID> idsMembrosMinisterio) {
        return escalaMinisterioRepository.findMembrosMinisterioByEscalaEventoIdAndIds (igrejaId, escalaEventoId, idsMembrosMinisterio);
    }

    private List<EscalaMinisterio> montarEscalasParaSalver (
            Map<UUID, MembroMinisterio> membrosMinisterioPorId,
            EscalaEvento escalaEvento,
            List<EscalaMinisterioSalvarDTO> escalasMinisterio
    ) {
        List<EscalaMinisterio> escalasParaSalvar = new ArrayList<>();
        for (EscalaMinisterioSalvarDTO item : escalasMinisterio) {
            escalasParaSalvar.add(EscalaMinisterio.builder()
                    .escalaEvento(escalaEvento)
                    .membroMinisterio(membrosMinisterioPorId.get(item.idExternoMembroMinisterio()))
                    .statusEscalaMinisterio(item.status() == null ? EnumStatusEscalaMinisterio.CONFIRMADO : item.status())
                    .build());
        }

        return escalasParaSalvar;
    }
}


