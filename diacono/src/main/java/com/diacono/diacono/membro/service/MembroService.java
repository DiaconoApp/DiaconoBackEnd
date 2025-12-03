package com.diacono.diacono.membro.service;

import com.diacono.diacono.Igreja.model.entity.Igreja;
import com.diacono.diacono.Igreja.service.IgrejaService;
import com.diacono.diacono.cadastro.model.dto.CadastroExternoDTO;
import com.diacono.diacono.evento.model.dto.response.EventoUnicoSimplificadoDTO;
import com.diacono.diacono.global.dto.response.RestResponseMessage;
import com.diacono.diacono.global.error.exceptions.ObjectExistsException;
import com.diacono.diacono.global.error.exceptions.ObjectNotFoundException;
import com.diacono.diacono.global.error.exceptions.ObjectSaveErrorException;
import com.diacono.diacono.global.util.JwtUtils;
import com.diacono.diacono.membro.mapper.MembroMapper;
import com.diacono.diacono.membro.model.dto.request.MembroCreateDTO;
<<<<<<< HEAD
import com.diacono.diacono.membro.model.dto.response.*;
=======
import com.diacono.diacono.membro.model.dto.response.MembroResponseDTO;
import com.diacono.diacono.membro.model.dto.response.MembroSimplificadoDTO;
>>>>>>> dc17664dcfb8076eb7297f094b34a048c0bed446
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

import java.time.LocalDateTime;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;



@Service
public class MembroService {

    private final MembroRepository membroRepository;
    private final MembroMapper membroMapper;
    private final BCryptPasswordEncoder passwordEncoder;
    private final MinisterioService ministerioService;
    private final MembroMinisterioService membroMinisterioService;
    private final IgrejaService igrejaService;
    private final JwtUtils jwtUtils;

    public MembroService(MembroRepository membroRepository, MembroMapper membroMapper, BCryptPasswordEncoder passwordEncoder, MinisterioService ministerioService, MembroMinisterioService membroMinisterioService, IgrejaService igrejaService, JwtUtils jwtUtils) {
        this.membroRepository = membroRepository;
        this.membroMapper = membroMapper;
        this.passwordEncoder = passwordEncoder;
        this.ministerioService = ministerioService;
        this.membroMinisterioService = membroMinisterioService;
        this.igrejaService = igrejaService;
        this.jwtUtils = jwtUtils;
    }


    @Transactional(readOnly = true)
    public Page<MembroResponseDTO> buscarTodosSemFiltro(Pageable pageable) {

        Page<Membro> membrosPage = membroRepository.findByIgreja_IdExterno(jwtUtils.getIgrejaId(),pageable);
        validarMembrosEncontradosPage(membrosPage);

        return membrosPage.map(membroMapper::paraMembroResponseDTO);
    }

