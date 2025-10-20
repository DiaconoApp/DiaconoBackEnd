package com.diacono.diacono.ministerio.repository;

import com.diacono.diacono.ministerio.model.entity.Ministerio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public interface MinisteriosRepository extends JpaRepository<Ministerio, Long> {

    Ministerio findByIdExterno(UUID idExterno);

    Set<Ministerio> findAllByIdExternoIn(List<UUID> idExterno);
}
