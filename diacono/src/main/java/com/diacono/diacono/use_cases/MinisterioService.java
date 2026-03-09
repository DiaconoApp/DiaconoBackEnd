package com.diacono.diacono.use_cases;

import com.diacono.diacono.applications.dtos.RestResponseMessageDTO;
import com.diacono.diacono.global.error.exceptions.FieldInvalidException;
import com.diacono.diacono.global.error.exceptions.ObjectNotFoundException;
import com.diacono.diacono.global.util.JwtUtils;
import com.diacono.diacono.domain.enums.EnumCargoMembro;
import com.diacono.diacono.domain.enums.EnumStatusMembro;
import com.diacono.diacono.domain.entity.Membro;
import com.diacono.diacono.infrastructure.persistence.MembroJpaRepository;
import com.diacono.diacono.applications.dtos.membro.MembroMinisterioCreateDTO;
import com.diacono.diacono.applications.dtos.membro.MembroMinisterioInfoMembroDTO;
import com.diacono.diacono.domain.enums.EnumCargoMembroMinisterio;
import com.diacono.diacono.domain.entity.MembroMinisterio;
import com.diacono.diacono.applications.mappers.ministerio.MinisterioMapper;
import com.diacono.diacono.applications.dtos.ministerio.MinisterioCreateDTO;
import com.diacono.diacono.applications.dtos.ministerio.MinisterioUpdateDTO;
import com.diacono.diacono.applications.dtos.ministerio.MinisterioKpisResponseDTO;
import com.diacono.diacono.applications.dtos.ministerio.MinisterioSimplificadoDTO;
import com.diacono.diacono.applications.dtos.ministerio.MinisterioSuperSimplificadoDTO;
import com.diacono.diacono.domain.enums.EnumStatusMinisterio;
import com.diacono.diacono.domain.entity.Ministerio;
import com.diacono.diacono.infrastructure.persistence.MinisteriosJpaRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.*;

@Service
public class MinisterioService {

    private final MinisteriosJpaRepository ministerios;
    private final MinisterioMapper mapper;
    private final MembroMinisterioService membroMinisterio;
    private final MembroJpaRepository membro;
    private final JwtUtils jwtUtils;

    public MinisterioService(MinisteriosJpaRepository ministerios, MinisterioMapper mapper, MembroMinisterioService membroMinisterio, MembroJpaRepository membro, JwtUtils jwtUtils) {
        this.ministerios = ministerios;
        this.mapper = mapper;
        this.membroMinisterio = membroMinisterio;
        this.membro = membro;
        this.jwtUtils = jwtUtils;
    }

    //METODOS PRINCIPAIS

    //USO GERAL

    public List<MinisterioSimplificadoDTO> buscarMinisteriosGerais() {

        List<Ministerio> ministeriosResponse = ministerios.findByIgreja_IdExterno(jwtUtils.getIgrejaId());
        if (ministeriosResponse.isEmpty()) {
            throw new ObjectNotFoundException("Nenhum ministério encontrado");

        }

        List<MinisterioSimplificadoDTO> responses = ministeriosResponse.stream()
                .map(mapper::paraMinisterioSimplificadoDTO)
                .toList();

        return responses;

    }

    //VISAO GOVERNO

    public Page<MinisterioSimplificadoDTO> buscarMinisteriosGoverno(Pageable pageable) {

        Page<Ministerio> ministeriosPage = ministerios.findByIgreja_IdExterno(jwtUtils.getIgrejaId(), pageable);
        if (ministeriosPage.isEmpty()) {
            throw new ObjectNotFoundException("Nenhum ministério encontrado");
        }

        //mapper::paraMinisterioSimplificadoDTO == (m -> mapper.paraMinisterioSimplificadoDTO(m))
        Page<MinisterioSimplificadoDTO> responses = ministeriosPage.map(mapper::paraMinisterioSimplificadoDTO);

        return responses;

    }

    public Page<MinisterioSimplificadoDTO> buscarMinisteriosGovernoComFiltro(Pageable pageable, String buscaGeral, EnumStatusMinisterio status) {

        String stringBusca = "%" + buscaGeral.trim().toUpperCase() + "%";

        Page<Ministerio> ministeriosPage = ministerios.buscarComFiltros(pageable, stringBusca, status, jwtUtils.getIgrejaId());
        if (ministeriosPage.isEmpty()) {
            throw new ObjectNotFoundException("Nenhum ministério encontrado");
        }

        //mapper::paraMinisterioSimplificadoDTO == (m -> mapper.paraMinisterioSimplificadoDTO(m))
        Page<MinisterioSimplificadoDTO> responses = ministeriosPage
                .map(mapper::paraMinisterioSimplificadoDTO);

        return responses;

    }

