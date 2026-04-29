package com.diacono.diacono.infrastructure.controllers;


import com.diacono.diacono.applications.dtos.evento.EventoCreateDTO;
import com.diacono.diacono.applications.dtos.evento.EventoUpdateDTO;
import com.diacono.diacono.applications.dtos.evento.EnderecoEventoSimplificadoDTO;
import com.diacono.diacono.applications.dtos.evento.EventoCompletoDTO;
import com.diacono.diacono.applications.dtos.evento.EventoSimplificadoDTO;
import com.diacono.diacono.applications.dtos.RestResponseMessageDTO;
import com.diacono.diacono.global.error.comuns.ApiErrorsComuns;
import com.diacono.diacono.usecases.eventos.*;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/eventos")
public class EventoController {

    private final BuscarEventosPorMesEAnoUseCase buscarEventosPorMesEAnoUseCase;
    private final BuscarEventoEspecificoUseCase buscarEventoEspecificoUseCase;
    private final BuscarEnderecoEventoUseCase buscarEnderecoEventoUseCase;
    private final CriarEventoUseCase criarEventoUseCase;
    private final ApagarEventoUnicoUseCase apagarEventoUseCase;
    private final ApagarEventosMultiplosUseCase apagarEventosMultiplosUseCase;
    private final AtualizarEventoUseCase atualizarEventoUseCase;

    public EventoController(BuscarEventosPorMesEAnoUseCase buscarEventosPorMesEAnoUseCase, BuscarEventoEspecificoUseCase buscarEventoEspecificoUseCase, BuscarEnderecoEventoUseCase buscarEnderecoEventoUseCase, CriarEventoUseCase criarEventoUseCase, ApagarEventoUnicoUseCase apagarEventoUseCase, ApagarEventosMultiplosUseCase apagarEventosMultiplosUseCase, AtualizarEventoUseCase atualizarEventoUseCase) {
        this.buscarEventosPorMesEAnoUseCase = buscarEventosPorMesEAnoUseCase;
        this.buscarEventoEspecificoUseCase = buscarEventoEspecificoUseCase;
        this.buscarEnderecoEventoUseCase = buscarEnderecoEventoUseCase;
        this.criarEventoUseCase = criarEventoUseCase;
        this.apagarEventoUseCase = apagarEventoUseCase;
        this.apagarEventosMultiplosUseCase = apagarEventosMultiplosUseCase;
        this.atualizarEventoUseCase = atualizarEventoUseCase;
    }

    @ApiErrorsComuns
    @ApiResponse(responseCode = "200", description = "Eventos encontrados com sucesso")
    @GetMapping
    //@PreAuthorize("hasAnyAuthority('SCOPE_MEMBRO','SCOPE_LIDER_MINISTERIO', 'SCOPE_GOVERNO')")
    public ResponseEntity<EventoSimplificadoDTO> buscarEventosPorMesEAno(@RequestParam int mes, @RequestParam int ano){
        //completo
        return ResponseEntity.status(HttpStatus.OK).body(buscarEventosPorMesEAnoUseCase.execute(mes, ano));
    }

    @ApiErrorsComuns
    @ApiResponse(responseCode = "200", description = "Evento encontrado com sucesso")
    @GetMapping("/{id}")
    //@PreAuthorize("hasAnyAuthority('SCOPE_MEMBRO', 'SCOPE_LIDER_MINISTERIO', 'SCOPE_GOVERNO')")
    public ResponseEntity<EventoCompletoDTO> buscarEventoEspecifico(@PathVariable("id") UUID id){
        //COMPLETO
        return ResponseEntity.status(HttpStatus.OK).body(buscarEventoEspecificoUseCase.execute(id));
    }

    @ApiErrorsComuns
    @ApiResponse(responseCode = "200", description = "Evento encontrado com sucesso")
    @GetMapping("/enderecos")
    //@PreAuthorize("hasAnyAuthority('SCOPE_MEMBRO', 'SCOPE_LIDER_MINISTERIO', 'SCOPE_GOVERNO')")
    public ResponseEntity<EnderecoEventoSimplificadoDTO> buscarEnderecoEvento(){
        //COMPLETO
        //COLOCAR ISSO NUM CACHE
        return ResponseEntity.status(HttpStatus.OK).body(buscarEnderecoEventoUseCase.execute());
    }

    @ApiErrorsComuns
    @ApiResponse(responseCode = "201", description = "Evento criado com sucesso")
    @PostMapping
    public ResponseEntity<RestResponseMessageDTO> criarEvento(@RequestBody @Valid EventoCreateDTO request){
        //CONCLUIDO
        return ResponseEntity.status(HttpStatus.CREATED).body(criarEventoUseCase.execute(request));
    }

    @ApiErrorsComuns
    @ApiResponse(responseCode = "204", description = "Evento deletado com sucesso")
    @DeleteMapping("/unico/{id}")
    public ResponseEntity<RestResponseMessageDTO> apagarEventoUnico(@PathVariable UUID id){
       return ResponseEntity.status(HttpStatus.NO_CONTENT).body(apagarEventoUseCase.execute(id));
    }

    @ApiErrorsComuns
    @ApiResponse(responseCode = "204", description = "Eventos deletados com sucesso")
    @DeleteMapping("/multiplos/{id}")
    public ResponseEntity<RestResponseMessageDTO> apagarEventosMultiplos(@PathVariable UUID id){
        //completo
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(apagarEventosMultiplosUseCase.execute(id));
    }

    @ApiErrorsComuns
    @ApiResponse(responseCode = "204", description = "Evento atualizado com sucesso")
    @PatchMapping("/{id}")
    public ResponseEntity<RestResponseMessageDTO> atualizarEvento(@RequestBody @Valid EventoUpdateDTO evento, @PathVariable("id") UUID id){

        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(atualizarEventoUseCase.execute(evento, id));
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

}
