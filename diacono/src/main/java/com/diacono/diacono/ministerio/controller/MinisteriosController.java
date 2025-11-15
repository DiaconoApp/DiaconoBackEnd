package com.diacono.diacono.ministerio.controller;

import com.diacono.diacono.global.dto.response.RestResponseMessage;
import com.diacono.diacono.global.error.exceptions.FieldInvalidException;
import com.diacono.diacono.membro.model.entity.EnumStatusMembro;
import com.diacono.diacono.membroministerio.model.dto.request.MembroMinisterioCreateDTO;
import com.diacono.diacono.membroministerio.model.dto.response.MembroMinisterioDTO;
import com.diacono.diacono.ministerio.model.dto.MinisterioCreateDTO;
import com.diacono.diacono.ministerio.model.dto.MinisterioUpdateDTO;
import com.diacono.diacono.ministerio.model.dto.response.MinisterioSimplificadoDTO;
import com.diacono.diacono.ministerio.model.entity.EnumStatusMinisterio;
import com.diacono.diacono.ministerio.service.MinisterioService;
import jakarta.validation.Valid;
import org.apache.coyote.Response;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/ministerios")
public class MinisteriosController {

    private final MinisterioService ministerio;

    public MinisteriosController(MinisterioService ministerio) {
        this.ministerio = ministerio;
    }

    //VISAO GOVERNO

    @GetMapping("/governo")
    public ResponseEntity<List<MinisterioSimplificadoDTO>> buscarMinisteriosGoverno(Pageable pageable, @RequestParam(required = false, defaultValue = "") String buscaGeral,
                                                                                    @RequestParam(required = false) EnumStatusMinisterio status){

        boolean semBusca = (buscaGeral == null || buscaGeral.isBlank());
        boolean semStatus = (status == null);

        if (semBusca && semStatus) {
            return ResponseEntity.status(HttpStatus.OK).body(ministerio.buscarMinisteriosGoverno(pageable));
        }

        return ResponseEntity.status(HttpStatus.OK).body(
                ministerio.buscarMinisteriosGovernoComFiltro(pageable, buscaGeral, status)
        );

    }

    @PostMapping("/governo")
    public ResponseEntity<RestResponseMessage> adicionarMinisterio(@RequestBody @Valid MinisterioCreateDTO ministerioCreateDTO){
        return ResponseEntity.status(HttpStatus.CREATED).body(
                ministerio.criarMinisterio(ministerioCreateDTO)
        );
    }

    @PatchMapping("/governo/{idMinisterio}")
    public ResponseEntity<RestResponseMessage> editarMinisterio (@PathVariable UUID idMinisterio, @RequestBody @Valid MinisterioUpdateDTO ministerioUpdateDTO){
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(ministerio.editarMinisterio(ministerioUpdateDTO, idMinisterio));
    }

    //VISAO  LIDER MINISTERIO

    @GetMapping("/lider-ministerio/{idMinisterio}")
    public ResponseEntity<List<MembroMinisterioDTO>> buscarMembroMinisterioLiderMinisterio(@PathVariable UUID idMinisterio, Pageable pageable, @RequestParam(required = false, defaultValue = "") String buscaGeral,
                                                                                            @RequestParam(required = false) EnumStatusMembro status){

        if(idMinisterio == null || idMinisterio.toString().isBlank()){
            throw new FieldInvalidException("ID do ministério não pode ser nulo");
        }
        boolean semBusca = (buscaGeral == null || buscaGeral.isBlank());
        boolean semStatus = (status == null);

        if (semBusca && semStatus) {
            return ResponseEntity.status(HttpStatus.OK).body(ministerio.buscarMembroMinisterioLiderMinisterio(idMinisterio, pageable));
        }

        return ResponseEntity.status(HttpStatus.OK).body(
                ministerio.buscarMembroMinisterioLiderMinisterioComFiltro(idMinisterio, pageable, buscaGeral, status)
        );

    }

    @PatchMapping("/lider-ministerio/{idMinisterio}")
    public ResponseEntity<RestResponseMessage> adicionarMembroMinisterioLiderMinisterio (@PathVariable UUID idMinisterio, @RequestBody @Valid MembroMinisterioCreateDTO membroMinisterio){
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(
                ministerio.adicionarMembroMinisterioLiderMinisterio(idMinisterio, membroMinisterio)
        );
    }

    @DeleteMapping("/lider-ministerio/{idMinisterio}/{idMembroMinisterio}")
    public ResponseEntity<RestResponseMessage> removerMembroMinisterioLiderMinisterio (@PathVariable UUID idMinisterio, @PathVariable UUID idMembroMinisterio){
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(
                ministerio.removerMembroMinisterioLiderMinisterio(idMinisterio, idMembroMinisterio)
        );
    }


}