    @Transactional
    public RestResponseMessageDTO criarMinisterio(MinisterioCreateDTO ministerioDTO) {

        //buscar lider primeiro para adicionar como membro do ministério
        Membro liderMinisterio = membro.findByIdExterno(ministerioDTO.idLider());

        if (liderMinisterio == null) {
            throw new ObjectNotFoundException("Líder do ministério não encontrado");
        }

        if (liderMinisterio.getCargoMembro() != EnumCargoMembro.LIDER_MINISTERIO) {
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
                .dataRegistro(data)
                .build();

        Set<MembroMinisterio> membroMinisterios = novoMinisterio.getMembros();

        if (membroMinisterios == null) {
            membroMinisterios = new HashSet<>();
        }

        membroMinisterios.add(membroLider);
        novoMinisterio.setMembros(membroMinisterios);
        ministerios.save(novoMinisterio);

        return new RestResponseMessageDTO(HttpStatus.CREATED, "Ministério criado com sucesso");

    }

    @Transactional
    public RestResponseMessageDTO editarMinisterio(MinisterioUpdateDTO ministerioDTO, UUID idMinisterio) {

        Ministerio ministerioExistente = ministerios.findByIdExterno(idMinisterio);


        if (ministerioExistente == null) {
            throw new ObjectNotFoundException("Ministério não encontrado");
        }

        if (ministerioDTO.idLider() != null) {


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


            if (liderNovoExisteNoMinisterio == null) {


                liderNovo.setCargoMembro(EnumCargoMembro.LIDER_MINISTERIO);

                MembroMinisterio novoMembroMinisterio = MembroMinisterio.builder()
                        .membro(liderNovo)
                        .ministerio(ministerioExistente)
                        .cargoMembro(EnumCargoMembroMinisterio.LIDER_MINISTERIO)
                        .nomeMinisterio(ministerioExistente.getNome())
                        .build();

                ministerioExistente.getMembros().add(novoMembroMinisterio);



            } else {

                liderNovo.setCargoMembro(EnumCargoMembro.LIDER_MINISTERIO);
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

        if (ministerioDTO.nome() != null && !ministerioDTO.nome().isBlank()) {
            ministerioExistente.setNome(ministerioDTO.nome());
        }

        if (ministerioDTO.status() != null) {
            ministerioExistente.setStatus(ministerioDTO.status());
        }


        ministerios.save(ministerioExistente);

        return new RestResponseMessageDTO(HttpStatus.OK, "Ministério atualizado com sucesso");
    }

    //VISAO LIDER MINISTERIO

    public Page<MembroMinisterioInfoMembroDTO> buscarMembroMinisterioLiderMinisterio(UUID idMinisterio, Pageable page) {
        return membroMinisterio.buscarPorMembroMinisterioSemFiltro(idMinisterio, page);
    }

    public Page<MembroMinisterioInfoMembroDTO> buscarMembroMinisterioLiderMinisterioComFiltro(UUID idMinisterio, Pageable page, String texto, EnumStatusMembro status) {
        return membroMinisterio.buscarPorMembroMinisterioComFiltro(idMinisterio, page, texto, status);
    }

    @Transactional
    public RestResponseMessageDTO adicionarMembroMinisterioLiderMinisterio(UUID idMinisterio, MembroMinisterioCreateDTO dto) {

        if (dto == null) {
            throw new FieldInvalidException("Dados do membro do ministério não podem ser nulos");
        }

        Long idMinisterioNovo = ministerios.buscarIdPorUUID(idMinisterio);

        if (idMinisterioNovo == null) {
            throw new ObjectNotFoundException("Ministério não encontrado");
        }

        Long idMembroNovo = membro.buscarIdPorUUID(dto.idExterno());

        if (idMembroNovo == null) {
            throw new ObjectNotFoundException("Membro não encontrado");
        }

        membroMinisterio.adicionarMembroMinisterioLiderMinisterio(idMinisterioNovo, idMembroNovo);

        return new RestResponseMessageDTO(HttpStatus.OK, "Membro adicionado ao ministério com sucesso");
    }

    @Transactional
    public RestResponseMessageDTO removerMembroMinisterioLiderMinisterio(UUID idMinisterio, UUID idMembroMinisterio) {

        membroMinisterio.removerMembroMinisterioLiderMinisterio(idMinisterio, idMembroMinisterio);

        return new RestResponseMessageDTO(HttpStatus.OK, "Membro removido do ministério com sucesso");
    }

    public List<MinisterioSuperSimplificadoDTO> buscarMinisteriosLiderMinisterio() {

        UUID idExternoMembro = jwtUtils.getSubject();
        UUID idIgreja = jwtUtils.getIgrejaId();

        List<MinisterioSuperSimplificadoDTO> ministerios =  membroMinisterio.buscarMinisterioLider(idExternoMembro, idIgreja);

        return ministerios;
    }

    /*ESSE METODO SE RELACIONA COM EVENTO*/

    public Set<Ministerio> buscarPorUUID(List<UUID> idExterno) {
        Set<Ministerio> ministerios = this.ministerios.findAllByIdExternoIn(idExterno);

        if (ministerios.isEmpty()) {
            throw new ObjectNotFoundException("Ministérios não encontrados");
        }

        return ministerios;
    }

    public Ministerio buscarPorUUID(UUID idExterno) {
        Ministerio ministerios = this.ministerios.findByIdExterno(idExterno);

        if (ministerios == null) {
            throw new ObjectNotFoundException("Ministérios não encontrados");
        }

        return ministerios;
    }

    //usado para dashboards

    public MinisterioKpisResponseDTO ministerioBuscarKpis(int anoFim) {
        UUID igrejaId = jwtUtils.getIgrejaId();
        MinisterioKpisResponseDTO response = ministerios.buscarKpis(igrejaId, anoFim);
        return response;
    }

}
