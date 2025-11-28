package com.diacono.diacono.membro.repository;

import com.diacono.diacono.membro.model.dto.response.MembroDashEvolucaoDTO;
import com.diacono.diacono.membro.model.dto.response.MembroKpiResponseDTO;
import com.diacono.diacono.membro.model.entity.Membro;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
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
        SELECT m.id FROM Membro m
        WHERE m.idExterno = :idExterno
    """)
    Long buscarIdPorUUID(UUID idExterno);


    Membro findAllByIgreja_IdExterno(UUID idExterno);

    //dashboards

    @Query("""
    SELECT new com.diacono.diacono.membro.model.dto.response.MembrosKpiResponseDTO(
        SUM(CASE WHEN m.status = com.diacono.diacono.membro.model.entity.EnumStatusMembro.ATIVO THEN 1 ELSE 0 END),
        SUM(CASE WHEN FUNCTION('YEAR', m.dataCadastro) = :anoInicio THEN 1 ELSE 0 END),
        SUM(CASE WHEN FUNCTION('YEAR', m.dataCadastro) = :anoFim THEN 1 ELSE 0 END)
    )
    FROM Membro m
    WHERE m.igreja.idExterno = :idExternoIgreja
    """)
    MembroKpiResponseDTO buscarKpisMembros(UUID idExternoIgreja, int anoInicio, int anoFim);

    @Query("""
    SELECT new com.diacono.diacono.membro.model.dto.response.MembroDashEvolucaoDTO(
        FUNCTION('YEAR', m.dataCadastro),
        COUNT(m)
    )
    FROM Membro m
        WHERE m.igreja.idExterno = :idExternoIgreja
        AND FUNCTION('YEAR', m.dataCadastro) BETWEEN :anoInicio AND :anoFim
    GROUP BY FUNCTION('YEAR', m.dataCadastro)
    ORDER BY FUNCTION('YEAR', m.dataCadastro)
    """)
    List<MembroDashEvolucaoDTO> buscarMembrosPorAno(
            UUID idExternoIgreja,
            int anoInicio,
            int anoFim
    );

}