    @Transactional(readOnly = true)
    public Page<MembroResponseDTO> buscarTodosComFiltro(
            Pageable pageable,
            String termoBusca,
            EnumStatusMembro status,
            UUID fkMinisterio) {

        //REFATORAR

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
                    }else if(passaNoFiltro && (fkMinisterio == null)){
                        boolean naoTemMinisterio = membro.getMinisterios() == null || membro.getMinisterios().isEmpty();
                        if (!naoTemMinisterio) {
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

        if (membroDTO.idExternoMinisterios() == null) {
            Membro response = criarMembroSemMinisterio(membroDTO);
            return new RestResponseMessage(HttpStatus.CREATED, "Usuário cadastrado com sucesso");
        }

        return criarMembroComMinisterio(membroDTO);
    }

    // METODOS AUXILIARES


    private Membro criarMembroSemMinisterio(MembroCreateDTO membroDTO) {

        if (membroDTO.cargo().equals(EnumCargoMembro.LIDER_MINISTERIO) && (membroDTO.idExternoMinisterios() == null)) {
            throw new ObjectSaveErrorException("Para cadastrar um líder de ministério, é necessário associar um ministério ao membro.");
        }

        Membro membroExistente = membroRepository.findByEmailOrCpf(membroDTO.email(), membroDTO.cpf());

        if(membroExistente != null){
            throw new ObjectExistsException("Email ja cadastrado");
        }

        LocalDate dataHoje = LocalDate.now();

        Membro membro = membroMapper.paraMembro(membroDTO);
        Igreja igreja = igrejaService.buscarUUID(membroDTO.fkIgreja());
        membro.setStatus(EnumStatusMembro.ATIVO);
        membro.setIgreja(igreja);
        membro.setDataRegistro(dataHoje);
        membro.setCargoMembro(membroDTO.cargo());
        membro.setSenha(hashSenha(membroDTO.senha()));
        Membro membroSalvo = membroRepository.save(membro);
        validaCriacao(membroSalvo);

        return membroSalvo;

    }


    private RestResponseMessage criarMembroComMinisterio(MembroCreateDTO membroDTO) {
        Ministerio ministerios = ministerioService.buscarPorUUID(membroDTO.idExternoMinisterios());

        Membro membro = criarMembroSemMinisterio(membroDTO);
        membroMinisterioService.apagarMembroMinisterioPorMembro(membro);



        MembroMinisterio membroMinisterio = MembroMinisterio.builder()
                .membro(membro)
                .ministerio(ministerios)
                .cargoMembro(EnumCargoMembroMinisterio.MEMBRO_MINISTERIO)
                .nomeMinisterio(ministerios.getNome())
                .build();

        membroMinisterioService.salvarTodos(membroMinisterio);

        return new RestResponseMessage(HttpStatus.CREATED, "Usuário cadastrado com sucesso");

    }


    private void validaCriacao(Membro membro) {
        if (membro == null) {
            throw new ObjectSaveErrorException("Não foi possível cadastrar o usuário");
        }
    }

    private List<Membro> buscaMembros(String busca) {

        String buscaFormatada = "%" + busca.toLowerCase() + "%";
        List<Membro> membros = membroRepository.findAllWithFilter(buscaFormatada, jwtUtils.getIgrejaId());

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

    /*METODO QUE SE RELACIONA COM A ENTIDADE EVENTO E MINISTERIO*/

    public Membro buscarPorUUID(UUID idExterno) {
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

        Membro membroExistente = membroRepository.findByEmailOrCpf(membroDTO.email(), membroDTO.cpf());

        if(membroExistente != null){
            throw new ObjectExistsException("Erro ao se cadastrar");
        }

        LocalDate dataHoje = LocalDate.now();

        Membro membro = membroMapper.paraMembro(membroDTO);
        Igreja igreja = igrejaService.buscarUUID(membroDTO.fkIgreja());
        membro.setStatus(EnumStatusMembro.ATIVO);
        membro.setIgreja(igreja);
        membro.setCargoMembro(EnumCargoMembro.MEMBRO);
        membro.setDataRegistro(dataHoje);
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
        Membro membroD = membroRepository.save(membro);
        membroRepository.flush();
        return membroD;
    }

    // Metodo que se relaciona com Escala
    public List<MembroSimplificadoDTO> buscarMembrosDisponiveisParaEscala(UUID idExternoMinisterio, EventoUnicoSimplificadoDTO eventoUnicoSimplificadoDTO) {
        LocalDateTime horarioInicio = eventoUnicoSimplificadoDTO.dataHoraInicio();
        LocalDateTime horarioFim = eventoUnicoSimplificadoDTO.dataHoraFim();

        List<MembroSimplificadoDTO> membrosMinisteriosLivres = membroRepository.findMembrosMinisteriosSemEscala(idExternoMinisterio, horarioInicio, horarioFim);

        if (membrosMinisteriosLivres.isEmpty()) {
            throw new ObjectNotFoundException("Nenhum membro disponível para escala encontrado.");
        }
        return membrosMinisteriosLivres;
    }

    //METODO QUE SE RELACIONA COM DASHBARDS

    public MembroKpiResponseDTO buscarKpis(int anoInicio, int anoFim){

        UUID idExternoIgreja = jwtUtils.getIgrejaId();

        return membroRepository.buscarKpisMembros(idExternoIgreja, anoInicio, anoFim);

    }

    public List<MembroDashEvolucaoDTO> buscarDashEvolucao(int anoInicio, int anoFim) {

        UUID idExternoIgreja = jwtUtils.getIgrejaId();

        return membroRepository.buscarMembrosPorAno(idExternoIgreja, anoInicio, anoFim);
    }

    public MembroDashFaixaEtariaDTO buscarDashFaixaEtaria(int anoInicio, int anoFim) {

        UUID idExternoIgreja = jwtUtils.getIgrejaId();

        return membroRepository.buscarMembrosPorFaixaEtaria(idExternoIgreja, anoFim, anoInicio);
    }

    public MembroDashGeneroDTO buscarDashGenero(int anoInicio, int anoFim) {

        UUID idExternoIgreja = jwtUtils.getIgrejaId();

        return membroRepository.buscarMembrosPorGenero(idExternoIgreja, anoInicio, anoFim);
    }

}