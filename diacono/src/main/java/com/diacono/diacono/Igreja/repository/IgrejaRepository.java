package com.diacono.diacono.Igreja.repository;

import com.diacono.diacono.Igreja.model.entity.Igreja;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface IgrejaRepository extends JpaRepository<Igreja, Long> {

    Igreja findByIdExterno(UUID idExterno);

}
