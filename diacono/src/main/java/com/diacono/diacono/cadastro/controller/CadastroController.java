package com.diacono.diacono.cadastro.controller;

import com.diacono.diacono.Igreja.model.dto.response.IgrejaSemiCompletoDTO;
import com.diacono.diacono.cadastro.model.dto.CadastroExternoDTO;
import com.diacono.diacono.cadastro.service.CadastroService;
import com.diacono.diacono.global.dto.response.RestResponseMessage;
import com.diacono.diacono.global.error.comuns.ApiErrorsComuns;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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
    // OWASP A01: regra de acesso explicita no backend para endpoint publico.
    @PreAuthorize("permitAll()")
    // OWASP A05: resposta restrita a JSON.
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<IgrejaSemiCompletoDTO>> buscarIgrejas() {
        List<IgrejaSemiCompletoDTO> igrejas = cadastroService.buscarIgrejas();

        return ResponseEntity.status(HttpStatus.OK)
                .headers(buildSecurityHeaders(false))
                .body(igrejas);
    }

    @ApiErrorsComuns
    @ApiResponse(responseCode = "201", description = "Membro criado com sucesso")
    // OWASP A01: regra de acesso explicita no backend para endpoint publico de cadastro.
    @PreAuthorize("permitAll()")
    // OWASP A05/A07: aceita e retorna apenas JSON para reduzir interpretações indevidas.
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<RestResponseMessage> cadastrarMembro(@RequestBody @Valid CadastroExternoDTO cadastroDTO) {
        RestResponseMessage response = cadastroService.cadastrarMembro(cadastroDTO);

        return ResponseEntity.status(HttpStatus.CREATED)
                .headers(buildSecurityHeaders(true))
                .body(response);
    }

    private HttpHeaders buildSecurityHeaders(boolean noStore) {
        HttpHeaders headers = new HttpHeaders();

        // OWASP A05: evita MIME sniffing no cliente.
        headers.add("X-Content-Type-Options", "nosniff");

        // OWASP A02/A07: evita cache de respostas sensiveis no fluxo de cadastro.
        if (noStore) {
            headers.add("Cache-Control", "no-store, no-cache, must-revalidate, max-age=0");
            headers.add("Pragma", "no-cache");
        }

        return headers;
    }
}
