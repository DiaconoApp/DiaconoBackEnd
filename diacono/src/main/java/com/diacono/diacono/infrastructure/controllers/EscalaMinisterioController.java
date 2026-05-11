package com.diacono.diacono.infrastructure.controllers;

import com.diacono.diacono.applications.dtos.escalaministerio.*;
import com.diacono.diacono.applications.dtos.RestResponseMessageDTO;
import com.diacono.diacono.domain.enums.EnumStatusEscalaMinisterio;
import com.diacono.diacono.global.util.JwtUtils;
import com.diacono.diacono.usecases.escalasministerio.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Positive;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/escalas-ministerio")
@Validated
public class EscalaMinisterioController {

    private static final Logger logger = LoggerFactory.getLogger(EscalaMinisterioController.class);

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
    @PreAuthorize("hasAnyAuthority('SCOPE_LIDER_MINISTERIO', 'SCOPE_GOVERNO')")
    public ResponseEntity<List<EscalaMinisterioConsolidadoDTO>> buscarEscalaMinisterioConsolidadoPorMesAno(
            @RequestParam @Min(1) @Max(12) int mes,
            @RequestParam @Positive int ano,
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
    @PreAuthorize("hasAnyAuthority('SCOPE_LIDER_MINISTERIO', 'SCOPE_GOVERNO')")
    public ResponseEntity<Integer> buscarMembrosMinisterioDisponiveisPorEscalaEventoId(@PathVariable UUID escalaEventoId) {
        UUID igrejaId = jwtUtils.getIgrejaId();
        UUID membroId = jwtUtils.getSubject();

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(buscarMembrosMinisterioDisponiveisPorEscalaEventoIdUseCase.execute(escalaEventoId, igrejaId, membroId));
    }

    @GetMapping("/lider-ministerio/{escalaEventoId}")
    @PreAuthorize("hasAnyAuthority('SCOPE_LIDER_MINISTERIO', 'SCOPE_GOVERNO')")
    public ResponseEntity<List<EscalaMembroMinisterioDTO>> buscarMembrosMinisterioPorEscalaEventoId(@PathVariable UUID escalaEventoId) {
        UUID igrejaId = jwtUtils.getIgrejaId();
        UUID membroId = jwtUtils.getSubject();

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(buscarMembrosMinisterioPorEscalaEventoIdUseCase.execute(escalaEventoId, igrejaId, membroId));
    }

    @GetMapping("/lider-ministerio/{escalaEventoId}/{quantidadeMembrosRandomizados}")
    @PreAuthorize("hasAnyAuthority('SCOPE_LIDER_MINISTERIO', 'SCOPE_GOVERNO')")
    public ResponseEntity<List<EscalaMembroMinisterioSimplificadoDTO>> buscarMembrosMinisterioRandomizadosPorEscalaEventoId(
            @PathVariable UUID escalaEventoId,
            @PathVariable @Positive int quantidadeMembrosRandomizados
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
    @PreAuthorize("hasAnyAuthority('SCOPE_LIDER_MINISTERIO', 'SCOPE_GOVERNO')")
    public ResponseEntity<List<EscalaMembroMinisterioSimplificadoDTO>> RevisarMembrosMinisterioRandomizadosPorEscalaEventoId(
            @PathVariable UUID escalaEventoId,
            @PathVariable UUID membroMinisterioIdASerTrocado,
            @RequestBody @Valid List<EscalaMembroMinisterioSimplificadoDTO>  membrosMinisterioSelecionados

    ) {
        UUID igrejaId = jwtUtils.getIgrejaId();
        UUID membroId = jwtUtils.getSubject();

        try {
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(revisarMembrosMinisterioRandomizadosPorEscalaEventoIdUseCase.execute(
                            escalaEventoId,
                            igrejaId,
                            membroId,
                            membroMinisterioIdASerTrocado,
                            membrosMinisterioSelecionados
                    ));
        } catch (RuntimeException ex) {
            logger.warn("Falha ao revisar randomizacao de membros. escalaEventoId={}, igrejaId={}", escalaEventoId, igrejaId);
            throw ex;
        }
    }

    @PatchMapping("/lider-ministerio/{escalaEventoId}")
    @PreAuthorize("hasAnyAuthority('SCOPE_LIDER_MINISTERIO', 'SCOPE_GOVERNO')")
    public ResponseEntity<RestResponseMessageDTO> salvarEscalaMinisterioPorEscalaEventoId(
            @PathVariable UUID escalaEventoId,
            @RequestBody @Valid List<EscalaMinisterioSalvarDTO> escalasMinisterio
    ) {
        UUID igrejaId = jwtUtils.getIgrejaId();
        UUID membroId = jwtUtils.getSubject();

        try {
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(salvarEscalaMinisterioPorEscalaEventoIdUseCase.execute(escalaEventoId, igrejaId, membroId, escalasMinisterio));
        } catch (RuntimeException ex) {
            logger.warn("Falha ao salvar escala de ministerio. escalaEventoId={}, igrejaId={}", escalaEventoId, igrejaId);
            throw ex;
        }
    }

    @GetMapping("/membro")
    @PreAuthorize("hasAnyAuthority('SCOPE_MEMBRO', 'SCOPE_LIDER_MINISTERIO', 'SCOPE_GOVERNO')")
    public ResponseEntity<List<EscalaMinisterioDTO>> buscarEscalaMinisterioPorMembroIdMesAno(
            @RequestParam @Min(1) @Max(12) int mes,
            @RequestParam @Positive int ano,
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
