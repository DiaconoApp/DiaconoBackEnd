package com.diacono.diacono.membro.controller;

import com.diacono.diacono.infrastructure.controllers.MembroController;
import com.diacono.diacono.applications.dtos.RestResponseMessageDTO;
import com.diacono.diacono.applications.dtos.membro.MembroCreateDTO;
import com.diacono.diacono.applications.dtos.membro.MembroResponseDTO;
import com.diacono.diacono.domain.enums.EnumCargoMembro;
import com.diacono.diacono.domain.enums.EnumGeneroMembro;
import com.diacono.diacono.domain.enums.EnumStatusMembro;
import com.diacono.diacono.use_cases.MembroService;
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

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MembroControllerTest {

    @Mock
    private MembroService membroService;

    @InjectMocks
    private MembroController membroController;

    private Pageable pageable;

    @BeforeEach
    void setUp() {
        pageable = PageRequest.of(0, 10);
    }

    @Test
    @DisplayName("Deve criar membro com sucesso")
    void criarMembroSucesso() {
        UUID fkIgreja = UUID.randomUUID();
        MembroCreateDTO createDTO = new MembroCreateDTO(
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

        RestResponseMessageDTO expectedResponse = new RestResponseMessageDTO(
                HttpStatus.CREATED,
                "Membro criado com sucesso"
        );

        when(membroService.criarMembro(createDTO)).thenReturn(expectedResponse);

        ResponseEntity<RestResponseMessageDTO> response = membroController.criarMembro(createDTO);

        assertNotNull(response);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Membro criado com sucesso", response.getBody().getMessage());
        verify(membroService).criarMembro(createDTO);
    }

    @Test
    @DisplayName("Deve buscar todos os membros sem filtros com sucesso")
    void buscarTodosSemFiltrosSucesso() {
        MembroResponseDTO dto1 = new MembroResponseDTO(
                UUID.randomUUID(),
                "João Silva",
                "joao@email.com",
                "11999999999",
                null,
                null,
                EnumStatusMembro.ATIVO
        );
        MembroResponseDTO dto2 = new MembroResponseDTO(
                UUID.randomUUID(),
                "Maria Santos",
                "maria@email.com",
                "11988888888",
                null,
                null,
                EnumStatusMembro.ATIVO
        );

        List<MembroResponseDTO> membros = Arrays.asList(dto1, dto2);
        Page<MembroResponseDTO> membrosPage = new PageImpl<>(membros, pageable, membros.size());

        when(membroService.buscarTodosSemFiltro(pageable)).thenReturn(membrosPage);

        ResponseEntity<Page<MembroResponseDTO>> response = membroController.buscarTodos(
                pageable, "", null, null
        );

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(2, response.getBody().getTotalElements());
        assertEquals("João Silva", response.getBody().getContent().get(0).nome());
        assertEquals("Maria Santos", response.getBody().getContent().get(1).nome());
        verify(membroService).buscarTodosSemFiltro(pageable);
        verify(membroService, never()).buscarTodosComFiltro(any(), any(), any(), any());
    }

    @Test
    @DisplayName("Deve buscar todos os membros com filtro de busca geral com sucesso")
    void buscarTodosComFiltroBuscaGeralSucesso() {
        String buscaGeral = "João";

        MembroResponseDTO dto = new MembroResponseDTO(
                UUID.randomUUID(),
                "João Silva",
                "joao@email.com",
                "11999999999",
                null,
                null,
                EnumStatusMembro.ATIVO
        );

        List<MembroResponseDTO> membros = Collections.singletonList(dto);
        Page<MembroResponseDTO> membrosPage = new PageImpl<>(membros, pageable, membros.size());

        when(membroService.buscarTodosComFiltro(pageable, buscaGeral, null, null)).thenReturn(membrosPage);

        ResponseEntity<Page<MembroResponseDTO>> response = membroController.buscarTodos(
                pageable, buscaGeral, null, null
        );

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().getTotalElements());
        assertEquals("João Silva", response.getBody().getContent().getFirst().nome());
        verify(membroService).buscarTodosComFiltro(pageable, buscaGeral, null, null);
        verify(membroService, never()).buscarTodosSemFiltro(any());
    }

    @Test
    @DisplayName("Deve buscar todos os membros com filtro de status com sucesso")
    void buscarTodosComFiltroStatusSucesso() {
        EnumStatusMembro status = EnumStatusMembro.ATIVO;

        MembroResponseDTO dto1 = new MembroResponseDTO(
                UUID.randomUUID(),
                "João Silva",
                "joao@email.com",
                "11999999999",
                null,
                null,
                EnumStatusMembro.ATIVO
        );
        MembroResponseDTO dto2 = new MembroResponseDTO(
                UUID.randomUUID(),
                "Maria Santos",
                "maria@email.com",
                "11988888888",
                null,
                null,
                EnumStatusMembro.ATIVO
        );

        List<MembroResponseDTO> membros = Arrays.asList(dto1, dto2);
        Page<MembroResponseDTO> membrosPage = new PageImpl<>(membros, pageable, membros.size());

        when(membroService.buscarTodosComFiltro(pageable, "", status, null)).thenReturn(membrosPage);

        ResponseEntity<Page<MembroResponseDTO>> response = membroController.buscarTodos(
                pageable, "", status, null
        );

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(2, response.getBody().getTotalElements());
        verify(membroService).buscarTodosComFiltro(pageable, "", status, null);
        verify(membroService, never()).buscarTodosSemFiltro(any());
    }

    @Test
    @DisplayName("Deve buscar todos os membros com filtro de ministério com sucesso")
    void buscarTodosComFiltroMinisterioSucesso() {
        UUID fkMinisterio = UUID.randomUUID();

        MembroResponseDTO dto = new MembroResponseDTO(
                UUID.randomUUID(),
                "João Silva",
                "joao@email.com",
                "11999999999",
                null,
                null,
                EnumStatusMembro.ATIVO
        );

        List<MembroResponseDTO> membros = Collections.singletonList(dto);
        Page<MembroResponseDTO> membrosPage = new PageImpl<>(membros, pageable, membros.size());

        when(membroService.buscarTodosComFiltro(pageable, "", null, fkMinisterio)).thenReturn(membrosPage);

        ResponseEntity<Page<MembroResponseDTO>> response = membroController.buscarTodos(
                pageable, "", null, fkMinisterio
        );

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().getTotalElements());
        verify(membroService).buscarTodosComFiltro(pageable, "", null, fkMinisterio);
        verify(membroService, never()).buscarTodosSemFiltro(any());
    }

    @Test
    @DisplayName("Deve buscar todos os membros com todos os filtros aplicados com sucesso")
    void buscarTodosComTodosFiltrosSucesso() {
        String buscaGeral = "João";
        EnumStatusMembro status = EnumStatusMembro.ATIVO;
        UUID fkMinisterio = UUID.randomUUID();

        MembroResponseDTO dto = new MembroResponseDTO(
                UUID.randomUUID(),
                "João Silva",
                "joao@email.com",
                "11999999999",
                null,
                null,
                EnumStatusMembro.ATIVO
        );

        List<MembroResponseDTO> membros = Collections.singletonList(dto);
        Page<MembroResponseDTO> membrosPage = new PageImpl<>(membros, pageable, membros.size());

        when(membroService.buscarTodosComFiltro(pageable, buscaGeral, status, fkMinisterio)).thenReturn(membrosPage);

        ResponseEntity<Page<MembroResponseDTO>> response = membroController.buscarTodos(
                pageable, buscaGeral, status, fkMinisterio
        );

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().getTotalElements());
        assertEquals("João Silva", response.getBody().getContent().getFirst().nome());
        verify(membroService).buscarTodosComFiltro(pageable, buscaGeral, status, fkMinisterio);
        verify(membroService, never()).buscarTodosSemFiltro(any());
    }

    @Test
    @DisplayName("Deve buscar membros sem filtros quando buscaGeral é null")
    void buscarTodosBuscaGeralNullSemFiltrosSucesso() {
        MembroResponseDTO dto = new MembroResponseDTO(
                UUID.randomUUID(),
                "João Silva",
                "joao@email.com",
                "11999999999",
                null,
                null,
                EnumStatusMembro.ATIVO
        );

        List<MembroResponseDTO> membros = Collections.singletonList(dto);
        Page<MembroResponseDTO> membrosPage = new PageImpl<>(membros, pageable, membros.size());

        when(membroService.buscarTodosSemFiltro(pageable)).thenReturn(membrosPage);

        ResponseEntity<Page<MembroResponseDTO>> response = membroController.buscarTodos(
                pageable, null, null, null
        );

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().getTotalElements());
        verify(membroService).buscarTodosSemFiltro(pageable);
        verify(membroService, never()).buscarTodosComFiltro(any(), any(), any(), any());
    }
}