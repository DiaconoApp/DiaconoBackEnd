package com.diacono.diacono.usecases.escalasministerio;

import com.diacono.diacono.applications.dtos.escalaministerio.EscalaMembroMinisterioDTO;
import com.diacono.diacono.applications.dtos.ministerio.MinisterioSuperSimplificadoDTO;
import com.diacono.diacono.domain.repository.MembroMinisterioRepository;
import com.diacono.diacono.global.error.exceptions.FieldInvalidException;
import com.diacono.diacono.global.error.exceptions.ObjectNotFoundException;
import com.diacono.diacono.domain.repository.EscalaEventoRepository;
import com.diacono.diacono.domain.repository.EscalaMinisterioRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Service
public class BuscarMembrosMinisterioPorEscalaEventoIdUseCase {
    // TODO: Adicionar valicao se escala evento faz parte de ministerio que o usuario lidera
    private static final Logger logger = LoggerFactory.getLogger(BuscarMembrosMinisterioPorEscalaEventoIdUseCase.class);

    private final EscalaMinisterioRepository escalaMinisterioRepository;
    private final EscalaEventoRepository escalaEventoRepository;
    private final MembroMinisterioRepository membroMinisterioRepository;

    public BuscarMembrosMinisterioPorEscalaEventoIdUseCase(
            EscalaMinisterioRepository escalaMinisterioRepository,
            EscalaEventoRepository escalaEventoRepository,
            MembroMinisterioRepository membroMinisterioRepository
    ) {
        this.escalaMinisterioRepository = escalaMinisterioRepository;
        this.escalaEventoRepository = escalaEventoRepository;
        this.membroMinisterioRepository = membroMinisterioRepository;
    }

    public List<EscalaMembroMinisterioDTO> execute(UUID escalaEventoId, UUID igrejaId, UUID membroId) {
        validarIdsObrigatorios(escalaEventoId, igrejaId, membroId);
        validarEscalaEventoId(escalaEventoId, igrejaId, membroId);

        List<EscalaMembroMinisterioDTO> membrosMinisterio = buscarMembroMinisterioByEscalaEventoId(igrejaId, escalaEventoId);
        Set<UUID> membrosOcupados = new HashSet<>(
                escalaMinisterioRepository.findMembrosMinisterioOcupadosByEscalaEventoId(igrejaId, escalaEventoId)
        );

        List<EscalaMembroMinisterioDTO> membrosComStatus = membrosMinisterio.stream()
                .map(membro -> new EscalaMembroMinisterioDTO(
                        membro.membroMinisterioId(),
                        membro.nomeMembro(),
                        membro.status(),
                        membrosOcupados.contains(membro.membroMinisterioId())
                ))
                .toList();

        logger.info("Consulta membros ministério por escala: escalaEventoId=[{}], igrejaId=[{}], membroId=[{}], quantidadeMembros=[{}]",
                escalaEventoId, igrejaId, membroId, membrosComStatus.size());

        return membrosComStatus;
    }

    private void validarIdsObrigatorios(UUID escalaEventoId, UUID igrejaId, UUID membroId) {
        if (escalaEventoId == null || igrejaId == null || membroId == null) {
            logger.warn("Consulta membros ministério por escala com ids inválidos: escalaEventoIdPresente=[{}], igrejaIdPresente=[{}], membroIdPresente=[{}]",
                    escalaEventoId != null, igrejaId != null, membroId != null);
            throw new FieldInvalidException("Ids de escalaEvento, igreja e membro sao obrigatórios");
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

    private List<EscalaMembroMinisterioDTO> buscarMembroMinisterioByEscalaEventoId(UUID igrejaId, UUID escalaEventoId) {
        return escalaMinisterioRepository
                .findEscalaMembroMinisterioByEscalaEventoId(igrejaId, escalaEventoId);
    }
}
