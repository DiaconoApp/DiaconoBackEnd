package com.diacono.diacono.ministerio.service;

import com.diacono.diacono.global.dto.response.RestResponseMessage;
import com.diacono.diacono.global.error.exceptions.FieldInvalidException;
import com.diacono.diacono.global.error.exceptions.ObjectNotFoundException;
import com.diacono.diacono.global.util.JwtUtils;
import com.diacono.diacono.membro.model.entity.EnumCargoMembro;
import com.diacono.diacono.membro.model.entity.EnumStatusMembro;
import com.diacono.diacono.membro.model.entity.Membro;
import com.diacono.diacono.membro.repository.MembroRepository;
import com.diacono.diacono.membroministerio.model.dto.request.MembroMinisterioCreateDTO;
import com.diacono.diacono.membroministerio.model.dto.response.MembroMinisterioDTO;
import com.diacono.diacono.membroministerio.model.entity.EnumCargoMembroMinisterio;
import com.diacono.diacono.membroministerio.model.entity.MembroMinisterio;
import com.diacono.diacono.membroministerio.service.MembroMinisterioService;
import com.diacono.diacono.ministerio.mapper.MinisterioMapper;
import com.diacono.diacono.ministerio.model.dto.MinisterioCreateDTO;
import com.diacono.diacono.ministerio.model.dto.MinisterioUpdateDTO;
import com.diacono.diacono.ministerio.model.dto.response.MinisterioKpisResponseDTO;
import com.diacono.diacono.ministerio.model.dto.response.MinisterioSimplificadoDTO;
import com.diacono.diacono.ministerio.model.entity.EnumStatusMinisterio;
import com.diacono.diacono.ministerio.model.entity.Ministerio;
import com.diacono.diacono.ministerio.repository.MinisteriosRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.*;

@Service
public class MinisterioService {

    private final MinisteriosRepository ministerios;
    private final MinisterioMapper mapper;
    private final MembroMinisterioService membroMinisterio;
    private final MembroRepository membro;
    private final JwtUtils jwtUtils;

    public MinisterioService(MinisteriosRepository ministerios, MinisterioMapper mapper, MembroMinisterioService membroMinisterio, MembroRepository membro, JwtUtils jwtUtils) {
        this.ministerios = ministerios;
        this.mapper = mapper;
        this.membroMinisterio = membroMinisterio;
        this.membro = membro;
        this.jwtUtils = jwtUtils;
    }

    //METODOS PRINCIPAIS

    //USO GERAL

    public List<MinisterioSimplificadoDTO> buscarMinisteriosGerais(){

        List<Ministerio> ministeriosResponse = ministerios.findByIgreja_IdExterno(jwtUtils.getIgrejaId());
        if(ministeriosResponse.isEmpty()){
            throw new ObjectNotFoundException("Nenhum ministério encontrado");

        }

        List<MinisterioSimplificadoDTO> responses = ministeriosResponse.stream()
                .map(mapper::paraMinisterioSimplificadoDTO)
                .toList();

        return responses;

    }

    //VISAO GOVERNO

    public Page<MinisterioSimplificadoDTO> buscarMinisteriosGoverno(Pageable pageable){

        Page<Ministerio> ministeriosPage = ministerios.findByIgreja_IdExterno(jwtUtils.getIgrejaId(),pageable);
        if(ministeriosPage.isEmpty()){
            throw new ObjectNotFoundException("Nenhum ministério encontrado");
        }

        //mapper::paraMinisterioSimplificadoDTO == (m -> mapper.paraMinisterioSimplificadoDTO(m))
        Page<MinisterioSimplificadoDTO> responses = ministeriosPage.map(mapper::paraMinisterioSimplificadoDTO);

        return responses;

    }

