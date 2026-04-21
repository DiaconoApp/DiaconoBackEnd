package com.diacono.diacono.infrastructure.persistence.gateway;

import com.diacono.diacono.applications.dtos.ministerio.MinisterioKpisResponseDTO;
import com.diacono.diacono.applications.dtos.ministerio.MinisterioSuperSimplificadoDTO;
import com.diacono.diacono.domain.entity.Ministerio;
import com.diacono.diacono.domain.enums.EnumStatusMinisterio;
import com.diacono.diacono.domain.repository.MinisteriosRepository;
import com.diacono.diacono.infrastructure.persistence.springdata.MinisteriosJpaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Repository
public class MinisteriosRepositoryImpl implements MinisteriosRepository {

    private final MinisteriosJpaRepository jpaRepository;

    public MinisteriosRepositoryImpl(MinisteriosJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public List<Ministerio> findByIgrejaIdExterno(UUID idExterno) {
        return jpaRepository.findByIgreja_IdExterno(idExterno);
    }

    @Override
    public Page<Ministerio> findByIgrejaIdExterno(UUID idExterno, Pageable pageable) {
        return jpaRepository.findByIgreja_IdExterno(idExterno, pageable);
    }

    @Override
    public Optional<Ministerio> findByIdExterno(UUID idExterno) {
        return Optional.ofNullable(jpaRepository.findByIdExterno(idExterno));
    }

    @Override
    public Set<Ministerio> findAllByIdExternoIn(List<UUID> idExterno) {
        return jpaRepository.findAllByIdExternoIn(idExterno);
    }

    @Override
    public Page<Ministerio> buscarComFiltros(Pageable pageable, String busca, EnumStatusMinisterio status, UUID fkIgreja) {
        return jpaRepository.buscarComFiltros(pageable, busca, status, fkIgreja);
    }

    @Override
    public Optional<Long> buscarIdPorUUID(UUID idExterno) {
        return Optional.ofNullable(jpaRepository.buscarIdPorUUID(idExterno));
    }

    @Override
    public List<MinisterioSuperSimplificadoDTO> buscarMinisteriosMembro(UUID idExternoMembro, UUID idExternoIgreja) {
        return jpaRepository.buscarMinisteriosMembro(idExternoMembro, idExternoIgreja);
    }

    @Override
    public Ministerio save(Ministerio ministerio) {
        return jpaRepository.save(ministerio);
    }

    @Override
    public MinisterioKpisResponseDTO buscarKpis(UUID fkIgreja, int dataFim) {
        return jpaRepository.buscarKpis(fkIgreja, dataFim);
    }
}