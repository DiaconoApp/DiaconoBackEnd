package com.diacono.diacono.usecases.escalasministerio;

import com.diacono.diacono.applications.dtos.escalaministerio.EscalaMinisterioConsolidadoDTO;
import com.diacono.diacono.applications.dtos.ministerio.MinisterioSuperSimplificadoDTO;
import com.diacono.diacono.domain.enums.EnumStatusEscalaMinisterio;
import com.diacono.diacono.domain.repository.EscalaMinisterioRepository;
import com.diacono.diacono.domain.repository.MembroMinisterioRepository;
import com.diacono.diacono.global.error.exceptions.FieldInvalidException;
import com.diacono.diacono.global.error.exceptions.ObjectNotFoundException;
import com.diacono.diacono.usecases.escalasevento.validation.ValidarMesEAno;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
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
    private EscalaMinisterioRepository escalaMinisterioRepository;

    @Mock
    private MembroMinisterioRepository membroMinisterioRepository;

    @Spy
    private ValidarMesEAno validarMesEAno = new ValidarMesEAno();

    @InjectMocks
    private BuscarEscalaMinisterioConsolidadoPorMesAnoUseCase useCase;

    @Test
    void deveRetornarEscalaConsolidadaDoMinisterioComSucesso() {
        UUID idIgreja = UUID.fromString("11111111-1111-1111-1111-111111111111");
        UUID idMembro = UUID.fromString("22222222-2222-2222-2222-222222222222");
        UUID idMinisterio = UUID.fromString("33333333-3333-3333-3333-333333333333");

        List<EscalaMinisterioConsolidadoDTO> esperado = List.of(
                new EscalaMinisterioConsolidadoDTO(
                        UUID.fromString("44444444-4444-4444-4444-444444444444"),
                        "Culto de Jovens",
                        LocalDateTime.of(2026, 5, 15, 21, 0),
                        LocalDateTime.of(2026, 5, 15, 19, 0),
                        1,
                        1,
                        EnumStatusEscalaMinisterio.CONFIRMADO
                )
        );

        when(membroMinisterioRepository.buscarMinisterioLider(idMembro, idIgreja))
                .thenReturn(List.of(new MinisterioSuperSimplificadoDTO(idMinisterio, "Louvor")));

        when(escalaMinisterioRepository.findEscalaMinisterioConsolidadoByPeriodo(
                idIgreja,
                LocalDateTime.of(2026, 5, 1, 0, 0),
                LocalDateTime.of(2026, 5, 31, 23, 59, 59),
                EnumStatusEscalaMinisterio.CONFIRMADO,
                List.of(idMinisterio),
                "Culto"
        )).thenReturn(esperado);

        List<EscalaMinisterioConsolidadoDTO> response = useCase.execute(
                idIgreja,
                idMembro,
                null,
                5,
                2026,
                EnumStatusEscalaMinisterio.CONFIRMADO,
                "Culto"
        );

        assertEquals(esperado, response);
    }

    @Test
    void deveRetornarEscalaConsolidadaQuandoMinisterioInformadoPertenceAoLider() {
        UUID idIgreja = UUID.fromString("11111111-1111-1111-1111-111111111111");
        UUID idMembro = UUID.fromString("22222222-2222-2222-2222-222222222222");
        UUID idMinisterio = UUID.fromString("33333333-3333-3333-3333-333333333333");

        List<EscalaMinisterioConsolidadoDTO> esperado = List.of();

        when(membroMinisterioRepository.buscarMinisterioLider(idMembro, idIgreja))
                .thenReturn(List.of(new MinisterioSuperSimplificadoDTO(idMinisterio, "Louvor")));

        when(escalaMinisterioRepository.findEscalaMinisterioConsolidadoByPeriodo(
                idIgreja,
                LocalDateTime.of(2026, 5, 1, 0, 0),
                LocalDateTime.of(2026, 5, 31, 23, 59, 59),
                EnumStatusEscalaMinisterio.CONFIRMADO,
                List.of(idMinisterio),
                "Culto"
        )).thenReturn(esperado);

        List<EscalaMinisterioConsolidadoDTO> response = useCase.execute(
                idIgreja,
                idMembro,
                idMinisterio,
                5,
                2026,
                EnumStatusEscalaMinisterio.CONFIRMADO,
                "Culto"
        );

        assertEquals(esperado, response);
    }

    @Test
    void deveLancarExcecaoQuandoMinisterioInformadoNaoPertencerAoLider() {
        UUID idIgreja = UUID.fromString("11111111-1111-1111-1111-111111111111");
        UUID idMembro = UUID.fromString("22222222-2222-2222-2222-222222222222");
        UUID idMinisterioSolicitado = UUID.fromString("33333333-3333-3333-3333-333333333333");
        UUID idMinisterioDoLider = UUID.fromString("44444444-4444-4444-4444-444444444444");

        when(membroMinisterioRepository.buscarMinisterioLider(idMembro, idIgreja))
                .thenReturn(List.of(new MinisterioSuperSimplificadoDTO(idMinisterioDoLider, "Louvor")));

        ObjectNotFoundException ex = assertThrows(
                ObjectNotFoundException.class,
                () -> useCase.execute(idIgreja, idMembro, idMinisterioSolicitado, 5, 2026, null, null)
        );

        assertTrue(ex.getMessage().contains("não possui vínculo com o ministério solicitado"));
    }

    @Test
    void deveLancarExcecaoQuandoMesForInvalido() {
        FieldInvalidException ex = assertThrows(
                FieldInvalidException.class,
                () -> useCase.execute(UUID.randomUUID(), UUID.randomUUID(), null, 13, 2026, null, null)
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
                () -> useCase.execute(idIgreja, idMembro, null, 5, 2026, null, null)
        );

        assertTrue(ex.getMessage().contains("Nenhum ministério encontrado para o líder informado."));
    }
}

