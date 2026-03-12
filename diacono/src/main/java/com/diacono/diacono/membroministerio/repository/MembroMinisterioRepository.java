package com.diacono.diacono.membroministerio.repository;

import com.diacono.diacono.membro.model.entity.EnumStatusMembro;
import com.diacono.diacono.membro.model.entity.Membro;
import com.diacono.diacono.membroministerio.model.dto.response.MinisterioDashEvolucaoDTO;
import com.diacono.diacono.membroministerio.model.dto.response.MinisterioDashQuantidadeMembrosDTO;
import com.diacono.diacono.membroministerio.model.entity.MembroMinisterio;
import com.diacono.diacono.ministerio.model.dto.response.MinisterioSuperSimplificadoDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface MembroMinisterioRepository extends JpaRepository<MembroMinisterio, Long> {

    int deleteByMembro(Membro membro);

    List<MembroMinisterio> findAllByIdExternoIn(List<UUID> idsExternoMembroMinisterio);

    // OWASP A01: query sensivel filtrada por ministerio; contexto de tenant valida em camada superior.
    @Query("""
            SELECT mm FROM MembroMinisterio mm
            JOIN mm.membro m
            WHERE mm.ministerio.idExterno = :idMinisterio
            AND (:status IS NULL OR m.status = :status)
            AND (:busca IS NULL OR
            LOWER(m.nome) LIKE LOWER(CONCAT('%', :busca, '%'))
            OR LOWER(m.email) LIKE LOWER(CONCAT('%', :busca, '%')))
            """)
    Page<MembroMinisterio> buscarPorMembroMinisterioComFiltro(Pageable pageable, @Param("idMinisterio") UUID idMinisterio, @Param("busca") String texto, @Param("status") EnumStatusMembro status);

    // OWASP A01: query basica com filtro de ministerio.
    @Query("""
            SELECT mm FROM MembroMinisterio mm
            JOIN mm.membro m
            WHERE mm.ministerio.idExterno = :idMinisterio
            """)
    Page<MembroMinisterio> buscarPorMembroMinisterioSemFiltro(Pageable pageable, @Param("idMinisterio") UUID idMinisterio);

    // OWASP A01: delecao protegida por chave composta membro+ministerio.
    int deleteByMembroIdExternoAndMinisterioIdExterno(UUID membroIdExterno, UUID ministerioIdExterno);

    // OWASP A01: consulta sensivel com triplo filtro: membro, ministerio e igreja.
    @Query("""
            SELECT new com.diacono.diacono.ministerio.model.dto.response.MinisterioSuperSimplificadoDTO(
                ms.idExterno,
                ms.nome
            )
            FROM MembroMinisterio mm
            JOIN mm.ministerio ms
            JOIN mm.membro m
            WHERE m.idExterno = :idExternoMembro
            AND ms.igreja.idExterno = :idExternoIgreja
            AND mm.cargoMembro = 'LIDER_MINISTERIO'
            """)
    List<MinisterioSuperSimplificadoDTO> buscarMinisterioLider(@Param("idExternoMembro") UUID idExternoMembro, @Param("idExternoIgreja") UUID idExternoIgreja);

    // DASH - OWASP A01: queries de dashboard com escopo explicito por igreja.

    // OWASP A01: agregacao sensivel restrita ao ano especifico e igreja autenticada.
    @Query("""
            SELECT new com.diacono.diacono.membroministerio.model.dto.response.MinisterioDashEvolucaoDTO(
                COUNT(mb),
                mb.dataRegistro
            )
            FROM MembroMinisterio mb
            JOIN mb.ministerio m
            WHERE m.igreja.idExterno = :idIgreja
            AND (:idMinisterio IS NULL OR m.idExterno = :idMinisterio)
            AND FUNCTION('YEAR', mb.dataRegistro) = :anoFim
            GROUP BY mb.dataRegistro
            ORDER BY mb.dataRegistro ASC
            """)
    List<MinisterioDashEvolucaoDTO> buscarDashEvolucaoUmAno(@Param("anoFim") int anoFim, @Param("idMinisterio") UUID idMinisterio, @Param("idIgreja") UUID idIgreja);

    // OWASP A01: agregacao sensivel restrita ao intervalo de anos e igreja autenticada.
    @Query("""
            SELECT new com.diacono.diacono.membroministerio.model.dto.response.MinisterioDashEvolucaoDTO(
                COUNT(mb),
                mb.dataRegistro
            )
            FROM MembroMinisterio mb
            JOIN mb.ministerio m
            WHERE m.igreja.idExterno = :idIgreja
            AND (:idMinisterio IS NULL OR m.idExterno = :idMinisterio)
            AND FUNCTION('YEAR', mb.dataRegistro) BETWEEN :anoInicio AND :anoFim
            GROUP BY mb.dataRegistro
            ORDER BY mb.dataRegistro ASC
            """)
    List<MinisterioDashEvolucaoDTO> buscarDashEvolucaoPeriodo(@Param("anoInicio") int anoInicio, @Param("anoFim") int anoFim, @Param("idMinisterio") UUID idMinisterio, @Param("idIgreja") UUID idIgreja);

    // OWASP A01: agregacao de quantidade de membros por ministerio restrita a igreja.
    @Query("""
            SELECT new com.diacono.diacono.membroministerio.model.dto.response.MinisterioDashQuantidadeMembrosDTO(
                m.nome,
                COUNT(mb)
            )
            FROM MembroMinisterio mb
            JOIN mb.ministerio m
            WHERE m.igreja.idExterno = :idIgreja
            AND FUNCTION('YEAR', mb.dataRegistro) BETWEEN :anoInicio AND :anoFim
            GROUP BY m.nome
            """)
    List<MinisterioDashQuantidadeMembrosDTO> buscarQuantidadeMembros(@Param("anoInicio") int anoInicio, @Param("anoFim") int anoFim, @Param("idIgreja") UUID idIgreja);

}
