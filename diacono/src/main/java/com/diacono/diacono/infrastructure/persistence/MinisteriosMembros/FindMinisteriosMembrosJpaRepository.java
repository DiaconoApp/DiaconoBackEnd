package com.diacono.diacono.infrastructure.persistence.MinisteriosMembros;

import com.diacono.diacono.applications.dtos.ministerio.MinisterioSuperSimplificadoDTO;
import com.diacono.diacono.domain.entity.MembroMinisterio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface FindMinisteriosMembrosJpaRepository extends JpaRepository<MembroMinisterio, Long> {

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
            AND ms.status = com.diacono.diacono.domain.enums.EnumStatusMinisterio.ATIVO
            AND m.status = com.diacono.diacono.domain.enums.EnumStatusMembro.ATIVO
            """)
    List<MinisterioSuperSimplificadoDTO> buscarMinisteriosMembro(
            @Param("idExternoMembro") UUID idExternoMembro,
            @Param("idExternoIgreja") UUID idExternoIgreja
    );
}


