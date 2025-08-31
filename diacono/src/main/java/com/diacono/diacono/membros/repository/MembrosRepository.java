package com.diacono.diacono.membros.repository;

import com.diacono.diacono.membros.entity.Membro;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MembrosRepository extends JpaRepository<Membro, Integer> {
}
