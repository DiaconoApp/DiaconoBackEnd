package com.diacono.diacono.membro.controller;

import com.diacono.diacono.global.dto.response.RestResponseMessage;
import com.diacono.diacono.global.error.comuns.ApiErrorsComuns;
import com.diacono.diacono.global.error.exceptions.FieldInvalidException;
import com.diacono.diacono.membro.model.dto.request.MembroCreateDTO;
import com.diacono.diacono.membro.model.dto.response.MembroResponseDTO;
import com.diacono.diacono.membro.model.entity.EnumStatusMembro;
import com.diacono.diacono.membro.service.MembroService;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;
// TODO: Padronizar criação de headers de resposta para evitar cache e proteger dados sensiveis, aplicando em todos os controllers.
@RestController
@RequestMapping("/membros")
public class MembroController {

    // OWASP A05/A07: limite de pagina para reduzir abuso de recursos via paginacao.
    private static final int MAX_PAGE_SIZE = 100;
    // OWASP A01: limite para evitar payload de filtro excessivo e custo de query desproporcional.
    private static final int MAX_BUSCA_GERAL_LENGTH = 255;

    private final MembroService membrosService;

    public MembroController(MembroService membrosService) {
        this.membrosService = membrosService;
    }

    @ApiErrorsComuns
    @ApiResponse(responseCode = "201", description = "Membro criado com sucesso")
    // OWASP A01: criacao de membro exige permissao administrativa explicita no backend.
    @PreAuthorize("hasAuthority('SCOPE_GOVERNO')")
    // OWASP A05: restringe input/output para JSON.
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<RestResponseMessage> criarMembro(@RequestBody @Valid MembroCreateDTO membroDTO) {

        RestResponseMessage response = membrosService.criarMembro(membroDTO);

        return ResponseEntity.status(HttpStatus.CREATED)
                .headers(buildNoStoreHeaders())
                .body(response);
    }

    @ApiErrorsComuns
    @ApiResponse(responseCode = "200", description = "Membros encontrados com sucesso")
    // OWASP A01: listagem de membros exige autenticacao e perfil autorizado.
    @PreAuthorize("hasAnyAuthority('SCOPE_GOVERNO','SCOPE_LIDER_MINISTERIO')")
    // OWASP A05: restringe resposta para JSON.
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Page<MembroResponseDTO>> buscarTodos(
            Pageable pageable,
            @RequestParam(required = false, defaultValue = "") String buscaGeral,
            @RequestParam(required = false) EnumStatusMembro status,
            @RequestParam(required = false) UUID fkMinisterio) {

        // OWASP A01/A07: validacoes defensivas de parametros de busca e paginacao.
        validateBuscaGeral(buscaGeral);
        validatePageable(pageable);

        String buscaNormalizada = buscaGeral == null ? "" : buscaGeral.trim();

        if (fkMinisterio == null && status == null) {
            Page<MembroResponseDTO> response = membrosService.buscarTodosSemFiltro(pageable);
            return ResponseEntity.status(HttpStatus.OK)
                    .headers(buildNoStoreHeaders())
                    .body(response);
        }

        Page<MembroResponseDTO> response = membrosService.buscarTodosComFiltro(pageable, buscaNormalizada, status, fkMinisterio);

        return ResponseEntity.status(HttpStatus.OK)
                .headers(buildNoStoreHeaders())
                .body(response);
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


    // OWASP A02/A05: evita cache de resposta com dados de membros.
    private HttpHeaders buildNoStoreHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Cache-Control", "no-store, no-cache, must-revalidate, max-age=0");
        headers.add("Pragma", "no-cache");
        headers.add("X-Content-Type-Options", "nosniff");
        return headers;
    }

    private void validateBuscaGeral(String buscaGeral) {
        if (buscaGeral != null && buscaGeral.length() > MAX_BUSCA_GERAL_LENGTH) {
            throw new FieldInvalidException("buscaGeral excede o limite de 255 caracteres");
        }
    }

    private void validatePageable(Pageable pageable) {
        if (pageable != null && pageable.getPageSize() > MAX_PAGE_SIZE) {
            throw new FieldInvalidException("pageSize excede o limite maximo de 100");
        }
    }
}
