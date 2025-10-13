package com.diacono.diacono.evento.controller;


import com.diacono.diacono.evento.model.dto.request.EventoCreateDTO;
import com.diacono.diacono.evento.model.dto.request.EventoUpdateDTO;
import com.diacono.diacono.evento.model.dto.response.EventoCompletoDTO;
import com.diacono.diacono.evento.model.dto.response.EventoSimplificadoDTO;
import com.diacono.diacono.evento.service.EventoService;
import com.diacono.diacono.global.dto.response.RestResponseMessage;
import com.diacono.diacono.global.error.comuns.ApiErrorsComuns;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.UUID;

@RestController
@RequestMapping("/eventos")
public class EventoController {

    private final EventoService eventoService;

    public EventoController(EventoService eventoService) {
        this.eventoService = eventoService;
    }

    @ApiErrorsComuns
    @ApiResponse(responseCode = "200", description = "Eventos encontrados com sucesso")
    @GetMapping
    public ResponseEntity<EventoSimplificadoDTO> buscarEventosPorMesEAno(@RequestParam int mes, @RequestParam int ano){
        EventoSimplificadoDTO evento = eventoService.buscarEventosPorMesEAno(mes, ano);

        return ResponseEntity.status(HttpStatus.OK).body(evento);
    }

    @ApiErrorsComuns
    @ApiResponse(responseCode = "200", description = "Evento encontrado com sucesso")
    @GetMapping("/{id}/{data}")
    public ResponseEntity<EventoCompletoDTO> buscarEventoEspecifico(@PathVariable("id") UUID id, @PathVariable("data")  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate data){
        EventoCompletoDTO evento = eventoService.buscarEventoEspecifico(id, data);

        return ResponseEntity.status(HttpStatus.OK).body(evento);

    }

    @ApiErrorsComuns
    @ApiResponse(responseCode = "201", description = "Evento criado com sucesso")
    @PostMapping
    public ResponseEntity<RestResponseMessage> criarEvento(@RequestBody @Valid EventoCreateDTO request){
        return ResponseEntity.status(HttpStatus.CREATED).body(eventoService.criarEvento(request));
    }

    @ApiErrorsComuns
    @ApiResponse(responseCode = "200", description = "Evento deletado com sucesso")
    @DeleteMapping("/{id}")
    public ResponseEntity<RestResponseMessage> apagarEvento(@PathVariable UUID id){
       return ResponseEntity.status(HttpStatus.OK).body(eventoService.apagarEvento(id));
    }

    @ApiErrorsComuns
    @ApiResponse(responseCode = "200", description = "Evento atualizado com sucesso")
    @PutMapping("/{id}")
    public ResponseEntity<RestResponseMessage> atualizarEvento(@RequestBody EventoUpdateDTO evento, @PathVariable("id") UUID id){
        return ResponseEntity.status(HttpStatus.OK).body(eventoService.alterarEvento(evento, id));
    }



}
