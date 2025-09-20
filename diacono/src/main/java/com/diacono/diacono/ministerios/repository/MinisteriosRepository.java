package com.diacono.diacono.ministerios.repository;

import com.diacono.diacono.ministerios.model.entity.Ministerio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MinisteriosRepository extends JpaRepository<Ministerio, Integer> {
}
