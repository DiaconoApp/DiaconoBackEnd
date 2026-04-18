package com.diacono.diacono.infrastructure.persistence.MembroMinisterio;

import com.diacono.diacono.applications.dtos.ministerio.MinisterioDashEvolucaoDTO;
import com.diacono.diacono.applications.dtos.ministerio.MinisterioDashQuantidadeMembrosDTO;
import com.diacono.diacono.applications.dtos.ministerio.MinisterioSuperSimplificadoDTO;
import com.diacono.diacono.domain.entity.Membro;
import com.diacono.diacono.domain.entity.MembroMinisterio;
import com.diacono.diacono.domain.enums.EnumStatusMembro;
import com.diacono.diacono.domain.repository.MembroMinisterioRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public class MembroMinisterioRepositoryImpl implements MembroMinisterioRepository {

    private final MembroMinisterioJpaRepository jpaRepository;

    public MembroMinisterioRepositoryImpl(MembroMinisterioJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public int deleteByMembro(Membro membro) {
        return jpaRepository.deleteByMembro(membro);
    }

    @Override
    public MembroMinisterio save(MembroMinisterio membroMinisterio) {
        return jpaRepository.save(membroMinisterio);
    }

    @Override
    public Page<MembroMinisterio> buscarPorMembroMinisterioComFiltro(
            Pageable pageable, UUID idMinisterio, String texto, EnumStatusMembro status) {
        return jpaRepository.buscarPorMembroMinisterioComFiltro(pageable, idMinisterio, texto, status);
    }

    @Override
    public Page<MembroMinisterio> buscarPorMembroMinisterioSemFiltro(Pageable pageable, UUID idMinisterio) {
        return jpaRepository.buscarPorMembroMinisterioSemFiltro(pageable, idMinisterio);
    }

    @Override
    public int deleteByMembroIdExternoAndMinisterioIdExterno(UUID membroIdExterno, UUID ministerioIdExterno) {
        return jpaRepository.deleteByMembroIdExternoAndMinisterioIdExterno(membroIdExterno, ministerioIdExterno);
    }

    @Override
    public List<MinisterioSuperSimplificadoDTO> buscarMembro(UUID idExternoMembro, UUID idExternoIgreja) {
        return jpaRepository.buscarMembro(idExternoMembro, idExternoIgreja);
    };

    @Override
    public List<MinisterioSuperSimplificadoDTO> buscarMinisterioLider(UUID idExternoMembro, UUID idExternoIgreja) {
        return jpaRepository.buscarMinisterioLider(idExternoMembro, idExternoIgreja);
    }

    @Override
    public List<MembroMinisterio> findAllByIdExternoIn(List<UUID> idsExternoMembroMinisterio) {
        return jpaRepository.findAllByIdExternoIn(idsExternoMembroMinisterio);
    }

    @Override
    public List<MinisterioDashEvolucaoDTO> buscarDashEvolucaoUmAno(int anoFim, UUID idMinisterio, UUID idIgreja) {
        return jpaRepository.buscarDashEvolucaoUmAno(anoFim, idMinisterio, idIgreja);
    }

    @Override
    public List<MinisterioDashEvolucaoDTO> buscarDashEvolucaoPeriodo(int anoInicio, int anoFim, UUID idMinisterio, UUID idIgreja) {
        return jpaRepository.buscarDashEvolucaoPeriodo(anoInicio, anoFim, idMinisterio, idIgreja);
    }

    @Override
    public List<MinisterioDashQuantidadeMembrosDTO> buscarQuantidadeMembros(int anoInicio, int anoFim, UUID igrejaId) {
        return jpaRepository.buscarQuantidadeMembros(anoInicio, anoFim, igrejaId);
    }
}