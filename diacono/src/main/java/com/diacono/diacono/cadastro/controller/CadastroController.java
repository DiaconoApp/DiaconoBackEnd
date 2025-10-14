package com.diacono.diacono.cadastro.controller;

import com.diacono.diacono.Igreja.model.dto.response.IgrejaSemiCompletoDTO;
import com.diacono.diacono.cadastro.service.CadastroService;
import com.diacono.diacono.global.dto.response.RestResponseMessage;
import com.diacono.diacono.global.error.comuns.ApiErrorsComuns;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

    public void cadastrarMembro(){

    }

}
