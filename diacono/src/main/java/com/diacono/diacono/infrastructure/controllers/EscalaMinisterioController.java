package com.diacono.diacono.infrastructure.controllers;

import com.diacono.diacono.applications.dtos.escalaministerio.EscalaMembroMinisterioDTO;
import com.diacono.diacono.applications.dtos.escalaministerio.EscalaMinisterioConsolidadoDTO;
import com.diacono.diacono.applications.dtos.escalaministerio.EscalaMinisterioDTO;
import com.diacono.diacono.domain.enums.EnumStatusEscalaMinisterio;
import com.diacono.diacono.global.util.JwtUtils;
import com.diacono.diacono.usecases.escalasministerio.BuscarEscalaMinisterioConsolidadoPorMesAnoUseCase;
import com.diacono.diacono.usecases.escalasministerio.BuscarEscalaMinisterioPorMembroIdMesAnoUseCase;
import com.diacono.diacono.usecases.escalasministerio.BuscarMembrosMinisterioPorEscalaEventoIdUseCase;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/escalas-ministerio")
public class EscalaMinisterioController {

    private final BuscarEscalaMinisterioConsolidadoPorMesAnoUseCase buscarEscalaMinisterioConsolidadoPorMesAnoUseCase;
    private final BuscarEscalaMinisterioPorMembroIdMesAnoUseCase buscarEscalaMinisterioPorMembroIdMesAnoUseCase;
    private final BuscarMembrosMinisterioPorEscalaEventoIdUseCase buscarMembrosMinisterioPorEscalaEventoIdUseCase;
    private final JwtUtils jwtUtils;

    public EscalaMinisterioController(
        BuscarEscalaMinisterioConsolidadoPorMesAnoUseCase buscarEscalaMinisterioConsolidadoPorMesAnoUseCase,
        BuscarEscalaMinisterioPorMembroIdMesAnoUseCase buscarEscalaMinisterioPorMembroIdMesAnoUseCase,
        BuscarMembrosMinisterioPorEscalaEventoIdUseCase buscarMembrosMinisterioPorEscalaEventoIdUseCase,
        JwtUtils jwtUtils
    ) {
        this.buscarEscalaMinisterioConsolidadoPorMesAnoUseCase = buscarEscalaMinisterioConsolidadoPorMesAnoUseCase;
        this.buscarEscalaMinisterioPorMembroIdMesAnoUseCase = buscarEscalaMinisterioPorMembroIdMesAnoUseCase;
        this.buscarMembrosMinisterioPorEscalaEventoIdUseCase = buscarMembrosMinisterioPorEscalaEventoIdUseCase;
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

    @GetMapping("/lider-ministerio/{escalaEventoId}")
    public ResponseEntity<List<EscalaMembroMinisterioDTO>> buscarMembrosMinisterioPorEscalaEventoId(@PathVariable UUID escalaEventoId) {
        UUID igrejaId = jwtUtils.getIgrejaId();
        UUID membroId = jwtUtils.getSubject();

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(buscarMembrosMinisterioPorEscalaEventoIdUseCase.execute(escalaEventoId, igrejaId, membroId));
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
