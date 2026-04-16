package com.diacono.diacono.infrastructure.controllers;

import com.diacono.diacono.applications.dtos.escalasevento.EscalaEventoConsolidadoDTO;
import com.diacono.diacono.domain.enums.EnumStatusEvento;
import com.diacono.diacono.global.util.JwtUtils;
import com.diacono.diacono.usecases.escalasministerio.BuscarEscalaMinisterioConsolidadoPorMesAnoUseCase;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/escalas-ministerio/lider-ministerio")
public class EscalaMinisterioController {

    private final BuscarEscalaMinisterioConsolidadoPorMesAnoUseCase buscarEscalaMinisterioConsolidadoPorMesAnoUseCase;
    private final JwtUtils jwtUtils;

    public EscalaMinisterioController(
        BuscarEscalaMinisterioConsolidadoPorMesAnoUseCase buscarEscalaMinisterioConsolidadoPorMesAnoUseCase,
        JwtUtils jwtUtils
    ) {
        this.buscarEscalaMinisterioConsolidadoPorMesAnoUseCase = buscarEscalaMinisterioConsolidadoPorMesAnoUseCase;
        this.jwtUtils = jwtUtils;
    }

    @GetMapping
    public ResponseEntity<List<EscalaEventoConsolidadoDTO>> buscarEscalaMinisterioConsolidadoPorMesAno(
            @RequestParam int mes,
            @RequestParam int ano,
            @RequestParam(required = false) EnumStatusEvento status,
            @RequestParam(required = false) String nomeEvento
    ){
        UUID idIgreja = jwtUtils.getIgrejaId();
        UUID idMembro = jwtUtils.getSubject();

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(buscarEscalaMinisterioConsolidadoPorMesAnoUseCase.execute(idIgreja, idMembro, mes, ano, status, nomeEvento));
    }
}
