package com.diacono.diacono.evento.controller;


import com.diacono.diacono.evento.service.EventoService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/eventos")
public class EventoController {

    private final EventoService eventoService;

    public EventoController(EventoService eventoService) {
        this.eventoService = eventoService;
    }

    @GetMapping
    public void buscarEventosPorMesEAno(@RequestParam int mes, @RequestParam int ano){
        
        if (mes < 1 || mes > 12) {
            throw new IllegalArgumentException("Mês inválido");
        }

        eventoService.buscarEventosPorMesEAno(mes, ano);
    }
}
