package com.diacono.diacono.membro.repository;

import com.diacono.diacono.membro.model.entity.Membro;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface MembrosRepository extends JpaRepository<Membro, Long> {

    Optional<Membro> findByIdExterno(UUID idExterno);

    @Query("SELECT COUNT(m) FROM Membro m WHERE m.statusMembro = 'ATIVO'")
    Long countMembroStatusIgualAtivo();

    @Query("SELECT COUNT(m) FROM Membro m WHERE SIZE(m.ministerio) > 0")
    Long countMembrosComMinisterio();

    @Query("SELECT COUNT(m) FROM Membro m WHERE m.discipulador IS NOT NULL")
    Long countMembrosDiscipulados();
}
