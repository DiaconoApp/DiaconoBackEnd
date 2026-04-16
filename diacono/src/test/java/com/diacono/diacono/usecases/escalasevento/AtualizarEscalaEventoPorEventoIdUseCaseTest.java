package com.diacono.diacono.usecases.escalasevento;

import com.diacono.diacono.applications.dtos.RestResponseMessageDTO;
import com.diacono.diacono.applications.dtos.escalasevento.EscalaEventoEscaladoDTO;
import com.diacono.diacono.domain.entity.EscalaEvento;
import com.diacono.diacono.domain.entity.Evento;
import com.diacono.diacono.domain.entity.Igreja;
import com.diacono.diacono.domain.repository.EventoRepository;
import com.diacono.diacono.global.error.exceptions.FieldInvalidException;
import com.diacono.diacono.global.error.exceptions.ObjectNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AtualizarEscalaEventoPorEventoIdUseCaseTest {

    @Mock
    private EventoRepository eventoRepository;

    @Mock
    private GerarEscalaEventoUseCase gerarEscalaEventoUseCase;

    @InjectMocks
    private AtualizarEscalaEventoPorEventoIdUseCase useCase;

    @Test
    void deveAtualizarEscalaDoEventoComSucesso() {
        UUID igrejaId = UUID.fromString("11111111-1111-1111-1111-111111111111");
        UUID eventoId = UUID.fromString("22222222-2222-2222-2222-222222222222");
        UUID ministerioIdUm = UUID.fromString("33333333-3333-3333-3333-333333333333");
        UUID ministerioIdDois = UUID.fromString("44444444-4444-4444-4444-444444444444");

        Igreja igreja = Igreja.builder().nome("Igreja Central").build();
        ReflectionTestUtils.setField(igreja, "idExterno", igrejaId);

        Evento evento = Evento.builder()
                .igreja(igreja)
                .escalaEvento(new HashSet<>())
                .build();

        List<EscalaEventoEscaladoDTO> request = List.of(
                new EscalaEventoEscaladoDTO(ministerioIdUm, "Louvor", null, true),
                new EscalaEventoEscaladoDTO(ministerioIdDois, "Recepcao", null, false),
                new EscalaEventoEscaladoDTO(ministerioIdDois, "Recepcao", null, true)
        );

        Set<EscalaEvento> escalaAtualizada = new HashSet<>();

        when(eventoRepository.findByIdExterno(eventoId)).thenReturn(Optional.of(evento));
        when(gerarEscalaEventoUseCase.executeParaAtualizacao(evento, evento.getEscalaEvento(), List.of(ministerioIdUm, ministerioIdDois)))
                .thenReturn(escalaAtualizada);

        RestResponseMessageDTO response = useCase.execute(igrejaId, eventoId, request);

        assertEquals(HttpStatus.OK, response.getStatus());
        assertEquals("Escala do evento atualizada com sucesso", response.getMessage());
        assertEquals(escalaAtualizada, evento.getEscalaEvento());
        verify(eventoRepository).save(evento);
    }

    @Test
    void deveLancarExcecaoQuandoRequestVazio() {
        UUID igrejaId = UUID.fromString("11111111-1111-1111-1111-111111111111");
        UUID eventoId = UUID.fromString("22222222-2222-2222-2222-222222222222");

        FieldInvalidException ex = assertThrows(FieldInvalidException.class,
                () -> useCase.execute(igrejaId, eventoId, List.of()));

        assertEquals("Lista de escalas do evento nao pode estar vazia", ex.getMessage());
    }

    @Test
    void deveLancarExcecaoQuandoEventoNaoPertencerAIgreja() {
        UUID igrejaTokenId = UUID.fromString("11111111-1111-1111-1111-111111111111");
        UUID igrejaEventoId = UUID.fromString("55555555-5555-5555-5555-555555555555");
        UUID eventoId = UUID.fromString("22222222-2222-2222-2222-222222222222");

        Igreja igrejaEvento = Igreja.builder().nome("Outra Igreja").build();
        ReflectionTestUtils.setField(igrejaEvento, "idExterno", igrejaEventoId);

        Evento evento = Evento.builder()
                .igreja(igrejaEvento)
                .escalaEvento(new HashSet<>())
                .build();

        List<EscalaEventoEscaladoDTO> request = List.of(
                new EscalaEventoEscaladoDTO(UUID.fromString("33333333-3333-3333-3333-333333333333"), "Louvor", null, true)
        );

        when(eventoRepository.findByIdExterno(eventoId)).thenReturn(Optional.of(evento));

        ObjectNotFoundException ex = assertThrows(ObjectNotFoundException.class,
                () -> useCase.execute(igrejaTokenId, eventoId, request));

        assertEquals("Evento nao encontrado", ex.getMessage());
    }
}

