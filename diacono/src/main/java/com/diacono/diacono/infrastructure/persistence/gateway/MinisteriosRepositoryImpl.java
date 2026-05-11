package com.diacono.diacono.infrastructure.persistence.gateway;

import com.diacono.diacono.applications.dtos.ministerio.MinisterioKpisResponseDTO;
import com.diacono.diacono.applications.dtos.ministerio.MinisterioSuperSimplificadoDTO;
import com.diacono.diacono.domain.entity.Ministerio;
import com.diacono.diacono.domain.enums.EnumStatusMinisterio;
import com.diacono.diacono.domain.repository.MinisteriosRepository;
import com.diacono.diacono.infrastructure.persistence.springdata.MinisteriosJpaRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Repository
public class MinisteriosRepositoryImpl implements MinisteriosRepository {

    private static final Logger logger = LoggerFactory.getLogger(MinisteriosRepositoryImpl.class);

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
    @Deprecated
    public Optional<Ministerio> findByIdExterno(UUID idExterno) {
        logger.warn("Uso de findByIdExterno sem scoping por igreja. idExterno=[{}]", idExterno);
        return Optional.ofNullable(jpaRepository.findByIdExterno(idExterno));
    }

    @Override
    public Optional<Ministerio> findByIdExternoAndIgrejaId(UUID idExterno, UUID igrejaId) {
        return jpaRepository.findByIdExternoAndIgreja_IdExterno(idExterno, igrejaId);
    }

    @Override
    @Deprecated
    public Set<Ministerio> findAllByIdExternoIn(List<UUID> idExterno) {
        logger.warn("Uso de findAllByIdExternoIn sem scoping por igreja. quantidade=[{}]", idExterno.size());
        return jpaRepository.findAllByIdExternoIn(idExterno);
    }

    @Override
    public Set<Ministerio> findAllByIdExternoInAndIgrejaId(List<UUID> idExterno, UUID igrejaId) {
        return jpaRepository.findAllByIdExternoInAndIgreja_IdExterno(idExterno, igrejaId);
    }

    @Override
    public Page<Ministerio> buscarComFiltros(Pageable pageable, String busca, EnumStatusMinisterio status, UUID fkIgreja) {
        return jpaRepository.buscarComFiltros(pageable, busca, status, fkIgreja);
    }

    @Override
    @Deprecated
    public Optional<Long> buscarIdPorUUID(UUID idExterno) {
        logger.warn("Uso de buscarIdPorUUID sem scoping por igreja. idExterno=[{}]", idExterno);
        return Optional.ofNullable(jpaRepository.buscarIdPorUUID(idExterno));
    }

    @Override
    public Optional<Long> buscarIdPorUUIDAndIgrejaId(UUID idExterno, UUID igrejaId) {
        return Optional.ofNullable(jpaRepository.buscarIdPorUUIDAndIgrejaIdExterno(idExterno, igrejaId));
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