package com.diacono.diacono.infrastructure.controllers;

import com.diacono.diacono.applications.dtos.RestResponseMessageDTO;
import com.diacono.diacono.applications.dtos.membro.MembroDetalheResponseDTO;
import com.diacono.diacono.applications.dtos.membro.MembroUpdateDTO;
import com.diacono.diacono.global.error.comuns.ApiErrorsComuns;
import com.diacono.diacono.global.util.JwtUtils;
import com.diacono.diacono.usecases.BuscarPerfilUseCase;
import com.diacono.diacono.usecases.membro.AtualizarMembroUseCase;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/perfil")
public class PerfilController {

    private final BuscarPerfilUseCase buscarPerfilUseCase;
    private final AtualizarMembroUseCase atualizarMembroUseCase;
    private final JwtUtils jwtUtils;

    public PerfilController(BuscarPerfilUseCase buscarPerfilUseCase, AtualizarMembroUseCase atualizarMembroUseCase, JwtUtils jwtUtils) {
        this.buscarPerfilUseCase = buscarPerfilUseCase;
        this.atualizarMembroUseCase = atualizarMembroUseCase;
        this.jwtUtils = jwtUtils;
    }

    @ApiErrorsComuns
    @ApiResponse(responseCode = "200", description = "Perfil encontrado com sucesso")
    @GetMapping
    public ResponseEntity<MembroDetalheResponseDTO> buscarPerfil() {
        MembroDetalheResponseDTO response = buscarPerfilUseCase.execute();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @ApiErrorsComuns
    @ApiResponse(responseCode = "200", description = "Perfil atualizado com sucesso")
    @PatchMapping
    public ResponseEntity<RestResponseMessageDTO> atualizarPerfil(@RequestBody @Valid MembroUpdateDTO request) {
        UUID idExternoMembro = jwtUtils.getSubject();
        RestResponseMessageDTO response = atualizarMembroUseCase.execute(idExternoMembro, request);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}

