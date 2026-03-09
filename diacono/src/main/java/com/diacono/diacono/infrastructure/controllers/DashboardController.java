package com.diacono.diacono.infrastructure.controllers;


import com.diacono.diacono.applications.dtos.ministerio.KpisMinisteriosDTO;
import com.diacono.diacono.use_cases.DashboardService;
import com.diacono.diacono.applications.dtos.dashboard.DashboardFaixaEtariaMembroDTO;
import com.diacono.diacono.applications.dtos.dashboard.DashboardGeneroMembroDTO;
import com.diacono.diacono.applications.dtos.ministerio.KpisMembrosDTO;
import com.diacono.diacono.applications.dtos.ministerio.MinisterioEventoDashDTO;
import com.diacono.diacono.applications.dtos.membro.MembroDashEvolucaoDTO;
import com.diacono.diacono.applications.dtos.ministerio.MinisterioDashEvolucaoDTO;
import com.diacono.diacono.applications.dtos.ministerio.MinisterioDashQuantidadeMembrosDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/dashboards")
public class DashboardController {

    private final DashboardService dashboardService;

    public DashboardController(DashboardService dashboardService) {
        this.dashboardService = dashboardService;
    }

    //Membros

    @GetMapping("/membros/kpis")
    //@PreAuthorize("hasAnyAuthority('SCOPE_MEMBRO','SCOPE_LIDER_MINISTERIO', 'SCOPE_GOVERNO')")
    public ResponseEntity<KpisMembrosDTO> membroBuscarKpis(@RequestParam int anoInicio, @RequestParam int anoFim) {
        return ResponseEntity.status(HttpStatus.OK).body(dashboardService.buscarKpisMembros(anoInicio, anoFim));
    }

    @GetMapping("/membros/evolucao")
    //@PreAuthorize("hasAnyAuthority('SCOPE_MEMBRO','SCOPE_LIDER_MINISTERIO', 'SCOPE_GOVERNO')")
    public ResponseEntity<List<MembroDashEvolucaoDTO>> membroBuscarDashEvolucao(@RequestParam int anoInicio, @RequestParam int anoFim) {

        return ResponseEntity.status(HttpStatus.OK).body(dashboardService.buscarDashEvolucao(anoInicio, anoFim));

    }

    @GetMapping("/membros/faixa-etaria")
    //@PreAuthorize("hasAnyAuthority('SCOPE_MEMBRO','SCOPE_LIDER_MINISTERIO', 'SCOPE_GOVERNO')")
    public ResponseEntity<DashboardFaixaEtariaMembroDTO> membroBuscarDashFaixaEtaria(@RequestParam int anoInicio, @RequestParam int anoFim) {

        return ResponseEntity.status(HttpStatus.OK).body(dashboardService.buscarDashFaixaEtaria(anoInicio, anoFim));

    }

    @GetMapping("/membros/genero")
    //@PreAuthorize("hasAnyAuthority('SCOPE_MEMBRO','SCOPE_LIDER_MINISTERIO', 'SCOPE_GOVERNO')")
    public ResponseEntity<DashboardGeneroMembroDTO> membroBuscarDashGenero(@RequestParam int anoInicio, @RequestParam int anoFim) {

        return ResponseEntity.status(HttpStatus.OK).body(dashboardService.buscarDashGenero(anoInicio, anoFim));

    }

    //Ministerios

    @GetMapping("/ministerios/kpis")
    //@PreAuthorize("hasAnyAuthority('SCOPE_MEMBRO','SCOPE_LIDER_MINISTERIO', 'SCOPE_GOVERNO')")
    public ResponseEntity<KpisMinisteriosDTO> ministerioBuscarKpis(@RequestParam int anoInicio, @RequestParam int anoFim) {

        return ResponseEntity.status(HttpStatus.OK).body(dashboardService.ministerioBuscarKpis(anoInicio, anoFim));

    }

    @GetMapping("/ministerios/evolucao/{idMinisterio}")
    //@PreAuthorize("hasAnyAuthority('SCOPE_MEMBRO','SCOPE_LIDER_MINISTERIO', 'SCOPE_GOVERNO')")
    public ResponseEntity<List<MinisterioDashEvolucaoDTO>> ministerioBuscarDashEvolucao(@RequestParam int anoInicio, @RequestParam int anoFim, @PathVariable UUID idMinisterio) {

        return ResponseEntity.status(HttpStatus.OK).body(dashboardService.ministerioBuscarDashEvolucao(anoInicio, anoFim, idMinisterio));

    }

    @GetMapping("/ministerios/quantidade-membros")
    //@PreAuthorize("hasAnyAuthority('SCOPE_MEMBRO','SCOPE_LIDER_MINISTERIO', 'SCOPE_GOVERNO')")
    public ResponseEntity<List<MinisterioDashQuantidadeMembrosDTO>> ministerioBuscarDashQuantidadeMembro(@RequestParam int anoInicio, @RequestParam int anoFim) {

        return ResponseEntity.status(HttpStatus.OK).body(dashboardService.ministerioBuscarDashQuantidadeMembro(anoInicio, anoFim));

    }

    @GetMapping("/ministerios/quantidade-eventos")
    //@PreAuthorize("hasAnyAuthority('SCOPE_MEMBRO','SCOPE_LIDER_MINISTERIO', 'SCOPE_GOVERNO')")
    public ResponseEntity<List<MinisterioEventoDashDTO>> ministerioBuscarDashQuantidadeEventos(@RequestParam int anoInicio, @RequestParam int anoFim) {

        return ResponseEntity.status(HttpStatus.OK).body(dashboardService.ministerioBuscarDashQuantidadeEventos(anoInicio, anoFim));

    }


}
