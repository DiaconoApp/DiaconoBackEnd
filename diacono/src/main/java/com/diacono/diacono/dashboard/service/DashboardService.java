package com.diacono.diacono.dashboard.service;

import com.diacono.diacono.dashboard.model.response.membro.DashboardFaixaEtariaMembroDTO;
import com.diacono.diacono.dashboard.model.response.membro.DashboardGeneroMembroDTO;
import com.diacono.diacono.dashboard.model.response.membro.KpisMembrosDTO;
import com.diacono.diacono.membro.model.dto.response.MembroDashEvolucaoDTO;
import com.diacono.diacono.membro.model.dto.response.MembroDashFaixaEtariaDTO;
import com.diacono.diacono.membro.model.dto.response.MembroDashGeneroDTO;
import com.diacono.diacono.membro.model.dto.response.MembroKpiResponseDTO;
import com.diacono.diacono.membro.service.MembroService;
import com.diacono.diacono.ministerio.model.dto.response.MinisterioKpisResponseDTO;
import com.diacono.diacono.ministerio.service.MinisterioService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class DashboardService {

    private final MembroService membroService;
    private final MinisterioService ministerioService;

    public DashboardService(MembroService membroService) {
        this.membroService = membroService;
    }

    public KpisMembrosDTO buscarKpisMembros(int anoInicio, int anoFim) {

        MembroKpiResponseDTO kpis = membroService.buscarKpis(anoInicio, anoFim);

        long membrosAtivos = kpis.membrosAtivos();
        long totalMembrosAnoInicio = kpis.totalAnoInicio();
        long totalMembrosAnoFim = kpis.totalAnoFim();

        long membrosNovos = totalMembrosAnoFim - totalMembrosAnoInicio;

        long retencao = membrosAtivos == 0 ? 0 : (membrosAtivos - membrosNovos) * 100 / membrosAtivos;

        KpisMembrosDTO kpisMembrosDTO = new KpisMembrosDTO(
                membrosAtivos,
                membrosNovos,
                retencao
        );

        return kpisMembrosDTO;

    }

    public List<MembroDashEvolucaoDTO> buscarDashEvolucao(int anoInicio, int anoFim) {

        List<MembroDashEvolucaoDTO> bruto = membroService.buscarDashEvolucao(anoInicio, anoFim);

        Map<Integer, Long> mapa = bruto.stream()
                .collect(Collectors.toMap(
                        MembroDashEvolucaoDTO::getAno,
                        MembroDashEvolucaoDTO::getQuantidade
                ));

        List<MembroDashEvolucaoDTO> completo = new ArrayList<>();

        for (int ano = anoInicio; ano <= anoFim; ano++) {
            long qtd = mapa.getOrDefault(ano, 0L);
            completo.add(new MembroDashEvolucaoDTO(ano, qtd));
        }
        
        return completo;

    }

    public DashboardFaixaEtariaMembroDTO buscarDashFaixaEtaria(int anoInicio, int anoFim) {

        MembroDashFaixaEtariaDTO faixaEtaria = membroService.buscarDashFaixaEtaria(anoInicio, anoFim);

        long total = faixaEtaria.criancas() + faixaEtaria.adolescentes() + faixaEtaria.jovens()
                + faixaEtaria.adultos() + faixaEtaria.idosos();

        DashboardFaixaEtariaMembroDTO faixaEtariaDTO = new DashboardFaixaEtariaMembroDTO(
                faixaEtaria.criancas()/total * 100,
                faixaEtaria.adolescentes()/total * 100,
                faixaEtaria.jovens()/total * 100,
                faixaEtaria.adultos()/total * 100,
                faixaEtaria.idosos()/total * 100
        );

        return faixaEtariaDTO;

    }

    public DashboardGeneroMembroDTO buscarDashGenero(int anoInicio, int anoFim) {

        MembroDashGeneroDTO genero = membroService.buscarDashGenero(anoInicio, anoFim);

        long total = genero.masculino() + genero.feminino();

        DashboardGeneroMembroDTO response = new DashboardGeneroMembroDTO(
                genero.masculino()/total * 100,
                genero.feminino()/total * 100
        );

        return response;

    }

    // Ministerios

    public void ministerioBuscarKpis(int anoInicio, int anoFim) {

        MinisterioKpisResponseDTO kpis = ministerioService.ministerioBuscarKpis(anoInicio, anoFim); //ministerioService.buscarKpis(anoInicio, anoFim);

        long ativos = 0L;
        long mediaMembrosPorMinisterio = 0L;
        String ministerioMaisEngajado = "";
    }

}
