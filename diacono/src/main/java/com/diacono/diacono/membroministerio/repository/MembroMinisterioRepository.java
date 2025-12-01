package com.diacono.diacono.membroministerio.repository;

import com.diacono.diacono.membro.model.entity.EnumStatusMembro;
import com.diacono.diacono.membro.model.entity.Membro;
import com.diacono.diacono.membroministerio.model.entity.MembroMinisterio;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface MembroMinisterioRepository extends JpaRepository<MembroMinisterio, Long> {

    int deleteByMembro(Membro membro);

    List<MembroMinisterio> findAllByIdExternoIn(List<UUID> idsExternoMembroMinisterio);
    @Query("""
       SELECT mm FROM MembroMinisterio mm
       JOIN mm.membro m
       WHERE mm.ministerio.idExterno = :idMinisterio
         AND (:status IS NULL OR m.status = :status)
       AND (:busca IS NULL OR m.nome LIKE :busca
            OR m.email LIKE :busca)
       """)
    Page<MembroMinisterio> buscarPorMembroMinisterioComFiltro(Pageable pageable, UUID idMinisterio, String texto, EnumStatusMembro status);

    @Query("""
       SELECT mm FROM MembroMinisterio mm
       JOIN mm.membro m
       WHERE mm.ministerio.idExterno = :idMinisterio
       """)
    Page<MembroMinisterio> buscarPorMembroMinisterioSemFiltro(Pageable pageable, UUID idMinisterio);

    int deleteByMembroIdExternoAndMinisterioIdExterno(UUID membroIdExterno, UUID ministerioIdExterno);

}
