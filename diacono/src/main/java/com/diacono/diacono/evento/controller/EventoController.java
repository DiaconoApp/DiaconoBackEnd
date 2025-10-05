package com.diacono.diacono.evento.controller;


import com.diacono.diacono.evento.model.dto.request.EventoCreateDTO;
import com.diacono.diacono.evento.service.EventoService;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping("/{id}")
    public void buscarEventoEspecifico(@PathVariable UUID id){
        eventoService.buscarEventoEspecifico(id);
    }

    @PostMapping
    public void criarEvento(@RequestBody EventoCreateDTO request){

    }

}
