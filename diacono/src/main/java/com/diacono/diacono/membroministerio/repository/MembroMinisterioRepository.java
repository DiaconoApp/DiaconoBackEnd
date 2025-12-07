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

    @Query("""
            SELECT mm FROM MembroMinisterio mm
            JOIN mm.membro m
            WHERE mm.ministerio.idExterno = :idMinisterio
              AND (:status IS NULL OR m.status = :status)
            AND (:busca IS NULL OR m.nome LIKE :busca
                 OR m.email LIKE :busca)
            """)
    Page<MembroMinisterio> buscarPorMembroMinisterioComFiltro(Pageable pageable, UUID idMinisterio, String texto, EnumStatusMembro status);

    @Query("""
            SELECT mm FROM MembroMinisterio mm
            JOIN mm.membro m
            WHERE mm.ministerio.idExterno = :idMinisterio
            """)
    Page<MembroMinisterio> buscarPorMembroMinisterioSemFiltro(Pageable pageable, UUID idMinisterio);

    int deleteByMembroIdExternoAndMinisterioIdExterno(UUID membroIdExterno, UUID ministerioIdExterno);


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
    List<MinisterioSuperSimplificadoDTO> buscarMinisterioLider(UUID idExternoMembro, UUID idExternoIgreja);


    //DASH

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
    List<MinisterioDashEvolucaoDTO> buscarDashEvolucaoUmAno(int anoFim, UUID idMinisterio, UUID idIgreja);

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
    List<MinisterioDashEvolucaoDTO> buscarDashEvolucaoPeriodo(int anoInicio, int anoFim, UUID idMinisterio, UUID idIgreja);

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
    List<MinisterioDashQuantidadeMembrosDTO> buscarQuantidadeMembros(int anoInicio, int anoFim, @Param("idIgreja") UUID igrejaId);

}
