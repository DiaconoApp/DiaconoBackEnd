package com.diacono.diacono.dashboard.Service;

import com.diacono.diacono.dashboard.model.response.membro.KpisMembrosDTO;
import com.diacono.diacono.membro.model.dto.response.MembroDashEvolucaoDTO;
import com.diacono.diacono.membro.model.dto.response.MembroKpiResponseDTO;
import com.diacono.diacono.membro.service.MembroService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class DashboardService {

    private final MembroService membroService;

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


}
