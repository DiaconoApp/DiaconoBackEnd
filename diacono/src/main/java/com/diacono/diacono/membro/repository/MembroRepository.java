package com.diacono.diacono.membro.repository;

import com.diacono.diacono.membro.model.dto.response.MembroDashEvolucaoDTO;
import com.diacono.diacono.membro.model.dto.response.MembroDashFaixaEtariaDTO;
import com.diacono.diacono.membro.model.dto.response.MembroDashGeneroDTO;
import com.diacono.diacono.membro.model.dto.response.MembroKpiResponseDTO;
import com.diacono.diacono.membro.model.entity.Membro;
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
public interface MembroRepository extends JpaRepository<Membro, Long> {

    // OWASP A01: método legado sem escopo por igreja; prefira findByIgreja_IdExternoAndIdExterno.
    @Deprecated
    Membro findByIdExterno(UUID idExterno);

    // OWASP A01: leitura segura por UUID + igreja para evitar acesso cross-tenant.
    Optional<Membro> findByIgreja_IdExternoAndIdExterno(UUID fkIgreja, UUID idExterno);

    Page<Membro> findByIgreja_IdExterno(UUID fkIgreja, Pageable pageable);

    // OWASP A01: contagem legada global; prefira countMembroStatusIgualAtivoByIgreja para escopo tenant.
    @Deprecated
    @Query("SELECT COUNT(m) FROM Membro m WHERE m.status = 'ATIVO'")
    Long countMembroStatusIgualAtivo();

    // OWASP A01: contagem segura por igreja autenticada.
    @Query("SELECT COUNT(m) FROM Membro m WHERE m.igreja.idExterno = :fkIgreja AND m.status = 'ATIVO'")
    Long countMembroStatusIgualAtivoByIgreja(@Param("fkIgreja") UUID fkIgreja);

    // OWASP A01: contagem legada global; prefira countMembrosComMinisterioByIgreja para escopo tenant.
    @Deprecated
    @Query("SELECT COUNT(m) FROM Membro m WHERE SIZE(m.ministerios) > 0")
    Long countMembrosComMinisterio();

    // OWASP A01: contagem segura por igreja autenticada.
    @Query("SELECT COUNT(m) FROM Membro m WHERE m.igreja.idExterno = :fkIgreja AND SIZE(m.ministerios) > 0")
    Long countMembrosComMinisterioByIgreja(@Param("fkIgreja") UUID fkIgreja);

    // OWASP A01: contagem legada global; prefira countMembrosDiscipuladosByIgreja para escopo tenant.
    @Deprecated
    @Query("SELECT COUNT(m) FROM Membro m WHERE m.discipulador IS NOT NULL")
    Long countMembrosDiscipulados();

    // OWASP A01: contagem segura por igreja autenticada.
    @Query("SELECT COUNT(m) FROM Membro m WHERE m.igreja.idExterno = :fkIgreja AND m.discipulador IS NOT NULL")
    Long countMembrosDiscipuladosByIgreja(@Param("fkIgreja") UUID fkIgreja);

    @Query("""
            SELECT m FROM Membro m
            WHERE
            (:buscaGeral IS NULL OR
            LOWER(m.nome) LIKE LOWER(:buscaGeral)
            OR LOWER(m.email) LIKE LOWER(:buscaGeral)
            OR LOWER(m.celular) LIKE LOWER(:buscaGeral))
            AND m.igreja.idExterno = :fkIgreja
            ORDER BY m.nome
            """)
    List<Membro> findAllWithFilter(
            @Param("buscaGeral") String buscaGeral, @Param("fkIgreja") UUID fkIgreja
    );

    // OWASP A01: método legado sem escopo; prefira findByEmailOrCpfAndIgreja_IdExterno quando aplicável.
    @Deprecated
    Membro findByEmailOrCpf(String email, String cpf);

    // OWASP A01: valida existência por email/cpf no escopo da igreja.
    Membro findByEmailOrCpfAndIgreja_IdExterno(String email, String cpf, UUID fkIgreja);

    // OWASP A01: método legado sem escopo; útil para login global, prefira scoped em fluxos internos.
    @Deprecated
    Membro findByEmail(String email);

    // OWASP A01: leitura segura por email + igreja para evitar vazamento cross-tenant.
    Optional<Membro> findByIgreja_IdExternoAndEmail(UUID fkIgreja, String email);

//    @Query("""
//            SELECT new com.diacono.diacono.membro.model.dto.response.MembroSimplificadoDTO(
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

    // OWASP A01: consulta legada sem escopo por igreja; prefira buscarIdPorUUIDAndIgreja.
    @Deprecated
    @Query("""
                SELECT m.idInterno FROM Membro m
                WHERE m.idExterno = :idExterno
            """)
    Long buscarIdPorUUID(@Param("idExterno") UUID idExterno);

    // OWASP A01: busca segura do ID interno no contexto da igreja.
    @Query("""
                SELECT m.idInterno FROM Membro m
                WHERE m.idExterno = :idExterno
                AND m.igreja.idExterno = :fkIgreja
            """)
    Long buscarIdPorUUIDAndIgreja(@Param("idExterno") UUID idExterno,
                                  @Param("fkIgreja") UUID fkIgreja);

    // OWASP A01: método legado com retorno único; prefira variantes paginadas/filtradas por igreja.
    @Deprecated
    Membro findAllByIgreja_IdExterno(UUID idExterno);

    // dashboards

    @Query("""
            SELECT new com.diacono.diacono.membro.model.dto.response.MembroKpiResponseDTO(
                SUM(CASE WHEN m.status = com.diacono.diacono.membro.model.entity.EnumStatusMembro.ATIVO AND FUNCTION('YEAR', m.dataRegistro) BETWEEN :anoInicio AND :anoFim  THEN 1 ELSE 0 END),
                SUM(CASE WHEN m.status = com.diacono.diacono.membro.model.entity.EnumStatusMembro.INATIVO AND FUNCTION('YEAR', m.dataRegistro) BETWEEN :anoInicio AND :anoFim  THEN 1 ELSE 0 END),
                SUM(CASE WHEN FUNCTION('YEAR', m.dataRegistro) = :anoInicio THEN 1 ELSE 0 END),
                SUM(CASE WHEN FUNCTION('YEAR', m.dataRegistro) = :anoFim THEN 1 ELSE 0 END)
            )
            FROM Membro m
            WHERE m.igreja.idExterno = :idExternoIgreja
            """)
    MembroKpiResponseDTO buscarKpisMembros(UUID idExternoIgreja, int anoInicio, int anoFim);

    @Query("""
            SELECT new com.diacono.diacono.membro.model.dto.response.MembroDashEvolucaoDTO(
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
            SELECT new com.diacono.diacono.membro.model.dto.response.MembroDashFaixaEtariaDTO(
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
            SELECT new com.diacono.diacono.membro.model.dto.response.MembroDashGeneroDTO(
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
