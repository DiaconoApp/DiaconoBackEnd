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
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/membros")
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
    public ResponseEntity<RestResponseMessageDTO> criarMembro(@RequestBody @Valid MembroCreateDTO membroDTO){

        RestResponseMessageDTO response = criarMembroUseCase.execute(membroDTO);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @ApiErrorsComuns
    @ApiResponse(responseCode = "200", description = "Membros encontrados com sucesso")
    @GetMapping
    public ResponseEntity<Page<MembroResponseDTO>> buscarTodos(Pageable pageable, @RequestParam(required = false, defaultValue = "") String buscaGeral,
                                                                        @RequestParam(required = false) EnumStatusMembro status,
                                                                        @RequestParam(required = false) UUID fkMinisterio) {

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
    public ResponseEntity<MembroResponseDTO> buscarPorId(@PathVariable UUID idExterno) {
        MembroResponseDTO response = buscarMembroPorUUIDUseCase.execute(idExterno);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PatchMapping("/{idExterno}")
    public ResponseEntity<RestResponseMessageDTO> atualizarMembro(
            @PathVariable UUID idExterno,
            @RequestBody @Valid MembroUpdateDTO request) {

        RestResponseMessageDTO response = atualizarMembroUseCase.execute(idExterno, request);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }


//    @ApiErrorsComuns
//    @ApiResponse(responseCode = "200", description = "Membros encontrados com sucesso")
//    @GetMapping("/{idExternoMinisterio}")
//    public ResponseEntity<List<MembroSimplificadoDTO>> buscarMembrosPorMinisterioSemEscala(
//            @RequestParam UUID idExternoMinisterio,
//            @RequestBody EventoUnicoSimplificadoDTO eventoUnicoSimplificadoDTO
//            ) {
//        List<MembroSimplificadoDTO> membrosDisponiveisParaEscala = membrosService.buscarMembrosDisponiveisParaEscala(idExternoMinisterio, eventoUnicoSimplificadoDTO);
//        return ResponseEntity.status(HttpStatus.OK).body(membrosDisponiveisParaEscala);
//    }


}
