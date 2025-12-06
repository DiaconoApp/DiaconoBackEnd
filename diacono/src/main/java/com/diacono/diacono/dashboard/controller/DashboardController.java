package com.diacono.diacono.dashboard.controller;



import com.diacono.diacono.dashboard.model.response.ministerio.KpisMinisteriosDTO;
import com.diacono.diacono.dashboard.service.DashboardService;
import com.diacono.diacono.dashboard.model.response.membro.DashboardFaixaEtariaMembroDTO;
import com.diacono.diacono.dashboard.model.response.membro.DashboardGeneroMembroDTO;
import com.diacono.diacono.dashboard.model.response.membro.KpisMembrosDTO;
import com.diacono.diacono.membro.model.dto.response.MembroDashEvolucaoDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class DashboardController {

    private final DashboardService dashboardService;

    public DashboardController(DashboardService dashboardService) {
        this.dashboardService = dashboardService;
    }

    //Membros

    @GetMapping("/membros/kpis")
    //@PreAuthorize("hasAnyAuthority('SCOPE_MEMBRO','SCOPE_LIDER_MINISTERIO', 'SCOPE_GOVERNO')")
    public ResponseEntity<KpisMembrosDTO> buscarKpis(@RequestParam int anoInicio, @RequestParam int anoFim) {
        return ResponseEntity.status(HttpStatus.OK).body(dashboardService.buscarKpisMembros(anoInicio, anoFim));
    }

    @GetMapping("/membros/evolucao")
    //@PreAuthorize("hasAnyAuthority('SCOPE_MEMBRO','SCOPE_LIDER_MINISTERIO', 'SCOPE_GOVERNO')")
    public ResponseEntity<List<MembroDashEvolucaoDTO>> buscarDashEvolucao(@RequestParam int anoInicio, @RequestParam int anoFim) {

        return ResponseEntity.status(HttpStatus.OK).body(dashboardService.buscarDashEvolucao(anoInicio, anoFim));

    }

    @GetMapping("/membros/faixa-etaria")
    //@PreAuthorize("hasAnyAuthority('SCOPE_MEMBRO','SCOPE_LIDER_MINISTERIO', 'SCOPE_GOVERNO')")
    public ResponseEntity<DashboardFaixaEtariaMembroDTO> buscarDashFaixaEtaria(@RequestParam int anoInicio, @RequestParam int anoFim) {

        return ResponseEntity.status(HttpStatus.OK).body(dashboardService.buscarDashFaixaEtaria(anoInicio, anoFim));

    }

    @GetMapping("/membros/genero")
    //@PreAuthorize("hasAnyAuthority('SCOPE_MEMBRO','SCOPE_LIDER_MINISTERIO', 'SCOPE_GOVERNO')")
    public ResponseEntity<DashboardGeneroMembroDTO> buscarDashGenero(@RequestParam int anoInicio, @RequestParam int anoFim) {

        return ResponseEntity.status(HttpStatus.OK).body(dashboardService.buscarDashGenero(anoInicio, anoFim));

    }

    //Ministerios

    @GetMapping("/ministerios/kpis")
    //@PreAuthorize("hasAnyAuthority('SCOPE_MEMBRO','SCOPE_LIDER_MINISTERIO', 'SCOPE_GOVERNO')")
    public ResponseEntity<KpisMinisteriosDTO> ministerioBuscarKpis(@RequestParam int anoInicio, @RequestParam int anoFim) {

        return ResponseEntity.status(HttpStatus.OK).body(dashboardService.ministerioBuscarKpis(anoInicio, anoFim));

    }


}
