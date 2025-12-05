package com.diacono.diacono.eventoministerio.controller;

import com.diacono.diacono.eventoministerio.model.dto.request.EventoMinisterioNaoConfirmadoDTO;
import com.diacono.diacono.eventoministerio.model.dto.response.EventoMinisterioEscalaDTO;
import com.diacono.diacono.eventoministerio.model.entity.EventoMinisterio;
import com.diacono.diacono.eventoministerio.service.EventoMinisterioService;
import com.diacono.diacono.global.error.comuns.ApiErrorsComuns;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/eventos-ministerios")
public class EventoMinisterioController {

    private final EventoMinisterioService eventoMinisterioService;

    public EventoMinisterioController(EventoMinisterioService eventoMinisterioService) {
        this.eventoMinisterioService = eventoMinisterioService;
    }

    @ApiErrorsComuns
    @ApiResponse(responseCode = "200", description = "EventoMinisterio buscados com sucesso")
    @GetMapping("/evento-ministerio/{idExternoMinisterio}")
    public ResponseEntity<List<EventoMinisterioEscalaDTO>> buscarEventosMinisteriosPorMinisterioIdMesAno(
            @PathVariable UUID idExternoMinisterio,
            @RequestParam int mes,
            @RequestParam int ano
    ){
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(eventoMinisterioService.buscarEventosMinisteriosPorMinisterioMesAno(idExternoMinisterio, mes, ano));
    }

    @ApiErrorsComuns
    @ApiResponse(responseCode = "200", description = "EventoMinisterio buscados com sucesso")
    @GetMapping("/evento-ministerio/governo")
    public ResponseEntity<List<EventoMinisterioEscalaDTO>> buscarEventosMinisteriosPorMesAno(
            @RequestParam int mes,
            @RequestParam int ano
    ){
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(eventoMinisterioService.buscarEventosMinisteriosPorMesAno(mes, ano));
    }


    @ApiErrorsComuns
    @ApiResponse(responseCode = "200", description = "EventoMinisterio salvado com sucesso")
    @PostMapping("/evento-ministerio")
    public ResponseEntity<EventoMinisterio> salvarEventoMinisterio (
            @RequestBody EventoMinisterio eventoMinisterio
    ){
        EventoMinisterio eventoMinisterioSalvo = eventoMinisterioService.salvarEventoMinisterio(eventoMinisterio);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(eventoMinisterioSalvo);
    }

    @ApiErrorsComuns
    @ApiResponse
    @PatchMapping("/evento-ministerio/confirmar")
    public ResponseEntity<EventoMinisterio> confirmarEventoMinisterio (
            @RequestBody EventoMinisterioNaoConfirmadoDTO eventoMinisterioAConfirmar
    ){
        EventoMinisterio eventoMinisterioConfirmado = eventoMinisterioService.confirmarEventoMinisterio(eventoMinisterioAConfirmar);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(eventoMinisterioConfirmado);
    }

    @ApiErrorsComuns
    @ApiResponse(responseCode = "200", description = "EventoMinisterio deletado com sucesso")
    @DeleteMapping
    public ResponseEntity<Integer> deletarEventoMinisterio (
            @RequestBody EventoMinisterio eventoMinisterio
    ){
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(eventoMinisterioService.deletarEventoMinisterio(eventoMinisterio));
    }

}
