package com.diacono.diacono.use_cases;

import com.diacono.diacono.applications.dtos.dashboard.DashboardFaixaEtariaMembroDTO;
import com.diacono.diacono.applications.dtos.dashboard.DashboardGeneroMembroDTO;
import com.diacono.diacono.applications.dtos.ministerio.KpisMembrosDTO;
import com.diacono.diacono.applications.dtos.ministerio.KpisMinisteriosDTO;
import com.diacono.diacono.applications.dtos.evento.EventoKpiDTO;
import com.diacono.diacono.applications.dtos.ministerio.MinisterioEventoDashDTO;
import com.diacono.diacono.global.error.exceptions.FieldInvalidException;
import com.diacono.diacono.global.error.exceptions.ObjectNotFoundException;
import com.diacono.diacono.applications.dtos.membro.MembroDashEvolucaoDTO;
import com.diacono.diacono.applications.dtos.membro.MembroDashFaixaEtariaDTO;
import com.diacono.diacono.applications.dtos.membro.MembroDashGeneroDTO;
import com.diacono.diacono.applications.dtos.membro.MembroKpiResponseDTO;
import com.diacono.diacono.applications.dtos.ministerio.MinisterioDashEvolucaoDTO;
import com.diacono.diacono.applications.dtos.ministerio.MinisterioDashQuantidadeMembrosDTO;
import com.diacono.diacono.applications.dtos.ministerio.MinisterioKpisResponseDTO;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class DashboardService {

    private final MembroService membroService;
    private final MinisterioService ministerioService;
    private final EventoService eventoService;
    private final MembroMinisterioService membroMinisterioService;

    public DashboardService(MembroService membroService, MinisterioService ministerioService, EventoService eventoService, MembroMinisterioService membroMinisterioService) {
        this.membroService = membroService;
        this.ministerioService = ministerioService;
        this.eventoService = eventoService;
        this.membroMinisterioService = membroMinisterioService;
    }

    public KpisMembrosDTO buscarKpisMembros(int anoInicio, int anoFim) {

        validarAnoInicioEFim(anoInicio, anoFim);

        MembroKpiResponseDTO kpis = membroService.buscarKpis(anoInicio, anoFim);

        long membrosAtivos = kpis.membrosAtivos();
        long membrosInativos = kpis.membrosInativos();
        long totalMembrosAnoInicio = kpis.totalAnoInicio();
        long totalMembros = membrosAtivos + membrosInativos;


        long membrosNovos = totalMembros;


        if (anoInicio == anoFim) {
            membrosNovos = totalMembrosAnoInicio;
        }

        double resultado = Math.round((membrosAtivos - membrosInativos) * 100.0 / totalMembros);


        double retencao = membrosInativos == 0 ? 100 : resultado;


        KpisMembrosDTO kpisMembrosDTO = new KpisMembrosDTO(
                membrosAtivos,
                membrosNovos,
                retencao
        );

        return kpisMembrosDTO;

    }

    public List<MembroDashEvolucaoDTO> buscarDashEvolucao(int anoInicio, int anoFim) {

        validarAnoInicioEFim(anoInicio, anoFim);

        List<MembroDashEvolucaoDTO> bruto = membroService.buscarDashEvolucao(anoInicio, anoFim);

        if (bruto == null || bruto.isEmpty()) {
            throw new ObjectNotFoundException("Nenhum dado encontrado para o período informado.");
        }

        return bruto;
    }

    public DashboardFaixaEtariaMembroDTO buscarDashFaixaEtaria(int anoInicio, int anoFim) {

        validarAnoInicioEFim(anoInicio, anoFim);

        MembroDashFaixaEtariaDTO faixaEtaria = membroService.buscarDashFaixaEtaria(anoFim);

        long total = faixaEtaria.criancas() + faixaEtaria.adolescentes() + faixaEtaria.jovens()
                + faixaEtaria.adultos() + faixaEtaria.idosos();


        if (total == 0) {
            return new DashboardFaixaEtariaMembroDTO(0, 0, 0, 0, 0);
        }

        DashboardFaixaEtariaMembroDTO faixaEtariaDTO = new DashboardFaixaEtariaMembroDTO(
                (faixaEtaria.criancas() * 100) / total,
                (faixaEtaria.adolescentes() * 100) / total,
                (faixaEtaria.jovens() * 100) / total,
                (faixaEtaria.adultos() * 100) / total,
                (faixaEtaria.idosos() * 100) / total
        );

        return faixaEtariaDTO;
    }

    public DashboardGeneroMembroDTO buscarDashGenero(int anoInicio, int anoFim) {

        validarAnoInicioEFim(anoInicio, anoFim);

        MembroDashGeneroDTO genero = membroService.buscarDashGenero(anoFim);

        if (genero == null){
            throw new ObjectNotFoundException("Nenhum dado encontrado para o período informado.");
        }

        long total = genero.masculino() + genero.feminino();


        if (total == 0) {
            return new DashboardGeneroMembroDTO(0, 0);
        }

        double masculinoPercent = (double) genero.masculino() / total * 100;
        double femininoPercent = (double) genero.feminino() / total * 100;

        DashboardGeneroMembroDTO response = new DashboardGeneroMembroDTO(
                masculinoPercent,
                femininoPercent
        );

        return response;
    }

    // Ministerios

    public KpisMinisteriosDTO ministerioBuscarKpis(int anoInicio, int anoFim) {

        validarAnoInicioEFim(anoInicio, anoFim);

        MinisterioKpisResponseDTO kpiMinisterio = ministerioService.ministerioBuscarKpis(anoFim);
        List<EventoKpiDTO> kpiEvento = eventoService.buscarKpisEvento(anoInicio, anoFim);

        if (kpiMinisterio == null || kpiEvento == null || kpiEvento.isEmpty()){
            throw new ObjectNotFoundException("Nenhum dado encontrado para o período informado.");
        }

        EventoKpiDTO eventoRetido = kpiEvento.get(0);


        KpisMinisteriosDTO response = new KpisMinisteriosDTO(
                eventoRetido,
                kpiMinisterio
        );

        return response;
    }

    public List<MinisterioDashEvolucaoDTO> ministerioBuscarDashEvolucao(int anoInicio, int anoFim, UUID idMinisterio) {

        if (idMinisterio == null) {
            throw new ObjectNotFoundException("Nenhum dado encontrado para o período informado.");
        }

        validarAnoInicioEFim(anoInicio, anoFim);

        List<MinisterioDashEvolucaoDTO> response = membroMinisterioService.ministerioBuscarDashEvolucao(anoInicio, anoFim, idMinisterio);

        if (response == null || response.isEmpty()) {
            throw new ObjectNotFoundException("Nenhum dado encontrado para o período informado.");
        }

        return response;

    }

    public List<MinisterioDashQuantidadeMembrosDTO> ministerioBuscarDashQuantidadeMembro(int anoInicio, int anoFim) {

        validarAnoInicioEFim(anoInicio, anoFim);

        List<MinisterioDashQuantidadeMembrosDTO> response = membroMinisterioService.ministerioBuscarDashQuantidadeMembro(anoInicio, anoFim);

        if (response == null || response.isEmpty()) {
            throw new ObjectNotFoundException("Nenhum dado encontrado para o período informado.");
        }

        return response;

    }

    public List<MinisterioEventoDashDTO> ministerioBuscarDashQuantidadeEventos(int anoInicio, int anoFim) {

        validarAnoInicioEFim(anoInicio, anoFim);


        List<MinisterioEventoDashDTO> response = eventoService.ministerioBuscarDashQuantidadeEventos(anoInicio, anoFim);

        if (response == null || response.isEmpty()) {
            throw new ObjectNotFoundException("Nenhum dado encontrado para o período informado.");
        }

        return response;

    }

    //validacoes

    private void validarAnoInicioEFim(int anoInicio, int anoFim) {
        if (anoInicio > anoFim) {
            throw new FieldInvalidException("Ano de início não pode ser maior que ano de fim.");
        }

        if (anoInicio <= 0 || anoFim <= 0) {
            throw new FieldInvalidException("Ano de início e ano de fim devem ser informados.");
        }
    }

}
