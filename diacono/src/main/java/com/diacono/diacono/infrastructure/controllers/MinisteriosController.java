package com.diacono.diacono.infrastructure.controllers;

import com.diacono.diacono.applications.dtos.RestResponseMessageDTO;
import com.diacono.diacono.global.error.exceptions.FieldInvalidException;
import com.diacono.diacono.domain.enums.EnumStatusMembro;
import com.diacono.diacono.applications.dtos.membro.MembroMinisterioCreateDTO;
import com.diacono.diacono.applications.dtos.membro.MembroMinisterioInfoMembroDTO;
import com.diacono.diacono.applications.dtos.ministerio.MinisterioCreateDTO;
import com.diacono.diacono.applications.dtos.ministerio.MinisterioUpdateDTO;
import com.diacono.diacono.applications.dtos.ministerio.MinisterioSimplificadoDTO;
import com.diacono.diacono.applications.dtos.ministerio.MinisterioSuperSimplificadoDTO;
import com.diacono.diacono.domain.enums.EnumStatusMinisterio;
import com.diacono.diacono.usecases.ministerio.BuscarMembroMinisterioLiderMinisterioComFiltroUseCase;
import com.diacono.diacono.usecases.ministerio.*;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/ministerios")
public class MinisteriosController {

    private final BuscarMembroMinisterioLiderMinisterioComFiltroUseCase buscarMembroMinisterioLiderMinisterioComFiltroUseCase;
    private final BuscarMinisteriosGeraisUseCase buscarMinisteriosGeraisUseCase;
    private final AdicionarMinisterioUseCase adicionarMinisterioUseCase;
    private final EditarMinisterioUseCase editarMinisterioUseCase;
    private final RemoverMembroMinisterioLiderMinisterioUseCase removerMembroMinisterioLiderMinisterioUseCase;
    private final AdicionarMembroMinisterioLiderMinisterioUseCase adicionarMembroMinisterioLiderMinisterioUseCase;
    private final BuscarMinisteriosLiderMinisterioUseCase buscarMinisteriosLiderMinisterioUseCase;
    private final BuscarMinisteriosGovernoSemFiltroUseCase buscarMinisteriosGovernoSemFiltroUseCase;
    private final BuscarMinisteriosGovernoComFiltroUseCase buscarMinisteriosGovernoComFiltroUseCase;
    private final BuscarMembroMinisterioLiderMinisterioSemFiltroUseCase buscarMembroMinisterioLiderMinisterioSemFiltroUseCase;

    public MinisteriosController(BuscarMembroMinisterioLiderMinisterioComFiltroUseCase buscarMembroMinisterioLiderMinisterioComFiltroUseCase, BuscarMinisteriosGeraisUseCase buscarMinisteriosGeraisUseCase, AdicionarMinisterioUseCase adicionarMinisterioUseCase, EditarMinisterioUseCase editarMinisterioUseCase, RemoverMembroMinisterioLiderMinisterioUseCase removerMembroMinisterioLiderMinisterioUseCase, AdicionarMembroMinisterioLiderMinisterioUseCase adicionarMembroMinisterioLiderMinisterioUseCase, BuscarMinisteriosLiderMinisterioUseCase buscarMinisteriosLiderMinisterioUseCase, BuscarMinisteriosGovernoSemFiltroUseCase buscarMinisteriosGovernoSemFiltroUseCase, BuscarMinisteriosGovernoComFiltroUseCase buscarMinisteriosGovernoComFiltroUseCase, BuscarMembroMinisterioLiderMinisterioSemFiltroUseCase buscarMembroMinisterioLiderMinisterioSemFiltroUseCase) {
        this.buscarMembroMinisterioLiderMinisterioComFiltroUseCase = buscarMembroMinisterioLiderMinisterioComFiltroUseCase;
        this.buscarMinisteriosGeraisUseCase = buscarMinisteriosGeraisUseCase;
        this.adicionarMinisterioUseCase = adicionarMinisterioUseCase;
        this.editarMinisterioUseCase = editarMinisterioUseCase;
        this.removerMembroMinisterioLiderMinisterioUseCase = removerMembroMinisterioLiderMinisterioUseCase;
        this.adicionarMembroMinisterioLiderMinisterioUseCase = adicionarMembroMinisterioLiderMinisterioUseCase;
        this.buscarMinisteriosLiderMinisterioUseCase = buscarMinisteriosLiderMinisterioUseCase;
        this.buscarMinisteriosGovernoSemFiltroUseCase = buscarMinisteriosGovernoSemFiltroUseCase;
        this.buscarMinisteriosGovernoComFiltroUseCase = buscarMinisteriosGovernoComFiltroUseCase;
        this.buscarMembroMinisterioLiderMinisterioSemFiltroUseCase = buscarMembroMinisterioLiderMinisterioSemFiltroUseCase;
    }

