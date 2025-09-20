package com.diacono.diacono.membros.repository;

import com.diacono.diacono.membros.model.entity.Membro;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MembrosRepository extends JpaRepository<Membro, Integer> {
}
