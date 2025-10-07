package com.diacono.diacono.evento.controller;


import com.diacono.diacono.evento.model.dto.request.EventoCreateDTO;
import com.diacono.diacono.evento.service.EventoService;
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
    public void criarEvento(@RequestBody EventoCreateDTO request){
        eventoService.criarEvento(request);
    }

    @DeleteMapping("/{id}")
    public void apagarEvento(@PathVariable UUID idExterno){
        eventoService.apagarEvento(idExterno);
    }



}
