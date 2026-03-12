package com.diacono.diacono.ministerio.repository;

import com.diacono.diacono.ministerio.model.dto.response.MinisterioKpisResponseDTO;
import com.diacono.diacono.ministerio.model.entity.EnumStatusMinisterio;
import com.diacono.diacono.ministerio.model.entity.Ministerio;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public interface MinisteriosRepository extends JpaRepository<Ministerio, Long> {

    List<Ministerio> findByIgreja_IdExterno(UUID idExterno);

    Page<Ministerio> findByIgreja_IdExterno(UUID idExterno, Pageable pageable);

    // OWASP A01: metodo legado sem escopo por igreja; prefira findByIgreja_IdExternoAndIdExterno.
    @Deprecated
    Ministerio findByIdExterno(UUID idExterno);

    // OWASP A01: leitura segura por UUID + igreja para evitar acesso cross-tenant.
    Ministerio findByIgreja_IdExternoAndIdExterno (UUID fkIgreja, UUID idExterno);

    // OWASP A01: metodo legado sem escopo por igreja; prefira findAllBYIgreja_IdExternoAndIdExternoIn.
    @Deprecated
    Set<Ministerio> findAllByIdExternoIn(List<UUID> idExterno);

    // OWASP A01: leitura segura por lista de UUIDs + igreja para evitar acesso cross-tenant.
    Set<Ministerio> findAllBYIgreja_IdExternoAndIdExternoIn(UUID fkIgreja, List<UUID> idExterno);

    @Query("""
    SELECT m FROM Ministerio m
    WHERE (:busca IS NULL OR UPPER(m.nome) LIKE :busca OR UPPER(m.nomeLider) LIKE :busca) AND
    (:status IS NULL OR m.status = :status) AND (m.igreja.idExterno = :fkIgreja)
    """)
    Page<Ministerio> buscarComFiltros(Pageable pageable,
                                      @Param("busca") String busca,
                                      @Param("status") EnumStatusMinisterio status,
                                      @Param("fkIgreja") UUID fkIgreja);

    // OWASP A01: metodo legado sem escopo por igreja; prefira buscarIdPorIgrejaAndUUID.
    @Deprecated
    @Query("""
        SELECT m.idInterno FROM Ministerio m
        WHERE m.idExterno = :idExterno
    """)
    Long buscarIdPorUUID(@Param("idExterno") UUID idExterno);

    // OWASP A01: busca segura de ID interno por UUID + igreja.
    @Query("""
        SELECT m.idInterno FROM Ministerio m
        WHERE m.idExterno = :idExterno AND m.igreja.idExterno = :fkIgreja
    """)
    Long buscarIdPorIgrejaAndUUID(@Param("fkIgreja") UUID fkIgreja,
                                  @Param("idExterno") UUID idExterno);


    @Query("""
            
            SELECT new com.diacono.diacono.ministerio.model.dto.response.MinisterioKpisResponseDTO(
                COUNT(m),
                CASE WHEN COUNT(m) = 0 THEN 0 ELSE CAST(SUM(SIZE(m.membros)) AS double) / COUNT(m) END
            )
            FROM Ministerio m
            WHERE m.igreja.idExterno = :fkIgreja
            AND FUNCTION('YEAR', m.dataCriacao) <= :dataFim
            """)
    MinisterioKpisResponseDTO buscarKpis(@Param("fkIgreja") UUID fkIgreja, int dataFim);

}
