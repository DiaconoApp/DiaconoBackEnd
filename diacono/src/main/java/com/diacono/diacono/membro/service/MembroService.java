package com.diacono.diacono.membro.service;

import com.diacono.diacono.Igreja.model.entity.Igreja;
import com.diacono.diacono.Igreja.service.IgrejaService;
import com.diacono.diacono.cadastro.model.dto.CadastroExternoDTO;
import com.diacono.diacono.global.dto.response.RestResponseMessage;
import com.diacono.diacono.global.error.exceptions.FieldInvalidException;
import com.diacono.diacono.global.error.exceptions.ObjectExistsException;
import com.diacono.diacono.global.error.exceptions.ObjectNotFoundException;
import com.diacono.diacono.global.error.exceptions.ObjectSaveErrorException;
import com.diacono.diacono.global.util.JwtUtils;
import com.diacono.diacono.membro.mapper.MembroMapper;
import com.diacono.diacono.membro.model.dto.request.MembroCreateDTO;
import com.diacono.diacono.membro.model.dto.response.MembroDashEvolucaoDTO;
import com.diacono.diacono.membro.model.dto.response.MembroDashFaixaEtariaDTO;
import com.diacono.diacono.membro.model.dto.response.MembroDashGeneroDTO;
import com.diacono.diacono.membro.model.dto.response.MembroKpiResponseDTO;
import com.diacono.diacono.membro.model.dto.response.MembroResponseDTO;
import com.diacono.diacono.membro.model.entity.EnumCargoMembro;
import com.diacono.diacono.membro.model.entity.EnumStatusMembro;
import com.diacono.diacono.membro.model.entity.Membro;
import com.diacono.diacono.membro.repository.MembroRepository;
import com.diacono.diacono.membroministerio.model.entity.EnumCargoMembroMinisterio;
import com.diacono.diacono.membroministerio.model.entity.MembroMinisterio;
import com.diacono.diacono.membroministerio.service.MembroMinisterioService;
import com.diacono.diacono.ministerio.model.entity.Ministerio;
import com.diacono.diacono.ministerio.service.MinisterioService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class MembroService {

    // OWASP A05: logging seguro para auditoria sem expor segredos.
    private static final Logger logger = LoggerFactory.getLogger(MembroService.class);

    // OWASP A02: bcrypt processa no maximo 72 caracteres da senha.
    private static final int MAX_PASSWORD_LENGTH = 72;
    // OWASP A05/A07: limite defensivo para filtros textuais.
    private static final int MAX_SEARCH_LENGTH = 255;

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
        UUID igrejaId = jwtUtils.getIgrejaId();
        Page<Membro> membrosPage = membroRepository.findByIgreja_IdExterno(igrejaId, pageable);
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
                    if (status != null) {
                        boolean statusValido = membro.getStatus() != null && membro.getStatus().equals(status);
                        if (!statusValido) {
                            return false;
                        }
                    }

                    if (fkMinisterio != null) {
                        return membro.getMinisterios().stream()
                                .anyMatch(mm -> mm.getMinisterio().getIdExterno().equals(fkMinisterio));
                    }

                    return true;
                })
                .collect(Collectors.toList());

        validarMembrosEncontradosList(membrosFiltrados);

        int pageSize = pageable.getPageSize();
        int start = (int) pageable.getOffset();
        int end = Math.min(start + pageSize, membrosFiltrados.size());

        List<Membro> contentListForPage;

        if (start > end) {
            contentListForPage = Collections.emptyList();
        } else {
            contentListForPage = membrosFiltrados.subList(start, end);
        }

        List<MembroResponseDTO> responseContent = membroMapper.paraMembrosResponseDTO(contentListForPage);

        return new PageImpl<>(responseContent, pageable, membrosFiltrados.size());
    }

    @Transactional
    public RestResponseMessage criarMembro(MembroCreateDTO membroDTO) {
        if (membroDTO == null) {
            throw new ObjectSaveErrorException("Dados do membro nao podem ser nulos.");
        }

        if (membroDTO.idExternoMinisterios() == null) {
            criarMembroSemMinisterio(membroDTO);
            return new RestResponseMessage(HttpStatus.CREATED, "Usuario cadastrado com sucesso");
        }

        return criarMembroComMinisterio(membroDTO);
    }

    // METODOS AUXILIARES

    private Membro criarMembroSemMinisterio(MembroCreateDTO membroDTO) {
        if (membroDTO == null) {
            throw new ObjectSaveErrorException("Dados do membro nao podem ser nulos.");
        }

        EnumCargoMembro cargo = membroDTO.cargo() == null ? EnumCargoMembro.MEMBRO : membroDTO.cargo();
        if (cargo.equals(EnumCargoMembro.LIDER_MINISTERIO) && membroDTO.idExternoMinisterios() == null) {
            throw new ObjectSaveErrorException("Para cadastrar um lider de ministerio, e necessario associar um ministerio ao membro.");
        }

        String emailNormalizado = normalizeEmail(membroDTO.email());
        Membro membroExistente = membroRepository.findByEmailOrCpf(emailNormalizado, membroDTO.cpf());
        if (membroExistente != null) {
            throw new ObjectExistsException("Email ou CPF ja cadastrado");
        }

        LocalDate dataHoje = LocalDate.now();

        Membro membro = membroMapper.paraMembro(membroDTO);
        Igreja igreja = igrejaService.buscarUUID(membroDTO.fkIgreja());

        membro.setEmail(emailNormalizado);
        membro.setStatus(EnumStatusMembro.ATIVO);
        membro.setIgreja(igreja);
        membro.setDataRegistro(dataHoje);
        membro.setGeneroMembro(membroDTO.generoMembro());
        membro.setCargoMembro(cargo);
        membro.setSenha(hashSenha(membroDTO.senha()));

        Membro membroSalvo = membroRepository.save(membro);
        validaCriacao(membroSalvo);

        return membroSalvo;
    }

    private RestResponseMessage criarMembroComMinisterio(MembroCreateDTO membroDTO) {
        Ministerio ministerio = ministerioService.buscarPorUUID(membroDTO.idExternoMinisterios());

        Membro membro = criarMembroSemMinisterio(membroDTO);
        membroMinisterioService.apagarMembroMinisterioPorMembro(membro);

        MembroMinisterio membroMinisterio = MembroMinisterio.builder()
                .membro(membro)
                .ministerio(ministerio)
                .cargoMembro(EnumCargoMembroMinisterio.MEMBRO_MINISTERIO)
                .nomeMinisterio(ministerio.getNome())
                .build();

        membroMinisterioService.salvarTodos(membroMinisterio);

        return new RestResponseMessage(HttpStatus.CREATED, "Usuario cadastrado com sucesso");
    }

    private void validaCriacao(Membro membro) {
        if (membro == null) {
            throw new ObjectSaveErrorException("Nao foi possivel cadastrar o usuario");
        }
    }

    private List<Membro> buscaMembros(String busca) {
        String buscaFormatada = null;

        if (busca != null && !busca.isBlank()) {
            if (busca.length() > MAX_SEARCH_LENGTH) {
                throw new FieldInvalidException("Termo de busca excede o limite permitido");
            }
            buscaFormatada = "%" + busca.trim() + "%";
        }

        List<Membro> membros = membroRepository.findAllWithFilter(buscaFormatada, jwtUtils.getIgrejaId());

        // OWASP A05: null-safe check evita NPE e comportamento inconsistente.
        if (membros == null || membros.isEmpty()) {
            throw new ObjectNotFoundException("Nenhum membro encontrado");
        }

        return membros;
    }

    private void validarMembrosEncontradosList(List<Membro> membros) {
        if (membros == null || membros.isEmpty()) {
            throw new ObjectNotFoundException("Nenhum membro encontrado");
        }
    }

    private void validarMembrosEncontradosPage(Page<Membro> membros) {
        if (membros == null || membros.isEmpty()) {
            throw new ObjectNotFoundException("Nenhum membro encontrado");
        }
    }

    public String hashSenha(String senha) {
        // OWASP A02: validacoes defensivas antes do hash.
        if (senha == null || senha.isBlank()) {
            throw new ObjectSaveErrorException("Senha nao pode ser vazia");
        }
        if (senha.length() > MAX_PASSWORD_LENGTH) {
            throw new ObjectSaveErrorException("Senha excede tamanho maximo permitido");
        }
        return passwordEncoder.encode(senha);
    }

    /* METODO QUE SE RELACIONA COM A ENTIDADE EVENTO E MINISTERIO */

    @Transactional(readOnly = true)
    public Membro buscarPorUUID(UUID idExterno) {
        if (idExterno == null) {
            throw new ObjectNotFoundException("Membro nao encontrado.");
        }

        Membro membro = membroRepository.findByIdExterno(idExterno);
        if (membro == null) {
            throw new ObjectNotFoundException("Membro nao encontrado.");
        }

        return membro;
    }

    //METODO QUE SE RELACIONA COM

    public Membro criarMembroExterno(CadastroExternoDTO membroDTO) {
        if (membroDTO == null) {
            throw new ObjectSaveErrorException("Dados do membro nao podem ser nulos.");
        }

        String emailNormalizado = normalizeEmail(membroDTO.email());
        Membro membroExistente = membroRepository.findByEmailOrCpf(emailNormalizado, membroDTO.cpf());
        if (membroExistente != null) {
            // OWASP A05/A07: mensagem generica para evitar enumeracao de conta.
            throw new ObjectExistsException("Erro ao se cadastrar");
        }

        LocalDate dataHoje = LocalDate.now();

        Membro membro = membroMapper.paraMembro(membroDTO);
        Igreja igreja = igrejaService.buscarUUID(membroDTO.fkIgreja());

        membro.setEmail(emailNormalizado);
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

    // METODO QUE SE RELACIONA COM LOGIN GOOGLE

    @Transactional(readOnly = true)
    public Membro buscarPorEmail(String email) {
        if (email == null || email.isBlank()) {
            return null;
        }

        String emailNormalizado = normalizeEmail(email);
        return membroRepository.findByEmail(emailNormalizado);
    }

    @Transactional
    public Membro salvarMembro(Membro membro) {
        Membro membroD = membroRepository.save(membro);
        membroRepository.flush();
        return membroD;
    }

    // Metodo que se relaciona com Escala
//    public List<MembroSimplificadoDTO> buscarMembrosDisponiveisParaEscala(UUID idExternoMinisterio, EventoUnicoSimplificadoDTO eventoUnicoSimplificadoDTO) {
//        LocalDateTime horarioInicio = eventoUnicoSimplificadoDTO.dataHoraInicio();
//        LocalDateTime horarioFim = eventoUnicoSimplificadoDTO.dataHoraFim();
//
//        List<MembroSimplificadoDTO> membrosMinisteriosLivres = membroRepository.findMembrosMinisteriosSemEscala(idExternoMinisterio, horarioInicio, horarioFim);
//
//        if (membrosMinisteriosLivres.isEmpty()) {
//            throw new ObjectNotFoundException("Nenhum membro disponível para escala encontrado.");
//        }
//        return membrosMinisteriosLivres;
//    }

    // METODO QUE SE RELACIONA COM DASHBARDS

    public MembroKpiResponseDTO buscarKpis(int anoInicio, int anoFim) {
        UUID idExternoIgreja = jwtUtils.getIgrejaId();
        return membroRepository.buscarKpisMembros(idExternoIgreja, anoInicio, anoFim);
    }

    public List<MembroDashEvolucaoDTO> buscarDashEvolucao(int anoInicio, int anoFim) {
        UUID idExternoIgreja = jwtUtils.getIgrejaId();
        return membroRepository.buscarMembrosPorAno(idExternoIgreja, anoInicio, anoFim);
    }

    public MembroDashFaixaEtariaDTO buscarDashFaixaEtaria(int anoFim) {
        UUID idExternoIgreja = jwtUtils.getIgrejaId();
        return membroRepository.buscarMembrosPorFaixaEtaria(idExternoIgreja, anoFim);
    }

    public MembroDashGeneroDTO buscarDashGenero(int anoFim) {
        UUID idExternoIgreja = jwtUtils.getIgrejaId();
        MembroDashGeneroDTO response = membroRepository.buscarMembrosPorGenero(idExternoIgreja, anoFim);

        // OWASP A05: remove saida em console para nao expor dados em runtime.
        logger.debug("Dashboard de genero calculado para igreja ID: {}", idExternoIgreja);
        return response;
    }

    private String normalizeEmail(String email) {
        if (email == null || email.isBlank()) {
            throw new ObjectSaveErrorException("Email nao pode ser vazio");
        }
        return email.trim().toLowerCase(Locale.ROOT);
    }
}
