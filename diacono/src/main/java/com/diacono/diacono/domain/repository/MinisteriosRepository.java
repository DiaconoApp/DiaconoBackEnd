package com.diacono.diacono.domain.repository;

import com.diacono.diacono.applications.dtos.ministerio.MinisterioKpisResponseDTO;
import com.diacono.diacono.applications.dtos.ministerio.MinisterioSuperSimplificadoDTO;
import com.diacono.diacono.domain.entity.Ministerio;
import com.diacono.diacono.domain.enums.EnumStatusMinisterio;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

public interface MinisteriosRepository {
    List<Ministerio> findByIgrejaIdExterno(UUID idExterno);
    Page<Ministerio> findByIgrejaIdExterno(UUID idExterno, Pageable pageable);

    Optional<Ministerio> findByIdExterno(UUID idExterno);
    Set<Ministerio> findAllByIdExternoIn(List<UUID> idExterno);

    Page<Ministerio> buscarComFiltros(Pageable pageable, String busca, EnumStatusMinisterio status, UUID fkIgreja);
    Optional<Long> buscarIdPorUUID(UUID idExterno);
    List<MinisterioSuperSimplificadoDTO> buscarMinisteriosMembro(UUID idExternoMembro, UUID idExternoIgreja);

    Ministerio save(Ministerio ministerio);
    MinisterioKpisResponseDTO buscarKpis(UUID fkIgreja, int dataFim);
}