package com.diacono.diacono.membro.service;

import com.diacono.diacono.Igreja.model.entity.Igreja;
import com.diacono.diacono.Igreja.service.IgrejaService;
import com.diacono.diacono.global.dto.response.RestResponseMessage;
import com.diacono.diacono.global.error.exceptions.ObjectNotFoundException;
import com.diacono.diacono.global.error.exceptions.ObjectSaveErrorException;
import com.diacono.diacono.membro.exceptions.MembroNaoEncontradoException;
import com.diacono.diacono.membro.mapper.MembroMapper;
import com.diacono.diacono.membro.model.dto.request.MembroCreateDTO;
import com.diacono.diacono.membro.model.dto.response.MembroResponseDTO;
import com.diacono.diacono.membro.model.entity.EnumStatusMembro;
import com.diacono.diacono.membro.model.entity.Membro;
import com.diacono.diacono.membro.repository.MembroRepository;
import com.diacono.diacono.membro.model.entity.EnumCargoMembro;
import com.diacono.diacono.membroministerio.model.entity.EnumCargoMembroMinisterio;
import com.diacono.diacono.membroministerio.model.entity.MembroMinisterio;
import com.diacono.diacono.membroministerio.service.MembroMinisterioService;
import com.diacono.diacono.ministerio.model.entity.Ministerio;
import com.diacono.diacono.ministerio.service.MinisterioService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class MembroService {

    private final MembroRepository membroRepository;
    private final MembroMapper membroMapper;
    private final PasswordEncoder passwordEncoder;
    private final MinisterioService ministerioService;
    private final MembroMinisterioService membroMinisterioService;
    private final IgrejaService igrejaService;

    public MembroService(MembroRepository membroRepository, MembroMapper membroMapper, PasswordEncoder passwordEncoder, MinisterioService ministerioService, MembroMinisterioService membroMinisterioService, IgrejaService igrejaService) {
        this.membroRepository = membroRepository;
        this.membroMapper = membroMapper;
        this.passwordEncoder = passwordEncoder;
        this.ministerioService = ministerioService;
        this.membroMinisterioService = membroMinisterioService;
        this.igrejaService = igrejaService;
    }

    @Transactional(readOnly = true)
    public Page<MembroResponseDTO> buscarTodosSemFiltro(Pageable pageable) {

        List<Membro> membros = membroRepository.findAll();
        validarMembrosEncontradosList(membros);
        List<MembroResponseDTO> response = membroMapper.paraMembrosResponseDTO(membros);
        validaResponseList(response);

        Page<MembroResponseDTO> responsePage = new PageImpl<>(
                response,
                pageable,
                response.size()

        );

        return responsePage;
    }

    @Transactional(readOnly = true)
    public Page<MembroResponseDTO> buscarTodosComFiltro(
            Pageable pageable,
            String termoBusca,
            EnumStatusMembro status,
            UUID fkMinisterio) {

        List<Membro> membrosPage = buscaMembros(termoBusca);

        if (status == null && fkMinisterio == null) {
            List<MembroResponseDTO> response = membroMapper.paraMembrosResponseDTO(membrosPage);
            Page<MembroResponseDTO> responsePage = new PageImpl<>(
                    response,
                    pageable,
                    response.size()
            );
            validaResponsePage(responsePage);
            return responsePage;
        }

        List<Membro> membrosFiltradosList = membrosPage.stream()
                .filter(membro -> {

                    boolean passaNoFiltro = true;
                    if (passaNoFiltro && status != null) {
                        if (!membro.getStatus().equals(status)) {
                            passaNoFiltro = false;
                        }
                    }

                    if (passaNoFiltro && fkMinisterio != null) {
                        if (!fkMinisterio.equals(membro.getIdExterno())) {
                            passaNoFiltro = false;
                        }
                    }

                    return passaNoFiltro;
                })
                .collect(Collectors.toList());

        List<MembroResponseDTO> response = membroMapper.paraMembrosResponseDTO(membrosFiltradosList);
        validaResponseList(response);

        Page<MembroResponseDTO> responsePage = new PageImpl<>(
                response,
                pageable,
                response.size()

        );

        validaResponsePage(responsePage);

        return responsePage;

    }

    @Transactional
    public RestResponseMessage criarMembro(MembroCreateDTO membroDTO) {

        if (membroDTO == null) {
            throw new ObjectSaveErrorException("Dados do membro não podem ser nulos.");
        }

        if (membroDTO.idExternoMinisterios() == null || membroDTO.idExternoMinisterios().isEmpty()) {
            Membro response = criarMembroSemMinisterio(membroDTO);
            return new RestResponseMessage(HttpStatus.CREATED, "Usuário cadastrado com sucesso");
        }

        return criarMembroComMinisterio(membroDTO);
    }

    // METODOS AUXILIARES

    private Membro criarMembroSemMinisterio(MembroCreateDTO membroDTO) {

        if (membroDTO.cargo().equals(EnumCargoMembro.LIDER_MINISTERIO)) {
            throw new ObjectSaveErrorException("Para cadastrar um líder de ministério, é necessário associar um ministério ao membro.");
        }

        Membro membro = membroMapper.paraMembro(membroDTO);
        Igreja igreja = igrejaService.buscarUUID(membroDTO.fkIgreja());
        membro.setIgreja(igreja);
        membro.setCargoMembro(membroDTO.cargo());
        membro.setSenha(hashSenha(membroDTO.senha()));
        Membro membroSalvo = membroRepository.save(membro);
        validaCriacao(membroSalvo);

        return membroSalvo;

    }

    private RestResponseMessage criarMembroComMinisterio(MembroCreateDTO membroDTO) {
        Set<Ministerio> ministerios = ministerioService.buscarPorUUID(membroDTO.idExternoMinisterios());

        Membro membro = criarMembroSemMinisterio(membroDTO);
        membroMinisterioService.apagarMembroMinisterioPorMembro(membro);

        List<MembroMinisterio> novasAssociacoes = ministerios.stream()
                .map(ministerio -> {

                    MembroMinisterio associaco = new MembroMinisterio();

                    associaco.setMembro(membro);
                    associaco.setMinisterio(ministerio);

                    if (membro.getCargoMembro().equals(EnumCargoMembro.LIDER_MINISTERIO)) {
                        associaco.setCargoMembro(EnumCargoMembroMinisterio.LIDER_MINISTERIO);
                    }

                    return associaco;

                })
                .collect(Collectors.toList());

        membroMinisterioService.salvarTodos(novasAssociacoes);

        return new RestResponseMessage(HttpStatus.CREATED, "Usuário cadastrado com sucesso");

    }


    private void validaCriacao(Membro membro) {
        if (membro == null) {
            throw new ObjectSaveErrorException("Não foi possível cadastrar o usuário");
        }
    }

    private List<Membro> buscaMembros(String busca) {

        String buscaFormatada = "%" + busca + "%";
        List<Membro> membros = membroRepository.findAllWithFilter(buscaFormatada);
        validarMembrosEncontradosList(membros);

        return membros;
    }

    private void validarMembrosEncontradosList(List<Membro> membros) {
        if (membros.isEmpty()) {
            throw new ObjectNotFoundException("Nenhum membro encontrado");
        }
    }

    private void validarMembrosEncontradosPage(Page<Membro> membros) {
        if (membros.isEmpty()) {
            throw new ObjectNotFoundException("Nenhum membro encontrado");
        }
    }

    private void validaResponseList(List<MembroResponseDTO> membros) {
        if (membros == null || membros.isEmpty()) {
            throw new ObjectNotFoundException("Não foi possível converter para DTOs");
        }
    }

    ;

    private void validaResponsePage(Page<MembroResponseDTO> membros) {
        if (membros == null || membros.isEmpty()) {
            throw new ObjectNotFoundException("Não foi possível converter para DTOs");
        }
    }

    ;


    public String hashSenha(String senha) {
        return passwordEncoder.encode(senha);
    }

    /*MÉTODO QUE SE RELACIONA COM A ENTIDADE EVENTO*/

    public Membro buscarPorUUID(UUID idExterno) {
        /*FAZER VALIDAÇÃO DE PRESENÇA -- LANÇAR EXCEÇÃO*/
        Membro membro = membroRepository.findByIdExterno(idExterno)
                .orElseThrow(() -> new MembroNaoEncontradoException("Membro não encontrado com ID: " + idExterno));

        return membro;
    }
}