    public Page<MinisterioSimplificadoDTO> buscarMinisteriosGovernoComFiltro(Pageable pageable, String buscaGeral, EnumStatusMinisterio status){

        String stringBusca = "%" + buscaGeral.trim().toUpperCase() + "%";

        Page<Ministerio> ministeriosPage = ministerios.buscarComFiltros(pageable, stringBusca, status, jwtUtils.getIgrejaId());
        if(ministeriosPage.isEmpty()){
            throw new ObjectNotFoundException("Nenhum ministério encontrado");
        }

        //mapper::paraMinisterioSimplificadoDTO == (m -> mapper.paraMinisterioSimplificadoDTO(m))
        Page<MinisterioSimplificadoDTO> responses = ministeriosPage
                .map(mapper::paraMinisterioSimplificadoDTO);

        return responses;

    }

    @Transactional
    public RestResponseMessage criarMinisterio(MinisterioCreateDTO ministerioDTO){

        //buscar lider primeiro para adicionar como membro do ministério
        Membro liderMinisterio = membro.findByIdExterno(ministerioDTO.idLider());

        if(liderMinisterio == null){
            throw new ObjectNotFoundException("Líder do ministério não encontrado");
        }

        if(liderMinisterio.getCargoMembro() != EnumCargoMembro.LIDER_MINISTERIO){
            liderMinisterio.setCargoMembro(EnumCargoMembro.LIDER_MINISTERIO);
        }

        LocalDate data = LocalDate.now();
        EnumStatusMinisterio status = EnumStatusMinisterio.ATIVO;

        Ministerio novoMinisterio = Ministerio.builder()
                        .nome(ministerioDTO.nome())
                        .dataCriacao(data)
                        .igreja(liderMinisterio.getIgreja())
                        .nomeLider(liderMinisterio.getNome())
                        .status(status)
                        .build();

        MembroMinisterio membroLider = MembroMinisterio.builder()
                .membro(liderMinisterio)
                .ministerio(novoMinisterio)
                .cargoMembro(EnumCargoMembroMinisterio.LIDER_MINISTERIO)
                .nomeMinisterio(novoMinisterio.getNome())
                .build();

        Set<MembroMinisterio> membroMinisterios = novoMinisterio.getMembros();

        if(membroMinisterios == null){
            membroMinisterios = new HashSet<>();
        }

        membroMinisterios.add(membroLider);
        novoMinisterio.setMembros(membroMinisterios);
        ministerios.save(novoMinisterio);

        return new RestResponseMessage(HttpStatus.CREATED, "Ministério criado com sucesso");

    }

    @Transactional
    public RestResponseMessage editarMinisterio(MinisterioUpdateDTO ministerioDTO, UUID idMinisterio){

        Ministerio ministerioExistente = ministerios.findByIdExterno(idMinisterio);

        if(ministerioExistente == null){
            throw new ObjectNotFoundException("Ministério não encontrado");
        }

        if(ministerioDTO.idLider() != null) {
            Membro liderNovo = membro.findByIdExterno(ministerioDTO.idLider());

            if (liderNovo == null) {
                throw new ObjectNotFoundException("Novo líder não encontrado");
            }

            MembroMinisterio atual = ministerioExistente.getMembros().stream()
                    .filter(m -> m.getCargoMembro() == EnumCargoMembroMinisterio.LIDER_MINISTERIO)
                    .findFirst()
                    .orElseThrow(() -> new ObjectNotFoundException("Líder do ministério não encontrado"));

            Membro liderAntigo = atual.getMembro();

            MembroMinisterio liderNovoExisteNoMinisterio = ministerioExistente.getMembros().stream()
                    .filter(m -> m.getMembro().getIdExterno().equals(liderNovo.getIdExterno()))
                    .findFirst()
                    .orElse(null);


            if(liderNovoExisteNoMinisterio == null){

                MembroMinisterio novoMembroMinisterio = MembroMinisterio.builder()
                        .membro(liderNovo)
                        .ministerio(ministerioExistente)
                        .cargoMembro(EnumCargoMembroMinisterio.LIDER_MINISTERIO)
                        .nomeMinisterio(ministerioExistente.getNome())
                        .build();

                ministerioExistente.getMembros().add(novoMembroMinisterio);

            }else{
                liderNovoExisteNoMinisterio.setCargoMembro(EnumCargoMembroMinisterio.LIDER_MINISTERIO);
                atual.setCargoMembro(EnumCargoMembroMinisterio.MEMBRO_MINISTERIO);
            }

            ministerioExistente.setNomeLider(liderNovo.getNome());

            boolean aindaELiderDeAlgumMinisterio = liderAntigo.getMinisterios().stream()
                    .anyMatch(mm ->
                            mm.getCargoMembro() == EnumCargoMembroMinisterio.LIDER_MINISTERIO
                                    && !mm.getMinisterio().getIdExterno().equals(ministerioExistente.getIdExterno())
                    );

            if (!aindaELiderDeAlgumMinisterio) {
                liderAntigo.setCargoMembro(EnumCargoMembro.MEMBRO);
            }

        }

        if(ministerioDTO.nome() != null && !ministerioDTO.nome().isBlank()){
            ministerioExistente.setNome(ministerioDTO.nome());
        }

        if(ministerioDTO.status() != null){
            ministerioExistente.setStatus(ministerioDTO.status());
        }

        ministerios.save(ministerioExistente);

        return new RestResponseMessage(HttpStatus.OK, "Ministério atualizado com sucesso");
    }

