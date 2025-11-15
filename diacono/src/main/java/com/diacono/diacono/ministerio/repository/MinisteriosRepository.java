package com.diacono.diacono.ministerio.repository;

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

    Ministerio findByIdExterno(UUID idExterno);

    Set<Ministerio> findAllByIdExternoIn(List<UUID> idExterno);

    @Query("""
    SELECT m FROM Ministerio m
    WHERE (:busca IS NULL OR nome LIKE :busca OR nomeLider LIKE :busca) AND
    (:status IS NULL OR m.status = :status)
    """)
    Page<Ministerio> buscarComFiltros(Pageable pageable,
                                      @Param("busca") String busca,
                                      @Param("status") EnumStatusMinisterio status);

    @Query("""
        SELECT m.id FROM Ministerio m
        WHERE m.idExterno = :idExterno
    """)
    Long buscarIdPorUUID(@Param("idExterno") UUID idExterno);
}
