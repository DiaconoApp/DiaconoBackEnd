package com.diacono.diacono.infrastructure.controllers;

import com.diacono.diacono.applications.dtos.RestResponseMessageDTO;
import com.diacono.diacono.domain.enums.EnumStatusMembro;
import com.diacono.diacono.applications.dtos.membro.MembroMinisterioCreateDTO;
import com.diacono.diacono.applications.dtos.membro.MembroMinisterioInfoMembroDTO;
import com.diacono.diacono.applications.dtos.ministerio.MinisterioCreateDTO;
import com.diacono.diacono.applications.dtos.ministerio.MinisterioUpdateDTO;
import com.diacono.diacono.applications.dtos.ministerio.MinisterioSimplificadoDTO;
import com.diacono.diacono.applications.dtos.ministerio.MinisterioSuperSimplificadoDTO;
import com.diacono.diacono.domain.enums.EnumStatusMinisterio;
import com.diacono.diacono.global.util.JwtUtils;
import com.diacono.diacono.usecases.ministerio.BuscarMembroMinisterioLiderMinisterioComFiltroUseCase;
import com.diacono.diacono.usecases.ministerio.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@Validated
@RequestMapping("/api/v1/ministerios")
public class MinisteriosController {

    private static final Logger logger = LoggerFactory.getLogger(MinisteriosController.class);

    private final BuscarMembroMinisterioLiderMinisterioComFiltroUseCase buscarMembroMinisterioLiderMinisterioComFiltroUseCase;
    private final BuscarMinisteriosGeraisUseCase buscarMinisteriosGeraisUseCase;
    private final AdicionarMinisterioUseCase adicionarMinisterioUseCase;
    private final EditarMinisterioUseCase editarMinisterioUseCase;
    private final RemoverMembroMinisterioLiderMinisterioUseCase removerMembroMinisterioLiderMinisterioUseCase;
    private final AdicionarMembroMinisterioLiderMinisterioUseCase adicionarMembroMinisterioLiderMinisterioUseCase;
    private final BuscarMinisteriosLiderMinisterioUseCase buscarMinisteriosLiderMinisterioUseCase;
    private final BuscarMinisteriosMembroUseCase buscarMinisteriosMembroUseCase;
    private final BuscarMinisteriosGovernoSemFiltroUseCase buscarMinisteriosGovernoSemFiltroUseCase;
    private final BuscarMinisteriosGovernoComFiltroUseCase buscarMinisteriosGovernoComFiltroUseCase;
    private final BuscarMembroMinisterioLiderMinisterioSemFiltroUseCase buscarMembroMinisterioLiderMinisterioSemFiltroUseCase;
    private final JwtUtils jwtUtils;


    public MinisteriosController(BuscarMembroMinisterioLiderMinisterioComFiltroUseCase buscarMembroMinisterioLiderMinisterioComFiltroUseCase, BuscarMinisteriosGeraisUseCase buscarMinisteriosGeraisUseCase, AdicionarMinisterioUseCase adicionarMinisterioUseCase, EditarMinisterioUseCase editarMinisterioUseCase, RemoverMembroMinisterioLiderMinisterioUseCase removerMembroMinisterioLiderMinisterioUseCase, AdicionarMembroMinisterioLiderMinisterioUseCase adicionarMembroMinisterioLiderMinisterioUseCase, BuscarMinisteriosLiderMinisterioUseCase buscarMinisteriosLiderMinisterioUseCase, BuscarMinisteriosMembroUseCase buscarMinisteriosMembroUseCase, BuscarMinisteriosGovernoSemFiltroUseCase buscarMinisteriosGovernoSemFiltroUseCase, BuscarMinisteriosGovernoComFiltroUseCase buscarMinisteriosGovernoComFiltroUseCase, BuscarMembroMinisterioLiderMinisterioSemFiltroUseCase buscarMembroMinisterioLiderMinisterioSemFiltroUseCase, JwtUtils jwtUtils) {
        this.buscarMembroMinisterioLiderMinisterioComFiltroUseCase = buscarMembroMinisterioLiderMinisterioComFiltroUseCase;
        this.buscarMinisteriosGeraisUseCase = buscarMinisteriosGeraisUseCase;
        this.adicionarMinisterioUseCase = adicionarMinisterioUseCase;
        this.editarMinisterioUseCase = editarMinisterioUseCase;
        this.removerMembroMinisterioLiderMinisterioUseCase = removerMembroMinisterioLiderMinisterioUseCase;
        this.adicionarMembroMinisterioLiderMinisterioUseCase = adicionarMembroMinisterioLiderMinisterioUseCase;
        this.buscarMinisteriosLiderMinisterioUseCase = buscarMinisteriosLiderMinisterioUseCase;
        this.buscarMinisteriosMembroUseCase = buscarMinisteriosMembroUseCase;
        this.buscarMinisteriosGovernoSemFiltroUseCase = buscarMinisteriosGovernoSemFiltroUseCase;
        this.buscarMinisteriosGovernoComFiltroUseCase = buscarMinisteriosGovernoComFiltroUseCase;
        this.buscarMembroMinisterioLiderMinisterioSemFiltroUseCase = buscarMembroMinisterioLiderMinisterioSemFiltroUseCase;
        this.jwtUtils = jwtUtils;
    }