    //VISAO LIDER MINISTERIO

    public Page<MembroMinisterioDTO> buscarMembroMinisterioLiderMinisterio(UUID idMinisterio, Pageable page){
        return membroMinisterio.buscarPorMembroMinisterioSemFiltro(idMinisterio, page);
    }

    public Page<MembroMinisterioDTO> buscarMembroMinisterioLiderMinisterioComFiltro(UUID idMinisterio, Pageable page, String texto, EnumStatusMembro status){
        return membroMinisterio.buscarPorMembroMinisterioComFiltro(idMinisterio, page, texto, status);
    }

    @Transactional
    public RestResponseMessage adicionarMembroMinisterioLiderMinisterio(UUID idMinisterio, MembroMinisterioCreateDTO dto){

        if(dto == null){
            throw new FieldInvalidException("Dados do membro do ministério não podem ser nulos");
        }

        Long idMinisterioNovo = ministerios.buscarIdPorUUID(idMinisterio);

        if(idMinisterioNovo == null){
            throw new ObjectNotFoundException("Ministério não encontrado");
        }

        Long idMembroNovo = membro.buscarIdPorUUID(dto.idExterno());

        if(idMembroNovo == null){
            throw new ObjectNotFoundException("Membro não encontrado");
        }

        membroMinisterio.adicionarMembroMinisterioLiderMinisterio(idMinisterioNovo, idMembroNovo);

        return new RestResponseMessage(HttpStatus.OK, "Membro adicionado ao ministério com sucesso");
    }

    @Transactional
    public RestResponseMessage removerMembroMinisterioLiderMinisterio(UUID idMinisterio, UUID idMembroMinisterio){

        membroMinisterio.removerMembroMinisterioLiderMinisterio(idMinisterio, idMembroMinisterio);

        return new RestResponseMessage(HttpStatus.OK, "Membro removido do ministério com sucesso");
    }


    /*ESSE METODO SE RELACIONA COM EVENTO*/

    public Set<Ministerio> buscarPorUUID(List<UUID> idExterno){
        Set<Ministerio> ministerios = this.ministerios.findAllByIdExternoIn(idExterno);

        if (ministerios.isEmpty()) {
            throw new ObjectNotFoundException("Ministérios não encontrados");
        }

        return ministerios;
    }

    public Ministerio buscarPorUUID(UUID idExterno){
        Ministerio ministerios = this.ministerios.findByIdExterno(idExterno);

        if (ministerios == null) {
            throw new ObjectNotFoundException("Ministérios não encontrados");
        }

        return ministerios;
    }

    //usado para dashboards

    public MinisterioKpisResponseDTO ministerioBuscarKpis(int anoInicio, int anoFim){

        UUID igrejaId = jwtUtils.getIgrejaId();



        MinisterioKpisResponseDTO response = ministerios.buscarKpis(igrejaId, anoInicio, anoFim);

        return response;
    }


}
