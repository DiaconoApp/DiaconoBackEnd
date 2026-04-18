package com.diacono.diacono.infrastructure.controllers;

import com.diacono.diacono.applications.dtos.membro.MembroDetalheResponseDTO;
import com.diacono.diacono.global.error.comuns.ApiErrorsComuns;
import com.diacono.diacono.usecases.BuscarPerfilUseCase;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/perfil")
public class PerfilController {

    private final BuscarPerfilUseCase buscarPerfilUseCase;

    public PerfilController(BuscarPerfilUseCase buscarPerfilUseCase) {
        this.buscarPerfilUseCase = buscarPerfilUseCase;
    }

    @ApiErrorsComuns
    @ApiResponse(responseCode = "200", description = "Perfil encontrado com sucesso")
    @GetMapping
    public ResponseEntity<MembroDetalheResponseDTO> buscarPerfil() {
        MembroDetalheResponseDTO response = buscarPerfilUseCase.execute();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}

