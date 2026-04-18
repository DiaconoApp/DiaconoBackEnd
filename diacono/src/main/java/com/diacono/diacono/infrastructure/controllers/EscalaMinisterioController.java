package com.diacono.diacono.infrastructure.controllers;

import com.diacono.diacono.applications.dtos.escalaministerio.EscalaMinisterioConsolidadoDTO;
import com.diacono.diacono.applications.dtos.escalaministerio.EscalaMinisterioDTO;
import com.diacono.diacono.domain.enums.EnumStatusEscalaMinisterio;
import com.diacono.diacono.global.util.JwtUtils;
import com.diacono.diacono.usecases.escalasministerio.BuscarEscalaMinisterioConsolidadoPorMesAnoUseCase;
import com.diacono.diacono.usecases.escalasministerio.BuscarEscalaMinisterioPorMembroIdMesAnoUseCase;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/escalas-ministerio")
public class EscalaMinisterioController {

    private final BuscarEscalaMinisterioConsolidadoPorMesAnoUseCase buscarEscalaMinisterioConsolidadoPorMesAnoUseCase;
    private final BuscarEscalaMinisterioPorMembroIdMesAnoUseCase buscarEscalaMinisterioPorMembroIdMesAnoUseCase;
    private final JwtUtils jwtUtils;

    public EscalaMinisterioController(
        BuscarEscalaMinisterioConsolidadoPorMesAnoUseCase buscarEscalaMinisterioConsolidadoPorMesAnoUseCase,
        BuscarEscalaMinisterioPorMembroIdMesAnoUseCase buscarEscalaMinisterioPorMembroIdMesAnoUseCase,
        JwtUtils jwtUtils
    ) {
        this.buscarEscalaMinisterioConsolidadoPorMesAnoUseCase = buscarEscalaMinisterioConsolidadoPorMesAnoUseCase;
        this.buscarEscalaMinisterioPorMembroIdMesAnoUseCase = buscarEscalaMinisterioPorMembroIdMesAnoUseCase;
        this.jwtUtils = jwtUtils;
    }

    @GetMapping("/lider-ministerio")
    public ResponseEntity<List<EscalaMinisterioConsolidadoDTO>> buscarEscalaMinisterioConsolidadoPorMesAno(
            @RequestParam int mes,
            @RequestParam int ano,
            @RequestParam(required = false) EnumStatusEscalaMinisterio status,
            @RequestParam(required = false) UUID ministerioId,
            @RequestParam(required = false) String nomeEvento
    ){
        UUID igrejaId = jwtUtils.getIgrejaId();
        UUID membroId = jwtUtils.getSubject();

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(buscarEscalaMinisterioConsolidadoPorMesAnoUseCase.execute(igrejaId, membroId, ministerioId, mes, ano, status, nomeEvento));
    }

    @GetMapping("/membro")
    public ResponseEntity<List<EscalaMinisterioDTO>> buscarEscalaMinisterioPorMembroIdMesAno(
            @RequestParam int mes,
            @RequestParam int ano,
            @RequestParam(required = false) EnumStatusEscalaMinisterio status,
            @RequestParam(required = false) UUID ministerioId,
            @RequestParam(required = false) String nomeEvento
    ){
        UUID igrejaId = jwtUtils.getIgrejaId();
        UUID membroId = jwtUtils.getSubject();

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(buscarEscalaMinisterioPorMembroIdMesAnoUseCase.execute(igrejaId, membroId, ministerioId, mes, ano, status, nomeEvento));
    }
}
