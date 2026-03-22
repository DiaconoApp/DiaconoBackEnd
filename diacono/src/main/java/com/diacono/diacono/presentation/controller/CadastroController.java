package com.diacono.diacono.presentation.controller;

import com.diacono.diacono.presentation.dto.response.IgrejaSemiCompletoDTO;
import com.diacono.diacono.presentation.dto.request.CadastroExternoDTO;
import com.diacono.diacono.cadastro.service.CadastroService;
import com.diacono.diacono.presentation.dto.response.RestResponseMessage;
import com.diacono.diacono.presentation.error.ApiErrorsComuns;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/register")
public class CadastroController {

    private final CadastroService cadastroService;

    public CadastroController(CadastroService cadastroService) {
        this.cadastroService = cadastroService;
    }

    @ApiErrorsComuns
    @ApiResponse(responseCode = "200", description = "Igrejas encontradas com sucesso")
    @GetMapping
    public ResponseEntity<List<IgrejaSemiCompletoDTO>> buscarIgrejas(){
        List<IgrejaSemiCompletoDTO> igrejas = cadastroService.buscarIgrejas();
        return ResponseEntity.status(HttpStatus.OK).body(igrejas);
    }

    @ApiErrorsComuns
    @ApiResponse(responseCode = "201", description = "Membro criado com sucesso")
    @PostMapping
    public ResponseEntity<RestResponseMessage> cadastrarMembro(@RequestBody @Valid CadastroExternoDTO cadastroDTO){

        RestResponseMessage response = cadastroService.cadastrarMembro(cadastroDTO);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

}
