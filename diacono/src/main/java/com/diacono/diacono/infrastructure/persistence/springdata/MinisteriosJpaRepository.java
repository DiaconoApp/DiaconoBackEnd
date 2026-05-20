package com.diacono.diacono.infrastructure.persistence.springdata;

import com.diacono.diacono.applications.dtos.ministerio.MinisterioKpisResponseDTO;
import com.diacono.diacono.applications.dtos.ministerio.MinisterioSuperSimplificadoDTO;
import com.diacono.diacono.domain.enums.EnumStatusMinisterio;
import com.diacono.diacono.domain.entity.Ministerio;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public interface MinisteriosJpaRepository extends JpaRepository<Ministerio, Long> {

    List<Ministerio> findByIgreja_IdExterno(UUID idExterno);

    Page<Ministerio> findByIgreja_IdExterno(UUID idExterno, Pageable pageable);

    /**
     * @deprecated Sem scoping por igreja — sujeito a IDOR cross-tenant.
     * Usar {@link #findByIdExternoAndIgreja_IdExterno(UUID, UUID)} em substituição.
     */
    @Deprecated
    Ministerio findByIdExterno(UUID idExterno);

    /**
     * Busca ministério por UUID garantindo que pertence à igreja informada.
     */
    Optional<Ministerio> findByIdExternoAndIgreja_IdExterno(UUID idExterno, UUID igrejaIdExterno);

    /**
     * @deprecated Sem scoping por igreja — permite recuperação cross-tenant em lote.
     * Usar {@link #findAllByIdExternoInAndIgreja_IdExterno(List, UUID)} em substituição.
     */
    @Deprecated
    Set<Ministerio> findAllByIdExternoIn(List<UUID> idExterno);

    /**
     * Busca ministérios por lista de UUIDs garantindo que pertencem à igreja informada.
     */
    Set<Ministerio> findAllByIdExternoInAndIgreja_IdExterno(List<UUID> idExterno, UUID igrejaIdExterno);

    @Query("""
        SELECT m FROM Ministerio m
        WHERE (:busca IS NULL OR UPPER(nome) LIKE CONCAT('%', :busca, '%') OR UPPER(nomeLider) LIKE CONCAT('%', :busca, '%'))
        AND (:status IS NULL OR m.status = :status)
        AND (m.igreja.idExterno = :fkIgreja)
    """)
    Page<Ministerio> buscarComFiltros(
            Pageable pageable,
            @Param("busca") String busca,
            @Param("status") EnumStatusMinisterio status,
            @Param("fkIgreja") UUID fkIgreja
    );

    /**
     * @deprecated Sem scoping por igreja — expõe ID interno de qualquer tenant por UUID conhecido.
     * Usar {@link #buscarIdPorUUIDAndIgrejaIdExterno(UUID, UUID)} em substituição.
     */
    @Deprecated
    @Query("""
        SELECT m.id FROM Ministerio m
        WHERE m.idExterno = :idExterno
    """)
    Long buscarIdPorUUID(@Param("idExterno") UUID idExterno);

    /**
     * Busca ID interno do ministério garantindo que pertence à igreja informada.
     */
    @Query("""
        SELECT m.id FROM Ministerio m
        WHERE m.idExterno = :idExterno
        AND m.igreja.idExterno = :igrejaId
    """)
    Long buscarIdPorUUIDAndIgrejaIdExterno(
            @Param("idExterno") UUID idExterno,
            @Param("igrejaId") UUID igrejaId
    );

    @Query("""
        SELECT DISTINCT new com.diacono.diacono.applications.dtos.ministerio.MinisterioSuperSimplificadoDTO(
            ms.idExterno,
            ms.nome
        )
        FROM MembroMinisterio mm
        JOIN mm.ministerio ms
        JOIN mm.membro m
        WHERE m.idExterno = :idExternoMembro
        AND ms.igreja.idExterno = :idExternoIgreja
        AND ms.status = com.diacono.diacono.domain.enums.EnumStatusMinisterio.ATIVO
        AND m.status = com.diacono.diacono.domain.enums.EnumStatusMembro.ATIVO
    """)
    List<MinisterioSuperSimplificadoDTO> buscarMinisteriosMembro(
            @Param("idExternoMembro") UUID idExternoMembro,
            @Param("idExternoIgreja") UUID idExternoIgreja
    );

    @Query("""
        SELECT new com.diacono.diacono.applications.dtos.ministerio.MinisterioKpisResponseDTO(
            COUNT(m),
            CASE WHEN COUNT(m) = 0 THEN 0 ELSE CAST(SUM(SIZE(m.membros)) AS double) / COUNT(m) END
        )
        FROM Ministerio m
        WHERE m.igreja.idExterno = :fkIgreja
        AND FUNCTION('YEAR', m.dataCriacao) <= :dataFim
    """)
    MinisterioKpisResponseDTO buscarKpis(
            @Param("fkIgreja") UUID fkIgreja,
            @Param("dataFim") int dataFim
    );
}
