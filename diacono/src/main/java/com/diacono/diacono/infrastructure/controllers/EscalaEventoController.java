package com.diacono.diacono.infrastructure.controllers;

import com.diacono.diacono.applications.dtos.escalasevento.EscalaEventoConsolidadoDTO;
import com.diacono.diacono.applications.dtos.escalasevento.EscalaEventoEscaladoDTO;
import com.diacono.diacono.applications.dtos.RestResponseMessageDTO;
import com.diacono.diacono.domain.enums.EnumStatusEvento;
import com.diacono.diacono.global.util.JwtUtils;
import com.diacono.diacono.usecases.escalasevento.AtualizarEscalaEventoPorEventoIdUseCase;
import com.diacono.diacono.usecases.escalasevento.BuscarEscalaEventoConsolidadoPorMesAnoUseCase;
import com.diacono.diacono.usecases.escalasevento.BuscarEscalaEventoEscaladoPorEventoIdUseCase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/escalas-evento/governo")
@PreAuthorize("hasAnyAuthority('SCOPE_GOVERNO')")
public class EscalaEventoController {
    // TODO: Adicionar anotacoes APIErrosComuns, APIResponse, e Scope ou PreAuthorize

    private static final Logger logger = LoggerFactory.getLogger(EscalaEventoController.class);

    private final BuscarEscalaEventoConsolidadoPorMesAnoUseCase buscarEscalaEventoConsolidadoPorMesAnoUseCase;
    private final BuscarEscalaEventoEscaladoPorEventoIdUseCase buscarEscalaEventoEscaladoPorEventoIdUseCase;
    private final AtualizarEscalaEventoPorEventoIdUseCase atualizarEscalaEventoPorEventoIdUseCase;
    private final JwtUtils jwtUtils;

    public EscalaEventoController(
            BuscarEscalaEventoConsolidadoPorMesAnoUseCase buscarEscalaEventoConsolidadoPorMesAnoUseCase,
            BuscarEscalaEventoEscaladoPorEventoIdUseCase buscarEscalaEventoEscaladoPorEventoIdUseCase,
            AtualizarEscalaEventoPorEventoIdUseCase atualizarEscalaEventoPorEventoIdUseCase,
            JwtUtils jwtUtils
    ) {
        this.buscarEscalaEventoConsolidadoPorMesAnoUseCase = buscarEscalaEventoConsolidadoPorMesAnoUseCase;
        this.buscarEscalaEventoEscaladoPorEventoIdUseCase = buscarEscalaEventoEscaladoPorEventoIdUseCase;
        this.atualizarEscalaEventoPorEventoIdUseCase = atualizarEscalaEventoPorEventoIdUseCase;
        this.jwtUtils = jwtUtils;
    }

    @GetMapping
    public ResponseEntity<List<EscalaEventoConsolidadoDTO>> buscarEscalaEventoConsolidadoPorMesAno(
            @RequestParam int mes,
            @RequestParam int ano,
            @RequestParam(required = false) EnumStatusEvento status,
            @RequestParam(required = false) UUID ministerioId,
            @RequestParam(required = false) String nomeEvento
    ){
        UUID igrejaId = jwtUtils.getIgrejaId();

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(buscarEscalaEventoConsolidadoPorMesAnoUseCase.execute(igrejaId, mes, ano, status, ministerioId, nomeEvento));
    }

    @GetMapping("/{eventoId}")
    public ResponseEntity<List<EscalaEventoEscaladoDTO>> buscarEscalaEventoEscaladoPorEventoId(
            @PathVariable("eventoId") UUID eventoId
    ) {
        UUID igrejaId = jwtUtils.getIgrejaId();

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(buscarEscalaEventoEscaladoPorEventoIdUseCase.execute(igrejaId, eventoId));
    }

    @PatchMapping("/{eventoId}")
    public ResponseEntity<RestResponseMessageDTO> atualizarEscalaEventoPorEventoId(
            @PathVariable("eventoId") UUID eventoId,
            @RequestBody List<EscalaEventoEscaladoDTO> escalasEvento
    ) {
        UUID igrejaId = jwtUtils.getIgrejaId();

        try {
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(atualizarEscalaEventoPorEventoIdUseCase.execute(igrejaId, eventoId, escalasEvento));
        } catch (RuntimeException ex) {
            logger.warn("Falha ao atualizar escala de evento. eventoId={}, igrejaId={}", eventoId, igrejaId);
            throw ex;
        }
    }
}