    //USO GERAL

    @GetMapping
    public ResponseEntity<List<MinisterioSimplificadoDTO>> buscarMinisteriosGerais() {

        return ResponseEntity.status(HttpStatus.OK).body(buscarMinisteriosGeraisUseCase.execute());
    }

    //VISAO GOVERNO

    @GetMapping("/governo")
    public ResponseEntity<Page<MinisterioSimplificadoDTO>> buscarMinisteriosGoverno(Pageable pageable, @RequestParam(required = false, defaultValue = "") String buscaGeral,
                                                                                    @RequestParam(required = false) EnumStatusMinisterio status) {

        boolean semBusca = (buscaGeral == null || buscaGeral.isBlank());
        boolean semStatus = (status == null);

        Page pagina;

        if (semBusca && semStatus) {
            pagina = buscarMinisteriosGovernoSemFiltroUseCase.execute(pageable);
            return ResponseEntity.status(HttpStatus.OK).body(pagina);
        }

        pagina = buscarMinisteriosGovernoComFiltroUseCase.execute(pageable, buscaGeral, status);
        return ResponseEntity.status(HttpStatus.OK).body(
                pagina
        );

    }

    @PostMapping("/governo")
    public ResponseEntity<RestResponseMessageDTO> adicionarMinisterio(@RequestBody @Valid MinisterioCreateDTO ministerioCreateDTO) {
        return ResponseEntity.status(HttpStatus.CREATED).body(adicionarMinisterioUseCase.execute(ministerioCreateDTO));
    }

    @PatchMapping("/governo/{idMinisterio}") //OK
    public ResponseEntity<RestResponseMessageDTO> editarMinisterio(@PathVariable UUID idMinisterio, @RequestBody @Valid MinisterioUpdateDTO ministerioUpdateDTO) {
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(editarMinisterioUseCase.execute(ministerioUpdateDTO, idMinisterio));
    }

    //VISAO  LIDER MINISTERIO

    @GetMapping("/lider-ministerio/{idMinisterio}")
    public ResponseEntity<Page<MembroMinisterioInfoMembroDTO>> buscarMembroMinisterioLiderMinisterio(@PathVariable UUID idMinisterio, Pageable pageable, @RequestParam(required = false, defaultValue = "") String buscaGeral,
                                                                                                     @RequestParam(required = false) EnumStatusMembro status) {

        if (idMinisterio == null || idMinisterio.toString().isBlank()) {
            throw new FieldInvalidException("ID do ministério não pode ser nulo");
        }
        boolean semBusca = (buscaGeral == null || buscaGeral.isBlank());
        boolean semStatus = (status == null);

        Page<MembroMinisterioInfoMembroDTO> pagina;

        if (semBusca && semStatus) {
            pagina = buscarMembroMinisterioLiderMinisterioSemFiltroUseCase.execute(idMinisterio, pageable);
            return ResponseEntity.status(HttpStatus.OK).body(pagina);
        }

        pagina = buscarMembroMinisterioLiderMinisterioComFiltroUseCase.execute(idMinisterio, pageable, buscaGeral, status);

        return ResponseEntity.status(HttpStatus.OK).body(pagina);

    }

    @GetMapping("/lider-ministerio")
    public ResponseEntity<List<MinisterioSuperSimplificadoDTO>> buscarMinisteriosLiderMinisterio() {

        List<MinisterioSuperSimplificadoDTO> listaMinisterios = buscarMinisteriosLiderMinisterioUseCase.execute();

        return ResponseEntity.status(HttpStatus.OK).body(listaMinisterios);
    }

    @PatchMapping("/lider-ministerio/{idMinisterio}")
    public ResponseEntity<RestResponseMessageDTO> adicionarMembroMinisterioLiderMinisterio(@PathVariable UUID idMinisterio, @RequestBody @Valid MembroMinisterioCreateDTO membroMinisterio) {

        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(
                adicionarMembroMinisterioLiderMinisterioUseCase.execute(idMinisterio, membroMinisterio)
        );
    }

    @DeleteMapping("/lider-ministerio/{idMinisterio}/{idMembroMinisterio}")
    public ResponseEntity<RestResponseMessageDTO> removerMembroMinisterioLiderMinisterio(@PathVariable UUID idMinisterio, @PathVariable UUID idMembroMinisterio) {

        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(
                removerMembroMinisterioLiderMinisterioUseCase.execute(idMinisterio, idMembroMinisterio)
        );
    }
}
