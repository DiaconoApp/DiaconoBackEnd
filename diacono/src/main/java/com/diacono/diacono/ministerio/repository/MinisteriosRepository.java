package com.diacono.diacono.ministerio.repository;

import com.diacono.diacono.ministerio.model.entity.Ministerio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MinisteriosRepository extends JpaRepository<Ministerio, Integer> {
}
