package com.diacono.diacono.infrastructure.controllers;

import com.diacono.diacono.applications.dtos.RestResponseMessageDTO;
import com.diacono.diacono.applications.dtos.membro.MembroDetalheResponseDTO;
import com.diacono.diacono.applications.dtos.membro.MembroUpdateDTO;
import com.diacono.diacono.global.error.comuns.ApiErrorsComuns;
import com.diacono.diacono.applications.dtos.membro.MembroCreateDTO;
import com.diacono.diacono.applications.dtos.membro.MembroResponseDTO;
import com.diacono.diacono.domain.enums.EnumStatusMembro;
import com.diacono.diacono.usecases.membro.*;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/membros")
public class MembroController {

    private static final Logger logger = LoggerFactory.getLogger(MembroController.class);

    private final CriarMembroUseCase criarMembroUseCase;
    private final BuscarTodosSemFiltroUseCase buscarTodosSemFiltroUseCase;
    private final BuscarTodosComFiltroUseCase buscarTodosComFiltroUseCase;
    private final BuscarMembroPorUUIDUseCase buscarMembroPorUUIDUseCase;
    private final AtualizarMembroUseCase atualizarMembroUseCase;

    public MembroController(CriarMembroUseCase criarMembroUseCase, BuscarTodosSemFiltroUseCase buscarTodosSemFiltroUseCase, BuscarTodosComFiltroUseCase buscarTodosComFiltroUseCase, BuscarMembroPorUUIDUseCase buscarMembroPorUUIDUseCase, AtualizarMembroUseCase atualizarMembroUseCase) {
        this.criarMembroUseCase = criarMembroUseCase;
        this.buscarTodosSemFiltroUseCase = buscarTodosSemFiltroUseCase;
        this.buscarTodosComFiltroUseCase = buscarTodosComFiltroUseCase;
        this.buscarMembroPorUUIDUseCase = buscarMembroPorUUIDUseCase;
        this.atualizarMembroUseCase = atualizarMembroUseCase;
    }

    @ApiErrorsComuns
    @ApiResponse(responseCode = "201", description = "Membro criado com sucesso")
    @PostMapping
    @PreAuthorize("hasAnyAuthority('SCOPE_LIDER_MINISTERIO', 'SCOPE_GOVERNO')")
    public ResponseEntity<RestResponseMessageDTO> criarMembro(@RequestBody @Valid MembroCreateDTO membroDTO){
        try {
            RestResponseMessageDTO response = criarMembroUseCase.execute(membroDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (RuntimeException ex) {
            logger.warn("Falha ao criar membro.");
            throw ex;
        }
    }

    @ApiErrorsComuns
    @ApiResponse(responseCode = "200", description = "Membros encontrados com sucesso")
    @GetMapping
    @PreAuthorize("hasAnyAuthority('SCOPE_LIDER_MINISTERIO', 'SCOPE_GOVERNO')")
    public ResponseEntity<Page<MembroResponseDTO>> buscarTodos(
            Pageable pageable,
            @RequestParam(required = false, defaultValue = "") String buscaGeral,
            @RequestParam(required = false) EnumStatusMembro status,
            @RequestParam(required = false) UUID fkMinisterio
    ) {
        if((buscaGeral == null || buscaGeral.isBlank()) && fkMinisterio == null && status == null){

            Page<MembroResponseDTO> response = buscarTodosSemFiltroUseCase.execute(pageable);
            return ResponseEntity.status(HttpStatus.OK).body(response);
        }

        Page<MembroResponseDTO> response = buscarTodosComFiltroUseCase.execute(pageable, buscaGeral, status, fkMinisterio);

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @ApiErrorsComuns
    @ApiResponse(responseCode = "200", description = "Membro encontrado com sucesso")
    @GetMapping("/{idExterno}")
    @PreAuthorize("hasAnyAuthority('SCOPE_MEMBRO', 'SCOPE_LIDER_MINISTERIO', 'SCOPE_GOVERNO')")
    public ResponseEntity<MembroDetalheResponseDTO> buscarPorId(@PathVariable UUID idExterno) {
        try {
            MembroDetalheResponseDTO response = buscarMembroPorUUIDUseCase.execute(idExterno);
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (RuntimeException ex) {
            logger.warn("Falha ao buscar membro por idExterno.");
            throw ex;
        }
    }

    @PatchMapping("/{idExterno}")
    @PreAuthorize("hasAnyAuthority('SCOPE_LIDER_MINISTERIO', 'SCOPE_GOVERNO')")
    public ResponseEntity<RestResponseMessageDTO> atualizarMembro(
            @PathVariable UUID idExterno,
            @RequestBody @Valid MembroUpdateDTO request) {
        try {
            RestResponseMessageDTO response = atualizarMembroUseCase.execute(idExterno, request);
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (RuntimeException ex) {
            logger.warn("Falha ao atualizar membro. idExterno={}", idExterno);
            throw ex;
        }
    }
}
