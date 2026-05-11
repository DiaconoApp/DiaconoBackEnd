package com.diacono.diacono.usecases.escalasministerio;

import com.diacono.diacono.applications.dtos.escalaministerio.EscalaMembroMinisterioDTO;
import com.diacono.diacono.applications.dtos.escalaministerio.EscalaMembroMinisterioSimplificadoDTO;
import com.diacono.diacono.applications.dtos.ministerio.MinisterioSuperSimplificadoDTO;
import com.diacono.diacono.domain.repository.EscalaEventoRepository;
import com.diacono.diacono.domain.repository.EscalaMinisterioRepository;
import com.diacono.diacono.domain.repository.MembroMinisterioRepository;
import com.diacono.diacono.global.error.exceptions.FieldInvalidException;
import com.diacono.diacono.global.error.exceptions.ObjectNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Service
public class BuscarMembrosMinisterioRandomizadosPorEscalaEventoIdUseCase {
    private static final Logger logger = LoggerFactory.getLogger(BuscarMembrosMinisterioRandomizadosPorEscalaEventoIdUseCase.class);

    private final EscalaMinisterioRepository escalaMinisterioRepository;
    private final EscalaEventoRepository escalaEventoRepository;
    private final MembroMinisterioRepository membroMinisterioRepository;

    public BuscarMembrosMinisterioRandomizadosPorEscalaEventoIdUseCase(
            EscalaMinisterioRepository escalaMinisterioRepository,
            EscalaEventoRepository escalaEventoRepository,
            MembroMinisterioRepository membroMinisterioRepository
    ) {
        this.escalaMinisterioRepository = escalaMinisterioRepository;
        this.escalaEventoRepository = escalaEventoRepository;
        this.membroMinisterioRepository = membroMinisterioRepository;
    }

    public List<EscalaMembroMinisterioSimplificadoDTO> execute(
            UUID escalaEventoId,
            UUID igrejaId,
            UUID membroId,
            int quantidadeMembrosRandomizados
    ) {
        validarIdsObrigatorios(escalaEventoId, igrejaId, membroId);
        validarEscalaEventoId(escalaEventoId, igrejaId, membroId);
        validarQuantidadeMaiorQueZero(quantidadeMembrosRandomizados);

        List<EscalaMembroMinisterioDTO> membrosMinisterio = buscarMembroMinisterioByEscalaEventoId(igrejaId, escalaEventoId);
        List<EscalaMembroMinisterioDTO> membrosDisponiveis = filtrarMembrosDisponiveis(igrejaId, escalaEventoId, membrosMinisterio);
        validarQuantidadeMembrosRandomizados(quantidadeMembrosRandomizados, membrosDisponiveis);

        List<EscalaMembroMinisterioDTO> membrosRandomizados = randomizarMembros(membrosDisponiveis);

        List<EscalaMembroMinisterioSimplificadoDTO> resultado = membrosRandomizados.stream()
                .limit(quantidadeMembrosRandomizados)
                .map(membro -> new EscalaMembroMinisterioSimplificadoDTO(
                        membro.membroMinisterioId(),
                        membro.nomeMembro()
                ))
                .toList();

        logger.info("Randomizacao membros ministério: escalaEventoId=[{}], igrejaId=[{}], membroId=[{}], quantidadeSolicitada=[{}], quantidadeRetornada=[{}]",
                escalaEventoId, igrejaId, membroId, quantidadeMembrosRandomizados, resultado.size());

        return resultado;
    }

    private void validarIdsObrigatorios(UUID escalaEventoId, UUID igrejaId, UUID membroId) {
        if (escalaEventoId == null || igrejaId == null || membroId == null) {
            logger.warn("Randomizacao membros ministério com ids inválidos: escalaEventoIdPresente=[{}], igrejaIdPresente=[{}], membroIdPresente=[{}]",
                    escalaEventoId != null, igrejaId != null, membroId != null);
            throw new FieldInvalidException("Ids de escalaEvento, igreja e membro sao obrigatórios");
        }
    }

    private void validarQuantidadeMaiorQueZero(int quantidadeMembrosRandomizados) {
        if (quantidadeMembrosRandomizados <= 0) {
            throw new FieldInvalidException("A quantidade de membros randomizados deve ser maior que zero.");
        }
    }

    private void validarQuantidadeMembrosRandomizados(int quantidadeMembrosRandomizados, List<EscalaMembroMinisterioDTO> membrosDisponiveis) {
        if(quantidadeMembrosRandomizados > membrosDisponiveis.size()) {
            throw new FieldInvalidException("A quantidade de membros disponíveis é "+ membrosDisponiveis.size() +  ". Tente novamente.");
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

    private List<EscalaMembroMinisterioDTO> randomizarMembros(List<EscalaMembroMinisterioDTO> membrosDisponiveis) {
        List<EscalaMembroMinisterioDTO> membrosRandomizados = new ArrayList<>(membrosDisponiveis);
        Collections.shuffle(membrosRandomizados);
        return membrosRandomizados;
    }
}
