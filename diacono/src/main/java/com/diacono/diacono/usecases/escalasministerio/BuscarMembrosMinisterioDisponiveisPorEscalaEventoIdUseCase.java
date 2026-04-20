package com.diacono.diacono.usecases.escalasministerio;

import com.diacono.diacono.applications.dtos.escalaministerio.EscalaMembroMinisterioDTO;
import com.diacono.diacono.applications.dtos.ministerio.MinisterioSuperSimplificadoDTO;
import com.diacono.diacono.domain.repository.EscalaEventoRepository;
import com.diacono.diacono.domain.repository.EscalaMinisterioRepository;
import com.diacono.diacono.domain.repository.MembroMinisterioRepository;
import com.diacono.diacono.global.error.exceptions.FieldInvalidException;
import com.diacono.diacono.global.error.exceptions.ObjectNotFoundException;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class BuscarMembrosMinisterioDisponiveisPorEscalaEventoIdUseCase {

    private final EscalaMinisterioRepository escalaMinisterioRepository;
    private final EscalaEventoRepository escalaEventoRepository;
    private final MembroMinisterioRepository membroMinisterioRepository;

    public BuscarMembrosMinisterioDisponiveisPorEscalaEventoIdUseCase(
            EscalaMinisterioRepository escalaMinisterioRepository,
            EscalaEventoRepository escalaEventoRepository,
            MembroMinisterioRepository membroMinisterioRepository
    ) {
        this.escalaMinisterioRepository = escalaMinisterioRepository;
        this.escalaEventoRepository = escalaEventoRepository;
        this.membroMinisterioRepository = membroMinisterioRepository;
    }

    public Integer execute(
            UUID escalaEventoId,
            UUID igrejaId,
            UUID membroId
    ) {
        validarEscalaEventoId(escalaEventoId, igrejaId, membroId);

        List<EscalaMembroMinisterioDTO> membrosMinisterio = buscarMembroMinisterioByEscalaEventoId(igrejaId, escalaEventoId);
        List<EscalaMembroMinisterioDTO> membrosDisponiveis = filtrarMembrosDisponiveis(igrejaId, escalaEventoId, membrosMinisterio);

        System.out.println("Quantidade de membros disponíveis: " + membrosDisponiveis.size());
        membrosDisponiveis.forEach(m ->
                System.out.println("Membro: " + m.nomeMembro() + " - ID: " + m.membroMinisterioId())
        );
        return membrosDisponiveis.size();
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

    private List<EscalaMembroMinisterioDTO> buscarMembroMinisterioByEscalaEventoId(UUID igrejaId, UUID escalaEventoId) {
        return escalaMinisterioRepository
                .findEscalaMembroMinisterioByEscalaEventoId(igrejaId, escalaEventoId);
    }

    private List<EscalaMembroMinisterioDTO> filtrarMembrosDisponiveis(
            UUID igrejaId,
            UUID escalaEventoId,
            List<EscalaMembroMinisterioDTO> membrosMinisterio
    ) {
        Set<UUID> membrosOcupados = Set.copyOf(
                escalaMinisterioRepository.findMembrosMinisterioOcupadosByEscalaEventoId(igrejaId, escalaEventoId)
        );

        return membrosMinisterio.stream()
                .filter(membro -> !membrosOcupados.contains(membro.membroMinisterioId()))
                .map(membro -> new EscalaMembroMinisterioDTO(
                        membro.membroMinisterioId(),
                        membro.nomeMembro(),
                        membro.status(),
                        false
                ))
                .toList();
    }
}
