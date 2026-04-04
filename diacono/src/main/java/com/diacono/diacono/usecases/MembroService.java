package com.diacono.diacono.usecases;

import com.diacono.diacono.applications.dtos.membro.*;
import com.diacono.diacono.domain.entity.Igreja;
import com.diacono.diacono.applications.dtos.CadastroExternoDTO;
import com.diacono.diacono.applications.dtos.RestResponseMessageDTO;
import com.diacono.diacono.domain.repository.MembroRepository;
import com.diacono.diacono.global.error.exceptions.ObjectExistsException;
import com.diacono.diacono.global.error.exceptions.ObjectNotFoundException;
import com.diacono.diacono.global.error.exceptions.ObjectSaveErrorException;
import com.diacono.diacono.global.util.JwtUtils;
import com.diacono.diacono.applications.mappers.membro.MembroMapper;
import com.diacono.diacono.domain.enums.EnumStatusMembro;
import com.diacono.diacono.domain.entity.Membro;
import com.diacono.diacono.domain.enums.EnumCargoMembro;
import com.diacono.diacono.domain.enums.EnumCargoMembroMinisterio;
import com.diacono.diacono.domain.entity.MembroMinisterio;
import com.diacono.diacono.domain.entity.Ministerio;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

        Page<Membro> membrosPage = membroRepository.findByIgrejaIdExterno(jwtUtils.getIgrejaId(),pageable);

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
                            passaNoFiltro = true;
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
    public RestResponseMessageDTO criarMembro(MembroCreateDTO membroDTO) {

        if (membroDTO == null) {
            throw new ObjectSaveErrorException("Dados do membro não podem ser nulos.");
        }

        if (membroDTO.idExternoMinisterios() == null) {
            Membro response = criarMembroSemMinisterio(membroDTO);
            return new RestResponseMessageDTO(HttpStatus.CREATED, "Usuário cadastrado com sucesso");
        }

        return criarMembroComMinisterio(membroDTO);
    }

    // METODOS AUXILIARES


    private Membro criarMembroSemMinisterio(MembroCreateDTO membroDTO) {

        if (membroDTO.cargo().equals(EnumCargoMembro.LIDER_MINISTERIO) && (membroDTO.idExternoMinisterios() == null)) {
            throw new ObjectSaveErrorException("Para cadastrar um líder de ministério, é necessário associar um ministério ao membro.");
        }

        Membro membroExistente = membroRepository.findByEmailOrCpf(membroDTO.email(), membroDTO.cpf())
                .orElseThrow(() -> new ObjectNotFoundException("Membro não encontrado"));

        if(membroExistente != null){
            throw new ObjectExistsException("Email ou CPF ja cadastrado");
        }

        LocalDate dataHoje = LocalDate.now();

        Membro membro = membroMapper.paraMembro(membroDTO);
        Igreja igreja = igrejaService.buscarUUID(membroDTO.fkIgreja());
        membro.setStatus(EnumStatusMembro.ATIVO);
        membro.setIgreja(igreja);
        membro.setDataRegistro(dataHoje);
        membro.setGeneroMembro(membroDTO.generoMembro());
        membro.setCargoMembro(membroDTO.cargo());
        membro.setSenha(hashSenha(membroDTO.senha()));
        Membro membroSalvo = membroRepository.save(membro);
        validaCriacao(membroSalvo);

        return membroSalvo;

    }


    private RestResponseMessageDTO criarMembroComMinisterio(MembroCreateDTO membroDTO) {
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

        return new RestResponseMessageDTO(HttpStatus.CREATED, "Usuário cadastrado com sucesso");

    }


    private void validaCriacao(Membro membro) {
        if (membro == null) {
            throw new ObjectSaveErrorException("Não foi possível cadastrar o usuário");
        }
    }

    private List<Membro> buscaMembros(String busca) {


        String buscaFormatada = null;
        if (busca != null && !busca.isBlank()) {
            buscaFormatada = "%" + busca + "%";
        }

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
        Membro membro = membroRepository.findByIdExterno(idExterno)
                .orElseThrow(() -> new ObjectNotFoundException("Membro não encontrado"));

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

        Membro membroExistente = membroRepository.findByEmailOrCpf(membroDTO.email(), membroDTO.cpf())
                .orElseThrow(() -> new ObjectNotFoundException("Membro não encontrado"));

        if(membroExistente != null){
            throw new ObjectExistsException("Erro ao se cadastrar");
        }

        LocalDate dataHoje = LocalDate.now();

        Membro membro = membroMapper.paraMembro(membroDTO);
        Igreja igreja = igrejaService.buscarUUID(membroDTO.fkIgreja());
        membro.setStatus(EnumStatusMembro.ATIVO);
        membro.setIgreja(igreja);
        membro.setGeneroMembro(membroDTO.generoMembro());
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
        Membro membro = membroRepository.findByEmail(email)
                .orElseThrow(() -> new ObjectNotFoundException("Membro não encontrado com o email fornecido."));

        return membro;
    }

    @Transactional
    public Membro salvarMembro(Membro membro) {
        return membroRepository.saveAndFlush(membro);
    }

    // Metodo que se relaciona com Escala
//    public List<MembroSimplificadoDTO> buscarMembrosDisponiveisParaEscala(UUID idExternoMinisterio, EventoUnicoSimplificadoDTO eventoUnicoSimplificadoDTO) {
//        LocalDateTime horarioInicio = eventoUnicoSimplificadoDTO.dataHoraInicio();
//        LocalDateTime horarioFim = eventoUnicoSimplificadoDTO.dataHoraFim();
//
//        List<MembroSimplificadoDTO> membrosMinisteriosLivres = memRepository.findMembrosMinisteriosSemEscala(idExternoMinisterio, horarioInicio, horarioFim);
//
//        if (membrosMinisteriosLivres.isEmpty()) {
//            throw new ObjectNotFoundException("Nenhum membro disponível para escala encontrado.");
//        }
//        return membrosMinisteriosLivres;
//    }

    //METODO QUE SE RELACIONA COM DASHBARDS

    public MembroKpiResponseDTO buscarKpis(int anoInicio, int anoFim){

        UUID idExternoIgreja = jwtUtils.getIgrejaId();

        return membroRepository.buscarKpisMembros(idExternoIgreja, anoInicio, anoFim);
    }

    public List<MembroDashEvolucaoDTO> buscarDashEvolucao(int anoInicio, int anoFim) {

        UUID idExternoIgreja = jwtUtils.getIgrejaId();

        return membroRepository.buscarMembrosPorAno(idExternoIgreja, anoInicio, anoFim);
    }

    public MembroDashFaixaEtariaDTO buscarDashFaixaEtaria(int anoFim) {

        UUID idExternoIgreja = jwtUtils.getIgrejaId();

        MembroDashFaixaEtariaDTO response = membroRepository.buscarMembrosPorFaixaEtaria(idExternoIgreja, anoFim);


        return response;
    }

    public MembroDashGeneroDTO buscarDashGenero(int anoFim) {

        UUID idExternoIgreja = jwtUtils.getIgrejaId();

        MembroDashGeneroDTO response = membroRepository.buscarMembrosPorGenero(idExternoIgreja, anoFim);

        System.out.println(response.feminino());
        System.out.println(response.masculino());

        return response;
    }

}