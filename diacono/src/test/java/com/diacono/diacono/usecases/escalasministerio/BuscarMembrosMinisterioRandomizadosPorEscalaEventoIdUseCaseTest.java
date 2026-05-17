package com.diacono.diacono.usecases.escalasministerio;

import com.diacono.diacono.applications.dtos.escalaministerio.EscalaMembroMinisterioDTO;
import com.diacono.diacono.applications.dtos.escalaministerio.EscalaMembroMinisterioSimplificadoDTO;
import com.diacono.diacono.applications.dtos.ministerio.MinisterioSuperSimplificadoDTO;
import com.diacono.diacono.domain.repository.EscalaEventoRepository;
import com.diacono.diacono.domain.repository.EscalaMinisterioRepository;
import com.diacono.diacono.domain.repository.MembroMinisterioRepository;
import com.diacono.diacono.global.error.exceptions.FieldInvalidException;
import com.diacono.diacono.global.error.exceptions.ObjectNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Set;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@SuppressWarnings("unused")
class BuscarMembrosMinisterioRandomizadosPorEscalaEventoIdUseCaseTest {

    @Mock
    private EscalaMinisterioRepository escalaMinisterioRepository;

    @Mock
    private EscalaEventoRepository escalaEventoRepository;

    @Mock
    private MembroMinisterioRepository membroMinisterioRepository;

    @InjectMocks
    private BuscarMembrosMinisterioRandomizadosPorEscalaEventoIdUseCase useCase;

    @Test
    void deveRetornarSomenteMembrosNaoOcupadosRespeitandoQuantidade() {
        UUID igrejaId = UUID.fromString("11111111-1111-1111-1111-111111111111");
        UUID escalaEventoId = UUID.fromString("22222222-2222-2222-2222-222222222222");
        UUID membroId = UUID.fromString("33333333-3333-3333-3333-333333333333");
        UUID ministerioId = UUID.fromString("44444444-4444-4444-4444-444444444444");

        UUID membroLivreA = UUID.fromString("55555555-5555-5555-5555-555555555555");
        UUID membroLivreB = UUID.fromString("66666666-6666-6666-6666-666666666666");
        UUID membroOcupado = UUID.fromString("77777777-7777-7777-7777-777777777777");

        when(escalaEventoRepository.findMinisterioIdByEscalaEventoId(igrejaId, escalaEventoId))
                .thenReturn(ministerioId);
        when(membroMinisterioRepository.buscarMinisterioLider(membroId, igrejaId))
                .thenReturn(List.of(new MinisterioSuperSimplificadoDTO(ministerioId, "Louvor")));
        when(escalaMinisterioRepository.findEscalaMembroMinisterioByEscalaEventoId(igrejaId, escalaEventoId))
                .thenReturn(List.of(
                        new EscalaMembroMinisterioDTO(membroLivreA, "Ana", null, null),
                        new EscalaMembroMinisterioDTO(membroLivreB, "Bruno", null, null),
                        new EscalaMembroMinisterioDTO(membroOcupado, "Carlos", null, null)
                ));
        when(escalaMinisterioRepository.findMembrosMinisterioOcupadosByEscalaEventoId(igrejaId, escalaEventoId))
                .thenReturn(List.of(membroOcupado));

        List<EscalaMembroMinisterioSimplificadoDTO> response = useCase.execute(escalaEventoId, igrejaId, membroId, 2);

        assertEquals(2, response.size());
        assertEquals(Set.of(membroLivreA, membroLivreB),
                response.stream().map(EscalaMembroMinisterioSimplificadoDTO::idExternoMembroMinisterio).collect(java.util.stream.Collectors.toSet()));
    }

    @Test
    void deveLancarExcecaoQuandoQuantidadeForMenorOuIgualAZero() {
        UUID igrejaId = UUID.fromString("11111111-1111-1111-1111-111111111111");
        UUID escalaEventoId = UUID.fromString("22222222-2222-2222-2222-222222222222");
        UUID membroId = UUID.fromString("33333333-3333-3333-3333-333333333333");
        UUID ministerioId = UUID.fromString("44444444-4444-4444-4444-444444444444");

        when(escalaEventoRepository.findMinisterioIdByEscalaEventoId(igrejaId, escalaEventoId))
                .thenReturn(ministerioId);
        when(membroMinisterioRepository.buscarMinisterioLider(membroId, igrejaId))
                .thenReturn(List.of(new MinisterioSuperSimplificadoDTO(ministerioId, "Louvor")));

        FieldInvalidException ex = assertThrows(
                FieldInvalidException.class,
                () -> useCase.execute(escalaEventoId, igrejaId, membroId, 0)
        );

        assertEquals("A quantidade de membros randomizados deve ser maior que zero.", ex.getMessage());
        verify(escalaMinisterioRepository, never()).findEscalaMembroMinisterioByEscalaEventoId(igrejaId, escalaEventoId);
    }

    @Test
    void deveLancarExcecaoQuandoLiderNaoPossuiVinculoComMinisterioDaEscala() {
        UUID igrejaId = UUID.fromString("11111111-1111-1111-1111-111111111111");
        UUID escalaEventoId = UUID.fromString("22222222-2222-2222-2222-222222222222");
        UUID membroId = UUID.fromString("33333333-3333-3333-3333-333333333333");
        UUID ministerioEscalaId = UUID.fromString("44444444-4444-4444-4444-444444444444");
        UUID ministerioLiderId = UUID.fromString("55555555-5555-5555-5555-555555555555");

        when(escalaEventoRepository.findMinisterioIdByEscalaEventoId(igrejaId, escalaEventoId))
                .thenReturn(ministerioEscalaId);
        when(membroMinisterioRepository.buscarMinisterioLider(membroId, igrejaId))
                .thenReturn(List.of(new MinisterioSuperSimplificadoDTO(ministerioLiderId, "Kids")));

        ObjectNotFoundException ex = assertThrows(
                ObjectNotFoundException.class,
                () -> useCase.execute(escalaEventoId, igrejaId, membroId, 1)
        );

        assertEquals("O líder informado não possui vínculo com o ministério solicitado.", ex.getMessage());
        verify(escalaMinisterioRepository, never()).findEscalaMembroMinisterioByEscalaEventoId(igrejaId, escalaEventoId);
    }
}

