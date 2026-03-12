package com.diacono.diacono.ministerio.controller;

import com.diacono.diacono.global.dto.response.RestResponseMessage;
import com.diacono.diacono.global.error.exceptions.FieldInvalidException;
import com.diacono.diacono.membro.model.entity.EnumStatusMembro;
import com.diacono.diacono.membroministerio.model.dto.request.MembroMinisterioCreateDTO;
import com.diacono.diacono.membroministerio.model.dto.response.MembroMinisterioInfoMembroDTO;
import com.diacono.diacono.ministerio.model.dto.MinisterioCreateDTO;
import com.diacono.diacono.ministerio.model.dto.MinisterioUpdateDTO;
import com.diacono.diacono.ministerio.model.dto.response.MinisterioSimplificadoDTO;
import com.diacono.diacono.ministerio.model.dto.response.MinisterioSuperSimplificadoDTO;
import com.diacono.diacono.ministerio.model.entity.EnumStatusMinisterio;
import com.diacono.diacono.ministerio.service.MinisterioService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

/**
 * Controller de ministerios com autorizacao baseada em roles
 * OWASP A01: Implementa RBAC para diferentes niveis de acesso
 */
@RestController
@RequestMapping("/api/v1/ministerios")
public class MinisteriosController {

    private final MinisterioService ministerio;

    public MinisteriosController(MinisterioService ministerio) {
        this.ministerio = ministerio;
    }

    // USO GERAL - Endpoints autenticados basicos

    // OWASP A01: Requer autenticacao para listar ministerios
    // OWASP A05: Restringe resposta a JSON
    @PreAuthorize("isAuthenticated()")
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<MinisterioSimplificadoDTO>> buscarMinisteriosGerais() {
        return ResponseEntity.status(HttpStatus.OK).body(
                ministerio.buscarMinisteriosGerais()
        );

    }

    // VISAO GOVERNO - Endpoints restritos a administradores