    //USO GERAL

    @GetMapping
    @PreAuthorize("hasAnyAuthority('SCOPE_MEMBRO', 'SCOPE_LIDER_MINISTERIO', 'SCOPE_GOVERNO')")
    public ResponseEntity<List<MinisterioSimplificadoDTO>> buscarMinisteriosGerais() {
        UUID igrejaId = jwtUtils.getIgrejaId();

        return ResponseEntity.status(HttpStatus.OK).body(buscarMinisteriosGeraisUseCase.execute(igrejaId));
    }

    //VISAO GOVERNO

    @GetMapping("/governo")
    @PreAuthorize("hasAuthority('SCOPE_GOVERNO')")
    public ResponseEntity<Page<MinisterioSimplificadoDTO>> buscarMinisteriosGoverno(
            Pageable pageable,
            @RequestParam(required = false, defaultValue = "") @Size(max = 120) String buscaGeral,
            @RequestParam(required = false) EnumStatusMinisterio status
    ) {
        UUID igrejaId = jwtUtils.getIgrejaId();

        String buscaNormalizada = buscaGeral == null ? "" : buscaGeral.trim();

        boolean semBusca = buscaNormalizada.isBlank();
        boolean semStatus = (status == null);

        Page<MinisterioSimplificadoDTO> pagina;

        if (semBusca && semStatus) {
            pagina = buscarMinisteriosGovernoSemFiltroUseCase.execute(pageable, igrejaId);
            return ResponseEntity.status(HttpStatus.OK).body(pagina);
        }

        pagina = buscarMinisteriosGovernoComFiltroUseCase.execute(pageable, buscaNormalizada, status, igrejaId);
        return ResponseEntity.status(HttpStatus.OK).body(
                pagina
        );

    }

