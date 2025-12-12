package com.diacono.diacono.membro.controller;

import com.diacono.diacono.evento.model.dto.response.EventoUnicoSimplificadoDTO;
import com.diacono.diacono.global.dto.response.RestResponseMessage;
import com.diacono.diacono.global.error.comuns.ApiErrorsComuns;
import com.diacono.diacono.membro.model.dto.request.MembroCreateDTO;
import com.diacono.diacono.membro.model.dto.response.MembroResponseDTO;
import com.diacono.diacono.membro.model.dto.response.MembroSimplificadoDTO;
import com.diacono.diacono.membro.model.entity.EnumStatusMembro;
import com.diacono.diacono.membro.service.MembroService;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/membros")
public class MembroController {

    private final MembroService membrosService;

    public MembroController(MembroService membrosService) {
        this.membrosService = membrosService;
    }


    @ApiErrorsComuns
    @ApiResponse(responseCode = "201", description = "Membro criado com sucesso")
    @PostMapping
    public ResponseEntity<RestResponseMessage> criarMembro(@RequestBody @Valid MembroCreateDTO membroDTO){

        RestResponseMessage response = membrosService.criarMembro(membroDTO);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @ApiErrorsComuns
    @ApiResponse(responseCode = "200", description = "Membros encontrados com sucesso")
    @GetMapping
    public ResponseEntity<Page<MembroResponseDTO>> buscarTodos(Pageable pageable, @RequestParam(required = false, defaultValue = "") String buscaGeral,
                                                                        @RequestParam(required = false) EnumStatusMembro status,
                                                                        @RequestParam(required = false) UUID fkMinisterio) {

        if((buscaGeral == null || buscaGeral.isBlank()) && fkMinisterio == null && status == null){
            Page<MembroResponseDTO> response = membrosService.buscarTodosSemFiltro(pageable);
            return ResponseEntity.status(HttpStatus.OK).body(response);
        }


        Page<MembroResponseDTO> response = membrosService.buscarTodosComFiltro(pageable, buscaGeral, status, fkMinisterio);

        return ResponseEntity.status(HttpStatus.OK).body(response);

    }

//    @ApiErrorsComuns
//    @ApiResponse(responseCode = "200", description = "Membros encontrados com sucesso")
//    @GetMapping("/{idExternoMinisterio}")
//    public ResponseEntity<List<MembroSimplificadoDTO>> buscarMembrosPorMinisterioSemEscala(
//            @RequestParam UUID idExternoMinisterio,
//            @RequestBody EventoUnicoSimplificadoDTO eventoUnicoSimplificadoDTO
//            ) {
//        List<MembroSimplificadoDTO> membrosDisponiveisParaEscala = membrosService.buscarMembrosDisponiveisParaEscala(idExternoMinisterio, eventoUnicoSimplificadoDTO);
//        return ResponseEntity.status(HttpStatus.OK).body(membrosDisponiveisParaEscala);
//    }


}
