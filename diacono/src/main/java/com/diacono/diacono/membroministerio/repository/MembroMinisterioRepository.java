package com.diacono.diacono.membroministerio.repository;

import com.diacono.diacono.membro.model.entity.Membro;
import com.diacono.diacono.membroministerio.model.entity.MembroMinisterio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MembroMinisterioRepository extends JpaRepository<MembroMinisterio, Long> {

    int deleteByMembro(Membro membro);

}
