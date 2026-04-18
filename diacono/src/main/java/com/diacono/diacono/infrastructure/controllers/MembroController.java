package com.diacono.diacono.infrastructure.controllers;

import com.diacono.diacono.applications.dtos.RestResponseMessageDTO;
import com.diacono.diacono.applications.dtos.membro.MembroUpdateDTO;
import com.diacono.diacono.global.error.comuns.ApiErrorsComuns;
import com.diacono.diacono.applications.dtos.membro.MembroCreateDTO;
import com.diacono.diacono.applications.dtos.membro.MembroResponseDTO;
import com.diacono.diacono.domain.enums.EnumStatusMembro;
import com.diacono.diacono.usecases.membro.*;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/membros")
public class MembroController {

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
    @PreAuthorize("hasAuthority('SCOPE_GOVERNO')")
    public ResponseEntity<RestResponseMessageDTO> criarMembro(@RequestBody @Valid MembroCreateDTO membroDTO){
        RestResponseMessageDTO response = criarMembroUseCase.execute(membroDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @ApiErrorsComuns
    @ApiResponse(responseCode = "200", description = "Membros encontrados com sucesso")
    @GetMapping
    @PreAuthorize("hasAnyAuthority('SCOPE_LIDER_MINISTERIO','SCOPE_GOVERNO')")
    public ResponseEntity<Page<MembroResponseDTO>> buscarTodos(Pageable pageable,
                                                               @RequestParam(required = false, defaultValue = "") String buscaGeral,
                                                               @RequestParam(required = false) EnumStatusMembro status,
                                                               @RequestParam(required = false) UUID fkMinisterio) {
        if ((buscaGeral == null || buscaGeral.isBlank()) && fkMinisterio == null && status == null) {
            return ResponseEntity.ok(buscarTodosSemFiltroUseCase.execute(pageable));
        }
        return ResponseEntity.ok(buscarTodosComFiltroUseCase.execute(pageable, buscaGeral, status, fkMinisterio));
    }

    @ApiErrorsComuns
    @ApiResponse(responseCode = "200", description = "Perfil do usuário autenticado")
    @GetMapping("/me")
    @PreAuthorize("hasAnyAuthority('SCOPE_MEMBRO','SCOPE_LIDER_MINISTERIO','SCOPE_GOVERNO')")
    public ResponseEntity<MembroResponseDTO> buscarPerfilLogado(@AuthenticationPrincipal Jwt jwt) {
        UUID idExterno = UUID.fromString(jwt.getSubject());
        return ResponseEntity.ok(buscarMembroPorUUIDUseCase.execute(idExterno));
    }

    @ApiErrorsComuns
    @ApiResponse(responseCode = "200", description = "Membro encontrado com sucesso")
    @GetMapping("/{idExterno}")
    @PreAuthorize("hasAnyAuthority('SCOPE_MEMBRO','SCOPE_LIDER_MINISTERIO','SCOPE_GOVERNO')")
    public ResponseEntity<MembroResponseDTO> buscarPorId(@PathVariable UUID idExterno) {
        return ResponseEntity.ok(buscarMembroPorUUIDUseCase.execute(idExterno));
    }

    @ApiErrorsComuns
    @ApiResponse(responseCode = "200", description = "Membro atualizado com sucesso")
    @PatchMapping("/{idExterno}")
    @PreAuthorize("hasAnyAuthority('SCOPE_LIDER_MINISTERIO','SCOPE_GOVERNO')")
    public ResponseEntity<RestResponseMessageDTO> atualizarMembro(
            @PathVariable UUID idExterno,
            @RequestBody @Valid MembroUpdateDTO request) {
        return ResponseEntity.ok(atualizarMembroUseCase.execute(idExterno, request));
    }
}
