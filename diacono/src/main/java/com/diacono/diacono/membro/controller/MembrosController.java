package com.diacono.diacono.membro.controller;

import com.diacono.diacono.global.dto.response.RestResponseMessage;
import com.diacono.diacono.global.error.comuns.ApiErrorsComuns;
import com.diacono.diacono.membro.model.dto.MembrosCreateDTO;
import com.diacono.diacono.membro.model.dto.MembrosResponseDTO;
import com.diacono.diacono.membro.model.dto.MembrosUpdateDTO;
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
public class MembrosController {

    private final MembroService membrosService;

    public MembrosController(MembroService membrosService) {
        this.membrosService = membrosService;
    }

    @ApiErrorsComuns
    @ApiResponse(responseCode = "201", description = "Membro criado com sucesso")
    @PostMapping
    public ResponseEntity<MembrosResponseDTO> createMembro(@RequestBody @Valid MembrosCreateDTO membrosDTO){

        MembrosResponseDTO response = membrosService.criar(membrosDTO);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @ApiErrorsComuns
    @ApiResponse(responseCode = "200", description = "Membro atualizado com sucesso")
    @PatchMapping("/{idExterno}")
    public ResponseEntity<RestResponseMessage> updateMembro(@PathVariable UUID idExterno, @RequestBody @Valid MembrosUpdateDTO updateDTO) {
        return ResponseEntity.status(HttpStatus.OK).body(membrosService.atualizar(idExterno, updateDTO));
    }

    @ApiErrorsComuns
    @ApiResponse(responseCode = "200", description = "Membros encontrados com sucesso")
    @GetMapping
    public ResponseEntity<Page<MembrosResponseDTO>> getAllMembros(Pageable pageable) {

        Page<MembrosResponseDTO> response = membrosService.getAll(pageable);

        return response.hasContent()
                ? ResponseEntity.status(HttpStatus.OK).body(response)
                : ResponseEntity.noContent().build();
    }

    @ApiErrorsComuns
    @ApiResponse(responseCode = "200", description = "Membros ativos contabilizados com sucesso")
    @GetMapping("/ativos/count")
    public ResponseEntity<Long> getMembrosAtivos() {
        Long userAtivos = membrosService.getMembroAtivos();

        return ResponseEntity.status(HttpStatus.OK).body(userAtivos);
    }

    @ApiErrorsComuns
    @ApiResponse(responseCode = "200", description = "Membros com ministérios contabilizados com sucesso")
    @GetMapping("/com-ministerio/count")
    public ResponseEntity<Long> getMembrosComMinisterio() {
        Long userComMinisterio = membrosService.getMembrosComMinisterio();

        return ResponseEntity.status(HttpStatus.OK).body(userComMinisterio);
    }

    @ApiErrorsComuns
    @ApiResponse(responseCode = "200", description = "Membros discipulados contabilizados com sucesso")
    @GetMapping("/discipulados/count")
    public ResponseEntity<Long> getTotalMembrosDiscipulados() {
        Long countMembrosDiscipulados = membrosService.getCountMembrosDiscipulados();

        return ResponseEntity.status(HttpStatus.OK).body(countMembrosDiscipulados);
    }
}
