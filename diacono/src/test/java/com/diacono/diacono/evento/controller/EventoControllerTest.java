package com.diacono.diacono.evento.controller;

import com.diacono.diacono.infrastructure.controllers.EventoController;
import com.diacono.diacono.applications.dtos.evento.EnderecoEventoDTO;
import com.diacono.diacono.applications.dtos.evento.EventoCreateDTO;
import com.diacono.diacono.applications.dtos.evento.EventoUpdateDTO;
import com.diacono.diacono.applications.dtos.recorrencia.RecorrenciaCreateDTO;
import com.diacono.diacono.applications.dtos.evento.EnderecoEventoSimplificadoDTO;
import com.diacono.diacono.applications.dtos.evento.EventoCompletoDTO;
import com.diacono.diacono.applications.dtos.evento.EventoSimplificadoDTO;
import com.diacono.diacono.applications.dtos.evento.EventoUnicoSimplificadoDTO;
import com.diacono.diacono.domain.enums.TipoRecorrencia;
import com.diacono.diacono.use_cases.EventoService;
import com.diacono.diacono.applications.dtos.RestResponseMessageDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EventoControllerTest {

    @Mock
    private EventoService eventoService;

    @InjectMocks
    private EventoController eventoController;

    private UUID eventoId;

    @BeforeEach
    void setUp() {
        eventoId = UUID.randomUUID();
    }

    @Test
    @DisplayName("Deve buscar eventos por mês e ano com sucesso")
    void buscarEventosPorMesEAnoSucesso() {
        int mes = 11;
        int ano = 2025;

        EventoUnicoSimplificadoDTO eventoDTO = new EventoUnicoSimplificadoDTO(
                eventoId,
                "Culto de Celebração",
                LocalDateTime.now().plusDays(5).plusHours(2),
                LocalDateTime.now().plusDays(5)
        );

        EventoSimplificadoDTO expectedResponse = new EventoSimplificadoDTO(
                Collections.singletonList(eventoDTO)
        );

        when(eventoService.buscarEventosPorMesEAno(mes, ano)).thenReturn(expectedResponse);

        ResponseEntity<EventoSimplificadoDTO> response = eventoController.buscarEventosPorMesEAno(mes, ano);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().eventosMes().size());
        assertEquals("Culto de Celebração", response.getBody().eventosMes().getFirst().nome());
        verify(eventoService).buscarEventosPorMesEAno(mes, ano);
    }

    @Test
    @DisplayName("Deve buscar evento específico com sucesso")
    void buscarEventoEspecificoSucesso() {
        EventoCompletoDTO expectedResponse = new EventoCompletoDTO(
                "Culto de Celebração",
                "Descrição do evento",
                "Todos os membros",
                LocalDateTime.now().plusDays(5),
                LocalDateTime.now().plusDays(5).plusHours(2),
                BigDecimal.ZERO,
                null,
                null,
                null,
                null,
                null
        );

        when(eventoService.buscarEventoEspecifico(eventoId)).thenReturn(expectedResponse);

        ResponseEntity<EventoCompletoDTO> response = eventoController.buscarEventoEspecifico(eventoId);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Culto de Celebração", response.getBody().nome());
        assertEquals("Descrição do evento", response.getBody().descricao());
        assertEquals("Todos os membros", response.getBody().publicoAlvo());
        verify(eventoService).buscarEventoEspecifico(eventoId);
    }

    @Test
    @DisplayName("Deve buscar endereço de evento com sucesso")
    void buscarEnderecoEventoSucesso() {
        EnderecoEventoSimplificadoDTO expectedResponse = new EnderecoEventoSimplificadoDTO(
                "12345678",
                "Rua Principal",
                "São Paulo",
                "Centro",
                null,
                "123",
                "Igreja Central",
                UUID.randomUUID()
        );

        when(eventoService.buscarEnderecoEvento()).thenReturn(expectedResponse);

        ResponseEntity<EnderecoEventoSimplificadoDTO> response = eventoController.buscarEnderecoEvento();

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Rua Principal", response.getBody().rua());
        assertEquals("São Paulo", response.getBody().cidade());
        verify(eventoService).buscarEnderecoEvento();
    }

    @Test
    @DisplayName("Deve criar evento com sucesso")
    void criarEventoSucesso() {
        EnderecoEventoDTO enderecoDTO = new EnderecoEventoDTO(
                null,
                "12345678",
                "SP",
                "São Paulo",
                "Centro",
                "Rua Principal",
                "",
                "123",
                "Igreja Central"
        );

        RecorrenciaCreateDTO recorrenciaDTO = new RecorrenciaCreateDTO(
                TipoRecorrencia.NAO_REPETE,
                null,
                null
        );

        EventoCreateDTO createDTO = new EventoCreateDTO(
                List.of(UUID.randomUUID()),
                enderecoDTO,
                recorrenciaDTO,
                "Culto de Celebração",
                "Descrição do evento",
                "Todos os membros",
                LocalDateTime.now().plusDays(5),
                LocalDateTime.now().plusDays(5).plusHours(2),
                BigDecimal.ZERO
        );

        RestResponseMessageDTO expectedResponse = new RestResponseMessageDTO(
                HttpStatus.CREATED,
                "Evento criado com sucesso"
        );

        when(eventoService.criarEvento(createDTO)).thenReturn(expectedResponse);

        ResponseEntity<RestResponseMessageDTO> response = eventoController.criarEvento(createDTO);

        assertNotNull(response);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Evento criado com sucesso", response.getBody().getMessage());
        verify(eventoService).criarEvento(createDTO);
    }

    @Test
    @DisplayName("Deve apagar evento único com sucesso")
    void apagarEventoUnicoSucesso() {
        RestResponseMessageDTO expectedResponse = new RestResponseMessageDTO(
                HttpStatus.NO_CONTENT,
                "Evento deletado com sucesso"
        );

        when(eventoService.apagarEvento(eventoId)).thenReturn(expectedResponse);

        ResponseEntity<RestResponseMessageDTO> response = eventoController.apagarEventoUnico(eventoId);

        assertNotNull(response);
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Evento deletado com sucesso", response.getBody().getMessage());
        verify(eventoService).apagarEvento(eventoId);
    }

    @Test
    @DisplayName("Deve apagar eventos múltiplos com sucesso")
    void apagarEventosMultiplosSucesso() {
        RestResponseMessageDTO expectedResponse = new RestResponseMessageDTO(
                HttpStatus.NO_CONTENT,
                "Eventos deletados com sucesso"
        );

        when(eventoService.apagarEventosMultiplos(eventoId)).thenReturn(expectedResponse);

        ResponseEntity<RestResponseMessageDTO> response = eventoController.apagarEventosMultiplos(eventoId);

        assertNotNull(response);
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Eventos deletados com sucesso", response.getBody().getMessage());
        verify(eventoService).apagarEventosMultiplos(eventoId);
    }

    @Test
    @DisplayName("Deve atualizar evento com sucesso")
    void atualizarEventoSucesso() {
        EventoUpdateDTO updateDTO = new EventoUpdateDTO(
                List.of(UUID.randomUUID()),
                null,
                "Culto de Celebração Atualizado",
                "Nova descrição",
                "Todos os membros",
                LocalDateTime.now().plusDays(10),
                LocalDateTime.now().plusDays(10).plusHours(2),
                BigDecimal.valueOf(10.00)
        );

        RestResponseMessageDTO expectedResponse = new RestResponseMessageDTO(
                HttpStatus.NO_CONTENT,
                "Evento atualizado com sucesso"
        );

        when(eventoService.alterarEvento(updateDTO, eventoId)).thenReturn(expectedResponse);

        ResponseEntity<RestResponseMessageDTO> response = eventoController.atualizarEvento(updateDTO, eventoId);

        assertNotNull(response);
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Evento atualizado com sucesso", response.getBody().getMessage());
        verify(eventoService).alterarEvento(updateDTO, eventoId);
    }
}