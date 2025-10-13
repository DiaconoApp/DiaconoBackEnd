package com.diacono.diacono.ministerio.repository;

import com.diacono.diacono.ministerio.model.entity.Ministerio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Repository
public interface MinisteriosRepository extends JpaRepository<Ministerio, Long> {

    ArrayList<Ministerio> findByIdExterno(UUID idExterno);

}
