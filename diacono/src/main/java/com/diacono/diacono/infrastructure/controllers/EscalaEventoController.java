package com.diacono.diacono.infrastructure.controllers;

import com.diacono.diacono.applications.dtos.escalasevento.EscalaEventoConsolidadoDTO;
import com.diacono.diacono.applications.dtos.escalasevento.EscalaEventoDTO;
import com.diacono.diacono.domain.enums.EnumStatusEvento;
import com.diacono.diacono.global.util.JwtUtils;
import com.diacono.diacono.usecases.escalasevento.BuscarEscalaEventoConsolidadoPorMesAnoUseCase;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/escalas-evento/governo")
public class EscalaEventoController {

    private final BuscarEscalaEventoConsolidadoPorMesAnoUseCase buscarEscalaEventoConsolidadoPorMesAnoUseCase;
    private final JwtUtils jwtUtils;

    public EscalaEventoController(
            BuscarEscalaEventoConsolidadoPorMesAnoUseCase buscarEscalaEventoConsolidadoPorMesAnoUseCase,
            JwtUtils jwtUtils
    ) {
        this.buscarEscalaEventoConsolidadoPorMesAnoUseCase = buscarEscalaEventoConsolidadoPorMesAnoUseCase;
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
        UUID idIgreja = jwtUtils.getIgrejaId();

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(buscarEscalaEventoConsolidadoPorMesAnoUseCase.execute(idIgreja, mes, ano, status, ministerioId, nomeEvento));
    }

    public ResponseEntity<List<EscalaEventoDTO>> buscarEscalaEventoPorEvento() {
        // TODO - Fazer endpoint. Antes perguntar para Izael
        return null;
    }
}
