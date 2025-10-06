package com.diacono.diacono.membros.repository;

import com.diacono.diacono.membros.model.entity.Membro;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface MembrosRepository extends JpaRepository<Membro, Integer> {

    @Query("SELECT COUNT(m) FROM Membro m WHERE m.ativo = true")
    Long countMembroStatusIgualAtivo();

    @Query("""
              SELECT COUNT(m) 
              FROM Membro m 
              WHERE m.ministerio IS NOT NULL 
              AND TRIM(m.ministerio) <> ''
           """)
    Long countMembrosComMinisterio();

    @Query("SELECT COUNT(m) FROM Membro m WHERE m.discipulador IS NOT NULL")
    Long countMembrosDiscipulados();

}
