//package com.diacono.diacono.escala.controller;
//
//import com.diacono.diacono.escala.model.dto.request.EscalasSalvarDTO;
//import com.diacono.diacono.escala.model.dto.response.EscalaMembroDTO;
//import com.diacono.diacono.escala.model.entity.Escala;
//import com.diacono.diacono.escala.service.EscalaService;
//import com.diacono.diacono.evento.model.dto.response.EventoUnicoSimplificadoDTO;
//import com.diacono.diacono.global.error.comuns.ApiErrorsComuns;
//import com.diacono.diacono.membro.model.dto.response.MembroSimplificadoDTO;
//import io.swagger.v3.oas.annotations.responses.ApiResponse;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.List;
//import java.util.UUID;
//
//@RestController
//@RequestMapping("api/v1/escala")
//public class EscalaController {
//
//    private final EscalaService escalaService;
//
//    public EscalaController(EscalaService escalaService) {
//        this.escalaService = escalaService;
//    }
//
//    @ApiErrorsComuns
//    @ApiResponse(responseCode = "200", description = "Escalas buscadas com sucesso")
//    @GetMapping("/membro")
//    public ResponseEntity<List<EscalaMembroDTO>> buscarEscalasPorMembroIdMesAno(
//            @RequestParam int mes,
//            @RequestParam int ano
//    ){
//        return ResponseEntity
//                .status(HttpStatus.OK)
//                .body(escalaService.buscarEscalasPorMembroIdMesAno(mes, ano));
//    }
//
//    @ApiErrorsComuns
//    @ApiResponse(responseCode = "200", description = "Escalas buscadas com sucesso")
//    @GetMapping("/evento-ministerio")
//    public ResponseEntity <List<EscalaMembroDTO>> buscarEscalasPorEventoMinisterioId(
//            @RequestParam UUID eventoMinisterioId
//    ){
//        return ResponseEntity
//                .status(HttpStatus.OK)
//                .body(escalaService.buscarEscalasPorEventoMinisterioId(eventoMinisterioId));
//    }
//
//    @ApiErrorsComuns
//    @ApiResponse(responseCode = "200", description = "Escalas randomizadas geradas com sucesso")
//    @GetMapping("/randomizadas/{idExternoMinisterio}/{quantidadeDeEscalas}")
//    public ResponseEntity<List<MembroSimplificadoDTO>> buscarEscalasRandomizadas(
//            @PathVariable UUID idExternoMinisterio,
//            @PathVariable int quantidadeDeEscalas,
//            @RequestBody EventoUnicoSimplificadoDTO eventoUnicoSimplificadoDTO
//    ){
//        return ResponseEntity
//                .status(HttpStatus.OK)
//                .body(escalaService.buscarMembrosMinisterioesAleatorios(idExternoMinisterio, quantidadeDeEscalas, eventoUnicoSimplificadoDTO));
//    }
//
//    @ApiErrorsComuns
//    @ApiResponse(responseCode = "200", description = "Escalas salvas com sucesso")
//    @PostMapping("/evento-ministerio/{idExternoEventoMinisterio}")
//    public ResponseEntity<List<Escala>> salvarEscalasPorEventoMinisterioId(
//            @PathVariable UUID idExternoEventoMinisterio,
//            @RequestBody EscalasSalvarDTO escalas
//    ){
//        return ResponseEntity
//                .status(HttpStatus.OK)
//                .body(escalaService.salvarEscalasPorEventoMinisterio(idExternoEventoMinisterio, escalas));
//    }
//
//    @ApiErrorsComuns
//    @ApiResponse(responseCode = "200", description = "Escalas salvas com sucesso")
//    @PutMapping("/evento-ministerio/{idExternoEventoMinisterio}")
//    public ResponseEntity<List<Escala>> editarEscalasPorEventoMinisterioId(
//            @PathVariable UUID idExternoEventoMinisterio,
//            @RequestBody EscalasSalvarDTO escalas
//    ){
//        return ResponseEntity
//                .status(HttpStatus.OK)
//                .body(escalaService.editarEscalasPorEventoMinisterio(idExternoEventoMinisterio, escalas));
//    }
//
//    @ApiErrorsComuns
//    @ApiResponse(responseCode = "200", description = "Escalas salvas com sucesso")
//    @DeleteMapping("/evento-ministerio/{idExternoEventoMinisterio}")
//    public ResponseEntity<Integer> excluirEscalasPorEventoMinisterioId(
//            @PathVariable UUID idExternoEventoMinisterio
//    ){
//        return ResponseEntity
//                .status(HttpStatus.OK)
//                .body(escalaService.excluirEscalasPorEventoMinisterio(idExternoEventoMinisterio));
//    }
//
//}
