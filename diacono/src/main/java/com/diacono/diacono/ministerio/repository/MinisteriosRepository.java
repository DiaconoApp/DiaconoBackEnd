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

    Ministerio findByIdExterno(UUID idExterno);

    Set<Ministerio> findAllByIdExternoIn(List<UUID> idExterno);

    @Query("""
    SELECT m FROM Ministerio m
    WHERE (:busca IS NULL OR UPPER(nome) LIKE :busca OR UPPER(nomeLider) LIKE :busca) AND
    (:status IS NULL OR m.status = :status) AND (m.igreja.idExterno = :fkIgreja)
    """)
    Page<Ministerio> buscarComFiltros(Pageable pageable,
                                      @Param("busca") String busca,
                                      @Param("status") EnumStatusMinisterio status, @Param("fkIgreja") UUID fkIgreja);

    @Query("""
        SELECT m.id FROM Ministerio m
        WHERE m.idExterno = :idExterno
    """)
    Long buscarIdPorUUID(@Param("idExterno") UUID idExterno);

    @Query("""
            
            SELECT new com.diacono.diacono.ministerio.model.dto.response.MinisterioKpisResponseDTO(
                COUNT(m),
                CASE WHEN COUNT(m) = 0 THEN 0 ELSE CAST(SUM(SIZE(m.membros)) AS double) / COUNT(m) END
            )
            FROM Ministerio m
            WHERE m.igreja.idExterno = :fkIgreja
            AND FUNCTION('YEAR', m.dataCriacao) BETWEEN :dataInicio AND :dataFim 
            """)
    MinisterioKpisResponseDTO buscarKpis(@Param("fkIgreja") UUID fkIgreja, int dataInicio, int dataFim);
}
