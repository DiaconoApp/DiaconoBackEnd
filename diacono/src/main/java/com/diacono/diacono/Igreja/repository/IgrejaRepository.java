package com.diacono.diacono.Igreja.repository;

import com.diacono.diacono.Igreja.model.entity.Igreja;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface IgrejaRepository extends JpaRepository<Igreja, Long> {

    Igreja findByIdExterno(UUID idExterno);

}
