package com.diacono.diacono.membro.service;

import com.diacono.diacono.Igreja.model.entity.Igreja;
import com.diacono.diacono.Igreja.service.IgrejaService;
import com.diacono.diacono.cadastro.model.dto.CadastroExternoDTO;
import com.diacono.diacono.global.dto.response.RestResponseMessage;
import com.diacono.diacono.global.error.exceptions.ObjectExistsException;
import com.diacono.diacono.global.error.exceptions.ObjectNotFoundException;
import com.diacono.diacono.global.error.exceptions.ObjectSaveErrorException;
import com.diacono.diacono.global.util.JwtUtils;
import com.diacono.diacono.membro.mapper.MembroMapper;
import com.diacono.diacono.membro.model.dto.request.MembroCreateDTO;
import com.diacono.diacono.membro.model.dto.response.MembroResponseDTO;
import com.diacono.diacono.membro.model.entity.EnumCargoMembro;
import com.diacono.diacono.membro.model.entity.EnumGeneroMembro;
import com.diacono.diacono.membro.model.entity.EnumStatusMembro;
import com.diacono.diacono.membro.model.entity.Membro;
import com.diacono.diacono.membro.repository.MembroRepository;
import com.diacono.diacono.membroministerio.model.entity.MembroMinisterio;
import com.diacono.diacono.membroministerio.service.MembroMinisterioService;
import com.diacono.diacono.ministerio.model.entity.Ministerio;
import com.diacono.diacono.ministerio.service.MinisterioService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MembroServiceTest {

    @Mock
    private JwtUtils jwtUtils;

    @Mock
    private MembroRepository membroRepository;

    @Mock
    private MembroMapper membroMapper;

    @Mock
    private MinisterioService ministerioService;

    @Mock
    private MembroMinisterioService membroMinisterioService;

    @Mock
    private IgrejaService igrejaService;

    @Mock
    private BCryptPasswordEncoder passwordEncoder;

    @InjectMocks
    private MembroService membroService;

    private Pageable pageable;
    private UUID igrejaId;

    @BeforeEach
    void setUp() {
        pageable = PageRequest.of(0, 10);
        igrejaId = UUID.randomUUID();
    }

    @Test
    @DisplayName("Deve buscar todos os membros sem filtro com paginação")
    void buscarTodosSemFiltro() {
        Membro membro1 = new Membro();
        Membro membro2 = new Membro();

        MembroResponseDTO dto1 = new MembroResponseDTO(
                UUID.randomUUID(),
                "Membro 1",
                "membor@hotmail.com",
                "11999999999",
                null,
                null,
                EnumStatusMembro.ATIVO
        );
        MembroResponseDTO dto2 = new MembroResponseDTO(
                UUID.randomUUID(),
                "Membro 2",
                "membro2@hotmail.com",
                "11988888888",
                null,
                null,
                EnumStatusMembro.ATIVO
        );

        List<Membro> membros = List.of(membro1, membro2);
        Page<Membro> membroPage = new PageImpl<>(membros, pageable, membros.size());

        when(jwtUtils.getIgrejaId()).thenReturn(igrejaId);
        when(membroRepository.findByIgreja_IdExterno(igrejaId, pageable)).thenReturn(membroPage);
        when(membroMapper.paraMembroResponseDTO(membro1)).thenReturn(dto1);
        when(membroMapper.paraMembroResponseDTO(membro2)).thenReturn(dto2);

        Page<MembroResponseDTO> result = membroService.buscarTodosSemFiltro(pageable);

        assertNotNull(result);
        assertEquals(2, result.getTotalElements());
        assertEquals(dto1, result.getContent().get(0));
        assertEquals(dto2, result.getContent().get(1));
        verify(membroRepository).findByIgreja_IdExterno(igrejaId, pageable);
    }

    @Test
    @DisplayName("Deve buscar todos os membros com filtros aplicados")
    void buscarTodosComFiltro() {
        String termoBusca = "João";
        EnumStatusMembro status = EnumStatusMembro.ATIVO;
        UUID fkMinisterio = UUID.randomUUID();
        UUID igrejaId = UUID.randomUUID();

        Membro membro1 = new Membro();
        membro1.setStatus(EnumStatusMembro.ATIVO);
        membro1.setMinisterios(new HashSet<>());

        MembroMinisterio membroMinisterio = new MembroMinisterio();
        Ministerio ministerio = new Ministerio();
        ReflectionTestUtils.setField(ministerio, "idExterno", fkMinisterio);
        membroMinisterio.setMinisterio(ministerio);
        membro1.getMinisterios().add(membroMinisterio);

        Membro membro2 = new Membro();
        membro2.setStatus(EnumStatusMembro.ATIVO);
        membro2.setMinisterios(new HashSet<>());
        membro2.getMinisterios().add(membroMinisterio);

        MembroResponseDTO dto1 = new MembroResponseDTO(
                UUID.randomUUID(),
                "João Silva",
                "joao@hotmail.com",
                "11999999999",
                null,
                null,
                EnumStatusMembro.ATIVO
        );
        MembroResponseDTO dto2 = new MembroResponseDTO(
                UUID.randomUUID(),
                "João Santos",
                "joao.santos@hotmail.com",
                "11988888888",
                null,
                null,
                EnumStatusMembro.ATIVO
        );

        List<Membro> membrosEncontrados = List.of(membro1, membro2);
        List<MembroResponseDTO> responseDTOs = List.of(dto1, dto2);

        String buscaFormatada = "%" + termoBusca.toUpperCase() + "%";

        when(jwtUtils.getIgrejaId()).thenReturn(igrejaId);
        when(membroRepository.findAllWithFilter(buscaFormatada, igrejaId)).thenReturn(membrosEncontrados);
        when(membroMapper.paraMembrosResponseDTO(membrosEncontrados)).thenReturn(responseDTOs);

        Page<MembroResponseDTO> result = membroService.buscarTodosComFiltro(pageable, termoBusca, status, fkMinisterio);

        assertNotNull(result);
        assertEquals(2, result.getTotalElements());
        assertEquals(dto1, result.getContent().get(0));
        assertEquals(dto2, result.getContent().get(1));
        verify(membroRepository).findAllWithFilter(buscaFormatada, igrejaId);
        verify(membroMapper).paraMembrosResponseDTO(membrosEncontrados);
    }

    @Test
    @DisplayName("Deve criar um membro com sucesso")
    void criarMembro() {
        UUID fkIgreja = UUID.randomUUID();
        MembroCreateDTO membroCreateDTO = new MembroCreateDTO(
                fkIgreja,
                "João Silva",
                "12345678901",
                null,
                "joao@email.com",
                "123456789",
                "123456",
                null,
                EnumCargoMembro.MEMBRO,
                EnumGeneroMembro.FEMININO,
                null
        );

        Igreja igreja = new Igreja();
        Membro membro = new Membro();
        Membro membroSalvo = new Membro();
        ReflectionTestUtils.setField(membroSalvo, "idExterno", UUID.randomUUID());

        when(membroRepository.findByEmailOrCpf(membroCreateDTO.email(), membroCreateDTO.cpf())).thenReturn(null);
        when(membroMapper.paraMembro(membroCreateDTO)).thenReturn(membro);
        when(igrejaService.buscarUUID(fkIgreja)).thenReturn(igreja);
        when(passwordEncoder.encode(membroCreateDTO.senha())).thenReturn("senhaHasheada");
        when(membroRepository.save(membro)).thenReturn(membroSalvo);

        RestResponseMessage result = membroService.criarMembro(membroCreateDTO);

        assertNotNull(result);
        assertEquals("Usuário cadastrado com sucesso", result.getMessage());
        verify(membroRepository).findByEmailOrCpf(membroCreateDTO.email(), membroCreateDTO.cpf());
        verify(membroMapper).paraMembro(membroCreateDTO);
        verify(igrejaService).buscarUUID(fkIgreja);
        verify(passwordEncoder).encode(membroCreateDTO.senha());
        verify(membroRepository).save(membro);
    }

    @Test
    @DisplayName("Deve criar um membro sem ministério com sucesso")
    void criarMembrosemMinisterio() {
        UUID fkIgreja = UUID.randomUUID();
        MembroCreateDTO membroCreateDTO = new MembroCreateDTO(
                fkIgreja,
                "João Silva",
                "12345678901",
                null,
                "joao@email.com",
                "123456789",
                "123456",
                null,
                EnumCargoMembro.MEMBRO,
                EnumGeneroMembro.FEMININO,
                null
        );

        Igreja igreja = new Igreja();
        Membro membro = new Membro();
        Membro membroSalvo = new Membro();
        ReflectionTestUtils.setField(membroSalvo, "idExterno", UUID.randomUUID());

        when(membroRepository.findByEmailOrCpf(membroCreateDTO.email(), membroCreateDTO.cpf())).thenReturn(null);
        when(membroMapper.paraMembro(membroCreateDTO)).thenReturn(membro);
        when(igrejaService.buscarUUID(fkIgreja)).thenReturn(igreja);
        when(passwordEncoder.encode(membroCreateDTO.senha())).thenReturn("senhaHasheada");
        when(membroRepository.save(membro)).thenReturn(membroSalvo);

        RestResponseMessage result = membroService.criarMembro(membroCreateDTO);

        assertNotNull(result);
        assertEquals("Usuário cadastrado com sucesso", result.getMessage());
        verify(membroRepository).findByEmailOrCpf(membroCreateDTO.email(), membroCreateDTO.cpf());
        verify(membroMapper).paraMembro(membroCreateDTO);
        verify(igrejaService).buscarUUID(fkIgreja);
        verify(passwordEncoder).encode(membroCreateDTO.senha());
        verify(membroRepository).save(membro);
    }

    @Test
    @DisplayName("Deve criar um membro com ministério associado com sucesso")
    void criarMembrocomMinisterio() {
        UUID fkIgreja = UUID.randomUUID();
        UUID ministerioId = UUID.randomUUID();
        MembroCreateDTO membroCreateDTO = new MembroCreateDTO(
                fkIgreja,
                "João Silva",
                "12345678901",
                null,
                "joao@email.com",
                "123456789",
                "123456",
                ministerioId,
                EnumCargoMembro.MEMBRO,
                EnumGeneroMembro.FEMININO,
                null
        );

        Igreja igreja = new Igreja();
        Membro membro = new Membro();
        membro.setCargoMembro(EnumCargoMembro.MEMBRO);
        Membro membroSalvo = new Membro();
        membroSalvo.setCargoMembro(EnumCargoMembro.MEMBRO);
        ReflectionTestUtils.setField(membroSalvo, "idExterno", UUID.randomUUID());

        Ministerio ministerio = new Ministerio();
        ReflectionTestUtils.setField(ministerio, "idExterno", ministerioId);
        ministerio.setNome("Ministério de Louvor");

        when(membroRepository.findByEmailOrCpf(membroCreateDTO.email(), membroCreateDTO.cpf())).thenReturn(null);
        when(membroMapper.paraMembro(membroCreateDTO)).thenReturn(membro);
        when(igrejaService.buscarUUID(fkIgreja)).thenReturn(igreja);
        when(passwordEncoder.encode(membroCreateDTO.senha())).thenReturn("senhaHasheada");
        when(membroRepository.save(membro)).thenReturn(membroSalvo);
        when(ministerioService.buscarPorUUID(membroCreateDTO.idExternoMinisterios())).thenReturn(ministerio);

        RestResponseMessage result = membroService.criarMembro(membroCreateDTO);

        assertNotNull(result);
        assertEquals("Usuário cadastrado com sucesso", result.getMessage());
        verify(passwordEncoder).encode(membroCreateDTO.senha());
        verify(ministerioService).buscarPorUUID(membroCreateDTO.idExternoMinisterios());
        verify(membroMinisterioService).apagarMembroMinisterioPorMembro(membroSalvo);
        verify(membroMinisterioService).salvarTodos(any());
    }

    @Test
    @DisplayName("Deve lançar exceção quando dados do membro são nulos")
    void criarMembrodadosNulosdeveRetornarErro() {
        assertThrows(ObjectSaveErrorException.class, () -> membroService.criarMembro(null));
    }

    @Test
    @DisplayName("Deve lançar exceção quando email já existe")
    void criarMembroEmailJaExistenteDeveRetornarErro() {
        UUID fkIgreja = UUID.randomUUID();
        MembroCreateDTO membroCreateDTO = new MembroCreateDTO(
                fkIgreja,
                "João Silva",
                "12345678901",
                null,
                "joao@email.com",
                "123456789",
                "123456",
                null,
                EnumCargoMembro.MEMBRO,
                EnumGeneroMembro.FEMININO,
                null
        );

        Membro membroExistente = new Membro();

        when(membroRepository.findByEmailOrCpf(membroCreateDTO.email(), membroCreateDTO.cpf())).thenReturn(membroExistente);

        assertThrows(ObjectExistsException.class, () -> membroService.criarMembro(membroCreateDTO));
    }

    @Test
    @DisplayName("Deve lançar exceção quando líder não tem ministério associado")
    void criarMembroLiderSemMinisterioDeveRetornarErro() {
        UUID fkIgreja = UUID.randomUUID();
        MembroCreateDTO membroCreateDTO = new MembroCreateDTO(
                fkIgreja,
                "João Silva",
                "12345678901",
                null,
                "joao@email.com",
                "123456789",
                "123456",
                null,
                EnumCargoMembro.LIDER_MINISTERIO,
                EnumGeneroMembro.FEMININO,
                null
        );

        assertThrows(ObjectSaveErrorException.class, () -> membroService.criarMembro(membroCreateDTO));
    }

    @Test
    @DisplayName("Deve buscar membro por UUID com sucesso")
    void buscarPorUUIDMembroEncontrado() {
        UUID idExterno = UUID.randomUUID();
        Membro membro = new Membro();
        ReflectionTestUtils.setField(membro, "idExterno", idExterno);

        when(membroRepository.findByIdExterno(idExterno)).thenReturn(membro);

        Membro result = membroService.buscarPorUUID(idExterno);

        assertNotNull(result);
        assertEquals(membro, result);
        verify(membroRepository).findByIdExterno(idExterno);
    }

    @Test
    @DisplayName("Deve lançar exceção quando membro não é encontrado por UUID")
    void buscarPorUUIDMembroNaoEncontradoDeveRetornarErro() {
        UUID idExterno = UUID.randomUUID();

        when(membroRepository.findByIdExterno(idExterno)).thenReturn(null);

        assertThrows(ObjectNotFoundException.class, () -> membroService.buscarPorUUID(idExterno));
    }

    @Test
    @DisplayName("Deve criar membro externo com sucesso")
    void criarMembroExternoSucesso() {
        UUID fkIgreja = UUID.randomUUID();
        CadastroExternoDTO cadastroDTO = new CadastroExternoDTO(
                fkIgreja,
                "João Silva",
                "12345678901",
                null,
                "joao@email.com",
                "123456789",
                "123456",
                EnumGeneroMembro.FEMININO,
                null
        );

        Igreja igreja = new Igreja();
        Membro membro = new Membro();
        Membro membroSalvo = new Membro();
        ReflectionTestUtils.setField(membroSalvo, "idExterno", UUID.randomUUID());

        when(membroRepository.findByEmailOrCpf(cadastroDTO.email(), cadastroDTO.cpf())).thenReturn(null);
        when(membroMapper.paraMembro(cadastroDTO)).thenReturn(membro);
        when(igrejaService.buscarUUID(fkIgreja)).thenReturn(igreja);
        when(passwordEncoder.encode(cadastroDTO.senha())).thenReturn("senhaHasheada");
        when(membroRepository.save(membro)).thenReturn(membroSalvo);

        Membro result = membroService.criarMembroExterno(cadastroDTO);

        assertNotNull(result);
        assertEquals(membroSalvo, result);
        verify(membroRepository).findByEmailOrCpf(cadastroDTO.email(), cadastroDTO.cpf());
        verify(membroMapper).paraMembro(cadastroDTO);
        verify(igrejaService).buscarUUID(fkIgreja);
        verify(passwordEncoder).encode(cadastroDTO.senha());
        verify(membroRepository).save(membro);
    }

    @Test
    @DisplayName("Deve lançar exceção quando dados do membro externo são nulos")
    void criarMembroExternoDadosNulosDeveRetornarErro() {

        assertThrows(ObjectSaveErrorException.class, () -> membroService.criarMembroExterno(null));
    }

    @Test
    @DisplayName("Deve lançar exceção quando email do membro externo já existe")
    void criarMembroExternoEmailJaExisteDeveRetornarErro() {
        UUID fkIgreja = UUID.randomUUID();
        CadastroExternoDTO cadastroDTO = new CadastroExternoDTO(
                fkIgreja,
                "João Silva",
                "12345678901",
                null,
                "joao@email.com",
                "123456789",
                "123456",
                EnumGeneroMembro.FEMININO,
                null
        );

        Membro membroExistente = new Membro();

        when(membroRepository.findByEmailOrCpf(cadastroDTO.email(), cadastroDTO.cpf())).thenReturn(membroExistente);

        assertThrows(ObjectExistsException.class, () -> membroService.criarMembroExterno(cadastroDTO));
    }

    @Test
    @DisplayName("Deve buscar membro por email com sucesso")
    void buscarPorEmailMembroEncontrado() {
        String email = "joao@email.com";
        Membro membro = new Membro();
        membro.setEmail(email);

        when(membroRepository.findByEmail(email)).thenReturn(membro);

        Membro result = membroService.buscarPorEmail(email);

        assertEquals(membro, result);
        verify(membroRepository).findByEmail(email);
    }

    @Test
    @DisplayName("Deve retornar null quando membro não é encontrado por email")
    void buscarPorEmailMembroNaoEncontrado() {

        String email = "naoexiste@email.com";

        when(membroRepository.findByEmail(email)).thenReturn(null);

        Membro result = membroService.buscarPorEmail(email);

        assertNull(result);
        verify(membroRepository).findByEmail(email);
    }

    @Test
    @DisplayName("Deve salvar membro com sucesso")
    void salvarMembroSucesso() {
        Membro membro = new Membro();
        membro.setNome("João Silva");

        Membro membroSalvo = new Membro();
        membroSalvo.setNome("João Silva");
        ReflectionTestUtils.setField(membroSalvo, "idExterno", UUID.randomUUID());

        when(membroRepository.save(membro)).thenReturn(membroSalvo);

        Membro result = membroService.salvarMembro(membro);

        assertNotNull(result);
        assertEquals(membroSalvo, result);
        verify(membroRepository).save(membro);
        verify(membroRepository).flush();
    }

    @Test
    @DisplayName("Deve buscar membros com resultados encontrados")
    void buscaMembrosComResultadosSucesso() {
        String termoBusca = "João";
        String buscaFormatada = "%" + termoBusca.toUpperCase() + "%";
        UUID igrejaId = UUID.randomUUID();

        Membro membro1 = new Membro();
        membro1.setNome("João Silva");
        List<Membro> membrosEncontrados = List.of(membro1);

        when(jwtUtils.getIgrejaId()).thenReturn(igrejaId);
        when(membroRepository.findAllWithFilter(buscaFormatada, igrejaId)).thenReturn(membrosEncontrados);

        List<Membro> result = (List<Membro>) ReflectionTestUtils.invokeMethod(membroService, "buscaMembros", termoBusca);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(membro1, result.get(0));
        verify(membroRepository).findAllWithFilter(buscaFormatada, igrejaId);
    }

    @Test
    @DisplayName("Deve lançar exceção quando busca de membros não retorna resultados")
    void buscaMembrosSemFiltroNaoEncontrado() {
        String termoBusca = "Inexistente";
        String buscaFormatada = "%" + termoBusca.toUpperCase() + "%";
        UUID igrejaId = UUID.randomUUID();

        when(jwtUtils.getIgrejaId()).thenReturn(igrejaId);
        when(membroRepository.findAllWithFilter(buscaFormatada, igrejaId)).thenReturn(List.of());

        assertThrows(ObjectNotFoundException.class, () ->
            ReflectionTestUtils.invokeMethod(membroService, "buscaMembros", termoBusca));
    }

    @Test
    @DisplayName("Deve buscar todos os membros com filtro sem status mas com ministério")
    void buscarTodosComFiltroSemStatusComMinisterio() {
        String termoBusca = "João";
        EnumStatusMembro status = null;
        UUID fkMinisterio = UUID.randomUUID();
        UUID igrejaId = UUID.randomUUID();

        Membro membro1 = new Membro();
        membro1.setStatus(EnumStatusMembro.ATIVO);
        membro1.setMinisterios(new HashSet<>());

        MembroMinisterio membroMinisterio = new MembroMinisterio();
        Ministerio ministerio = new Ministerio();
        ReflectionTestUtils.setField(ministerio, "idExterno", fkMinisterio);
        membroMinisterio.setMinisterio(ministerio);
        membro1.getMinisterios().add(membroMinisterio);

        MembroResponseDTO dto1 = new MembroResponseDTO(
                UUID.randomUUID(),
                "João Silva",
                "joao@hotmail.com",
                "11999999999",
                null,
                null,
                EnumStatusMembro.ATIVO
        );

        List<Membro> membrosEncontrados = List.of(membro1);
        List<MembroResponseDTO> responseDTOs = List.of(dto1);

        String buscaFormatada = "%" + termoBusca.toUpperCase() + "%";

        when(jwtUtils.getIgrejaId()).thenReturn(igrejaId);
        when(membroRepository.findAllWithFilter(buscaFormatada, igrejaId)).thenReturn(membrosEncontrados);
        when(membroMapper.paraMembrosResponseDTO(membrosEncontrados)).thenReturn(responseDTOs);

        Page<MembroResponseDTO> result = membroService.buscarTodosComFiltro(pageable, termoBusca, status, fkMinisterio);

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        assertEquals(dto1, result.getContent().get(0));
        verify(membroRepository).findAllWithFilter(buscaFormatada, igrejaId);
        verify(membroMapper).paraMembrosResponseDTO(membrosEncontrados);
    }

    @Test
    @DisplayName("Deve lançar exceção quando nenhum membro passa nos filtros aplicados")
    void buscarTodosComFiltroNenhumMembroPassaNosFiltos() {
        String termoBusca = "João";
        EnumStatusMembro status = EnumStatusMembro.INATIVO;
        UUID fkMinisterio = UUID.randomUUID();
        UUID igrejaId = UUID.randomUUID();

        Membro membro1 = new Membro();
        membro1.setStatus(EnumStatusMembro.ATIVO);
        membro1.setMinisterios(new HashSet<>());

        List<Membro> membrosEncontrados = List.of(membro1);
        String buscaFormatada = "%" + termoBusca.toUpperCase() + "%";

        when(jwtUtils.getIgrejaId()).thenReturn(igrejaId);
        when(membroRepository.findAllWithFilter(buscaFormatada, igrejaId)).thenReturn(membrosEncontrados);

        assertThrows(ObjectNotFoundException.class, () ->
            membroService.buscarTodosComFiltro(pageable, termoBusca, status, fkMinisterio));
    }
}
