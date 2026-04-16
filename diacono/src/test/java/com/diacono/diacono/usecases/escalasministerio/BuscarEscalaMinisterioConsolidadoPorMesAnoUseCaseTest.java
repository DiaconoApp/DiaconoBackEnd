package com.diacono.diacono.usecases.escalasministerio;

import com.diacono.diacono.applications.dtos.escalasevento.EscalaEventoConsolidadoDTO;
import com.diacono.diacono.applications.dtos.ministerio.MinisterioSuperSimplificadoDTO;
import com.diacono.diacono.domain.enums.EnumStatusEvento;
import com.diacono.diacono.domain.repository.EscalaEventoRepository;
import com.diacono.diacono.domain.repository.MembroMinisterioRepository;
import com.diacono.diacono.global.error.exceptions.FieldInvalidException;
import com.diacono.diacono.global.error.exceptions.ObjectNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@SuppressWarnings("unused")
class BuscarEscalaMinisterioConsolidadoPorMesAnoUseCaseTest {

    @Mock
    private EscalaEventoRepository escalaEventoRepository;

    @Mock
    private MembroMinisterioRepository membroMinisterioRepository;

    @InjectMocks
    private BuscarEscalaMinisterioConsolidadoPorMesAnoUseCase useCase;

    @Test
    void deveRetornarEscalaConsolidadaDoMinisterioComSucesso() {
        UUID idIgreja = UUID.fromString("11111111-1111-1111-1111-111111111111");
        UUID idMembro = UUID.fromString("22222222-2222-2222-2222-222222222222");
        UUID idMinisterio = UUID.fromString("33333333-3333-3333-3333-333333333333");

        List<EscalaEventoConsolidadoDTO> esperado = List.of(
                new EscalaEventoConsolidadoDTO(
                        UUID.fromString("44444444-4444-4444-4444-444444444444"),
                        "Culto de Jovens",
                        LocalDateTime.of(2026, 5, 15, 21, 0),
                        LocalDateTime.of(2026, 5, 15, 19, 0),
                        1,
                        1,
                        EnumStatusEvento.ESCALADO
                )
        );

        when(membroMinisterioRepository.buscarMinisterioLider(idMembro, idIgreja))
                .thenReturn(List.of(new MinisterioSuperSimplificadoDTO(idMinisterio, "Louvor")));

        when(escalaEventoRepository.findEscalaEventoConsolidadoByPeriodo(
                idIgreja,
                LocalDateTime.of(2026, 5, 1, 0, 0),
                LocalDateTime.of(2026, 5, 31, 23, 59, 59),
                EnumStatusEvento.ESCALADO,
                idMinisterio,
                "Culto"
        )).thenReturn(esperado);

        List<EscalaEventoConsolidadoDTO> response = useCase.execute(
                idIgreja,
                idMembro,
                5,
                2026,
                EnumStatusEvento.ESCALADO,
                "Culto"
        );

        assertEquals(esperado, response);
    }

    @Test
    void deveLancarExcecaoQuandoMesForInvalido() {
        FieldInvalidException ex = assertThrows(
                FieldInvalidException.class,
                () -> useCase.execute(UUID.randomUUID(), UUID.randomUUID(), 13, 2026, null, null)
        );

        assertEquals("O mês precisa estar entre 1 e 12", ex.getMessage());
    }

    @Test
    void deveLancarExcecaoQuandoLiderNaoPossuirMinisterio() {
        UUID idIgreja = UUID.randomUUID();
        UUID idMembro = UUID.randomUUID();

        when(membroMinisterioRepository.buscarMinisterioLider(idMembro, idIgreja)).thenReturn(List.of());

        ObjectNotFoundException ex = assertThrows(
                ObjectNotFoundException.class,
                () -> useCase.execute(idIgreja, idMembro, 5, 2026, null, null)
        );

        assertTrue(ex.getMessage().contains("Nenhum ministério encontrado para o líder informado."));
    }
}