    @PostMapping("/governo")
    @PreAuthorize("hasAuthority('SCOPE_GOVERNO')")
    public ResponseEntity<RestResponseMessageDTO> adicionarMinisterio(@RequestBody @Valid MinisterioCreateDTO ministerioCreateDTO) {
        UUID igrejaId = jwtUtils.getIgrejaId();

        try {
            RestResponseMessageDTO response = adicionarMinisterioUseCase.execute(ministerioCreateDTO, igrejaId);
            logger.info("Operacao de criacao de ministerio executada.");
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (RuntimeException ex) {
            logger.warn("Falha ao criar ministerio.");
            throw ex;
        }
    }

    @PatchMapping("/governo/{idMinisterio}") //OK
    @PreAuthorize("hasAuthority('SCOPE_GOVERNO')")
    public ResponseEntity<RestResponseMessageDTO> editarMinisterio(@PathVariable @NotNull UUID idMinisterio, @RequestBody @Valid MinisterioUpdateDTO ministerioUpdateDTO) {
        UUID  igrejaIdToken = jwtUtils.getIgrejaId();

        try {
            RestResponseMessageDTO response = editarMinisterioUseCase.execute(ministerioUpdateDTO, idMinisterio, igrejaIdToken);
            logger.info("Operacao de edicao de ministerio executada. idMinisterio={}", idMinisterio);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(response);
        } catch (RuntimeException ex) {
            logger.warn("Falha ao editar ministerio. idMinisterio={}", idMinisterio);
            throw ex;
        }
    }

    //VISAO  LIDER MINISTERIO

    @GetMapping("/lider-ministerio/{idMinisterio}")
    @PreAuthorize("hasAnyAuthority('SCOPE_LIDER_MINISTERIO', 'SCOPE_GOVERNO')")
    public ResponseEntity<Page<MembroMinisterioInfoMembroDTO>> buscarMembroMinisterioLiderMinisterio(
            @PathVariable @NotNull UUID idMinisterio,
            Pageable pageable,
            @RequestParam(required = false, defaultValue = "") @Size(max = 120) String buscaGeral,
            @RequestParam(required = false) EnumStatusMembro status
    ) {
        String buscaNormalizada = buscaGeral == null ? "" : buscaGeral.trim();

        boolean semBusca = buscaNormalizada.isBlank();
        boolean semStatus = (status == null);

        Page<MembroMinisterioInfoMembroDTO> pagina;

        if (semBusca && semStatus) {
            pagina = buscarMembroMinisterioLiderMinisterioSemFiltroUseCase.execute(idMinisterio, pageable);
            return ResponseEntity.status(HttpStatus.OK).body(pagina);
        }

        pagina = buscarMembroMinisterioLiderMinisterioComFiltroUseCase.execute(idMinisterio, pageable, buscaNormalizada, status);

        return ResponseEntity.status(HttpStatus.OK).body(pagina);

    }

    @GetMapping("/lider-ministerio")
    @PreAuthorize("hasAnyAuthority('SCOPE_LIDER_MINISTERIO', 'SCOPE_GOVERNO')")
    public ResponseEntity<List<MinisterioSuperSimplificadoDTO>> buscarMinisteriosLiderMinisterio() {
        UUID membroId = jwtUtils.getSubject();
        UUID igrejaId = jwtUtils.getIgrejaId();

        List<MinisterioSuperSimplificadoDTO> listaMinisterios = buscarMinisteriosLiderMinisterioUseCase.execute(igrejaId, membroId);

        return ResponseEntity.status(HttpStatus.OK).body(listaMinisterios);
    }

    @GetMapping("/membro")
    @PreAuthorize("hasAnyAuthority('SCOPE_MEMBRO', 'SCOPE_LIDER_MINISTERIO', 'SCOPE_GOVERNO')")
    public ResponseEntity<List<MinisterioSuperSimplificadoDTO>> buscarMinisteriosMembro() {
        UUID membroId = jwtUtils.getSubject();
        UUID igrejaId = jwtUtils.getIgrejaId();

        List<MinisterioSuperSimplificadoDTO> listaMinisterios = buscarMinisteriosMembroUseCase.execute(igrejaId, membroId);

        return ResponseEntity.status(HttpStatus.OK).body(listaMinisterios);
    }

    @PatchMapping("/lider-ministerio/{idMinisterio}")
    @PreAuthorize("hasAnyAuthority('SCOPE_LIDER_MINISTERIO', 'SCOPE_GOVERNO')")
    public ResponseEntity<RestResponseMessageDTO> adicionarMembroMinisterioLiderMinisterio(
            @PathVariable @NotNull UUID idMinisterio,
            @RequestBody @Valid MembroMinisterioCreateDTO membroMinisterio
    ) {
        UUID igrejaIdToken = jwtUtils.getIgrejaId();
        UUID membroIdToken = jwtUtils.getSubject();

        try {
            RestResponseMessageDTO response = adicionarMembroMinisterioLiderMinisterioUseCase.execute(idMinisterio, membroMinisterio, igrejaIdToken, membroIdToken);
            logger.info("Operacao de adicao de membro em ministerio executada. idMinisterio={}", idMinisterio);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(response);
        } catch (RuntimeException ex) {
            logger.warn("Falha ao adicionar membro em ministerio. idMinisterio={}", idMinisterio);
            throw ex;
        }
    }

    @DeleteMapping("/lider-ministerio/{idMinisterio}/{idMembroMinisterio}")
    @PreAuthorize("hasAnyAuthority('SCOPE_LIDER_MINISTERIO', 'SCOPE_GOVERNO')")
    public ResponseEntity<RestResponseMessageDTO> removerMembroMinisterioLiderMinisterio(
            @PathVariable UUID idMinisterio,
            @PathVariable UUID idMembroMinisterio
    ) {
        UUID  igrejaIdToken = jwtUtils.getIgrejaId();

        try {
            RestResponseMessageDTO response = removerMembroMinisterioLiderMinisterioUseCase.execute(idMinisterio, idMembroMinisterio, igrejaIdToken);
            logger.info("Operacao de remocao de membro de ministerio executada. idMinisterio={} idMembroMinisterio={}", idMinisterio, idMembroMinisterio);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(response);
        } catch (RuntimeException ex) {
            logger.warn("Falha ao remover membro de ministerio. idMinisterio={} idMembroMinisterio={}", idMinisterio, idMembroMinisterio);
            throw ex;
        }
    }
}
