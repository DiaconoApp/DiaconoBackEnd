package com.diacono.diacono.domain.repository;

import com.diacono.diacono.applications.dtos.ministerio.MinisterioDashEvolucaoDTO;
import com.diacono.diacono.applications.dtos.ministerio.MinisterioDashQuantidadeMembrosDTO;
import com.diacono.diacono.applications.dtos.ministerio.MinisterioSuperSimplificadoDTO;
import com.diacono.diacono.domain.entity.Membro;
import com.diacono.diacono.domain.entity.MembroMinisterio;
import com.diacono.diacono.domain.enums.EnumStatusMembro;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.UUID;

public interface MembroMinisterioRepository {
    int deleteByMembro(Membro membro);
    MembroMinisterio save(MembroMinisterio membroMinisterio);

    Page<MembroMinisterio> buscarPorMembroMinisterioComFiltro(
            Pageable pageable, UUID idMinisterio, String texto, EnumStatusMembro status);

    Page<MembroMinisterio> buscarPorMembroMinisterioSemFiltro(Pageable pageable, UUID idMinisterio);

    int deleteByMembroIdExternoAndMinisterioIdExterno(UUID membroIdExterno, UUID ministerioIdExterno);
    List<MinisterioSuperSimplificadoDTO> buscarMembro(UUID idExternoMembro, UUID idExternoIgreja);
    List<MinisterioSuperSimplificadoDTO> buscarMinisterioLider(UUID idExternoMembro, UUID idExternoIgreja);
    List<MembroMinisterio> findAllByIdExternoIn(List<UUID> idsExternoMembroMinisterio);

    List<MinisterioDashEvolucaoDTO> buscarDashEvolucaoUmAno(int anoFim, UUID idMinisterio, UUID idIgreja);
    List<MinisterioDashEvolucaoDTO> buscarDashEvolucaoPeriodo(int anoInicio, int anoFim, UUID idMinisterio, UUID idIgreja);
    List<MinisterioDashQuantidadeMembrosDTO> buscarQuantidadeMembros(int anoInicio, int anoFim, UUID igrejaId);

    void flush();
}