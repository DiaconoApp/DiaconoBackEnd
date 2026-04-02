package com.diacono.diacono.ministerio.service;

import com.diacono.diacono.applications.dtos.RestResponseMessageDTO;
import com.diacono.diacono.global.error.exceptions.FieldInvalidException;
import com.diacono.diacono.global.error.exceptions.ObjectNotFoundException;
import com.diacono.diacono.global.util.JwtUtils;
import com.diacono.diacono.domain.enums.EnumStatusMembro;
import com.diacono.diacono.domain.entity.Membro;
import com.diacono.diacono.infrastructure.persistence.Membro.MembroJpaRepository;
import com.diacono.diacono.applications.dtos.membro.MembroMinisterioCreateDTO;
import com.diacono.diacono.applications.dtos.membro.MembroMinisterioInfoMembroDTO;
import com.diacono.diacono.domain.enums.EnumCargoMembroMinisterio;
import com.diacono.diacono.domain.entity.MembroMinisterio;
import com.diacono.diacono.use_cases.MembroMinisterioService;
import com.diacono.diacono.applications.mappers.ministerio.MinisterioMapper;
import com.diacono.diacono.applications.dtos.ministerio.MinisterioCreateDTO;
import com.diacono.diacono.applications.dtos.ministerio.MinisterioUpdateDTO;
import com.diacono.diacono.applications.dtos.ministerio.MinisterioSimplificadoDTO;
import com.diacono.diacono.domain.enums.EnumStatusMinisterio;
import com.diacono.diacono.domain.entity.Ministerio;
import com.diacono.diacono.infrastructure.persistence.Ministerios.MinisteriosJpaRepository;
import com.diacono.diacono.use_cases.MinisterioService;
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
    private JwtUtils jwtUtils;

    @Mock
    private MinisteriosJpaRepository ministeriosJpaRepository;

    @Mock
    private MinisterioMapper ministerioMapper;

    @Mock
    private MembroMinisterioService membroMinisterioService;

    @Mock
    private MembroJpaRepository membroJpaRepository;

    @InjectMocks
    private MinisterioService ministerioService;

    private Pageable pageable;
    private UUID ministerioId;
    private UUID liderId;
    private UUID membroId;
    private UUID igrejaId;

    @BeforeEach
    void setUp() {
        pageable = PageRequest.of(0, 10);
        ministerioId = UUID.randomUUID();
        liderId = UUID.randomUUID();
        membroId = UUID.randomUUID();
        igrejaId = UUID.randomUUID();
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

        when(jwtUtils.getIgrejaId()).thenReturn(igrejaId);
        when(ministeriosJpaRepository.findByIgreja_IdExterno(igrejaId, pageable)).thenReturn(ministeriosPage);
        when(ministerioMapper.paraMinisterioSimplificadoDTO(ministerio1)).thenReturn(dto1);
        when(ministerioMapper.paraMinisterioSimplificadoDTO(ministerio2)).thenReturn(dto2);

        Page<MinisterioSimplificadoDTO> result = ministerioService.buscarMinisteriosGoverno(pageable);

        assertNotNull(result);
        assertEquals(2, result.getTotalElements());
        assertEquals("Ministério de Louvor", result.getContent().get(0).nome());
        assertEquals("Ministério de Diaconia", result.getContent().get(1).nome());
        verify(ministeriosJpaRepository).findByIgreja_IdExterno(igrejaId, pageable);
    }

    @Test
    @DisplayName("Deve lançar exceção quando nenhum ministério é encontrado no governo")
    void buscarMinisteriosGovernoNenhumEncontradoDeveRetornarErro() {
        Page<Ministerio> ministeriosPageVazia = new PageImpl<>(Collections.emptyList(), pageable, 0);

        when(jwtUtils.getIgrejaId()).thenReturn(igrejaId);
        when(ministeriosJpaRepository.findByIgreja_IdExterno(igrejaId, pageable)).thenReturn(ministeriosPageVazia);

        assertThrows(ObjectNotFoundException.class,
            () -> ministerioService.buscarMinisteriosGoverno(pageable));
    }

    @Test
    @DisplayName("Deve buscar ministérios do governo com filtro com sucesso")
    void buscarMinisteriosGovernoComFiltroSucesso() {
        String buscaGeral = "Louvor";
        EnumStatusMinisterio status = EnumStatusMinisterio.ATIVO;
        String stringBusca = "%" + buscaGeral.trim().toUpperCase() + "%";
        UUID igrejaId = UUID.randomUUID();

        Ministerio ministerio = new Ministerio();
        ministerio.setNome("Ministério de Louvor");

        List<Ministerio> ministerios = Collections.singletonList(ministerio);
        Page<Ministerio> ministeriosPage = new PageImpl<>(ministerios, pageable, ministerios.size());

        MinisterioSimplificadoDTO dto = new MinisterioSimplificadoDTO(
            UUID.randomUUID(), "Ministério de Louvor", "João Silva", 
            EnumStatusMinisterio.ATIVO, LocalDate.now()
        );

        when(jwtUtils.getIgrejaId()).thenReturn(igrejaId);
        when(ministeriosJpaRepository.buscarComFiltros(pageable, stringBusca, status, igrejaId)).thenReturn(ministeriosPage);
        when(ministerioMapper.paraMinisterioSimplificadoDTO(ministerio)).thenReturn(dto);

        Page<MinisterioSimplificadoDTO> result = ministerioService.buscarMinisteriosGovernoComFiltro(pageable, buscaGeral, status);

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        assertEquals("Ministério de Louvor", result.getContent().get(0).nome());
        verify(ministeriosJpaRepository).buscarComFiltros(pageable, stringBusca, status, igrejaId);
    }

    @Test
    @DisplayName("Deve lançar exceção quando nenhum ministério é encontrado com filtro")
    void buscarMinisteriosGovernoComFiltroNenhumEncontradoDeveRetornarErro() {
        String buscaGeral = "Inexistente";
        EnumStatusMinisterio status = EnumStatusMinisterio.ATIVO;
        String stringBusca = "%" + buscaGeral.trim().toUpperCase() + "%";
        UUID igrejaId = UUID.randomUUID();

        Page<Ministerio> ministeriosPageVazia = new PageImpl<>(Collections.emptyList(), pageable, 0);

        when(jwtUtils.getIgrejaId()).thenReturn(igrejaId);
        when(ministeriosJpaRepository.buscarComFiltros(pageable, stringBusca, status, igrejaId)).thenReturn(ministeriosPageVazia);

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

        when(membroJpaRepository.findByIdExterno(liderId)).thenReturn(liderMinisterio);
        when(ministeriosJpaRepository.save(any(Ministerio.class))).thenReturn(novoMinisterio);

        RestResponseMessageDTO result = ministerioService.criarMinisterio(ministerioCreateDTO);

        assertNotNull(result);
        assertEquals(HttpStatus.CREATED, result.getStatus());
        assertEquals("Ministério criado com sucesso", result.getMessage());
        verify(membroJpaRepository).findByIdExterno(liderId);
        verify(ministeriosJpaRepository).save(any(Ministerio.class));
    }

    @Test
    @DisplayName("Deve lançar exceção quando líder não é encontrado ao criar ministério")
    void criarMinisterioLiderNaoEncontradoDeveRetornarErro() {
        MinisterioCreateDTO ministerioCreateDTO = new MinisterioCreateDTO(liderId, "Ministério de Louvor");

        when(membroJpaRepository.findByIdExterno(liderId)).thenReturn(null);

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
        ReflectionTestUtils.setField(liderAtual, "idExterno", UUID.randomUUID());

        MembroMinisterio membroMinisterioLider = new MembroMinisterio();
        membroMinisterioLider.setMembro(liderAtual);
        membroMinisterioLider.setCargoMembro(EnumCargoMembroMinisterio.LIDER_MINISTERIO);

        Set<MembroMinisterio> membros = new HashSet<>();
        membros.add(membroMinisterioLider);
        ministerioExistente.setMembros(membros);

        Membro novoLider = new Membro();
        novoLider.setNome("Novo Líder");
        ReflectionTestUtils.setField(novoLider, "idExterno", novoLiderId);

        when(ministeriosJpaRepository.findByIdExterno(ministerioId)).thenReturn(ministerioExistente);
        when(membroJpaRepository.findByIdExterno(novoLiderId)).thenReturn(novoLider);
        when(ministeriosJpaRepository.save(ministerioExistente)).thenReturn(ministerioExistente);

        RestResponseMessageDTO result = ministerioService.editarMinisterio(ministerioUpdateDTO, ministerioId);

        assertNotNull(result);
        assertEquals(HttpStatus.OK, result.getStatus());
        assertEquals("Ministério atualizado com sucesso", result.getMessage());
        assertEquals("Novo Nome Ministério", ministerioExistente.getNome());
        assertEquals(EnumStatusMinisterio.INATIVO, ministerioExistente.getStatus());
        verify(ministeriosJpaRepository).save(ministerioExistente);
    }

    @Test
    @DisplayName("Deve lançar exceção quando ministério não é encontrado ao editar")
    void editarMinisterioNaoEncontradoDeveRetornarErro() {
        MinisterioUpdateDTO ministerioUpdateDTO = new MinisterioUpdateDTO(
            "Novo Nome", EnumStatusMinisterio.ATIVO, liderId
        );

        when(ministeriosJpaRepository.findByIdExterno(ministerioId)).thenReturn(null);

        assertThrows(ObjectNotFoundException.class,
            () -> ministerioService.editarMinisterio(ministerioUpdateDTO, ministerioId));
    }

    @Test
    @DisplayName("Deve buscar membros do ministério para líder com sucesso")
    void buscarMembroMinisterioLiderMinisterioSucesso() {
        MembroMinisterioInfoMembroDTO dto1 = mock(MembroMinisterioInfoMembroDTO.class);
        MembroMinisterioInfoMembroDTO dto2 = mock(MembroMinisterioInfoMembroDTO.class);
        List<MembroMinisterioInfoMembroDTO> dtos = Arrays.asList(dto1, dto2);
        Page<MembroMinisterioInfoMembroDTO> expectedResult = new PageImpl<>(dtos, pageable, dtos.size());

        when(membroMinisterioService.buscarPorMembroMinisterioSemFiltro(ministerioId, pageable))
            .thenReturn(expectedResult);

        Page<MembroMinisterioInfoMembroDTO> result = ministerioService.buscarMembroMinisterioLiderMinisterio(ministerioId, pageable);

        assertNotNull(result);
        assertEquals(2, result.getTotalElements());
        verify(membroMinisterioService).buscarPorMembroMinisterioSemFiltro(ministerioId, pageable);
    }

    @Test
    @DisplayName("Deve buscar membros do ministério com filtro para líder com sucesso")
    void buscarMembroMinisterioLiderMinisterioComFiltroSucesso() {
        String texto = "João";
        EnumStatusMembro status = EnumStatusMembro.ATIVO;
        
        MembroMinisterioInfoMembroDTO dto = mock(MembroMinisterioInfoMembroDTO.class);
        List<MembroMinisterioInfoMembroDTO> dtos = Collections.singletonList(dto);
        Page<MembroMinisterioInfoMembroDTO> expectedResult = new PageImpl<>(dtos, pageable, dtos.size());

        when(membroMinisterioService.buscarPorMembroMinisterioComFiltro(ministerioId, pageable, texto, status))
            .thenReturn(expectedResult);

        Page<MembroMinisterioInfoMembroDTO> result = ministerioService.buscarMembroMinisterioLiderMinisterioComFiltro(
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

        when(ministeriosJpaRepository.buscarIdPorUUID(ministerioId)).thenReturn(idMinisterioLong);
        when(membroJpaRepository.buscarIdPorUUID(membroId)).thenReturn(idMembroLong);

        RestResponseMessageDTO result = ministerioService.adicionarMembroMinisterioLiderMinisterio(ministerioId, dto);

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

        when(ministeriosJpaRepository.buscarIdPorUUID(ministerioId)).thenReturn(null);

        assertThrows(ObjectNotFoundException.class,
            () -> ministerioService.adicionarMembroMinisterioLiderMinisterio(ministerioId, dto));
    }

    @Test
    @DisplayName("Deve lançar exceção quando membro não é encontrado ao adicionar ao ministério")
    void adicionarMembroMinisterioLiderMinisterioMembroNaoEncontradoDeveRetornarErro() {
        MembroMinisterioCreateDTO dto = new MembroMinisterioCreateDTO(membroId);
        Long idMinisterioLong = 1L;

        when(ministeriosJpaRepository.buscarIdPorUUID(ministerioId)).thenReturn(idMinisterioLong);
        when(membroJpaRepository.buscarIdPorUUID(membroId)).thenReturn(null);

        assertThrows(ObjectNotFoundException.class,
            () -> ministerioService.adicionarMembroMinisterioLiderMinisterio(ministerioId, dto));
    }

    @Test
    @DisplayName("Deve remover membro do ministério com sucesso")
    void removerMembroMinisterioLiderMinisterioSucesso() {
        UUID idMembroMinisterio = UUID.randomUUID();

        RestResponseMessageDTO result = ministerioService.removerMembroMinisterioLiderMinisterio(ministerioId, idMembroMinisterio);

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

        when(ministeriosJpaRepository.findAllByIdExternoIn(idsExternos)).thenReturn(ministeriosEncontrados);

        Set<Ministerio> result = ministerioService.buscarPorUUID(idsExternos);

        assertNotNull(result);
        assertEquals(2, result.size());
        assertTrue(result.contains(ministerio1));
        assertTrue(result.contains(ministerio2));
        verify(ministeriosJpaRepository).findAllByIdExternoIn(idsExternos);
    }

    @Test
    @DisplayName("Deve lançar exceção quando nenhum ministério é encontrado por UUID")
    void buscarPorUUIDNenhumEncontradoDeveRetornarErro() {
        List<UUID> idsExternos = Arrays.asList(ministerioId, UUID.randomUUID());
        Set<Ministerio> ministeriosVazios = new HashSet<>();

        when(ministeriosJpaRepository.findAllByIdExternoIn(idsExternos)).thenReturn(ministeriosVazios);

        assertThrows(ObjectNotFoundException.class,
            () -> ministerioService.buscarPorUUID(idsExternos));
    }
}
