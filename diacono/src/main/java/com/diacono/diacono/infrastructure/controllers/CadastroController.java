package com.diacono.diacono.infrastructure.controllers;

import com.diacono.diacono.applications.dtos.igreja.IgrejaSemiCompletoDTO;
import com.diacono.diacono.applications.dtos.CadastroExternoDTO;
import com.diacono.diacono.usecases.membro.CadastrarMembroUseCase;
import com.diacono.diacono.usecases.igreja.BuscasIgrejasUseCase;
import com.diacono.diacono.applications.dtos.RestResponseMessageDTO;
import com.diacono.diacono.global.error.comuns.ApiErrorsComuns;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/register")
public class CadastroController {

    private final CadastrarMembroUseCase cadastrarMembroUseCase;
    private final BuscasIgrejasUseCase buscasIgrejasUseCase;

    public CadastroController(CadastrarMembroUseCase cadastrarMembroUseCase, BuscasIgrejasUseCase buscasIgrejasUseCase) {
        this.cadastrarMembroUseCase = cadastrarMembroUseCase;
        this.buscasIgrejasUseCase = buscasIgrejasUseCase;
    }

    @ApiErrorsComuns
    @ApiResponse(responseCode = "200", description = "Igrejas encontradas com sucesso")
    @GetMapping
    public ResponseEntity<List<IgrejaSemiCompletoDTO>> buscarIgrejas(){

        List<IgrejaSemiCompletoDTO> igrejas = buscasIgrejasUseCase.execute();

        return ResponseEntity.status(HttpStatus.OK).body(igrejas);
    }

    @ApiErrorsComuns
    @ApiResponse(responseCode = "201", description = "Membro criado com sucesso")
    @PostMapping
    public ResponseEntity<RestResponseMessageDTO> cadastrarMembro(@RequestBody @Valid CadastroExternoDTO cadastroDTO){

        RestResponseMessageDTO response = cadastrarMembroUseCase.execute(cadastroDTO);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

}
