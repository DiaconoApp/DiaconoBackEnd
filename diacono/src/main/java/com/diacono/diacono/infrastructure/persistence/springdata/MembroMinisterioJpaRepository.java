package com.diacono.diacono.infrastructure.persistence.springdata;

import com.diacono.diacono.domain.enums.EnumStatusMembro;
import com.diacono.diacono.domain.entity.Membro;
import com.diacono.diacono.applications.dtos.ministerio.MinisterioDashEvolucaoDTO;
import com.diacono.diacono.applications.dtos.ministerio.MinisterioDashQuantidadeMembrosDTO;
import com.diacono.diacono.domain.entity.MembroMinisterio;
import com.diacono.diacono.applications.dtos.ministerio.MinisterioSuperSimplificadoDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface MembroMinisterioJpaRepository extends JpaRepository<MembroMinisterio, Long> {

    int deleteByMembro(Membro membro);

    List<MembroMinisterio> findAllByIdExternoIn(List<UUID> idsExternoMembroMinisterio);

    @Query("""
            SELECT mm FROM MembroMinisterio mm
            JOIN mm.membro m
            WHERE mm.ministerio.idExterno = :idMinisterio
            AND (:status IS NULL OR m.status = :status)
            AND (:busca IS NULL OR m.buscaTokens LIKE CONCAT('%', :busca, '%'))
                  """)
    Page<MembroMinisterio> buscarPorMembroMinisterioComFiltro(
            Pageable pageable,
            @Param("idMinisterio") UUID idMinisterio,
            @Param("busca") String texto,
            @Param("status") EnumStatusMembro status
    );

    @Query("""
            SELECT mm FROM MembroMinisterio mm
            JOIN mm.membro m
            WHERE mm.ministerio.idExterno = :idMinisterio
            """)
    Page<MembroMinisterio> buscarPorMembroMinisterioSemFiltro(
            Pageable pageable,
            @Param("idMinisterio") UUID idMinisterio
    );

    int deleteByMembroIdExternoAndMinisterioIdExterno(UUID membroIdExterno, UUID ministerioIdExterno);

    @Query("""
          SELECT DISTINCT new com.diacono.diacono.applications.dtos.ministerio.MinisterioSuperSimplificadoDTO(
                ms.idExterno,
                ms.nome
            )
            FROM MembroMinisterio mm
            JOIN mm.ministerio ms
            JOIN mm.membro m
            WHERE m.idExterno = :idExternoMembro
            AND ms.igreja.idExterno = :idExternoIgreja
            AND mm.cargoMembro = com.diacono.diacono.domain.enums.EnumCargoMembroMinisterio.MEMBRO_MINISTERIO
            AND m.igreja.idExterno = :idExternoIgreja
            """)
    List<MinisterioSuperSimplificadoDTO> buscarMembro(
            @Param("idExternoMembro") UUID idExternoMembro,
            @Param("idExternoIgreja") UUID idExternoIgreja
    );

    @Query("""
          SELECT DISTINCT new com.diacono.diacono.applications.dtos.ministerio.MinisterioSuperSimplificadoDTO(
                ms.idExterno,
                ms.nome
            )
            FROM MembroMinisterio mm
            JOIN mm.ministerio ms
            JOIN mm.membro m
            WHERE m.idExterno = :idExternoMembro
            AND ms.igreja.idExterno = :idExternoIgreja
            AND m.igreja.idExterno = :idExternoIgreja
            AND mm.cargoMembro = com.diacono.diacono.domain.enums.EnumCargoMembroMinisterio.LIDER_MINISTERIO
            AND ms.status = com.diacono.diacono.domain.enums.EnumStatusMinisterio.ATIVO
            AND m.status = com.diacono.diacono.domain.enums.EnumStatusMembro.ATIVO
            """)
    List<MinisterioSuperSimplificadoDTO> buscarMinisterioLider(
            @Param("idExternoMembro") UUID idExternoMembro,
            @Param("idExternoIgreja") UUID idExternoIgreja
    );


    //DASH

    @Query("""
            SELECT new com.diacono.diacono.applications.dtos.ministerio.MinisterioDashEvolucaoDTO(
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
    List<MinisterioDashEvolucaoDTO> buscarDashEvolucaoUmAno(
            @Param("anoFim") int anoFim,
            @Param("idMinisterio") UUID idMinisterio,
            @Param("idIgreja") UUID idIgreja
    );

    @Query("""
            SELECT new com.diacono.diacono.applications.dtos.ministerio.MinisterioDashEvolucaoDTO(
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
    List<MinisterioDashEvolucaoDTO> buscarDashEvolucaoPeriodo(
            @Param("anoInicio") int anoInicio,
            @Param("anoFim") int anoFim,
            @Param("idMinisterio") UUID idMinisterio,
            @Param("idIgreja") UUID idIgreja
    );

    @Query("""
            SELECT new com.diacono.diacono.applications.dtos.ministerio.MinisterioDashQuantidadeMembrosDTO(
                m.nome,
                COUNT(mb)
            )
            FROM MembroMinisterio mb
            JOIN mb.ministerio m
            WHERE m.igreja.idExterno = :idIgreja
            AND FUNCTION('YEAR', mb.dataRegistro) BETWEEN :anoInicio AND :anoFim
            GROUP BY m.nome

            """)
    List<MinisterioDashQuantidadeMembrosDTO> buscarQuantidadeMembros(
            @Param("anoInicio") int anoInicio,
            @Param("anoFim") int anoFim,
            @Param("idIgreja") UUID igrejaId
    );

}