    // OWASP A01: Restringe acesso a usuarios com role GOVERNO
    // OWASP A05: Restringe resposta a JSON
    @PreAuthorize("hasAuthority('SCOPE_GOVERNO')")
    @GetMapping(value = "/governo", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Page<MinisterioSimplificadoDTO>> buscarMinisteriosGoverno(
            Pageable pageable,
            @RequestParam(required = false, defaultValue = "") String buscaGeral,
            @RequestParam(required = false) EnumStatusMinisterio status) {

        boolean semBusca = (buscaGeral == null || buscaGeral.isBlank());
        boolean semStatus = (status == null);

        Page pagina;

        if (semBusca && semStatus) {
            pagina = ministerio.buscarMinisteriosGoverno(pageable);
            return ResponseEntity.status(HttpStatus.OK).body(pagina);
        }

        pagina = ministerio.buscarMinisteriosGovernoComFiltro(pageable, buscaGeral, status);
        return ResponseEntity.status(HttpStatus.OK).body(pagina);
    }

    // OWASP A01: Restringe criacao de ministerios a role GOVERNO
    // OWASP A05: Aceita e retorna apenas JSON
    @PreAuthorize("hasAuthority('SCOPE_GOVERNO')")
    @PostMapping(value = "/governo", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<RestResponseMessage> adicionarMinisterio(@RequestBody @Valid MinisterioCreateDTO ministerioCreateDTO) {
        return ResponseEntity.status(HttpStatus.CREATED).body(
                ministerio.criarMinisterio(ministerioCreateDTO)
        );
    }

    // OWASP A01: Restringe edicao de ministerios a role GOVERNO
    // OWASP A01: Valida UUID antes de processar
    // OWASP A05: Aceita e retorna apenas JSON
    @PreAuthorize("hasAuthority('SCOPE_GOVERNO')")
    @PatchMapping(value = "/governo/{idMinisterio}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<RestResponseMessage> editarMinisterio(
            @PathVariable UUID idMinisterio,
            @RequestBody @Valid MinisterioUpdateDTO ministerioUpdateDTO) {
        // OWASP A01: Validacao defensiva de ID obrigatorio
        validateUUID(idMinisterio, "ID do ministerio");

        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(
                ministerio.editarMinisterio(ministerioUpdateDTO, idMinisterio)
        );
    }

    // VISAO LIDER MINISTERIO - Endpoints para lideres de ministerio

    // OWASP A01: Restringe acesso a lideres de ministerio
    // OWASP A01: Valida UUID e propriedade do recurso via service
    // OWASP A05: Restringe resposta a JSON
    @PreAuthorize("hasAnyAuthority('SCOPE_LIDER_MINISTERIO', 'SCOPE_GOVERNO')")
    @GetMapping(value = "/lider-ministerio/{idMinisterio}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Page<MembroMinisterioInfoMembroDTO>> buscarMembroMinisterioLiderMinisterio(
            @PathVariable UUID idMinisterio,
            Pageable pageable,
            @RequestParam(required = false, defaultValue = "") String buscaGeral,
            @RequestParam(required = false) EnumStatusMembro status) {

        // OWASP A01: Validacao defensiva de ID obrigatorio
        validateUUID(idMinisterio, "ID do ministerio");

        boolean semBusca = (buscaGeral == null || buscaGeral.isBlank());
        boolean semStatus = (status == null);

        Page<MembroMinisterioInfoMembroDTO> pagina;

        if (semBusca && semStatus) {
            pagina = ministerio.buscarMembroMinisterioLiderMinisterio(idMinisterio, pageable);
            return ResponseEntity.status(HttpStatus.OK).body(pagina);
        }

        pagina = ministerio.buscarMembroMinisterioLiderMinisterioComFiltro(idMinisterio, pageable, buscaGeral, status);

        return ResponseEntity.status(HttpStatus.OK).body(pagina);
    }

    // OWASP A01: Restringe acesso a lideres de ministerio
    // OWASP A05: Restringe resposta a JSON
    @PreAuthorize("hasAnyAuthority('SCOPE_LIDER_MINISTERIO', 'SCOPE_GOVERNO')")
    @GetMapping(value = "/lider-ministerio", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<MinisterioSuperSimplificadoDTO>> buscarMinisteriosLiderMinisterio() {
        List<MinisterioSuperSimplificadoDTO> listaMinisterios = ministerio.buscarMinisteriosLiderMinisterio();

        return ResponseEntity.status(HttpStatus.OK).body(listaMinisterios);
    }

    // OWASP A01: Restringe adicao de membros a lideres do ministerio
    // OWASP A01: Valida UUID e propriedade via service
    // OWASP A05: Aceita e retorna apenas JSON
    @PreAuthorize("hasAnyAuthority('SCOPE_LIDER_MINISTERIO', 'SCOPE_GOVERNO')")
    @PatchMapping(value = "/lider-ministerio/{idMinisterio}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<RestResponseMessage> adicionarMembroMinisterioLiderMinisterio(
            @PathVariable UUID idMinisterio,
            @RequestBody @Valid MembroMinisterioCreateDTO membroMinisterio) {

        // OWASP A01: Validacao defensiva de ID obrigatorio
        validateUUID(idMinisterio, "ID do ministerio");

        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(
                ministerio.adicionarMembroMinisterioLiderMinisterio(idMinisterio, membroMinisterio)
        );
    }

    // OWASP A01: Restringe remocao de membros a lideres do ministerio
    // OWASP A01: Valida ambos os UUIDs antes de processar
    // OWASP A05: Restringe resposta a JSON
    @PreAuthorize("hasAnyAuthority('SCOPE_LIDER_MINISTERIO', 'SCOPE_GOVERNO')")
    @DeleteMapping(value = "/lider-ministerio/{idMinisterio}/{idMembroMinisterio}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<RestResponseMessage> removerMembroMinisterioLiderMinisterio(
            @PathVariable UUID idMinisterio,
            @PathVariable UUID idMembroMinisterio) {

        // OWASP A01: Validacao defensiva de IDs obrigatorios
        validateUUID(idMinisterio, "ID do ministerio");
        validateUUID(idMembroMinisterio, "ID do membro ministerio");

        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(
                ministerio.removerMembroMinisterioLiderMinisterio(idMinisterio, idMembroMinisterio)
        );
    }

    /**
     * OWASP A01: Metodo auxiliar para validacao defensiva de UUIDs
     * Previne processamento de IDs invalidos
     */
    private void validateUUID(UUID id, String fieldName) {
        if (id == null) {
            throw new FieldInvalidException(fieldName + " nao pode ser nulo");
        }
    }
}
