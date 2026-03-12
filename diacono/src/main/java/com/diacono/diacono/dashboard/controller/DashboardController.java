package com.diacono.diacono.dashboard.controller;


import com.diacono.diacono.dashboard.model.response.ministerio.KpisMinisteriosDTO;
import com.diacono.diacono.dashboard.service.DashboardService;
import com.diacono.diacono.dashboard.model.response.membro.DashboardFaixaEtariaMembroDTO;
import com.diacono.diacono.dashboard.model.response.membro.DashboardGeneroMembroDTO;
import com.diacono.diacono.dashboard.model.response.membro.KpisMembrosDTO;
import com.diacono.diacono.evento.model.dto.response.MinisterioEventoDashDTO;
import com.diacono.diacono.membro.model.dto.response.MembroDashEvolucaoDTO;
import com.diacono.diacono.membroministerio.model.dto.response.MinisterioDashEvolucaoDTO;
import com.diacono.diacono.membroministerio.model.dto.response.MinisterioDashQuantidadeMembrosDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.diacono.diacono.global.error.exceptions.FieldInvalidException;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/dashboards")
public class DashboardController {

    // OWASP A05/A07: limites defensivos para reduzir requests malformadas e abusivas.
    private static final int MIN_ANO = 1900;
    private static final int MAX_ANO = 2100;
    private static final int MAX_JANELA_ANOS = 10;

    private final DashboardService dashboardService;

    public DashboardController(DashboardService dashboardService) {
        this.dashboardService = dashboardService;
    }

    //Membros

    @GetMapping(value = "/membros/kpis", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasAnyAuthority('SCOPE_MEMBRO','SCOPE_LIDER_MINISTERIO', 'SCOPE_GOVERNO')")
    public ResponseEntity<KpisMembrosDTO> membroBuscarKpis(@RequestParam int anoInicio, @RequestParam int anoFim) {
        validarIntervaloAnos(anoInicio, anoFim);
        return ResponseEntity.status(HttpStatus.OK).body(dashboardService.buscarKpisMembros(anoInicio, anoFim));
    }

    @GetMapping(value = "/membros/evolucao", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasAnyAuthority('SCOPE_MEMBRO','SCOPE_LIDER_MINISTERIO', 'SCOPE_GOVERNO')")
    public ResponseEntity<List<MembroDashEvolucaoDTO>> membroBuscarDashEvolucao(@RequestParam int anoInicio, @RequestParam int anoFim) {
        validarIntervaloAnos(anoInicio, anoFim);
        return ResponseEntity.status(HttpStatus.OK).body(dashboardService.buscarDashEvolucao(anoInicio, anoFim));
    }

    @GetMapping(value = "/membros/faixa-etaria", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasAnyAuthority('SCOPE_MEMBRO','SCOPE_LIDER_MINISTERIO', 'SCOPE_GOVERNO')")
    public ResponseEntity<DashboardFaixaEtariaMembroDTO> membroBuscarDashFaixaEtaria(@RequestParam int anoInicio, @RequestParam int anoFim) {
        validarIntervaloAnos(anoInicio, anoFim);
        return ResponseEntity.status(HttpStatus.OK).body(dashboardService.buscarDashFaixaEtaria(anoInicio, anoFim));
    }

    @GetMapping(value = "/membros/genero", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasAnyAuthority('SCOPE_MEMBRO','SCOPE_LIDER_MINISTERIO', 'SCOPE_GOVERNO')")
    public ResponseEntity<DashboardGeneroMembroDTO> membroBuscarDashGenero(@RequestParam int anoInicio, @RequestParam int anoFim) {
        validarIntervaloAnos(anoInicio, anoFim);
        return ResponseEntity.status(HttpStatus.OK).body(dashboardService.buscarDashGenero(anoInicio, anoFim));
    }

    //Ministerios

    @GetMapping(value = "/ministerios/kpis", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasAnyAuthority('SCOPE_MEMBRO','SCOPE_LIDER_MINISTERIO', 'SCOPE_GOVERNO')")
    public ResponseEntity<KpisMinisteriosDTO> ministerioBuscarKpis(@RequestParam int anoInicio, @RequestParam int anoFim) {
        validarIntervaloAnos(anoInicio, anoFim);
        return ResponseEntity.status(HttpStatus.OK).body(dashboardService.ministerioBuscarKpis(anoInicio, anoFim));
    }

    @GetMapping(value = "/ministerios/evolucao/{idMinisterio}", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasAnyAuthority('SCOPE_MEMBRO','SCOPE_LIDER_MINISTERIO', 'SCOPE_GOVERNO')")
    public ResponseEntity<List<MinisterioDashEvolucaoDTO>> ministerioBuscarDashEvolucao(@RequestParam int anoInicio, @RequestParam int anoFim, @PathVariable UUID idMinisterio) {
        validarIntervaloAnos(anoInicio, anoFim);
        validarIdMinisterio(idMinisterio);
        return ResponseEntity.status(HttpStatus.OK).body(dashboardService.ministerioBuscarDashEvolucao(anoInicio, anoFim, idMinisterio));
    }

    @GetMapping(value = "/ministerios/quantidade-membros", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasAnyAuthority('SCOPE_MEMBRO','SCOPE_LIDER_MINISTERIO', 'SCOPE_GOVERNO')")
    public ResponseEntity<List<MinisterioDashQuantidadeMembrosDTO>> ministerioBuscarDashQuantidadeMembro(@RequestParam int anoInicio, @RequestParam int anoFim) {
        validarIntervaloAnos(anoInicio, anoFim);
        return ResponseEntity.status(HttpStatus.OK).body(dashboardService.ministerioBuscarDashQuantidadeMembro(anoInicio, anoFim));
    }

    @GetMapping(value = "/ministerios/quantidade-eventos", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasAnyAuthority('SCOPE_MEMBRO','SCOPE_LIDER_MINISTERIO', 'SCOPE_GOVERNO')")
    public ResponseEntity<List<MinisterioEventoDashDTO>> ministerioBuscarDashQuantidadeEventos(@RequestParam int anoInicio, @RequestParam int anoFim) {
        validarIntervaloAnos(anoInicio, anoFim);
        return ResponseEntity.status(HttpStatus.OK).body(dashboardService.ministerioBuscarDashQuantidadeEventos(anoInicio, anoFim));
    }

    // OWASP A05/A07: valida intervalos para evitar entrada invalida e queries excessivas.
    private void validarIntervaloAnos(int anoInicio, int anoFim) {
        if (anoInicio < MIN_ANO || anoInicio > MAX_ANO || anoFim < MIN_ANO || anoFim > MAX_ANO) {
            throw new FieldInvalidException("Os anos precisam estar entre 1900 e 2100");
        }
        if (anoInicio > anoFim) {
            throw new FieldInvalidException("anoInicio nao pode ser maior que anoFim");
        }
        if ((anoFim - anoInicio) > MAX_JANELA_ANOS) {
            throw new FieldInvalidException("O intervalo de anos nao pode ser maior que 10");
        }
    }

    // OWASP A01: valida identificador sensivel recebido por path.
    private void validarIdMinisterio(UUID idMinisterio) {
        if (idMinisterio == null) {
            throw new FieldInvalidException("O id do ministerio precisa ser informado");
        }
    }

}
