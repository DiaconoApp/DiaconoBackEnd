package com.diacono.diacono.infrastructure.persistence.springdata;

import com.diacono.diacono.applications.dtos.membro.MembroDashEvolucaoDTO;
import com.diacono.diacono.applications.dtos.membro.MembroDashFaixaEtariaDTO;
import com.diacono.diacono.applications.dtos.membro.MembroDashGeneroDTO;
import com.diacono.diacono.applications.dtos.membro.MembroKpiResponseDTO;
import com.diacono.diacono.domain.entity.Membro;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface MembroJpaRepository extends JpaRepository<Membro, Long> {

    /**
     * @deprecated Sem scoping por igreja — sujeito a IDOR cross-tenant.
     * Usar {@link #findByIdExternoAndIgrejaIdExterno(UUID, UUID)} em substituição.
     */
    @Deprecated
    Membro findByIdExterno(UUID idExterno);

    /**
     * Busca membro por UUID garantindo que pertence à igreja informada.
     */
    Optional<Membro> findByIdExternoAndIgrejaIdExterno(UUID idExterno, UUID igrejaIdExterno);

    Page<Membro> findByIgreja_IdExterno(UUID fkIgreja, Pageable pageable);

    @Query("SELECT COUNT(m) FROM Membro m WHERE m.status = 'ATIVO'")
    Long countMembroStatusIgualAtivo();

    @Query("SELECT COUNT(m) FROM Membro m WHERE SIZE(m.ministerios) > 0")
    Long countMembrosComMinisterio();

    @Query("SELECT COUNT(m) FROM Membro m WHERE m.discipulador IS NOT NULL")
    Long countMembrosDiscipulados();

    @Query("""
            SELECT m FROM Membro m
            WHERE
            (:buscaGeral IS NULL OR
            LOWER(nome) LIKE LOWER(:buscaGeral)
            OR LOWER(email) LIKE LOWER(:buscaGeral)
            OR LOWER(celular) LIKE LOWER(:buscaGeral))
            AND m.igreja.idExterno = :fkIgreja
            ORDER BY nome
            """
    )
    List<Membro> findAllWithFilter(
            @Param("buscaGeral") String buscaGeral, @Param("fkIgreja") UUID fkIgreja
    );

    Membro findByEmailOrCpf(String email, String cpf);

    Membro findByEmail(String email);


//    @Query("""
//            SELECT new com.diacono.diacono.applications.dtos.membro.MembroSimplificadoDTO(
//                mm.membro.idExterno,
//                mm.membro.nome
//            )
//            FROM MembroMinisterio mm
//            WHERE mm.ministerio.idExterno = :ministerioId
//            AND mm.membro.idExterno NOT IN (
//                SELECT escala.membroMinisterio.membro.idExterno
//                FROM Escala escala
//                WHERE escala.eventoMinisterio.evento.dataHoraInicio < :horarioFim
//                AND escala.eventoMinisterio.evento.dataHoraFim > :horarioInicio
//            )
//            """)
//    List<MembroSimplificadoDTO> findMembrosMinisteriosSemEscala(@Param("ministerioId") UUID ministerioId,
//                                                                @Param("horarioInicio") LocalDateTime horarioInicio,
//                                                                @Param("horarioFim") LocalDateTime horarioFim);

    /**
     * @deprecated Sem scoping por igreja — expõe ID interno de qualquer tenant por UUID conhecido.
     * Usar {@link #buscarIdPorUUIDAndIgrejaIdExterno(UUID, UUID)} em substituição.
     */
    @Deprecated
    @Query("""
                SELECT m.id FROM Membro m
                WHERE m.idExterno = :idExterno
            """)
    Long buscarIdPorUUID(UUID idExterno);

    /**
     * Busca ID interno do membro garantindo que pertence à igreja informada.
     * Previne exposição cross-tenant de IDs internos.
     */
    @Query("""
                SELECT m.id FROM Membro m
                WHERE m.idExterno = :idExterno
                AND m.igreja.idExterno = :igrejaId
            """)
    Long buscarIdPorUUIDAndIgrejaIdExterno(
            @Param("idExterno") UUID idExterno,
            @Param("igrejaId") UUID igrejaId
    );


    /**
     * @deprecated Retorna membro de qualquer igreja por UUID — sujeito a IDOR.
     * Sem scoping por igreja. Não utilize em novos desenvolvimentos.
     * Usar {@link #findByIdExternoAndIgrejaIdExterno(UUID, UUID)} em substituição.
     */
    @Deprecated
    Membro findAllByIgreja_IdExterno(UUID idExterno);

    //dashboards

    @Query("""
            SELECT new com.diacono.diacono.applications.dtos.membro.MembroKpiResponseDTO(
                SUM(CASE WHEN m.status = com.diacono.diacono.domain.enums.EnumStatusMembro.ATIVO AND FUNCTION('YEAR', m.dataRegistro) BETWEEN :anoInicio AND :anoFim  THEN 1 ELSE 0 END),
                SUM(CASE WHEN m.status = com.diacono.diacono.domain.enums.EnumStatusMembro.INATIVO AND FUNCTION('YEAR', m.dataRegistro) BETWEEN :anoInicio AND :anoFim  THEN 1 ELSE 0 END),
                SUM(CASE WHEN FUNCTION('YEAR', m.dataRegistro) = :anoInicio THEN 1 ELSE 0 END),
                SUM(CASE WHEN FUNCTION('YEAR', m.dataRegistro) = :anoFim THEN 1 ELSE 0 END)
            )
            FROM Membro m
            WHERE m.igreja.idExterno = :idExternoIgreja
            """)
    MembroKpiResponseDTO buscarKpisMembros(
            @Param("idExternoIgreja") UUID idExternoIgreja,
            @Param("anoInicio") int anoInicio,
            @Param("anoFim") int anoFim
    );

    @Query("""
            SELECT new com.diacono.diacono.applications.dtos.membro.MembroDashEvolucaoDTO(
                m.dataRegistro,
                COUNT(m.idInterno)
            )
            FROM Membro m
            WHERE m.igreja.idExterno = :idExternoIgreja
              AND FUNCTION('YEAR', m.dataRegistro) BETWEEN :anoInicio AND :anoFim
            GROUP BY m.dataRegistro
            ORDER BY m.dataRegistro
            """)
    List<MembroDashEvolucaoDTO> buscarMembrosPorAno(
            @Param("idExternoIgreja") UUID idExternoIgreja,
            @Param("anoInicio") int anoInicio,
            @Param("anoFim") int anoFim
    );

    @Query("""
            SELECT new com.diacono.diacono.applications.dtos.membro.MembroDashFaixaEtariaDTO(
                SUM(CASE WHEN (:anoFim - YEAR(m.dataNascimento)) BETWEEN 0 AND 11 THEN 1 ELSE 0 END),
                SUM(CASE WHEN (:anoFim - YEAR(m.dataNascimento)) BETWEEN 12 AND 17 THEN 1 ELSE 0 END),
                SUM(CASE WHEN (:anoFim - YEAR(m.dataNascimento)) BETWEEN 18 AND 29 THEN 1 ELSE 0 END),
                SUM(CASE WHEN (:anoFim - YEAR(m.dataNascimento)) BETWEEN 30 AND 59 THEN 1 ELSE 0 END),
                SUM(CASE WHEN (:anoFim - YEAR(m.dataNascimento)) >= 60 THEN 1 ELSE 0 END)
            )
            FROM Membro m
                WHERE m.igreja.idExterno = :idExternoIgreja
            """)
    MembroDashFaixaEtariaDTO buscarMembrosPorFaixaEtaria(
            @Param("idExternoIgreja") UUID idExternoIgreja,
            @Param("anoFim") int anoFim
    );

    @Query("""
            SELECT new com.diacono.diacono.applications.dtos.membro.MembroDashGeneroDTO(
                SUM(CASE WHEN m.generoMembro = 'MASCULINO' THEN 1 ELSE 0 END),
                SUM(CASE WHEN m.generoMembro = 'FEMININO' THEN 1 ELSE 0 END)
            )
            FROM Membro m
                WHERE m.igreja.idExterno = :idExternoIgreja AND FUNCTION('YEAR', m.dataRegistro) <=  :anoFim
            """)
    MembroDashGeneroDTO buscarMembrosPorGenero(
            @Param("idExternoIgreja") UUID idExternoIgreja,
            @Param("anoFim") int anoFim
    );

}
