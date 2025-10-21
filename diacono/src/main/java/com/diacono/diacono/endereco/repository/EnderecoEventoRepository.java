package com.diacono.diacono.endereco.repository;

import com.diacono.diacono.endereco.model.entity.EnderecoEvento;
import com.diacono.diacono.evento.model.entity.Evento;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface EnderecoEventoRepository extends JpaRepository<EnderecoEvento, Long> {
    EnderecoEvento findByIdExterno(UUID idExterno);
}
