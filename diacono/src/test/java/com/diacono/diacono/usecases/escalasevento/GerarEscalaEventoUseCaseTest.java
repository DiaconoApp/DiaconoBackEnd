package com.diacono.diacono.usecases.escalasevento;

import com.diacono.diacono.domain.entity.EscalaEvento;
import com.diacono.diacono.domain.entity.Evento;
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
    void deveReaproveitarEscalaExistenteEcriarSomenteASolicitada() {
        UUID idEvento = UUID.fromString("11111111-1111-1111-1111-111111111111");
        UUID idMinisterioExistente = UUID.fromString("22222222-2222-2222-2222-222222222222");
        UUID idMinisterioNovo = UUID.fromString("33333333-3333-3333-3333-333333333333");

        Evento evento = Evento.builder().build();
        ReflectionTestUtils.setField(evento, "idExterno", idEvento);

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

        when(buscarMinisterioPorUUIDUseCase.execute(List.of(idMinisterioExistente, idMinisterioNovo)))
                .thenReturn(Set.of(ministerioExistente, ministerioNovo));

        Set<EscalaEvento> resultado = useCase.executeParaAtualizacao(evento, escalasAtuais, List.of(idMinisterioExistente, idMinisterioNovo));

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
        Evento evento = Evento.builder().build();

        Set<EscalaEvento> resultado = useCase.executeParaAtualizacao(evento, new HashSet<>(), List.of());

        assertTrue(resultado.isEmpty());
        verifyNoInteractions(buscarMinisterioPorUUIDUseCase);
    }
}

