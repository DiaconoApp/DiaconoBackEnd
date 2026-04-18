package com.diacono.diacono.usecases.escalasministerio;

import com.diacono.diacono.applications.dtos.escalaministerio.EscalaMinisterioConsolidadoDTO;
import com.diacono.diacono.applications.dtos.escalaministerio.EscalaMinisterioDTO;
import com.diacono.diacono.applications.dtos.ministerio.MinisterioSuperSimplificadoDTO;
import com.diacono.diacono.domain.enums.EnumStatusEscalaMinisterio;
import com.diacono.diacono.domain.repository.EscalaMinisterioRepository;
import com.diacono.diacono.domain.repository.MembroMinisterioRepository;
import com.diacono.diacono.global.error.exceptions.ObjectNotFoundException;
import com.diacono.diacono.usecases.escalasevento.validation.ValidarMesEAno;
import com.diacono.diacono.usecases.ministerio.BuscarMinisteriosLiderMinisterioUseCase;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.List;
import java.util.UUID;

@Service
public class BuscarEscalaMinisterioPorMembroIdMesAnoUseCase {

    private final EscalaMinisterioRepository escalaMinisterioRepository;
    private final MembroMinisterioRepository membroMinisterioRepository;
    private final ValidarMesEAno validarMesEAno;
    private final BuscarMinisteriosLiderMinisterioUseCase buscarMinisteriosLiderMinisterioUseCase;

    public BuscarEscalaMinisterioPorMembroIdMesAnoUseCase(
            EscalaMinisterioRepository escalaMinisterioRepository,
            MembroMinisterioRepository membroMinisterioRepository,
            ValidarMesEAno validarMesEAno,
            BuscarMinisteriosLiderMinisterioUseCase buscarMinisteriosLiderMinisterioUseCase) {
        this.escalaMinisterioRepository = escalaMinisterioRepository;
        this.membroMinisterioRepository = membroMinisterioRepository;
        this.validarMesEAno = validarMesEAno;
        this.buscarMinisteriosLiderMinisterioUseCase = buscarMinisteriosLiderMinisterioUseCase;
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
        validarMesEAno.validarMesEAno(mes, ano);

        if (ministerioId != null) {
            validarMinisterioId(ministerioId, membroId, igrejaId);
        }

        YearMonth anoMes = YearMonth.of(ano, mes);
        LocalDateTime inicioMes = anoMes.atDay(1).atStartOfDay();
        LocalDateTime fimMes = anoMes.atEndOfMonth().atTime(23, 59, 59);

        return escalaMinisterioRepository
                .findEscalaMinisterioByPeriodo(igrejaId, inicioMes, fimMes, status, membroId, ministerioId, nomeEvento);
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
}
