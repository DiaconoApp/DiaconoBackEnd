package com.diacono.diacono.infrastructure.controllers;

import com.diacono.diacono.applications.dtos.escalasevento.EscalaEventoDTO;
import com.diacono.diacono.domain.enums.EnumStatusEvento;
import com.diacono.diacono.global.util.JwtUtils;
import com.diacono.diacono.usecases.escalasevento.BuscarEscalaEventoPorMesAnoUseCase;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/escalas-evento")
public class EscalaEventoController {

    private final BuscarEscalaEventoPorMesAnoUseCase buscarEscalaEventoPorMesAnoUseCase;
    private final JwtUtils jwtUtils;

    public EscalaEventoController(
            BuscarEscalaEventoPorMesAnoUseCase buscarEscalaEventoPorMesAnoUseCase,
            JwtUtils jwtUtils
    ) {
        this.buscarEscalaEventoPorMesAnoUseCase = buscarEscalaEventoPorMesAnoUseCase;
        this.jwtUtils = jwtUtils;
    }

    @GetMapping
    public ResponseEntity<List<EscalaEventoDTO>> buscarEscalaEventoPorMesAno(
            @RequestParam int mes,
            @RequestParam int ano,
            @RequestParam(required = false) EnumStatusEvento status,
            @RequestParam(required = false) UUID ministerioId,
            @RequestParam(required = false) String nomeEvento
    ){
        UUID idIgreja = jwtUtils.getIgrejaId();

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(buscarEscalaEventoPorMesAnoUseCase.execute(idIgreja, mes, ano, status, ministerioId, nomeEvento));
    }
}
