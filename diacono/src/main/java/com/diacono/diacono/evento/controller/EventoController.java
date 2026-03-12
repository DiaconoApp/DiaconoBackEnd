package com.diacono.diacono.evento.controller;

import com.diacono.diacono.evento.model.dto.request.EventoCreateDTO;
import com.diacono.diacono.evento.model.dto.request.EventoUpdateDTO;
import com.diacono.diacono.evento.model.dto.response.EnderecoEventoSimplificadoDTO;
import com.diacono.diacono.evento.model.dto.response.EventoCompletoDTO;
import com.diacono.diacono.evento.model.dto.response.EventoSimplificadoDTO;
import com.diacono.diacono.evento.service.EventoService;
import com.diacono.diacono.global.dto.response.RestResponseMessage;
import com.diacono.diacono.global.error.comuns.ApiErrorsComuns;
import com.diacono.diacono.global.error.exceptions.FieldInvalidException;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;
// TODO: buscarEnderecoEvento colocar em cache

@RestController
@RequestMapping("/api/v1/eventos")
public class EventoController {

    private final EventoService eventoService;

    public EventoController(EventoService eventoService) {
        this.eventoService = eventoService;
    }

    @ApiErrorsComuns
    @ApiResponse(responseCode = "200", description = "Eventos encontrados com sucesso")
    // OWASP A01: leitura exige usuário autenticado com role permitida.
    @PreAuthorize("hasAnyAuthority('SCOPE_MEMBRO','SCOPE_LIDER_MINISTERIO','SCOPE_GOVERNO')")
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<EventoSimplificadoDTO> buscarEventosPorMesEAno(@RequestParam int mes, @RequestParam int ano) {
        // OWASP A01/A07: valida entradas antes da consulta.
        validateMesAno(mes, ano);

        return ResponseEntity.status(HttpStatus.OK)
                .headers(buildNoStoreHeaders())
                .body(eventoService.buscarEventosPorMesEAno(mes, ano));
    }

    @ApiErrorsComuns
    @ApiResponse(responseCode = "200", description = "Evento encontrado com sucesso")
    // OWASP A01: leitura exige usuário autenticado com role permitida.
    @PreAuthorize("hasAnyAuthority('SCOPE_MEMBRO','SCOPE_LIDER_MINISTERIO','SCOPE_GOVERNO')")
    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<EventoCompletoDTO> buscarEventoEspecifico(@PathVariable("id") UUID id) {
        // OWASP A01: valida UUID vindo da request.
        validateUUID(id);

        return ResponseEntity.status(HttpStatus.OK)
                .headers(buildNoStoreHeaders())
                .body(eventoService.buscarEventoEspecifico(id));
    }

    @ApiErrorsComuns
    @ApiResponse(responseCode = "200", description = "Evento encontrado com sucesso")
    // OWASP A01: leitura exige usuário autenticado com role permitida.
    @PreAuthorize("hasAnyAuthority('SCOPE_MEMBRO','SCOPE_LIDER_MINISTERIO','SCOPE_GOVERNO')")
    @GetMapping(value = "/enderecos", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<EnderecoEventoSimplificadoDTO> buscarEnderecoEvento() {
        return ResponseEntity.status(HttpStatus.OK)
                .headers(buildNoStoreHeaders())
                .body(eventoService.buscarEnderecoEvento());
    }

    @ApiErrorsComuns
    @ApiResponse(responseCode = "201", description = "Evento criado com sucesso")
    // OWASP A01: escrita restrita a perfis de liderança/governo.
    @PreAuthorize("hasAnyAuthority('SCOPE_LIDER_MINISTERIO','SCOPE_GOVERNO')")
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<RestResponseMessage> criarEvento(@RequestBody @Valid EventoCreateDTO request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .headers(buildNoStoreHeaders())
                .body(eventoService.criarEvento(request));
    }

    @ApiErrorsComuns
    @ApiResponse(responseCode = "204", description = "Evento deletado com sucesso")
    // OWASP A01: escrita restrita a perfis de liderança/governo.
    @PreAuthorize("hasAnyAuthority('SCOPE_LIDER_MINISTERIO','SCOPE_GOVERNO')")
    @DeleteMapping(value = "/unico/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<RestResponseMessage> apagarEventoUnico(@PathVariable UUID id) {
        validateUUID(id);

        return ResponseEntity.status(HttpStatus.NO_CONTENT)
                .headers(buildNoStoreHeaders())
                .body(eventoService.apagarEvento(id));
    }

    @ApiErrorsComuns
    @ApiResponse(responseCode = "204", description = "Eventos deletados com sucesso")
    // OWASP A01: escrita restrita a perfis de liderança/governo.
    @PreAuthorize("hasAnyAuthority('SCOPE_LIDER_MINISTERIO','SCOPE_GOVERNO')")
    @DeleteMapping(value = "/multiplos/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<RestResponseMessage> apagarEventosMultiplos(@PathVariable UUID id) {
        validateUUID(id);

        return ResponseEntity.status(HttpStatus.NO_CONTENT)
                .headers(buildNoStoreHeaders())
                .body(eventoService.apagarEventosMultiplos(id));
    }

    @ApiErrorsComuns
    @ApiResponse(responseCode = "204", description = "Evento atualizado com sucesso")
    // OWASP A01: escrita restrita a perfis de liderança/governo.
    @PreAuthorize("hasAnyAuthority('SCOPE_LIDER_MINISTERIO','SCOPE_GOVERNO')")
    @PatchMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<RestResponseMessage> atualizarEvento(@RequestBody @Valid EventoUpdateDTO evento, @PathVariable("id") UUID id) {
        validateUUID(id);

        return ResponseEntity.status(HttpStatus.NO_CONTENT)
                .headers(buildNoStoreHeaders())
                .body(eventoService.alterarEvento(evento, id));
    }

    // Utilizado para escalas
//    @ApiErrorsComuns
//    @ApiResponse(responseCode = "200", description = "Eventos encontrados com sucesso")
//    @GetMapping("/evento-ministerio")
//    public ResponseEntity<List<EventoComEventoMinisterioDTO>> buscarEventosComEventoMinisterioPorMesAno(
//            @RequestParam int mes,
//            @RequestParam int ano
//    ) {
//        return ResponseEntity.status(HttpStatus.OK)
//                .body(eventoService.buscarEventosComEventoMinisterioPorMesAno(mes, ano));
//    }

    // OWASP A01/A07: valida faixa de entrada para reduzir abuso e erro lógico.
    private void validateMesAno(int mes, int ano) {
        if (mes < 1 || mes > 12) {
            throw new FieldInvalidException("mes deve estar entre 1 e 12");
        }
        if (ano < 1900 || ano > 2100) {
            throw new FieldInvalidException("ano invalido");
        }
    }

    // OWASP A01: valida UUID obrigatório vindo da request.
    private void validateUUID(UUID id) {
        if (id == null) {
            throw new FieldInvalidException("id do evento nao pode ser nulo");
        }
    }

    // OWASP A02/A05: evita cache de respostas sensiveis e MIME sniffing.
    private HttpHeaders buildNoStoreHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Cache-Control", "no-store, no-cache, must-revalidate, max-age=0");
        headers.add("Pragma", "no-cache");
        headers.add("X-Content-Type-Options", "nosniff");
        return headers;
    }
}
