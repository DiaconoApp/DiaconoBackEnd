package com.diacono.diacono.infrastructure.controllers;


import com.diacono.diacono.applications.dtos.ministerio.KpisMinisteriosDTO;
import com.diacono.diacono.global.error.comuns.ApiErrorsComuns;
import com.diacono.diacono.usecases.dashboard.membros.BuscarKpiEvolucaoMembrosDashUseCase;
import com.diacono.diacono.usecases.dashboard.membros.BuscarKpiFaixaEtariaMembrosDashUseCase;
import com.diacono.diacono.usecases.dashboard.membros.BuscarKpiGeneroMembrosDashUseCase;
import com.diacono.diacono.usecases.dashboard.membros.BuscarKpiMembrosDashUseCase;
import com.diacono.diacono.usecases.dashboard.ministerios.buscarKpiQuantidadeEventosPorMinisterioDashUseCase;
import com.diacono.diacono.applications.dtos.dashboard.DashboardFaixaEtariaMembroDTO;
import com.diacono.diacono.applications.dtos.dashboard.DashboardGeneroMembroDTO;
import com.diacono.diacono.applications.dtos.ministerio.KpisMembrosDTO;
import com.diacono.diacono.applications.dtos.ministerio.MinisterioEventoDashDTO;
import com.diacono.diacono.applications.dtos.membro.MembroDashEvolucaoDTO;
import com.diacono.diacono.applications.dtos.ministerio.MinisterioDashEvolucaoDTO;
import com.diacono.diacono.applications.dtos.ministerio.MinisterioDashQuantidadeMembrosDTO;
import com.diacono.diacono.usecases.dashboard.ministerios.BuscarKpiEvolucaoMinisteriosDashUseCase;
import com.diacono.diacono.usecases.dashboard.ministerios.BuscarKpiMinisteriosDashUseCase;
import com.diacono.diacono.usecases.dashboard.ministerios.BuscarKpiQuantidadeMembrosPorMinisterioDashUseCase;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/dashboards")
public class DashboardController {

    private final buscarKpiQuantidadeEventosPorMinisterioDashUseCase buscarKpiQuantidadeEventosPorMinisterioDashUseCase;
    private final BuscarKpiMembrosDashUseCase buscarKpiMembrosDashUseCase;
    private final BuscarKpiEvolucaoMembrosDashUseCase buscarKpiEvolucaoMembrosDashUseCase;
    private final BuscarKpiFaixaEtariaMembrosDashUseCase buscarKpiFaixaEtariaMembrosDashUseCase;
    private final BuscarKpiGeneroMembrosDashUseCase buscarKpiGeneroMembrosDashUseCase;
    private final BuscarKpiMinisteriosDashUseCase buscarKpiMinisteriosDashUseCase;
    private final BuscarKpiEvolucaoMinisteriosDashUseCase buscarKpiEvolucaoMinisteriosDashUseCase;
    private final BuscarKpiQuantidadeMembrosPorMinisterioDashUseCase buscarKpiQuantidadeMembrosPorMinisterioDashUseCase;

    public DashboardController(buscarKpiQuantidadeEventosPorMinisterioDashUseCase buscarKpiQuantidadeEventosPorMinisterioDashUseCase, BuscarKpiMembrosDashUseCase buscarKpiMembrosDashUseCase, BuscarKpiEvolucaoMembrosDashUseCase buscarKpiEvolucaoMembrosDashUseCase, BuscarKpiFaixaEtariaMembrosDashUseCase buscarKpiFaixaEtariaMembrosDashUseCase, BuscarKpiGeneroMembrosDashUseCase buscarKpiGeneroMembrosDashUseCase, BuscarKpiMinisteriosDashUseCase buscarKpiMinisteriosDashUseCase, BuscarKpiEvolucaoMinisteriosDashUseCase buscarKpiEvolucaoMinisteriosDashUseCase, BuscarKpiQuantidadeMembrosPorMinisterioDashUseCase buscarKpiQuantidadeMembrosPorMinisterioDashUseCase) {
        this.buscarKpiQuantidadeEventosPorMinisterioDashUseCase = buscarKpiQuantidadeEventosPorMinisterioDashUseCase;
        this.buscarKpiMembrosDashUseCase = buscarKpiMembrosDashUseCase;
        this.buscarKpiEvolucaoMembrosDashUseCase = buscarKpiEvolucaoMembrosDashUseCase;
        this.buscarKpiFaixaEtariaMembrosDashUseCase = buscarKpiFaixaEtariaMembrosDashUseCase;
        this.buscarKpiGeneroMembrosDashUseCase = buscarKpiGeneroMembrosDashUseCase;
        this.buscarKpiMinisteriosDashUseCase = buscarKpiMinisteriosDashUseCase;
        this.buscarKpiEvolucaoMinisteriosDashUseCase = buscarKpiEvolucaoMinisteriosDashUseCase;
        this.buscarKpiQuantidadeMembrosPorMinisterioDashUseCase = buscarKpiQuantidadeMembrosPorMinisterioDashUseCase;
    }

