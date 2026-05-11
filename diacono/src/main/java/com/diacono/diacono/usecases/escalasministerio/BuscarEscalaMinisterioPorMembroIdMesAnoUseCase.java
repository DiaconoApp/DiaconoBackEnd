package com.diacono.diacono.usecases.escalasministerio;

import com.diacono.diacono.applications.dtos.escalaministerio.EscalaMinisterioConsolidadoDTO;
import com.diacono.diacono.applications.dtos.escalaministerio.EscalaMinisterioDTO;
import com.diacono.diacono.applications.dtos.ministerio.MinisterioSuperSimplificadoDTO;
import com.diacono.diacono.domain.enums.EnumStatusEscalaMinisterio;
import com.diacono.diacono.domain.repository.EscalaMinisterioRepository;
import com.diacono.diacono.domain.repository.MembroMinisterioRepository;
import com.diacono.diacono.global.error.exceptions.FieldInvalidException;
import com.diacono.diacono.global.error.exceptions.ObjectNotFoundException;
import com.diacono.diacono.usecases.escalasevento.validation.ValidarMesEAno;
import com.diacono.diacono.usecases.ministerio.BuscarMinisteriosLiderMinisterioUseCase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class BuscarEscalaMinisterioPorMembroIdMesAnoUseCase {
    private static final Logger logger = LoggerFactory.getLogger(BuscarEscalaMinisterioPorMembroIdMesAnoUseCase.class);

    private final EscalaMinisterioRepository escalaMinisterioRepository;
    private final MembroMinisterioRepository membroMinisterioRepository;
    private final ValidarMesEAno validarMesEAno;

    public BuscarEscalaMinisterioPorMembroIdMesAnoUseCase(
            EscalaMinisterioRepository escalaMinisterioRepository,
            MembroMinisterioRepository membroMinisterioRepository,
            ValidarMesEAno validarMesEAno) {
        this.escalaMinisterioRepository = escalaMinisterioRepository;
        this.membroMinisterioRepository = membroMinisterioRepository;
        this.validarMesEAno = validarMesEAno;
    }

    public List<EscalaMinisterioDTO> execute(
            UUID igrejaId,
            UUID  membroId,
            UUID ministerioId,
            Integer mes,
            Integer ano,
            EnumStatusEscalaMinisterio status,
            String nomeEvento
    ) {
        validarIdsObrigatorios(igrejaId, membroId);
        validarMesEAno.validarMesEAno(mes, ano);

        if (ministerioId != null) {
            validarMinisterioId(ministerioId, membroId, igrejaId);
        }

        YearMonth anoMes = YearMonth.of(ano, mes);
        LocalDateTime inicioMes = anoMes.atDay(1).atStartOfDay();
        LocalDateTime fimMes = anoMes.atEndOfMonth().atTime(23, 59, 59);
        String nomeEventoNormalizado = normalizarNomeEvento(nomeEvento);

        logger.info("Consulta escalas ministério por membro: igrejaId=[{}], membroId=[{}], mes=[{}], ano=[{}], status=[{}], ministerioId=[{}], filtroNomeEvento=[{}]",
                igrejaId, membroId, mes, ano, status, ministerioId, nomeEventoNormalizado != null);

        return escalaMinisterioRepository
                .findEscalaMinisterioByPeriodo(igrejaId, inicioMes, fimMes, status, membroId, ministerioId, nomeEventoNormalizado);
    }

    private void validarMinisterioId(UUID ministerioId, UUID membroId, UUID igrejaId) {
        List<UUID> listaMinisteriosMembro = membroMinisterioRepository
                .buscarMembro(membroId, igrejaId)
                .stream()
                .map(MinisterioSuperSimplificadoDTO::idExterno)
                .toList();

        if (!listaMinisteriosMembro.contains(ministerioId)) {
            throw new ObjectNotFoundException("O membro informado não possui vínculo com o ministério solicitado.");
        }
    }

    private void validarIdsObrigatorios(UUID igrejaId, UUID membroId) {
        if (igrejaId == null || membroId == null) {
            logger.warn("Consulta escalas ministério por membro com ids inválidos: igrejaIdPresente=[{}], membroIdPresente=[{}]",
                    igrejaId != null, membroId != null);
            throw new FieldInvalidException("Ids de igreja e membro sao obrigatórios");
        }
    }

    private String normalizarNomeEvento(String nomeEvento) {
        return Optional.ofNullable(nomeEvento)
                .map(String::trim)
                .filter(nome -> !nome.isBlank())
                .orElse(null);
    }
}
