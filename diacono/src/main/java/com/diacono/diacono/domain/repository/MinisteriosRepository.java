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

    /**
     * @deprecated Sem scoping por igreja — sujeito a IDOR cross-tenant.
     * Usar {@link #findByIdExternoAndIgrejaId(UUID, UUID)} em substituição.
     */
    @Deprecated
    Optional<Ministerio> findByIdExterno(UUID idExterno);

    /**
     * Busca ministério por UUID garantindo que pertence à igreja do token.
     */
    Optional<Ministerio> findByIdExternoAndIgrejaId(UUID idExterno, UUID igrejaId);

    /**
     * @deprecated Sem scoping por igreja — permite recuperação cross-tenant em lote.
     * Usar {@link #findAllByIdExternoInAndIgrejaId(List, UUID)} em substituição.
     */
    @Deprecated
    Set<Ministerio> findAllByIdExternoIn(List<UUID> idExterno);

    /**
     * Busca ministérios por lista de UUIDs garantindo que pertencem à igreja do token.
     */
    Set<Ministerio> findAllByIdExternoInAndIgrejaId(List<UUID> idExterno, UUID igrejaId);

    Page<Ministerio> buscarComFiltros(Pageable pageable, String busca, EnumStatusMinisterio status, UUID fkIgreja);

    /**
     * @deprecated Sem scoping por igreja — expõe ID interno de qualquer tenant por UUID conhecido.
     * Usar {@link #buscarIdPorUUIDAndIgrejaId(UUID, UUID)} em substituição.
     */
    @Deprecated
    Optional<Long> buscarIdPorUUID(UUID idExterno);

    /**
     * Busca ID interno do ministério garantindo que pertence à igreja do token.
     */
    Optional<Long> buscarIdPorUUIDAndIgrejaId(UUID idExterno, UUID igrejaId);

    List<MinisterioSuperSimplificadoDTO> buscarMinisteriosMembro(UUID idExternoMembro, UUID idExternoIgreja);

    Ministerio save(Ministerio ministerio);

    MinisterioKpisResponseDTO buscarKpis(UUID fkIgreja, int dataFim);
}