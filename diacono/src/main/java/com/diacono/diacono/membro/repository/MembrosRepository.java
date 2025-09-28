package com.diacono.diacono.membro.repository;

import com.diacono.diacono.membro.model.entity.Membro;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MembrosRepository extends JpaRepository<Membro, Integer> {
}
