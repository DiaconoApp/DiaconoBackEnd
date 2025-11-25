package com.diacono.diacono.ministerio.service;

import com.diacono.diacono.global.dto.response.RestResponseMessage;
import com.diacono.diacono.global.error.exceptions.FieldInvalidException;
import com.diacono.diacono.global.error.exceptions.ObjectNotFoundException;
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
import com.diacono.diacono.ministerio.model.dto.response.MinisterioSimplificadoDTO;
import com.diacono.diacono.ministerio.model.entity.EnumStatusMinisterio;
import com.diacono.diacono.ministerio.model.entity.Ministerio;
import com.diacono.diacono.ministerio.repository.MinisteriosRepository;
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
import org.springframework.http.HttpStatus;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MinisterioServiceTest {

    @Mock
    private MinisteriosRepository ministeriosRepository;

    @Mock
    private MinisterioMapper ministerioMapper;

    @Mock
    private MembroMinisterioService membroMinisterioService;

    @Mock
    private MembroRepository membroRepository;

    @InjectMocks
    private MinisterioService ministerioService;

    private Pageable pageable;
    private UUID ministerioId;
    private UUID liderId;
    private UUID membroId;

    @BeforeEach
    void setUp() {
        pageable = PageRequest.of(0, 10);
        ministerioId = UUID.randomUUID();
        liderId = UUID.randomUUID();
        membroId = UUID.randomUUID();
    }

    @Test
    @DisplayName("Deve buscar ministérios do governo com sucesso")
    void buscarMinisteriosGovernoSucesso() {
        Ministerio ministerio1 = new Ministerio();
        ministerio1.setNome("Ministério de Louvor");
        
        Ministerio ministerio2 = new Ministerio();
        ministerio2.setNome("Ministério de Diaconia");

        List<Ministerio> ministerios = Arrays.asList(ministerio1, ministerio2);
        Page<Ministerio> ministeriosPage = new PageImpl<>(ministerios, pageable, ministerios.size());

        MinisterioSimplificadoDTO dto1 = new MinisterioSimplificadoDTO(
            UUID.randomUUID(), "Ministério de Louvor", "João Silva", 
            EnumStatusMinisterio.ATIVO, LocalDate.now()
        );
        MinisterioSimplificadoDTO dto2 = new MinisterioSimplificadoDTO(
            UUID.randomUUID(), "Ministério de Diaconia", "Maria Santos", 
            EnumStatusMinisterio.ATIVO, LocalDate.now()
        );

        when(ministeriosRepository.findAll(pageable)).thenReturn(ministeriosPage);
        when(ministerioMapper.paraMinisterioSimplificadoDTO(ministerio1)).thenReturn(dto1);
        when(ministerioMapper.paraMinisterioSimplificadoDTO(ministerio2)).thenReturn(dto2);

        Page<MinisterioSimplificadoDTO> result = ministerioService.buscarMinisteriosGoverno(pageable);

        assertNotNull(result);
        assertEquals(2, result.getTotalElements());
        assertEquals("Ministério de Louvor", result.getContent().get(0).nome());
        assertEquals("Ministério de Diaconia", result.getContent().get(1).nome());
        verify(ministeriosRepository).findAll(pageable);
    }

    @Test
    @DisplayName("Deve lançar exceção quando nenhum ministério é encontrado no governo")
    void buscarMinisteriosGovernoNenhumEncontradoDeveRetornarErro() {
        Page<Ministerio> ministeriosPageVazia = new PageImpl<>(Collections.emptyList(), pageable, 0);

        when(ministeriosRepository.findAll(pageable)).thenReturn(ministeriosPageVazia);

        assertThrows(ObjectNotFoundException.class,
            () -> ministerioService.buscarMinisteriosGoverno(pageable));
    }

    @Test
    @DisplayName("Deve buscar ministérios do governo com filtro com sucesso")
    void buscarMinisteriosGovernoComFiltroSucesso() {
        String buscaGeral = "Louvor";
        EnumStatusMinisterio status = EnumStatusMinisterio.ATIVO;
        String stringBusca = "%" + buscaGeral.trim().toUpperCase() + "%";

        Ministerio ministerio = new Ministerio();
        ministerio.setNome("Ministério de Louvor");

        List<Ministerio> ministerios = Collections.singletonList(ministerio);
        Page<Ministerio> ministeriosPage = new PageImpl<>(ministerios, pageable, ministerios.size());

        MinisterioSimplificadoDTO dto = new MinisterioSimplificadoDTO(
            UUID.randomUUID(), "Ministério de Louvor", "João Silva", 
            EnumStatusMinisterio.ATIVO, LocalDate.now()
        );

        when(ministeriosRepository.buscarComFiltros(pageable, stringBusca, status)).thenReturn(ministeriosPage);
        when(ministerioMapper.paraMinisterioSimplificadoDTO(ministerio)).thenReturn(dto);

        Page<MinisterioSimplificadoDTO> result = ministerioService.buscarMinisteriosGovernoComFiltro(pageable, buscaGeral, status);

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        assertEquals("Ministério de Louvor", result.getContent().get(0).nome());
        verify(ministeriosRepository).buscarComFiltros(pageable, stringBusca, status);
    }

    @Test
    @DisplayName("Deve lançar exceção quando nenhum ministério é encontrado com filtro")
    void buscarMinisteriosGovernoComFiltroNenhumEncontradoDeveRetornarErro() {
        String buscaGeral = "Inexistente";
        EnumStatusMinisterio status = EnumStatusMinisterio.ATIVO;
        String stringBusca = "%" + buscaGeral.trim().toUpperCase() + "%";
        Page<Ministerio> ministeriosPageVazia = new PageImpl<>(Collections.emptyList(), pageable, 0);

        when(ministeriosRepository.buscarComFiltros(pageable, stringBusca, status)).thenReturn(ministeriosPageVazia);

        assertThrows(ObjectNotFoundException.class,
            () -> ministerioService.buscarMinisteriosGovernoComFiltro(pageable, buscaGeral, status));
    }

    @Test
    @DisplayName("Deve criar ministério com sucesso")
    void criarMinisterioSucesso() {
        MinisterioCreateDTO ministerioCreateDTO = new MinisterioCreateDTO(liderId, "Ministério de Louvor");

        Membro liderMinisterio = new Membro();
        liderMinisterio.setNome("João Silva");
        ReflectionTestUtils.setField(liderMinisterio, "idExterno", liderId);

        Ministerio novoMinisterio = new Ministerio();
        novoMinisterio.setMembros(new HashSet<>());

        when(membroRepository.findByIdExterno(liderId)).thenReturn(liderMinisterio);
        when(ministeriosRepository.save(any(Ministerio.class))).thenReturn(novoMinisterio);

        RestResponseMessage result = ministerioService.criarMinisterio(ministerioCreateDTO);

        assertNotNull(result);
        assertEquals(HttpStatus.CREATED, result.getStatus());
        assertEquals("Ministério criado com sucesso", result.getMessage());
        verify(membroRepository).findByIdExterno(liderId);
        verify(ministeriosRepository).save(any(Ministerio.class));
    }

    @Test
    @DisplayName("Deve lançar exceção quando líder não é encontrado ao criar ministério")
    void criarMinisterioLiderNaoEncontradoDeveRetornarErro() {
        MinisterioCreateDTO ministerioCreateDTO = new MinisterioCreateDTO(liderId, "Ministério de Louvor");

        when(membroRepository.findByIdExterno(liderId)).thenReturn(null);

        assertThrows(ObjectNotFoundException.class,
            () -> ministerioService.criarMinisterio(ministerioCreateDTO));
    }

    @Test
    @DisplayName("Deve editar ministério com sucesso")
    void editarMinisterioSucesso() {
        UUID novoLiderId = UUID.randomUUID();
        MinisterioUpdateDTO ministerioUpdateDTO = new MinisterioUpdateDTO(
            "Novo Nome Ministério", EnumStatusMinisterio.INATIVO, novoLiderId
        );

        Ministerio ministerioExistente = new Ministerio();
        ministerioExistente.setNome("Nome Antigo");
        ministerioExistente.setStatus(EnumStatusMinisterio.ATIVO);

        Membro liderAtual = new Membro();
        liderAtual.setNome("Líder Atual");

        MembroMinisterio membroMinisterioLider = new MembroMinisterio();
        membroMinisterioLider.setMembro(liderAtual);
        membroMinisterioLider.setCargoMembro(EnumCargoMembroMinisterio.LIDER_MINISTERIO);

        Set<MembroMinisterio> membros = new HashSet<>();
        membros.add(membroMinisterioLider);
        ministerioExistente.setMembros(membros);

        Membro novoLider = new Membro();
        novoLider.setNome("Novo Líder");

        when(ministeriosRepository.findByIdExterno(ministerioId)).thenReturn(ministerioExistente);
        when(membroRepository.findByIdExterno(novoLiderId)).thenReturn(novoLider);
        when(ministeriosRepository.save(ministerioExistente)).thenReturn(ministerioExistente);

        RestResponseMessage result = ministerioService.editarMinisterio(ministerioUpdateDTO, ministerioId);

        assertNotNull(result);
        assertEquals(HttpStatus.OK, result.getStatus());
        assertEquals("Ministério atualizado com sucesso", result.getMessage());
        assertEquals("Novo Nome Ministério", ministerioExistente.getNome());
        assertEquals(EnumStatusMinisterio.INATIVO, ministerioExistente.getStatus());
        verify(ministeriosRepository).save(ministerioExistente);
    }

    @Test
    @DisplayName("Deve lançar exceção quando ministério não é encontrado ao editar")
    void editarMinisterioNaoEncontradoDeveRetornarErro() {
        MinisterioUpdateDTO ministerioUpdateDTO = new MinisterioUpdateDTO(
            "Novo Nome", EnumStatusMinisterio.ATIVO, liderId
        );

        when(ministeriosRepository.findByIdExterno(ministerioId)).thenReturn(null);

        assertThrows(ObjectNotFoundException.class,
            () -> ministerioService.editarMinisterio(ministerioUpdateDTO, ministerioId));
    }

    @Test
    @DisplayName("Deve buscar membros do ministério para líder com sucesso")
    void buscarMembroMinisterioLiderMinisterioSucesso() {
        MembroMinisterioDTO dto1 = mock(MembroMinisterioDTO.class);
        MembroMinisterioDTO dto2 = mock(MembroMinisterioDTO.class);
        List<MembroMinisterioDTO> dtos = Arrays.asList(dto1, dto2);
        Page<MembroMinisterioDTO> expectedResult = new PageImpl<>(dtos, pageable, dtos.size());

        when(membroMinisterioService.buscarPorMembroMinisterioSemFiltro(ministerioId, pageable))
            .thenReturn((Page<MembroMinisterioDTO>) expectedResult);

        Page<MembroMinisterioDTO> result = ministerioService.buscarMembroMinisterioLiderMinisterio(ministerioId, pageable);

        assertNotNull(result);
        assertEquals(2, result.getTotalElements());
        verify(membroMinisterioService).buscarPorMembroMinisterioSemFiltro(ministerioId, pageable);
    }

    @Test
    @DisplayName("Deve buscar membros do ministério com filtro para líder com sucesso")
    void buscarMembroMinisterioLiderMinisterioComFiltroSucesso() {
        String texto = "João";
        EnumStatusMembro status = EnumStatusMembro.ATIVO;
        
        MembroMinisterioDTO dto = mock(MembroMinisterioDTO.class);
        List<MembroMinisterioDTO> dtos = Collections.singletonList(dto);
        Page<MembroMinisterioDTO> expectedResult = new PageImpl<>(dtos, pageable, dtos.size());

        when(membroMinisterioService.buscarPorMembroMinisterioComFiltro(ministerioId, pageable, texto, status))
            .thenReturn((Page<MembroMinisterioDTO>) expectedResult);

        Page<MembroMinisterioDTO> result = ministerioService.buscarMembroMinisterioLiderMinisterioComFiltro(
            ministerioId, pageable, texto, status
        );

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        verify(membroMinisterioService).buscarPorMembroMinisterioComFiltro(ministerioId, pageable, texto, status);
    }

    @Test
    @DisplayName("Deve adicionar membro ao ministério com sucesso")
    void adicionarMembroMinisterioLiderMinisterioSucesso() {
        MembroMinisterioCreateDTO dto = new MembroMinisterioCreateDTO(membroId);
        Long idMinisterioLong = 1L;
        Long idMembroLong = 2L;

        when(ministeriosRepository.buscarIdPorUUID(ministerioId)).thenReturn(idMinisterioLong);
        when(membroRepository.buscarIdPorUUID(membroId)).thenReturn(idMembroLong);

        RestResponseMessage result = ministerioService.adicionarMembroMinisterioLiderMinisterio(ministerioId, dto);

        assertNotNull(result);
        assertEquals(HttpStatus.OK, result.getStatus());
        assertEquals("Membro adicionado ao ministério com sucesso", result.getMessage());
        verify(membroMinisterioService).adicionarMembroMinisterioLiderMinisterio(idMinisterioLong, idMembroLong);
    }

    @Test
    @DisplayName("Deve lançar exceção quando dados do membro são nulos ao adicionar")
    void adicionarMembroMinisterioLiderMinisterioDadosNulosDeveRetornarErro() {
        assertThrows(FieldInvalidException.class,
            () -> ministerioService.adicionarMembroMinisterioLiderMinisterio(ministerioId, null));
    }

    @Test
    @DisplayName("Deve lançar exceção quando ministério não é encontrado ao adicionar membro")
    void adicionarMembroMinisterioLiderMinisterioMinisterioNaoEncontradoDeveRetornarErro() {
        MembroMinisterioCreateDTO dto = new MembroMinisterioCreateDTO(membroId);

        when(ministeriosRepository.buscarIdPorUUID(ministerioId)).thenReturn(null);

        assertThrows(ObjectNotFoundException.class,
            () -> ministerioService.adicionarMembroMinisterioLiderMinisterio(ministerioId, dto));
    }

    @Test
    @DisplayName("Deve lançar exceção quando membro não é encontrado ao adicionar ao ministério")
    void adicionarMembroMinisterioLiderMinisterioMembroNaoEncontradoDeveRetornarErro() {
        MembroMinisterioCreateDTO dto = new MembroMinisterioCreateDTO(membroId);
        Long idMinisterioLong = 1L;

        when(ministeriosRepository.buscarIdPorUUID(ministerioId)).thenReturn(idMinisterioLong);
        when(membroRepository.buscarIdPorUUID(membroId)).thenReturn(null);

        assertThrows(ObjectNotFoundException.class,
            () -> ministerioService.adicionarMembroMinisterioLiderMinisterio(ministerioId, dto));
    }

    @Test
    @DisplayName("Deve remover membro do ministério com sucesso")
    void removerMembroMinisterioLiderMinisterioSucesso() {
        UUID idMembroMinisterio = UUID.randomUUID();

        RestResponseMessage result = ministerioService.removerMembroMinisterioLiderMinisterio(ministerioId, idMembroMinisterio);

        assertNotNull(result);
        assertEquals(HttpStatus.OK, result.getStatus());
        assertEquals("Membro removido do ministério com sucesso", result.getMessage());
        verify(membroMinisterioService).removerMembroMinisterioLiderMinisterio(ministerioId, idMembroMinisterio);
    }

    @Test
    @DisplayName("Deve buscar ministérios por UUID com sucesso")
    void buscarPorUUIDSucesso() {
        List<UUID> idsExternos = Arrays.asList(ministerioId, UUID.randomUUID());

        Ministerio ministerio1 = new Ministerio();
        ministerio1.setNome("Ministério 1");

        Ministerio ministerio2 = new Ministerio();
        ministerio2.setNome("Ministério 2");

        Set<Ministerio> ministeriosEncontrados = new HashSet<>(Arrays.asList(ministerio1, ministerio2));

        when(ministeriosRepository.findAllByIdExternoIn(idsExternos)).thenReturn(ministeriosEncontrados);

        Set<Ministerio> result = ministerioService.buscarPorUUID(idsExternos);

        assertNotNull(result);
        assertEquals(2, result.size());
        assertTrue(result.contains(ministerio1));
        assertTrue(result.contains(ministerio2));
        verify(ministeriosRepository).findAllByIdExternoIn(idsExternos);
    }

    @Test
    @DisplayName("Deve lançar exceção quando nenhum ministério é encontrado por UUID")
    void buscarPorUUIDNenhumEncontradoDeveRetornarErro() {
        List<UUID> idsExternos = Arrays.asList(ministerioId, UUID.randomUUID());
        Set<Ministerio> ministeriosVazios = new HashSet<>();

        when(ministeriosRepository.findAllByIdExternoIn(idsExternos)).thenReturn(ministeriosVazios);

        assertThrows(ObjectNotFoundException.class,
            () -> ministerioService.buscarPorUUID(idsExternos));
    }
}
