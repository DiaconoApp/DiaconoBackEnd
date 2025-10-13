package com.diacono.diacono.evento.controller;


import com.diacono.diacono.evento.model.dto.request.EventoCreateDTO;
import com.diacono.diacono.evento.model.dto.request.EventoUpdateDTO;
import com.diacono.diacono.evento.service.EventoService;
import com.diacono.diacono.global.dto.response.RestResponseMessage;
import jakarta.validation.Valid;
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

    @GetMapping
    public void buscarEventosPorMesEAno(@RequestParam int mes, @RequestParam int ano){
        eventoService.buscarEventosPorMesEAno(mes, ano);
    }

    @GetMapping("/{id}/{dataHoje}")
    public void buscarEventoEspecifico(@PathVariable UUID id,@PathVariable LocalDate dataHoje){
        eventoService.buscarEventoEspecifico(id, dataHoje);
    }

    @PostMapping
    public ResponseEntity<RestResponseMessage> criarEvento(@RequestBody @Valid EventoCreateDTO request){
        return ResponseEntity.status(HttpStatus.CREATED).body(eventoService.criarEvento(request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<RestResponseMessage> apagarEvento(@PathVariable UUID id){
       return ResponseEntity.status(HttpStatus.OK).body(eventoService.apagarEvento(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<RestResponseMessage> atualizarEvento(@RequestBody EventoUpdateDTO evento, UUID id){
        return ResponseEntity.status(HttpStatus.OK).body(eventoService.alterarEvento(evento, id));
    }



}
