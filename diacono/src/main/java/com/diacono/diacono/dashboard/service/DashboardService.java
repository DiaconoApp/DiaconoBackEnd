package com.diacono.diacono.dashboard.service;

import com.diacono.diacono.dashboard.model.response.membro.DashboardFaixaEtariaMembroDTO;
import com.diacono.diacono.dashboard.model.response.membro.DashboardGeneroMembroDTO;
import com.diacono.diacono.dashboard.model.response.membro.KpisMembrosDTO;
import com.diacono.diacono.dashboard.model.response.ministerio.KpisMinisteriosDTO;
import com.diacono.diacono.evento.model.dto.response.EventoKpiDTO;
import com.diacono.diacono.evento.service.EventoService;
import com.diacono.diacono.evento.model.dto.response.MinisterioEventoDashDTO;
import com.diacono.diacono.global.error.exceptions.FieldInvalidException;
import com.diacono.diacono.global.error.exceptions.ObjectNotFoundException;
import com.diacono.diacono.membro.model.dto.response.MembroDashEvolucaoDTO;
import com.diacono.diacono.membro.model.dto.response.MembroDashFaixaEtariaDTO;
import com.diacono.diacono.membro.model.dto.response.MembroDashGeneroDTO;
import com.diacono.diacono.membro.model.dto.response.MembroKpiResponseDTO;
import com.diacono.diacono.membro.service.MembroService;
import com.diacono.diacono.membroministerio.model.dto.response.MinisterioDashEvolucaoDTO;
import com.diacono.diacono.membroministerio.model.dto.response.MinisterioDashQuantidadeMembrosDTO;
import com.diacono.diacono.membroministerio.service.MembroMinisterioService;
import com.diacono.diacono.ministerio.model.dto.response.MinisterioKpisResponseDTO;
import com.diacono.diacono.ministerio.service.MinisterioService;
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

    // OWASP A05: valida parametros antes de processar agregacoes sensíveis.
    public KpisMembrosDTO buscarKpisMembros(int anoInicio, int anoFim) {
        validarAnoInicioEFim(anoInicio, anoFim);

        MembroKpiResponseDTO kpis = membroService.buscarKpis(anoInicio, anoFim);

        // OWASP A05: evita divisao por zero em calculo de retencao.
        long membrosAtivos = kpis.membrosAtivos();
        long membrosInativos = kpis.membrosInativos();
        long totalMembrosAnoInicio = kpis.totalAnoInicio();
        long totalMembros = membrosAtivos + membrosInativos;

        if (totalMembros == 0) {
            return new KpisMembrosDTO(0, 0, 0);
        }

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

    // OWASP A05: valida resultado antes de retornar ao cliente.
    public List<MembroDashEvolucaoDTO> buscarDashEvolucao(int anoInicio, int anoFim) {
        validarAnoInicioEFim(anoInicio, anoFim);

        List<MembroDashEvolucaoDTO> bruto = membroService.buscarDashEvolucao(anoInicio, anoFim);

        if (bruto == null || bruto.isEmpty()) {
            throw new ObjectNotFoundException("Nenhum dado encontrado para o período informado.");
        }

        return bruto;
    }

    // OWASP A05: evita divisao por zero em calculo de percentual.
    public DashboardFaixaEtariaMembroDTO buscarDashFaixaEtaria(int anoInicio, int anoFim) {
        validarAnoInicioEFim(anoInicio, anoFim);

        MembroDashFaixaEtariaDTO faixaEtaria = membroService.buscarDashFaixaEtaria(anoFim);

        // OWASP A05: valida objeto retornado antes de processar.
        if (faixaEtaria == null) {
            throw new ObjectNotFoundException("Nenhum dado encontrado para o período informado.");
        }

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

    // OWASP A05: evita divisao por zero e valida objeto recebido.
    public DashboardGeneroMembroDTO buscarDashGenero(int anoInicio, int anoFim) {
        validarAnoInicioEFim(anoInicio, anoFim);

        MembroDashGeneroDTO genero = membroService.buscarDashGenero(anoFim);

        if (genero == null) {
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

    // OWASP A05: valida multiplas respostas antes de agregar dados sensíveis.
    public KpisMinisteriosDTO ministerioBuscarKpis(int anoInicio, int anoFim) {
        validarAnoInicioEFim(anoInicio, anoFim);

        MinisterioKpisResponseDTO kpiMinisterio = ministerioService.ministerioBuscarKpis(anoFim);
        List<EventoKpiDTO> kpiEvento = eventoService.buscarKpisEvento(anoInicio, anoFim);

        if (kpiMinisterio == null || kpiEvento == null || kpiEvento.isEmpty()) {
            throw new ObjectNotFoundException("Nenhum dado encontrado para o período informado.");
        }

        EventoKpiDTO eventoRetido = kpiEvento.get(0);

        KpisMinisteriosDTO response = new KpisMinisteriosDTO(
                eventoRetido,
                kpiMinisterio
        );

        return response;
    }

    // OWASP A01/A05: valida id do ministerio e parametros antes de acessar dados.
    public List<MinisterioDashEvolucaoDTO> ministerioBuscarDashEvolucao(int anoInicio, int anoFim, UUID idMinisterio) {
        // OWASP A05: valida UUID antes de fazer consulta ao banco.
        if (idMinisterio == null) {
            throw new FieldInvalidException("O id do ministerio precisa ser informado");
        }

        validarAnoInicioEFim(anoInicio, anoFim);

        List<MinisterioDashEvolucaoDTO> response = membroMinisterioService.ministerioBuscarDashEvolucao(anoInicio, anoFim, idMinisterio);

        if (response == null || response.isEmpty()) {
            throw new ObjectNotFoundException("Nenhum dado encontrado para o período informado.");
        }

        return response;
    }

    // OWASP A05: valida resultado antes de retornar.
    public List<MinisterioDashQuantidadeMembrosDTO> ministerioBuscarDashQuantidadeMembro(int anoInicio, int anoFim) {
        validarAnoInicioEFim(anoInicio, anoFim);

        List<MinisterioDashQuantidadeMembrosDTO> response = membroMinisterioService.ministerioBuscarDashQuantidadeMembro(anoInicio, anoFim);

        if (response == null || response.isEmpty()) {
            throw new ObjectNotFoundException("Nenhum dado encontrado para o período informado.");
        }

        return response;
    }

    // OWASP A05: valida resultado antes de retornar.
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
    }

}