    //Membros

    @ApiErrorsComuns
    @GetMapping("/membros/kpis")
    @PreAuthorize("hasAnyAuthority('SCOPE_LIDER_MINISTERIO', 'SCOPE_GOVERNO')")
    public ResponseEntity<KpisMembrosDTO> buscarKpisMembros(@RequestParam int anoInicio, @RequestParam int anoFim) {

        return ResponseEntity.status(HttpStatus.OK).body(buscarKpiMembrosDashUseCase.execute(anoInicio, anoFim));
    }

    @ApiErrorsComuns
    @GetMapping("/membros/evolucao")
    @PreAuthorize("hasAnyAuthority('SCOPE_LIDER_MINISTERIO', 'SCOPE_GOVERNO')")
    public ResponseEntity<List<MembroDashEvolucaoDTO>> buscarEvolucaoMembros(@RequestParam int anoInicio, @RequestParam int anoFim) {

        return ResponseEntity.status(HttpStatus.OK).body(buscarKpiEvolucaoMembrosDashUseCase.execute(anoInicio, anoFim));
    }

    @ApiErrorsComuns
    @GetMapping("/membros/faixa-etaria")
    @PreAuthorize("hasAnyAuthority('SCOPE_LIDER_MINISTERIO', 'SCOPE_GOVERNO')")
    public ResponseEntity<DashboardFaixaEtariaMembroDTO> buscarFaixaEtariaMembros(@RequestParam int anoInicio, @RequestParam int anoFim) {

        return ResponseEntity.status(HttpStatus.OK).body(buscarKpiFaixaEtariaMembrosDashUseCase.execute(anoInicio, anoFim));
    }

    @ApiErrorsComuns
    @GetMapping("/membros/genero")
    @PreAuthorize("hasAnyAuthority('SCOPE_LIDER_MINISTERIO', 'SCOPE_GOVERNO')")
    public ResponseEntity<DashboardGeneroMembroDTO> buscarGeneroMembros(@RequestParam int anoInicio, @RequestParam int anoFim) {

        return ResponseEntity.status(HttpStatus.OK).body(buscarKpiGeneroMembrosDashUseCase.execute(anoInicio, anoFim));
    }

    //Ministerios

    @ApiErrorsComuns
    @GetMapping("/ministerios/kpis")
    @PreAuthorize("hasAnyAuthority('SCOPE_LIDER_MINISTERIO', 'SCOPE_GOVERNO')")
    public ResponseEntity<KpisMinisteriosDTO> buscarKpisMinisterios(@RequestParam int anoInicio, @RequestParam int anoFim) {

        return ResponseEntity.status(HttpStatus.OK).body(buscarKpiMinisteriosDashUseCase.execute(anoInicio, anoFim));
    }

    @ApiErrorsComuns
    @GetMapping("/ministerios/evolucao/{idMinisterio}")
    @PreAuthorize("hasAnyAuthority('SCOPE_LIDER_MINISTERIO', 'SCOPE_GOVERNO')")
    public ResponseEntity<List<MinisterioDashEvolucaoDTO>> buscarEvolucaoMinisterio(@RequestParam int anoInicio, @RequestParam int anoFim, @PathVariable UUID idMinisterio) {

        return ResponseEntity.status(HttpStatus.OK).body(buscarKpiEvolucaoMinisteriosDashUseCase.execute(anoInicio, anoFim, idMinisterio));
    }

    @ApiErrorsComuns
    @GetMapping("/ministerios/quantidade-membros")
    @PreAuthorize("hasAnyAuthority('SCOPE_LIDER_MINISTERIO', 'SCOPE_GOVERNO')")
    public ResponseEntity<List<MinisterioDashQuantidadeMembrosDTO>> buscarQuantidadeMembrosPorMinisterio(@RequestParam int anoInicio, @RequestParam int anoFim) {

        return ResponseEntity.status(HttpStatus.OK).body(buscarKpiQuantidadeMembrosPorMinisterioDashUseCase.execute(anoInicio, anoFim));
    }

    @ApiErrorsComuns
    @GetMapping("/ministerios/quantidade-eventos")
    @PreAuthorize("hasAnyAuthority('SCOPE_LIDER_MINISTERIO', 'SCOPE_GOVERNO')")
    public ResponseEntity<List<MinisterioEventoDashDTO>> buscarQuantidadeEventosPorMinisterio(@RequestParam int anoInicio, @RequestParam int anoFim) {

        return ResponseEntity.status(HttpStatus.OK).body(buscarKpiQuantidadeEventosPorMinisterioDashUseCase.execute(anoInicio, anoFim));
    }


}
