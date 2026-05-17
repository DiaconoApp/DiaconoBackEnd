package com.diacono.diacono.infrastructure.persistence.gateway;

import com.diacono.diacono.applications.dtos.membro.*;
import com.diacono.diacono.domain.entity.Membro;
import com.diacono.diacono.domain.repository.MembroRepository;
import com.diacono.diacono.global.util.SensitiveSearchIndexUtils;
import com.diacono.diacono.infrastructure.persistence.springdata.MembroJpaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class MembroRepositoryImpl implements MembroRepository {

    private final MembroJpaRepository jpaRepository;

    public MembroRepositoryImpl(MembroJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public Page<Membro> findByIgrejaIdExterno(UUID fkIgreja, Pageable pageable) {
        return jpaRepository.findByIgreja_IdExterno(fkIgreja, pageable);
    }

    @Override
    public List<Membro> findAllWithFilter(String buscaGeral, UUID fkIgreja) {
        String buscaToken = SensitiveSearchIndexUtils.searchToken(buscaGeral);
        return jpaRepository.findAllWithFilter(SensitiveSearchIndexUtils.likeToken(buscaToken), fkIgreja);
    }

    @Override
    public Optional<Membro> findByIdExterno(UUID idExterno) {
        return Optional.ofNullable(jpaRepository.findByIdExterno(idExterno));
    }

    @Override
    public Optional<Membro> findByIdExternoAndIgrejaIdExterno(UUID idExterno, UUID igrejaIdExterno) {
        return jpaRepository.findByIdExternoAndIgrejaIdExterno(idExterno, igrejaIdExterno);
    }

    @Override
    public Optional<Membro> findByEmailOrCpf(String email, String cpf) {
        return Optional.ofNullable(jpaRepository.findByEmailHashOrCpfHash(
                SensitiveSearchIndexUtils.exactHash(email),
                SensitiveSearchIndexUtils.exactHash(cpf)
        ));
    }

    @Override
    public Optional<Membro> findByEmail(String email) {
        return Optional.ofNullable(jpaRepository.findByEmailHash(SensitiveSearchIndexUtils.exactHash(email)));
    }

    @Override
    public Membro save(Membro membro) {
        return jpaRepository.save(membro);
    }

    @Override
    public Membro saveAndFlush(Membro membro) {
        return jpaRepository.saveAndFlush(membro);
    }

    @Override
    public MembroKpiResponseDTO buscarKpisMembros(UUID idExternoIgreja, int anoInicio, int anoFim) {
        return jpaRepository.buscarKpisMembros(idExternoIgreja, anoInicio, anoFim);
    }

    @Override
    public List<MembroDashEvolucaoDTO> buscarMembrosPorAno(UUID idExternoIgreja, int anoInicio, int anoFim) {
        return jpaRepository.buscarMembrosPorAno(idExternoIgreja, anoInicio, anoFim);
    }

    @Override
    public MembroDashFaixaEtariaDTO buscarMembrosPorFaixaEtaria(UUID idExternoIgreja, int anoFim) {
        return jpaRepository.buscarMembrosPorFaixaEtaria(idExternoIgreja, anoFim);
    }

    @Override
    public MembroDashGeneroDTO buscarMembrosPorGenero(UUID idExternoIgreja, int anoFim) {
        return jpaRepository.buscarMembrosPorGenero(idExternoIgreja, anoFim);
    }
}
