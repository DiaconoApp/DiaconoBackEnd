package com.diacono.diacono.usecases;

import com.diacono.diacono.domain.repository.MembroMinisterioRepository;
import com.diacono.diacono.global.error.exceptions.ObjectNotFoundException;
import com.diacono.diacono.global.error.exceptions.ObjectSaveErrorException;
import com.diacono.diacono.global.util.JwtUtils;
import com.diacono.diacono.domain.enums.EnumStatusMembro;
import com.diacono.diacono.domain.entity.Membro;
import com.diacono.diacono.applications.mappers.membro.MembroMinisterioMapper;
import com.diacono.diacono.applications.dtos.membro.MembroMinisterioInfoMembroDTO;
import com.diacono.diacono.domain.enums.EnumCargoMembroMinisterio;
import com.diacono.diacono.domain.entity.MembroMinisterio;
import com.diacono.diacono.applications.dtos.ministerio.MinisterioDashEvolucaoDTO;
import com.diacono.diacono.applications.dtos.ministerio.MinisterioDashQuantidadeMembrosDTO;
import com.diacono.diacono.applications.dtos.ministerio.MinisterioSuperSimplificadoDTO;
import com.diacono.diacono.domain.entity.Ministerio;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
public class MembroMinisterioService {

    private final MembroMinisterioRepository membroMinisterioRepository;
    private final MembroMinisterioMapper mapper;
    private final JwtUtils jwtUtils;

    public MembroMinisterioService(MembroMinisterioRepository membroMinisterioRepository, MembroMinisterioMapper mapper, JwtUtils jwtUtils) {
        this.membroMinisterioRepository = membroMinisterioRepository;
        this.mapper = mapper;
        this.jwtUtils = jwtUtils;
    }

    public void apagarMembroMinisterioPorMembro(Membro membro){
        int count = membroMinisterioRepository.deleteByMembro(membro);

    }

    public void salvarTodos(MembroMinisterio membrosMinisterios){
        MembroMinisterio membroMinisterios = membroMinisterioRepository.save(membrosMinisterios);

        if(membroMinisterios == null || membroMinisterios.getIdInterno() == null){
            throw new ObjectSaveErrorException("Nenhum membro_ministerio foi salvo.");
        }

    }

    public Page<MembroMinisterioInfoMembroDTO> buscarPorMembroMinisterioComFiltro(UUID idMinisterio, Pageable pageable, String texto, EnumStatusMembro status){

        String textoFormatado = null;
        if (texto != null && !texto.isBlank()) {
            textoFormatado = "%" + texto + "%";
        }

        Page<MembroMinisterio> page = membroMinisterioRepository.buscarPorMembroMinisterioComFiltro(pageable,idMinisterio, textoFormatado, status);

        if(page.isEmpty()){
            throw new ObjectNotFoundException("Nenhum membro_ministerio encontrado com os filtros informados.");
        }

        //fazer o mapper para MembroMinisterioDTO
        Page<MembroMinisterioInfoMembroDTO> response = page.map(mapper::paraMembroMinisterioInfoMembroDTO);


        return response;
    }

    public Page<MembroMinisterioInfoMembroDTO> buscarPorMembroMinisterioSemFiltro(UUID idMinisterio, Pageable pageable){


        Page<MembroMinisterio> page = membroMinisterioRepository.buscarPorMembroMinisterioSemFiltro(pageable, idMinisterio);

        if(page.isEmpty()){
            throw new ObjectNotFoundException("Nenhum membro_ministerio encontrado com os filtros informados.");
        }

        //fazer o mapper para MembroMinisterioDTO
        Page<MembroMinisterioInfoMembroDTO> response = page.map(mapper::paraMembroMinisterioInfoMembroDTO);

        return response;
    }

    public void adicionarMembroMinisterioLiderMinisterio(Long idMinisterio, Long idMembro){

        Ministerio ministerio = Ministerio.builder()
                .idInterno(idMinisterio)
                .build();

        Membro membro = Membro.builder()
                .idInterno(idMembro)
                .build();

        LocalDate dataHoje = LocalDate.now();

        MembroMinisterio membroMinisterio = MembroMinisterio.builder()
                .ministerio(ministerio)
                .membro(membro)
                .cargoMembro(EnumCargoMembroMinisterio.MEMBRO_MINISTERIO)
                .nomeMinisterio(ministerio.getNome())
                .dataRegistro(dataHoje)
                .build();

        MembroMinisterio membroMinisterioSalvo = membroMinisterioRepository.save(membroMinisterio);

        if(membroMinisterioSalvo.getIdInterno() == null){
            throw new ObjectSaveErrorException("Membro do ministério não foi salvo.");
        }
    }

    public void removerMembroMinisterioLiderMinisterio(UUID idMinisterio, UUID idMembro){

        int count = membroMinisterioRepository.deleteByMembroIdExternoAndMinisterioIdExterno(idMembro, idMinisterio);

        if(count == 0){
            throw new ObjectNotFoundException("Membro do ministério não encontrado para remoção.");
        }

    }

    public List<MinisterioSuperSimplificadoDTO> buscarMinisterioLider(UUID idMembro, UUID idIgreja){

        List<MinisterioSuperSimplificadoDTO> ministerios = membroMinisterioRepository.buscarMinisterioLider(idMembro, idIgreja);

        if(ministerios.isEmpty()){
            throw new ObjectNotFoundException("Nenhum ministério encontrado para o líder informado.");
        }

        return ministerios;
    }

    // Metodo usado na escala service
    public List<MembroMinisterio> buscarMembroMinisterioPorId(List<UUID> idsExternoMembroMinisterio) {
        List<MembroMinisterio> membrosMinisterio = membroMinisterioRepository
                .findAllByIdExternoIn(idsExternoMembroMinisterio);

        if(membrosMinisterio.isEmpty()){
            throw new ObjectNotFoundException("Nenhum membro_ministerio encontrado para os IDs fornecidos.");
        }

        return membrosMinisterio;
    }


    //Dash
    public List<MinisterioDashEvolucaoDTO> ministerioBuscarDashEvolucao(int anoInicio, int anoFim, UUID idMinisterio){

        UUID idIgreja = jwtUtils.getIgrejaId();

        if(anoInicio == anoFim){
            List<MinisterioDashEvolucaoDTO> response = membroMinisterioRepository.buscarDashEvolucaoUmAno(anoFim, idMinisterio, idIgreja);
            return response;
        }

        List<MinisterioDashEvolucaoDTO> response = membroMinisterioRepository.buscarDashEvolucaoPeriodo(anoInicio,anoFim, idMinisterio, idIgreja);

        return response;

    }

    public List<MinisterioDashQuantidadeMembrosDTO> ministerioBuscarDashQuantidadeMembro(int anoInicio, int anoFim){

        UUID igrejaId = jwtUtils.getIgrejaId();

        List<MinisterioDashQuantidadeMembrosDTO> response = membroMinisterioRepository.buscarQuantidadeMembros(anoInicio, anoFim, igrejaId);
        return response;

    }

}
