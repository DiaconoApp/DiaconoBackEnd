package com.diacono.diacono.domain.repository;

import com.diacono.diacono.applications.dtos.membro.*;
import com.diacono.diacono.domain.entity.Membro;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface MembroRepository {
    Page<Membro> findByIgrejaIdExterno(UUID fkIgreja, Pageable pageable);
    List<Membro> findAllWithFilter(String buscaGeral, UUID fkIgreja);

    Optional<Membro> findByIdExterno(UUID idExterno);
    Optional<Membro> findByIdExternoAndIgrejaIdExterno(UUID idExterno, UUID igrejaIdExterno);
    Optional<Membro> findByEmailOrCpf(String email, String cpf);
    Optional<Membro> findByEmail(String email);

    Membro save(Membro membro);
    Membro saveAndFlush(Membro membro);

    MembroKpiResponseDTO buscarKpisMembros(UUID idExternoIgreja, int anoInicio, int anoFim);
    List<MembroDashEvolucaoDTO> buscarMembrosPorAno(UUID idExternoIgreja, int anoInicio, int anoFim);
    MembroDashFaixaEtariaDTO buscarMembrosPorFaixaEtaria(UUID idExternoIgreja, int anoFim);
    MembroDashGeneroDTO buscarMembrosPorGenero(UUID idExternoIgreja, int anoFim);
}