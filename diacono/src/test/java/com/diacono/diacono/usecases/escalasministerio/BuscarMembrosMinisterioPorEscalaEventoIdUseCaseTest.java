package com.diacono.diacono.usecases.escalasministerio;

import com.diacono.diacono.applications.dtos.escalaministerio.EscalaMembroMinisterioDTO;
import com.diacono.diacono.domain.repository.EscalaEventoRepository;
import com.diacono.diacono.domain.repository.EscalaMinisterioRepository;
import com.diacono.diacono.global.error.exceptions.ObjectNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@SuppressWarnings("unused")
class BuscarMembrosMinisterioPorEscalaEventoIdUseCaseTest {

    @Mock
    private EscalaMinisterioRepository escalaMinisterioRepository;

    @Mock
    private EscalaEventoRepository escalaEventoRepository;

    @InjectMocks
    private BuscarMembrosMinisterioPorEscalaEventoIdUseCase useCase;

    @Test
    void deveLancarExcecaoQuandoEscalaEventoNaoPertencerA_Igreja() {
        UUID igrejaId = UUID.fromString("11111111-1111-1111-1111-111111111111");
        UUID escalaEventoId = UUID.fromString("22222222-2222-2222-2222-222222222222");
        UUID membroId = UUID.fromString("33333333-3333-3333-3333-333333333333");

        when(escalaEventoRepository.findMinisterioIdByEscalaEventoId(igrejaId, escalaEventoId))
                .thenReturn(null);

        ObjectNotFoundException ex = assertThrows(
                ObjectNotFoundException.class,
                () -> useCase.execute(escalaEventoId, igrejaId, membroId)
        );

        assertEquals("Escala evento não encontrada para a igreja informada.", ex.getMessage());
        verify(escalaMinisterioRepository, never()).findEscalaMembroMinisterioByEscalaEventoId(igrejaId, escalaEventoId);
    }

    @Test
    void deveRetornarMembrosDoMinisterioDaEscalaEvento() {
        UUID igrejaId = UUID.fromString("11111111-1111-1111-1111-111111111111");
        UUID escalaEventoId = UUID.fromString("22222222-2222-2222-2222-222222222222");
        UUID membroId = UUID.fromString("33333333-3333-3333-3333-333333333333");
        UUID ministerioId = UUID.fromString("44444444-4444-4444-4444-444444444444");

        List<EscalaMembroMinisterioDTO> esperado = List.of();

        when(escalaEventoRepository.findMinisterioIdByEscalaEventoId(igrejaId, escalaEventoId))
                .thenReturn(ministerioId);
        when(escalaMinisterioRepository.findEscalaMembroMinisterioByEscalaEventoId(igrejaId, escalaEventoId))
                .thenReturn(esperado);

        List<EscalaMembroMinisterioDTO> response = useCase.execute(escalaEventoId, igrejaId, membroId);

        assertEquals(esperado, response);
        verify(escalaEventoRepository).findMinisterioIdByEscalaEventoId(igrejaId, escalaEventoId);
        verify(escalaMinisterioRepository).findEscalaMembroMinisterioByEscalaEventoId(igrejaId, escalaEventoId);
    }
}

