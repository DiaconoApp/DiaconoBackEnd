package com.diacono.diacono.membro.controller;

import com.diacono.diacono.global.dto.response.RestResponseMessage;
import com.diacono.diacono.global.error.comuns.ApiErrorsComuns;
import com.diacono.diacono.membro.model.dto.request.MembroCreateDTO;
import com.diacono.diacono.membro.model.dto.response.MembroResponseDTO;
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
    public ResponseEntity<Page<MembroResponseDTO>> buscarTodosSemFiltro(Pageable pageable) {

        Page<MembroResponseDTO> response = membrosService.buscarTodosSemFiltro(pageable);

        return ResponseEntity.status(HttpStatus.OK).body(response);

    }

    @ApiErrorsComuns
    @ApiResponse(responseCode = "200", description = "Membros encontrados com sucesso")
    @GetMapping
    public ResponseEntity<List<MembroResponseDTO>> buscarTodosComFiltro(Pageable pageable, @RequestParam(required = false) String buscaGeral,
                                                                        @RequestParam(required = false, defaultValue = "ATIVO") EnumStatusMembro status,
                                                                        @RequestParam(required = false) UUID fkMinisterio) {

        List<MembroResponseDTO> response = membrosService.buscarTodosComFiltro(pageable, buscaGeral, status, fkMinisterio);

        return ResponseEntity.status(HttpStatus.OK).body(response);

    }





//    @ApiErrorsComuns
//    @ApiResponse(responseCode = "200", description = "Membros ativos contabilizados com sucesso")
//    @GetMapping("/ativos/count")
//    public ResponseEntity<Long> getMembrosAtivos() {
//        Long userAtivos = membrosService.getMembroAtivos();
//
//        return ResponseEntity.status(HttpStatus.OK).body(userAtivos);
//    }
//
//    @ApiErrorsComuns
//    @ApiResponse(responseCode = "200", description = "Membros com ministérios contabilizados com sucesso")
//    @GetMapping("/com-ministerio/count")
//    public ResponseEntity<Long> getMembrosComMinisterio() {
//        Long userComMinisterio = membrosService.getMembrosComMinisterio();
//
//        return ResponseEntity.status(HttpStatus.OK).body(userComMinisterio);
//    }
//
//    @ApiErrorsComuns
//    @ApiResponse(responseCode = "200", description = "Membros discipulados contabilizados com sucesso")
//    @GetMapping("/discipulados/count")
//    public ResponseEntity<Long> getTotalMembrosDiscipulados() {
//        Long countMembrosDiscipulados = membrosService.getCountMembrosDiscipulados();
//
//        return ResponseEntity.status(HttpStatus.OK).body(countMembrosDiscipulados);
//    }
}
