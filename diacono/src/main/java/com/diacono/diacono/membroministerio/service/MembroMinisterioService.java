package com.diacono.diacono.membroministerio.service;

import com.diacono.diacono.global.error.exceptions.ObjectNotFoundException;
import com.diacono.diacono.global.error.exceptions.ObjectSaveErrorException;
import com.diacono.diacono.membro.model.entity.EnumStatusMembro;
import com.diacono.diacono.membro.model.entity.Membro;
import com.diacono.diacono.membroministerio.mapper.MembroMinisterioMapper;
import com.diacono.diacono.membroministerio.model.dto.response.MembroMinisterioDTO;
import com.diacono.diacono.membroministerio.model.entity.EnumCargoMembroMinisterio;
import com.diacono.diacono.membroministerio.model.entity.MembroMinisterio;
import com.diacono.diacono.membroministerio.repository.MembroMinisterioRepository;
import com.diacono.diacono.ministerio.model.dto.response.MinisterioSuperSimplificadoDTO;
import com.diacono.diacono.ministerio.model.entity.Ministerio;
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

    public MembroMinisterioService(MembroMinisterioRepository membroMinisterioRepository, MembroMinisterioMapper mapper) {
        this.membroMinisterioRepository = membroMinisterioRepository;
        this.mapper = mapper;
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

    public Page<MembroMinisterioDTO> buscarPorMembroMinisterioComFiltro(UUID idMinisterio, Pageable pageable, String texto, EnumStatusMembro status){

        String textoFormatado =  "%" + texto.toUpperCase() + "%" ;

        Page<MembroMinisterio> page = membroMinisterioRepository.buscarPorMembroMinisterioComFiltro(pageable,idMinisterio, textoFormatado, status);

        if(page.isEmpty()){
            throw new ObjectNotFoundException("Nenhum membro_ministerio encontrado com os filtros informados.");
        }

        //fazer o mapper para MembroMinisterioDTO
        Page<MembroMinisterioDTO> response = page.map(mapper::paraMembroMinisterioDTO);


        return response;
    }

    public Page<MembroMinisterioDTO> buscarPorMembroMinisterioSemFiltro(UUID idMinisterio, Pageable pageable){


        Page<MembroMinisterio> page = membroMinisterioRepository.buscarPorMembroMinisterioSemFiltro(pageable, idMinisterio);

        if(page.isEmpty()){
            throw new ObjectNotFoundException("Nenhum membro_ministerio encontrado com os filtros informados.");
        }

        //fazer o mapper para MembroMinisterioDTO
        Page<MembroMinisterioDTO> response = page.map(mapper::paraMembroMinisterioDTO);

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

}
