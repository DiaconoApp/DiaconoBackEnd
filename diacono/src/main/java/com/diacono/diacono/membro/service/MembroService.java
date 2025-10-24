package com.diacono.diacono.membro.service;

import com.diacono.diacono.Igreja.model.entity.Igreja;
import com.diacono.diacono.Igreja.service.IgrejaService;
import com.diacono.diacono.cadastro.model.dto.CadastroExternoDTO;
import com.diacono.diacono.global.dto.response.RestResponseMessage;
import com.diacono.diacono.global.error.exceptions.ObjectExistsException;
import com.diacono.diacono.global.error.exceptions.ObjectNotFoundException;
import com.diacono.diacono.global.error.exceptions.ObjectSaveErrorException;
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
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

// REMOVIDO: Este import era desnecessário e poderia causar conflitos.
// import static java.util.stream.Nodes.collect;

@Service
public class MembroService {

    private final MembroRepository membroRepository;
    private final MembroMapper membroMapper;
    private final BCryptPasswordEncoder passwordEncoder;
    private final MinisterioService ministerioService;
    private final MembroMinisterioService membroMinisterioService;
    private final IgrejaService igrejaService;

    public MembroService(MembroRepository membroRepository, MembroMapper membroMapper, BCryptPasswordEncoder passwordEncoder, MinisterioService ministerioService, MembroMinisterioService membroMinisterioService, IgrejaService igrejaService) {
        this.membroRepository = membroRepository;
        this.membroMapper = membroMapper;
        this.passwordEncoder = passwordEncoder;
        this.ministerioService = ministerioService;
        this.membroMinisterioService = membroMinisterioService;
        this.igrejaService = igrejaService;
    }


    @Transactional(readOnly = true)
    public Page<MembroResponseDTO> buscarTodosSemFiltro(Pageable pageable) {

        Page<Membro> membrosPage = membroRepository.findAll(pageable);
        validarMembrosEncontradosPage(membrosPage);

        return membrosPage.map(membroMapper::paraMembroResponseDTO);
    }

    @Transactional(readOnly = true)
    public Page<MembroResponseDTO> buscarTodosComFiltro(
            Pageable pageable,
            String termoBusca,
            EnumStatusMembro status,
            UUID fkMinisterio) {

        List<Membro> membrosBrutos = buscaMembros(termoBusca);

        List<Membro> membrosFiltrados = membrosBrutos.stream()
                .filter(membro -> {
                    boolean passaNoFiltro = true;

                    if (passaNoFiltro && status != null) {
                        if (membro.getStatus() == null || !membro.getStatus().equals(status)) {
                            passaNoFiltro = false;
                        }
                    }

                    if (passaNoFiltro && fkMinisterio != null) {
                        boolean pertenceAoMinisterio = membro.getMinisterios().stream()
                                .anyMatch(mm -> mm.getMinisterio().getIdExterno().equals(fkMinisterio));
                        if (!pertenceAoMinisterio) {
                            passaNoFiltro = false;
                        }
                    }

                    return passaNoFiltro;
                })
                .collect(Collectors.toList());

        validarMembrosEncontradosList(membrosFiltrados);

        int pageSize = pageable.getPageSize();
        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageSize), membrosFiltrados.size());

        List<Membro> contentListForPage;

        if (start > end) {
            contentListForPage = Collections.emptyList();
        } else {
            contentListForPage = membrosFiltrados.subList(start, end);
        }

        List<MembroResponseDTO> responseContent = membroMapper.paraMembrosResponseDTO(contentListForPage);

        return new PageImpl<>(
                responseContent,
                pageable,
                membrosFiltrados.size()
        );
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

        if (membroDTO.cargo().equals(EnumCargoMembro.LIDER_MINISTERIO) && (membroDTO.idExternoMinisterios() == null || membroDTO.idExternoMinisterios().isEmpty())) {
            throw new ObjectSaveErrorException("Para cadastrar um líder de ministério, é necessário associar um ministério ao membro.");
        }

        Membro membroExistente = membroRepository.findByEmail(membroDTO.email());

        if(membroExistente != null){
            throw new ObjectExistsException("Email ja cadastrado");
        }

        Membro membro = membroMapper.paraMembro(membroDTO);
        Igreja igreja = igrejaService.buscarUUID(membroDTO.fkIgreja());
        membro.setStatus(EnumStatusMembro.ATIVO);
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
                    associaco.setNomeMinisterio(ministerio.getNome());

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

        String buscaFormatada = "%" + busca.toUpperCase() + "%";
        List<Membro> membros = membroRepository.findAllWithFilter(buscaFormatada);

        if(membros.isEmpty() || membros == null){
            throw new ObjectNotFoundException("Nenhum membro encontrado");
        }

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


    private void validaResponsePage(Page<MembroResponseDTO> membros) {
        if (membros == null || membros.isEmpty()) {
            throw new ObjectNotFoundException("Não foi possível converter para DTOs");
        }
    }


    public String hashSenha(String senha) {
        return passwordEncoder.encode(senha);
    }

    /*MÉTODO QUE SE RELACIONA COM A ENTIDADE EVENTO*/

    public Membro buscarPorUUID(UUID idExterno) {
        /*FAZER VALIDAÇÃO DE PRESENÇA -- LANÇAR EXCEÇÃO*/
        Membro membro = membroRepository.findByIdExterno(idExterno);

        if(membro == null){
            throw new ObjectNotFoundException("Membro não encontrado.");
        }

        return membro;
    }

    //METODO QUE SE RELACIONA COM

    public Membro criarMembroExterno(CadastroExternoDTO membroDTO) {

        if(membroDTO == null){
            throw new ObjectSaveErrorException("Dados do membro não podem ser nulos.");
        }

        Membro membroExistente = membroRepository.findByEmail(membroDTO.email());

        if(membroExistente != null){
            throw new ObjectExistsException("Erro ao se cadastrar");
        }

        Membro membro = membroMapper.paraMembro(membroDTO);
        Igreja igreja = igrejaService.buscarUUID(membroDTO.fkIgreja());
        membro.setStatus(EnumStatusMembro.ATIVO);
        membro.setIgreja(igreja);
        membro.setCargoMembro(EnumCargoMembro.MEMBRO);
        membro.setSenha(hashSenha(membroDTO.senha()));
        Membro membroSalvo = membroRepository.save(membro);
        validaCriacao(membroSalvo);

        return membroSalvo;

    }

    //METODO Q SE RELACIONA COM LOGIN GOOGLE

    @Transactional
    public Membro buscarPorEmail(String email) {
        Membro membro = membroRepository.findByEmail(email);
        return membro;
    }

    @Transactional
    public Membro salvarMembro(Membro membro) {
        System.out.println(">>> Chamando salvarMembro() para: " + membro.getEmail());
        Membro membroD = membroRepository.save(membro);
        System.out.println(">>> Membro salvo (JPA retornou): " + membroD.getIdExterno());
        membroRepository.flush();
        return membroD;
    }
}