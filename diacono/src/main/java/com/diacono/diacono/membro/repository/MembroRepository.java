package com.diacono.diacono.membro.repository;

import com.diacono.diacono.membro.model.entity.Membro;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface MembroRepository extends JpaRepository<Membro, Long> {

    Membro findByIdExterno(UUID idExterno);

    @Query("SELECT COUNT(m) FROM Membro m WHERE m.status = 'ATIVO'")
    Long countMembroStatusIgualAtivo();

    @Query("SELECT COUNT(m) FROM Membro m WHERE SIZE(m.ministerios) > 0")
    Long countMembrosComMinisterio();

    @Query("SELECT COUNT(m) FROM Membro m WHERE m.discipulador IS NOT NULL")
    Long countMembrosDiscipulados();

    @Query("SELECT m FROM Membro m " +
            "WHERE nome LIKE :buscaGeral " +
            "OR email LIKE :buscaGeral " +
            "OR celular LIKE :buscaGeral"
            )
    List<Membro> findAllWithFilter(
            @Param("buscaGeral") String buscaGeral
    );

    Membro findByEmail(String email);

    @Query("""
        SELECT m.id FROM Membro m
        WHERE m.idExterno = :idExterno
    """)
    Long buscarIdPorUUID(UUID idExterno);
}
