package com.diacono.diacono.ministerio.controller;

import com.diacono.diacono.infrastructure.controllers.MinisteriosController;
import com.diacono.diacono.applications.dtos.RestResponseMessageDTO;
import com.diacono.diacono.global.error.exceptions.FieldInvalidException;
import com.diacono.diacono.domain.enums.EnumStatusMembro;
import com.diacono.diacono.applications.dtos.membro.MembroMinisterioCreateDTO;
import com.diacono.diacono.applications.dtos.membro.MembroMinisterioInfoMembroDTO;
import com.diacono.diacono.applications.dtos.ministerio.MinisterioCreateDTO;
import com.diacono.diacono.applications.dtos.ministerio.MinisterioUpdateDTO;
import com.diacono.diacono.applications.dtos.ministerio.MinisterioSimplificadoDTO;
import com.diacono.diacono.domain.enums.EnumStatusMinisterio;
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
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MinisteriosControllerTest {

    @Mock
    private MinisterioService ministerioService;

    @InjectMocks
    private MinisteriosController ministeriosController;

    private Pageable pageable;
    private UUID ministerioId;

    @BeforeEach
    void setUp() {
        pageable = PageRequest.of(0, 10);
        ministerioId = UUID.randomUUID();
    }

    @Test
    @DisplayName("Deve buscar ministérios gerais com sucesso")
    void buscarMinisteriosGeraisSucesso() {
        MinisterioSimplificadoDTO dto1 = new MinisterioSimplificadoDTO(
                UUID.randomUUID(),
                "Ministério de Louvor",
                "João Silva",
                EnumStatusMinisterio.ATIVO,
                LocalDate.now()
        );
        MinisterioSimplificadoDTO dto2 = new MinisterioSimplificadoDTO(
                UUID.randomUUID(),
                "Ministério de Diaconia",
                "Maria Santos",
                EnumStatusMinisterio.ATIVO,
                LocalDate.now()
        );
        List<MinisterioSimplificadoDTO> ministerios = Arrays.asList(dto1, dto2);

        when(ministerioService.buscarMinisteriosGerais()).thenReturn(ministerios);

        ResponseEntity<List<MinisterioSimplificadoDTO>> response = ministeriosController.buscarMinisteriosGerais();

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(2, response.getBody().size());
        assertEquals("Ministério de Louvor", response.getBody().get(0).nome());
        assertEquals("Ministério de Diaconia", response.getBody().get(1).nome());
        verify(ministerioService).buscarMinisteriosGerais();
    }

    @Test
    @DisplayName("Deve buscar ministérios governo sem filtros com sucesso")
    void buscarMinisteriosGovernoSemFiltrosSucesso() {
        MinisterioSimplificadoDTO dto1 = new MinisterioSimplificadoDTO(
                UUID.randomUUID(),
                "Ministério de Louvor",
                "João Silva",
                EnumStatusMinisterio.ATIVO,
                LocalDate.now()
        );
        MinisterioSimplificadoDTO dto2 = new MinisterioSimplificadoDTO(
                UUID.randomUUID(),
                "Ministério de Diaconia",
                "Maria Santos",
                EnumStatusMinisterio.ATIVO,
                LocalDate.now()
        );
        List<MinisterioSimplificadoDTO> ministerios = Arrays.asList(dto1, dto2);
        Page<MinisterioSimplificadoDTO> ministeriosPage = new PageImpl<>(ministerios, pageable, ministerios.size());

        when(ministerioService.buscarMinisteriosGoverno(pageable)).thenReturn(ministeriosPage);

        ResponseEntity<Page<MinisterioSimplificadoDTO>> response = ministeriosController.buscarMinisteriosGoverno(
                pageable, "", null
        );

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(2, response.getBody().getTotalElements());
        verify(ministerioService).buscarMinisteriosGoverno(pageable);
        verify(ministerioService, never()).buscarMinisteriosGovernoComFiltro(any(), any(), any());
    }

    @Test
    @DisplayName("Deve buscar ministérios governo com filtros com sucesso")
    void buscarMinisteriosGovernoComFiltrosSucesso() {
        String buscaGeral = "Louvor";
        EnumStatusMinisterio status = EnumStatusMinisterio.ATIVO;

        MinisterioSimplificadoDTO dto = new MinisterioSimplificadoDTO(
                UUID.randomUUID(),
                "Ministério de Louvor",
                "João Silva",
                EnumStatusMinisterio.ATIVO,
                LocalDate.now()
        );
        List<MinisterioSimplificadoDTO> ministerios = Collections.singletonList(dto);
        Page<MinisterioSimplificadoDTO> ministeriosPage = new PageImpl<>(ministerios, pageable, ministerios.size());

        when(ministerioService.buscarMinisteriosGovernoComFiltro(pageable, buscaGeral, status)).thenReturn(ministeriosPage);

        ResponseEntity<Page<MinisterioSimplificadoDTO>> response = ministeriosController.buscarMinisteriosGoverno(
                pageable, buscaGeral, status
        );

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().getTotalElements());
        assertEquals("Ministério de Louvor", response.getBody().getContent().getFirst().nome());
        verify(ministerioService).buscarMinisteriosGovernoComFiltro(pageable, buscaGeral, status);
        verify(ministerioService, never()).buscarMinisteriosGoverno(any());
    }

    @Test
    @DisplayName("Deve adicionar ministério com sucesso")
    void adicionarMinisterioSucesso() {
        MinisterioCreateDTO createDTO = new MinisterioCreateDTO(
                UUID.randomUUID(),
                "Ministério de Louvor"
        );

        RestResponseMessageDTO expectedResponse = new RestResponseMessageDTO(
                HttpStatus.CREATED,
                "Ministério criado com sucesso"
        );

        when(ministerioService.criarMinisterio(createDTO)).thenReturn(expectedResponse);

        ResponseEntity<RestResponseMessageDTO> response = ministeriosController.adicionarMinisterio(createDTO);

        assertNotNull(response);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Ministério criado com sucesso", response.getBody().getMessage());
        verify(ministerioService).criarMinisterio(createDTO);
    }

    @Test
    @DisplayName("Deve editar ministério com sucesso")
    void editarMinisterioSucesso() {
        MinisterioUpdateDTO updateDTO = new MinisterioUpdateDTO(
                "Ministério de Louvor Atualizado",
                EnumStatusMinisterio.ATIVO,
                UUID.randomUUID()
        );

        RestResponseMessageDTO expectedResponse = new RestResponseMessageDTO(
                HttpStatus.NO_CONTENT,
                "Ministério atualizado com sucesso"
        );

        when(ministerioService.editarMinisterio(updateDTO, ministerioId)).thenReturn(expectedResponse);

        ResponseEntity<RestResponseMessageDTO> response = ministeriosController.editarMinisterio(ministerioId, updateDTO);

        assertNotNull(response);
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Ministério atualizado com sucesso", response.getBody().getMessage());
        verify(ministerioService).editarMinisterio(updateDTO, ministerioId);
    }

    @Test
    @DisplayName("Deve buscar membros do ministério para líder sem filtros com sucesso")
    void buscarMembroMinisterioLiderMinisterioSemFiltrosSucesso() {
        MembroMinisterioInfoMembroDTO dto1 = mock(MembroMinisterioInfoMembroDTO.class);
        MembroMinisterioInfoMembroDTO dto2 = mock(MembroMinisterioInfoMembroDTO.class);
        List<MembroMinisterioInfoMembroDTO> membros = Arrays.asList(dto1, dto2);
        Page<MembroMinisterioInfoMembroDTO> membrosPage = new PageImpl<>(membros, pageable, membros.size());

        when(ministerioService.buscarMembroMinisterioLiderMinisterio(ministerioId, pageable)).thenReturn(membrosPage);

        ResponseEntity<Page<MembroMinisterioInfoMembroDTO>> response = ministeriosController.buscarMembroMinisterioLiderMinisterio(
                ministerioId, pageable, "", null
        );

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(2, response.getBody().getTotalElements());
        verify(ministerioService).buscarMembroMinisterioLiderMinisterio(ministerioId, pageable);
        verify(ministerioService, never()).buscarMembroMinisterioLiderMinisterioComFiltro(any(), any(), any(), any());
    }

    @Test
    @DisplayName("Deve buscar membros do ministério para líder com filtros com sucesso")
    void buscarMembroMinisterioLiderMinisterioComFiltrosSucesso() {
        String buscaGeral = "João";
        EnumStatusMembro status = EnumStatusMembro.ATIVO;

        MembroMinisterioInfoMembroDTO dto = mock(MembroMinisterioInfoMembroDTO.class);
        List<MembroMinisterioInfoMembroDTO> membros = Collections.singletonList(dto);
        Page<MembroMinisterioInfoMembroDTO> membrosPage = new PageImpl<>(membros, pageable, membros.size());

        when(ministerioService.buscarMembroMinisterioLiderMinisterioComFiltro(ministerioId, pageable, buscaGeral, status))
                .thenReturn(membrosPage);

        ResponseEntity<Page<MembroMinisterioInfoMembroDTO>> response = ministeriosController.buscarMembroMinisterioLiderMinisterio(
                ministerioId, pageable, buscaGeral, status
        );

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().getTotalElements());
        verify(ministerioService).buscarMembroMinisterioLiderMinisterioComFiltro(ministerioId, pageable, buscaGeral, status);
        verify(ministerioService, never()).buscarMembroMinisterioLiderMinisterio(any(), any());
    }

    @Test
    @DisplayName("Deve lançar exceção quando ID do ministério é nulo ao buscar membros")
    void buscarMembroMinisterioLiderMinisterioIdNuloDeveRetornarErro() {
        assertThrows(FieldInvalidException.class, () ->
                ministeriosController.buscarMembroMinisterioLiderMinisterio(null, pageable, "", null)
        );
    }

    @Test
    @DisplayName("Deve adicionar membro ao ministério para líder com sucesso")
    void adicionarMembroMinisterioLiderMinisterioSucesso() {
        MembroMinisterioCreateDTO createDTO = new MembroMinisterioCreateDTO(
                UUID.randomUUID()
        );

        RestResponseMessageDTO expectedResponse = new RestResponseMessageDTO(
                HttpStatus.NO_CONTENT,
                "Membro adicionado ao ministério com sucesso"
        );

        when(ministerioService.adicionarMembroMinisterioLiderMinisterio(ministerioId, createDTO)).thenReturn(expectedResponse);

        ResponseEntity<RestResponseMessageDTO> response = ministeriosController.adicionarMembroMinisterioLiderMinisterio(
                ministerioId, createDTO
        );

        assertNotNull(response);
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Membro adicionado ao ministério com sucesso", response.getBody().getMessage());
        verify(ministerioService).adicionarMembroMinisterioLiderMinisterio(ministerioId, createDTO);
    }

    @Test
    @DisplayName("Deve remover membro do ministério para líder com sucesso")
    void removerMembroMinisterioLiderMinisterioSucesso() {
        UUID idMembroMinisterio = UUID.randomUUID();

        RestResponseMessageDTO expectedResponse = new RestResponseMessageDTO(
                HttpStatus.NO_CONTENT,
                "Membro removido do ministério com sucesso"
        );

        when(ministerioService.removerMembroMinisterioLiderMinisterio(ministerioId, idMembroMinisterio)).thenReturn(expectedResponse);

        ResponseEntity<RestResponseMessageDTO> response = ministeriosController.removerMembroMinisterioLiderMinisterio(
                ministerioId, idMembroMinisterio
        );

        assertNotNull(response);
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Membro removido do ministério com sucesso", response.getBody().getMessage());
        verify(ministerioService).removerMembroMinisterioLiderMinisterio(ministerioId, idMembroMinisterio);
    }
}