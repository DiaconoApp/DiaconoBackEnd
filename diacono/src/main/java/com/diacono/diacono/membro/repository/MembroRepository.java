package com.diacono.diacono.membro.repository;

import com.diacono.diacono.membro.model.dto.response.MembroSimplificadoDTO;
import com.diacono.diacono.membro.model.entity.Membro;
import com.diacono.diacono.membroministerio.model.entity.MembroMinisterio;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface MembroRepository extends JpaRepository<Membro, Long> {

    Membro findByIdExterno(UUID idExterno);

    Page<Membro> findByIgreja_IdExterno(UUID fkIgreja, Pageable pageable);

    @Query("SELECT COUNT(m) FROM Membro m WHERE m.status = 'ATIVO'")
    Long countMembroStatusIgualAtivo();

    @Query("SELECT COUNT(m) FROM Membro m WHERE SIZE(m.ministerios) > 0")
    Long countMembrosComMinisterio();

    @Query("SELECT COUNT(m) FROM Membro m WHERE m.discipulador IS NOT NULL")
    Long countMembrosDiscipulados();

    @Query("SELECT m FROM Membro m " +
            "WHERE (nome LIKE :buscaGeral " +
            "OR email LIKE :buscaGeral " +
            "OR celular LIKE :buscaGeral)" +
            "AND m.igreja.idExterno = :fkIgreja "
            )
    List<Membro> findAllWithFilter(
            @Param("buscaGeral") String buscaGeral, @Param("fkIgreja") UUID fkIgreja
    );

    Membro findByEmailOrCpf(String email, String cpf);
    Membro findByEmail(String email);


    @Query("""
            SELECT new com.diacono.diacono.membro.model.dto.response.MembroSimplificadoDTO(
                mm.membro.idExterno,
                mm.membro.nome
            )
            FROM MembroMinisterio mm
            WHERE mm.ministerio.idExterno = :ministerioId
            AND mm.membro.idExterno NOT IN (
                SELECT escala.membroMinisterio.membro.idExterno
                FROM Escala escala
                WHERE escala.eventoMinisterio.evento.dataHoraInicio < :horarioFim
                AND escala.eventoMinisterio.evento.dataHoraFim > :horarioInicio
            )
            """)
    List<MembroSimplificadoDTO> findMembrosMinisteriosSemEscala(@Param("ministerioId") UUID ministerioId,
                                                                @Param("horarioInicio") LocalDateTime horarioInicio,
                                                                @Param("horarioFim") LocalDateTime horarioFim);
    @Query("""
        SELECT m.id FROM Membro m
        WHERE m.idExterno = :idExterno
    """)
    Long buscarIdPorUUID(UUID idExterno);


    Membro findAllByIgreja_IdExterno(UUID idExterno);
}
