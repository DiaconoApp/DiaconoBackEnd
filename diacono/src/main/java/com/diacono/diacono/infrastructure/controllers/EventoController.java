package com.diacono.diacono.infrastructure.controllers;


import com.diacono.diacono.applications.dtos.evento.EventoCreateDTO;
import com.diacono.diacono.applications.dtos.evento.EventoUpdateDTO;
import com.diacono.diacono.applications.dtos.evento.EnderecoEventoSimplificadoDTO;
import com.diacono.diacono.applications.dtos.evento.EventoCompletoDTO;
import com.diacono.diacono.applications.dtos.evento.EventoSimplificadoDTO;
import com.diacono.diacono.applications.dtos.RestResponseMessageDTO;
import com.diacono.diacono.global.error.comuns.ApiErrorsComuns;
import com.diacono.diacono.global.util.JwtUtils;
import com.diacono.diacono.usecases.eventos.*;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/eventos")
public class EventoController {

    private static final Logger logger = LoggerFactory.getLogger(EventoController.class);

    private final BuscarEventosPorMesEAnoUseCase buscarEventosPorMesEAnoUseCase;
    private final BuscarEventoEspecificoUseCase buscarEventoEspecificoUseCase;
    private final BuscarEnderecoEventoUseCase buscarEnderecoEventoUseCase;
    private final CriarEventoUseCase criarEventoUseCase;
    private final ApagarEventoUnicoUseCase apagarEventoUseCase;
    private final ApagarEventosMultiplosUseCase apagarEventosMultiplosUseCase;
    private final AtualizarEventoUseCase atualizarEventoUseCase;
    private final JwtUtils jwtUtils;

    public EventoController(BuscarEventosPorMesEAnoUseCase buscarEventosPorMesEAnoUseCase, BuscarEventoEspecificoUseCase buscarEventoEspecificoUseCase, BuscarEnderecoEventoUseCase buscarEnderecoEventoUseCase, CriarEventoUseCase criarEventoUseCase, ApagarEventoUnicoUseCase apagarEventoUseCase, ApagarEventosMultiplosUseCase apagarEventosMultiplosUseCase, AtualizarEventoUseCase atualizarEventoUseCase, JwtUtils jwtUtils) {
        this.buscarEventosPorMesEAnoUseCase = buscarEventosPorMesEAnoUseCase;
        this.buscarEventoEspecificoUseCase = buscarEventoEspecificoUseCase;
        this.buscarEnderecoEventoUseCase = buscarEnderecoEventoUseCase;
        this.criarEventoUseCase = criarEventoUseCase;
        this.apagarEventoUseCase = apagarEventoUseCase;
        this.apagarEventosMultiplosUseCase = apagarEventosMultiplosUseCase;
        this.atualizarEventoUseCase = atualizarEventoUseCase;
        this.jwtUtils = jwtUtils;
    }

    @ApiErrorsComuns
    @ApiResponse(responseCode = "200", description = "Eventos encontrados com sucesso")
    @GetMapping
    @PreAuthorize("hasAnyAuthority('SCOPE_MEMBRO','SCOPE_LIDER_MINISTERIO', 'SCOPE_GOVERNO')")
    public ResponseEntity<EventoSimplificadoDTO> buscarEventosPorMesEAno(@RequestParam int mes, @RequestParam int ano){
        //completo
        return ResponseEntity.status(HttpStatus.OK).body(buscarEventosPorMesEAnoUseCase.execute(mes, ano, jwtUtils.getIgrejaId()));
    }

    @ApiErrorsComuns
    @ApiResponse(responseCode = "200", description = "Evento encontrado com sucesso")
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('SCOPE_MEMBRO', 'SCOPE_LIDER_MINISTERIO', 'SCOPE_GOVERNO')")
    public ResponseEntity<EventoCompletoDTO> buscarEventoEspecifico(@PathVariable("id") UUID id){
        //COMPLETO
        return ResponseEntity.status(HttpStatus.OK).body(buscarEventoEspecificoUseCase.execute(id, jwtUtils.getIgrejaId()));
    }

    @ApiErrorsComuns
    @ApiResponse(responseCode = "200", description = "Evento encontrado com sucesso")
    @GetMapping("/enderecos")
    @PreAuthorize("hasAnyAuthority('SCOPE_MEMBRO', 'SCOPE_LIDER_MINISTERIO', 'SCOPE_GOVERNO')")
    public ResponseEntity<EnderecoEventoSimplificadoDTO> buscarEnderecoEvento(){
        //COMPLETO
        //COLOCAR ISSO NUM CACHE
        return ResponseEntity.status(HttpStatus.OK).body(buscarEnderecoEventoUseCase.execute(jwtUtils.getIgrejaId()));
    }

    @ApiErrorsComuns
    @ApiResponse(responseCode = "201", description = "Evento criado com sucesso")
    @PostMapping
    @PreAuthorize("hasAnyAuthority('SCOPE_LIDER_MINISTERIO', 'SCOPE_GOVERNO')")
    public ResponseEntity<RestResponseMessageDTO> criarEvento(@RequestBody @Valid EventoCreateDTO request){
        //CONCLUIDO
        try {
            return ResponseEntity.status(HttpStatus.CREATED).body(criarEventoUseCase.execute(request, jwtUtils.getIgrejaId()));
        } catch (RuntimeException ex) {
            logger.warn("Falha ao criar evento.");
            throw ex;
        }
    }

    @ApiErrorsComuns
    @ApiResponse(responseCode = "204", description = "Evento deletado com sucesso")
    @DeleteMapping("/unico/{id}")
    @PreAuthorize("hasAnyAuthority('SCOPE_LIDER_MINISTERIO', 'SCOPE_GOVERNO')")
    public ResponseEntity<RestResponseMessageDTO> apagarEventoUnico(@PathVariable UUID id){
       try {
           return ResponseEntity.status(HttpStatus.NO_CONTENT).body(apagarEventoUseCase.execute(id, jwtUtils.getIgrejaId()));
       } catch (RuntimeException ex) {
           logger.warn("Falha ao apagar evento unico. id={}", id);
           throw ex;
       }
    }

    @ApiErrorsComuns
    @ApiResponse(responseCode = "204", description = "Eventos deletados com sucesso")
    @DeleteMapping("/multiplos/{id}")
    @PreAuthorize("hasAnyAuthority('SCOPE_LIDER_MINISTERIO', 'SCOPE_GOVERNO')")
    public ResponseEntity<RestResponseMessageDTO> apagarEventosMultiplos(@PathVariable UUID id){
        //completo
        try {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(apagarEventosMultiplosUseCase.execute(id, jwtUtils.getIgrejaId()));
        } catch (RuntimeException ex) {
            logger.warn("Falha ao apagar eventos multiplos. id={}", id);
            throw ex;
        }
    }

    @ApiErrorsComuns
    @ApiResponse(responseCode = "204", description = "Evento atualizado com sucesso")
    @PatchMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('SCOPE_LIDER_MINISTERIO', 'SCOPE_GOVERNO')")
    public ResponseEntity<RestResponseMessageDTO> atualizarEvento(@RequestBody @Valid EventoUpdateDTO evento, @PathVariable("id") UUID id){
        try {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(atualizarEventoUseCase.execute(evento, id, jwtUtils.getIgrejaId()));
        } catch (RuntimeException ex) {
            logger.warn("Falha ao atualizar evento. id={}", id);
            throw ex;
        }
    }
}
