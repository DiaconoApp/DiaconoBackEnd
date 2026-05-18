package com.diacono.diacono.usecases.escalasevento;

import com.diacono.diacono.domain.entity.EscalaEvento;
import com.diacono.diacono.domain.entity.Evento;
import com.diacono.diacono.domain.entity.Igreja;
import com.diacono.diacono.domain.entity.Ministerio;
import com.diacono.diacono.domain.enums.EnumStatusEvento;
import com.diacono.diacono.usecases.eventos.BuscarMinisterioPorUUIDUseCase;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GerarEscalaEventoUseCaseTest {

    @Mock
    private BuscarMinisterioPorUUIDUseCase buscarMinisterioPorUUIDUseCase;

    @InjectMocks
    private GerarEscalaEventoUseCase useCase;

    @Test
    void deveGerarEscalasNaCriacaoMesmoQuandoEventoAindaNaoFoiPersistido() {
        UUID idIgreja = UUID.fromString("44444444-4444-4444-4444-444444444444");
        UUID idMinisterio = UUID.fromString("22222222-2222-2222-2222-222222222222");

        Evento evento = Evento.builder().build();

        Ministerio ministerio = Ministerio.builder().nome("Louvor").build();
        ReflectionTestUtils.setField(ministerio, "idExterno", idMinisterio);

        when(buscarMinisterioPorUUIDUseCase.execute(List.of(idMinisterio), idIgreja))
                .thenReturn(Set.of(ministerio));

        Set<EscalaEvento> resultado = useCase.executeParaCriacao(evento, List.of(idMinisterio), idIgreja);

        assertEquals(1, resultado.size());
        EscalaEvento escala = resultado.iterator().next();
        assertSame(evento, escala.getEvento());
        assertSame(ministerio, escala.getMinisterio());
    }

    @Test
    void deveReaproveitarEscalaExistenteEcriarSomenteASolicitada() {
        UUID idEvento = UUID.fromString("11111111-1111-1111-1111-111111111111");
        UUID idIgreja = UUID.fromString("44444444-4444-4444-4444-444444444444");
        UUID idMinisterioExistente = UUID.fromString("22222222-2222-2222-2222-222222222222");
        UUID idMinisterioNovo = UUID.fromString("33333333-3333-3333-3333-333333333333");

        Igreja igreja = Igreja.builder().nome("Igreja Pentecostal").build();
        ReflectionTestUtils.setField(igreja, "idExterno", idIgreja);

        Evento evento = Evento.builder().build();
        ReflectionTestUtils.setField(evento, "idExterno", idEvento);
        evento.setIgreja(igreja);

        Ministerio ministerioExistente = Ministerio.builder().nome("Louvor").build();
        ReflectionTestUtils.setField(ministerioExistente, "idExterno", idMinisterioExistente);

        Ministerio ministerioNovo = Ministerio.builder().nome("Intercessao").build();
        ReflectionTestUtils.setField(ministerioNovo, "idExterno", idMinisterioNovo);

        EscalaEvento escalaExistente = EscalaEvento.builder()
                .evento(evento)
                .ministerio(ministerioExistente)
                .statusEscalaEvento(EnumStatusEvento.PENDENTE)
                .build();

        Set<EscalaEvento> escalasAtuais = new HashSet<>(Set.of(escalaExistente));

        // A01: Mock agora recebe igrejaId para validação de escopo
        when(buscarMinisterioPorUUIDUseCase.execute(List.of(idMinisterioExistente, idMinisterioNovo), idIgreja))
                .thenReturn(Set.of(ministerioExistente, ministerioNovo));

            Set<EscalaEvento> resultado = useCase.executeParaAtualizacao(evento, escalasAtuais, List.of(idMinisterioExistente, idMinisterioNovo), idIgreja);

        assertEquals(2, resultado.size());
        assertTrue(resultado.contains(escalaExistente));
        assertSame(escalaExistente, resultado.stream()
                .filter(escala -> idMinisterioExistente.equals(escala.getMinisterio().getIdExterno()))
                .findFirst()
                .orElseThrow());
        assertEquals(EnumStatusEvento.PENDENTE, escalaExistente.getStatusEscalaEvento());

        EscalaEvento escalaCriada = resultado.stream()
                .filter(escala -> idMinisterioNovo.equals(escala.getMinisterio().getIdExterno()))
                .findFirst()
                .orElseThrow();
        assertEquals(EnumStatusEvento.PENDENTE, escalaCriada.getStatusEscalaEvento());
        assertSame(evento, escalaCriada.getEvento());
    }

    @Test
    void deveRetornarEscalaVaziaQuandoNaoHouverMinisteriosSelecionados() {
        UUID idIgreja = UUID.fromString("44444444-4444-4444-4444-444444444444");
        UUID idEvento = UUID.fromString("11111111-1111-1111-1111-111111111111");

        Igreja igreja = Igreja.builder().nome("Igreja Pentecostal").build();
        ReflectionTestUtils.setField(igreja, "idExterno", idIgreja);

        Evento evento = Evento.builder().build();
        ReflectionTestUtils.setField(evento, "idExterno", idEvento);
        evento.setIgreja(igreja);

            Set<EscalaEvento> resultado = useCase.executeParaAtualizacao(evento, new HashSet<>(), List.of(), idIgreja);

        assertTrue(resultado.isEmpty());
        verifyNoInteractions(buscarMinisterioPorUUIDUseCase);
    }
}
