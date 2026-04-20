package com.diacono.diacono.infrastructure.controllers;

import com.diacono.diacono.applications.dtos.escalaministerio.*;
import com.diacono.diacono.applications.dtos.RestResponseMessageDTO;
import com.diacono.diacono.domain.enums.EnumStatusEscalaMinisterio;
import com.diacono.diacono.global.util.JwtUtils;
import com.diacono.diacono.usecases.escalasministerio.*;
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
    private final BuscarMembrosMinisterioDisponiveisPorEscalaEventoIdUseCase buscarMembrosMinisterioDisponiveisPorEscalaEventoIdUseCase;
    private final BuscarMembrosMinisterioPorEscalaEventoIdUseCase buscarMembrosMinisterioPorEscalaEventoIdUseCase;
    private final SalvarEscalaMinisterioPorEscalaEventoIdUseCase salvarEscalaMinisterioPorEscalaEventoIdUseCase;
    private final BuscarMembrosMinisterioRandomizadosPorEscalaEventoIdUseCase buscarMembrosMinisterioRandomizadosPorEscalaEventoIdUseCase;
    private final RevisarMembrosMinisterioRandomizadosPorEscalaEventoIdUseCase revisarMembrosMinisterioRandomizadosPorEscalaEventoIdUseCase;
    private final JwtUtils jwtUtils;

    public EscalaMinisterioController(
        BuscarEscalaMinisterioConsolidadoPorMesAnoUseCase buscarEscalaMinisterioConsolidadoPorMesAnoUseCase,
        BuscarEscalaMinisterioPorMembroIdMesAnoUseCase buscarEscalaMinisterioPorMembroIdMesAnoUseCase,
        BuscarMembrosMinisterioDisponiveisPorEscalaEventoIdUseCase buscarMembrosMinisterioDisponiveisPorEscalaEventoIdUseCase,
        BuscarMembrosMinisterioPorEscalaEventoIdUseCase buscarMembrosMinisterioPorEscalaEventoIdUseCase,
        SalvarEscalaMinisterioPorEscalaEventoIdUseCase salvarEscalaMinisterioPorEscalaEventoIdUseCase,
        BuscarMembrosMinisterioRandomizadosPorEscalaEventoIdUseCase buscarMembrosMinisterioRandomizadosPorEscalaEventoIdUseCase,
        RevisarMembrosMinisterioRandomizadosPorEscalaEventoIdUseCase revisarMembrosMinisterioRandomizadosPorEscalaEventoIdUseCase,
        JwtUtils jwtUtils
    ) {
        this.buscarEscalaMinisterioConsolidadoPorMesAnoUseCase = buscarEscalaMinisterioConsolidadoPorMesAnoUseCase;
        this.buscarEscalaMinisterioPorMembroIdMesAnoUseCase = buscarEscalaMinisterioPorMembroIdMesAnoUseCase;
        this.buscarMembrosMinisterioDisponiveisPorEscalaEventoIdUseCase = buscarMembrosMinisterioDisponiveisPorEscalaEventoIdUseCase;
        this.buscarMembrosMinisterioPorEscalaEventoIdUseCase = buscarMembrosMinisterioPorEscalaEventoIdUseCase;
        this.salvarEscalaMinisterioPorEscalaEventoIdUseCase = salvarEscalaMinisterioPorEscalaEventoIdUseCase;
        this.buscarMembrosMinisterioRandomizadosPorEscalaEventoIdUseCase = buscarMembrosMinisterioRandomizadosPorEscalaEventoIdUseCase;
        this.revisarMembrosMinisterioRandomizadosPorEscalaEventoIdUseCase = revisarMembrosMinisterioRandomizadosPorEscalaEventoIdUseCase;
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

    @GetMapping("/lider-ministerio/{escalaEventoId}/membros-disponiveis")
    public ResponseEntity<Integer> buscarMembrosMinisterioDisponiveisPorEscalaEventoId(@PathVariable UUID escalaEventoId) {
        UUID igrejaId = jwtUtils.getIgrejaId();
        UUID membroId = jwtUtils.getSubject();

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(buscarMembrosMinisterioDisponiveisPorEscalaEventoIdUseCase.execute(escalaEventoId, igrejaId, membroId));
    }

    @GetMapping("/lider-ministerio/{escalaEventoId}")
    public ResponseEntity<List<EscalaMembroMinisterioDTO>> buscarMembrosMinisterioPorEscalaEventoId(@PathVariable UUID escalaEventoId) {
        UUID igrejaId = jwtUtils.getIgrejaId();
        UUID membroId = jwtUtils.getSubject();

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(buscarMembrosMinisterioPorEscalaEventoIdUseCase.execute(escalaEventoId, igrejaId, membroId));
    }

    @GetMapping("/lider-ministerio/{escalaEventoId}/{quantidadeMembrosRandomizados}")
    public ResponseEntity<List<EscalaMembroMinisterioSimplificadoDTO>> buscarMembrosMinisterioRandomizadosPorEscalaEventoId(
            @PathVariable UUID escalaEventoId,
            @PathVariable int quantidadeMembrosRandomizados
    ) {
        UUID igrejaId = jwtUtils.getIgrejaId();
        UUID membroId = jwtUtils.getSubject();

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(buscarMembrosMinisterioRandomizadosPorEscalaEventoIdUseCase.execute(
                        escalaEventoId,
                        igrejaId,
                        membroId,
                        quantidadeMembrosRandomizados
                ));
    }

    @PostMapping("/lider-ministerio/{escalaEventoId}/revisar-randomizacao/{membroMinisterioIdASerTrocado}")
    public ResponseEntity<List<EscalaMembroMinisterioSimplificadoDTO>> RevisarMembrosMinisterioRandomizadosPorEscalaEventoId(
            @PathVariable UUID escalaEventoId,
            @PathVariable UUID membroMinisterioIdASerTrocado,
            @RequestBody List<EscalaMembroMinisterioSimplificadoDTO>  membrosMinisterioSelecionados

    ) {
        UUID igrejaId = jwtUtils.getIgrejaId();
        UUID membroId = jwtUtils.getSubject();

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(revisarMembrosMinisterioRandomizadosPorEscalaEventoIdUseCase.execute(
                        escalaEventoId,
                        igrejaId,
                        membroId,
                        membroMinisterioIdASerTrocado,
                        membrosMinisterioSelecionados
                ));
    }

    @PatchMapping("/lider-ministerio/{escalaEventoId}")
    public ResponseEntity<RestResponseMessageDTO> salvarEscalaMinisterioPorEscalaEventoId(
            @PathVariable UUID escalaEventoId,
            @RequestBody List<EscalaMinisterioSalvarDTO> escalasMinisterio
    ) {
        UUID igrejaId = jwtUtils.getIgrejaId();
        UUID membroId = jwtUtils.getSubject();

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(salvarEscalaMinisterioPorEscalaEventoIdUseCase.execute(escalaEventoId, igrejaId, membroId, escalasMinisterio));
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
