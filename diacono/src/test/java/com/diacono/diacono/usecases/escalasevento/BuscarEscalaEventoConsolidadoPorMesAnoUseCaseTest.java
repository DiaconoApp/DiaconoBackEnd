package com.diacono.diacono.usecases.escalasevento;

import com.diacono.diacono.applications.dtos.escalasevento.EscalaEventoConsolidadoDTO;
import com.diacono.diacono.domain.enums.EnumStatusEvento;
import com.diacono.diacono.domain.repository.EscalaEventoRepository;
import com.diacono.diacono.global.error.exceptions.FieldInvalidException;
import com.diacono.diacono.usecases.escalasevento.validation.ValidarMesEAno;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.lang.reflect.Method;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@SuppressWarnings("unused")
class BuscarEscalaEventoConsolidadoPorMesAnoUseCaseTest {

    @Mock
    private EscalaEventoRepository escalaEventoRepository;

    @Spy
    private ValidarMesEAno validarMesEAno = new ValidarMesEAno();

    @InjectMocks
    private BuscarEscalaEventoConsolidadoPorMesAnoUseCase useCase;

    @Test
    void deveNormalizarNomeEventoComTrimAntesDeBuscar() {
        UUID idIgreja = UUID.fromString("11111111-1111-1111-1111-111111111111");
        UUID idMinisterio = UUID.fromString("22222222-2222-2222-2222-222222222222");
        List<EscalaEventoConsolidadoDTO> esperado = List.of(
                new EscalaEventoConsolidadoDTO(
                        UUID.fromString("33333333-3333-3333-3333-333333333333"),
                        "Culto de Jovens",
                        LocalDateTime.of(2026, 5, 15, 21, 0),
                        LocalDateTime.of(2026, 5, 15, 19, 0),
                        1,
                        1,
                        EnumStatusEvento.CONFIRMADO
                )
        );

        String nomeNormalizado = invokeNormalizarNomeEvento("  CuLtO  ");

        assertEquals("CuLtO", nomeNormalizado);

        when(escalaEventoRepository.findEscalaEventoConsolidadoByPeriodo(
                eq(idIgreja),
                any(LocalDateTime.class),
                any(LocalDateTime.class),
                eq(EnumStatusEvento.CONFIRMADO),
                eq(idMinisterio),
                eq(nomeNormalizado)
        )).thenReturn(esperado);

        List<EscalaEventoConsolidadoDTO> response = useCase.execute(
                idIgreja,
                5,
                2026,
                EnumStatusEvento.CONFIRMADO,
                idMinisterio,
                "  CuLtO  "
        );

        assertEquals(esperado, response);
        verify(escalaEventoRepository).findEscalaEventoConsolidadoByPeriodo(
                eq(idIgreja),
                any(LocalDateTime.class),
                any(LocalDateTime.class),
                eq(EnumStatusEvento.CONFIRMADO),
                eq(idMinisterio),
                eq(nomeNormalizado)
        );
    }

    @Test
    void deveTransformarNomeEventoEmBrancoParaNull() {
        UUID idIgreja = UUID.randomUUID();
        String nomeNormalizado = invokeNormalizarNomeEvento("   ");

        assertEquals(null, nomeNormalizado);

        when(escalaEventoRepository.findEscalaEventoConsolidadoByPeriodo(
                eq(idIgreja),
                any(LocalDateTime.class),
                any(LocalDateTime.class),
                eq(null),
                eq(null),
                eq(nomeNormalizado)
        )).thenReturn(List.of());

        List<EscalaEventoConsolidadoDTO> response = useCase.execute(
                idIgreja,
                5,
                2026,
                null,
                null,
                "   "
        );

        assertEquals(List.of(), response);
        verify(escalaEventoRepository).findEscalaEventoConsolidadoByPeriodo(
                eq(idIgreja),
                any(LocalDateTime.class),
                any(LocalDateTime.class),
                eq(null),
                eq(null),
                eq(nomeNormalizado)
        );
    }

    @Test
    void deveLancarExcecaoQuandoMesForInvalido() {
        FieldInvalidException ex = assertThrows(
                FieldInvalidException.class,
                () -> useCase.execute(UUID.randomUUID(), 13, 2026, null, null, null)
        );

        assertEquals("O mês precisa estar entre 1 e 12", ex.getMessage());
    }

    private String invokeNormalizarNomeEvento(String nomeEvento) {
        try {
            Method method = BuscarEscalaEventoConsolidadoPorMesAnoUseCase.class
                    .getDeclaredMethod("normalizarNomeEvento", String.class);
            method.setAccessible(true);
            return (String) method.invoke(useCase, nomeEvento);
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }
}
