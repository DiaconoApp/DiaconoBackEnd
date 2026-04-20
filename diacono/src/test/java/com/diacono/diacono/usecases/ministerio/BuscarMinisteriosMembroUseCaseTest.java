package com.diacono.diacono.usecases.ministerio;

import com.diacono.diacono.applications.dtos.ministerio.MinisterioSuperSimplificadoDTO;
import com.diacono.diacono.domain.repository.FindMinisteriosMembrosRepository;
import com.diacono.diacono.global.error.exceptions.ObjectNotFoundException;
import com.diacono.diacono.global.util.JwtUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BuscarMinisteriosMembroUseCaseTest {

    @Mock
    private FindMinisteriosMembrosRepository findMinisteriosMembrosRepository;

    @Mock
    private JwtUtils jwtUtils;

    @InjectMocks
    private BuscarMinisteriosMembroUseCase useCase;

    @Test
    void deveBuscarMinisteriosDoMembroPeloExecuteComSucesso() {
        UUID idMembro = UUID.fromString("11111111-1111-1111-1111-111111111111");
        UUID idIgreja = UUID.fromString("22222222-2222-2222-2222-222222222222");
        MinisterioSuperSimplificadoDTO dto = new MinisterioSuperSimplificadoDTO(
                UUID.fromString("33333333-3333-3333-3333-333333333333"),
                "Louvor"
        );

        when(jwtUtils.getSubject()).thenReturn(idMembro);
        when(jwtUtils.getIgrejaId()).thenReturn(idIgreja);
        when(findMinisteriosMembrosRepository.buscarMinisteriosMembro(idMembro, idIgreja)).thenReturn(List.of(dto));

        List<MinisterioSuperSimplificadoDTO> response = useCase.execute();

        assertEquals(1, response.size());
        assertEquals(dto, response.getFirst());
        verify(jwtUtils).getSubject();
        verify(jwtUtils).getIgrejaId();
        verify(findMinisteriosMembrosRepository).buscarMinisteriosMembro(idMembro, idIgreja);
    }

    @Test
    void deveBuscarMinisteriosDoMembroDiretamenteComSucesso() {
        UUID idMembro = UUID.fromString("11111111-1111-1111-1111-111111111111");
        UUID idIgreja = UUID.fromString("22222222-2222-2222-2222-222222222222");
        MinisterioSuperSimplificadoDTO dto = new MinisterioSuperSimplificadoDTO(
                UUID.fromString("33333333-3333-3333-3333-333333333333"),
                "Intercessao"
        );

        when(findMinisteriosMembrosRepository.buscarMinisteriosMembro(idMembro, idIgreja)).thenReturn(List.of(dto));

        List<MinisterioSuperSimplificadoDTO> response = useCase.buscarMinisteriosMembro(idMembro, idIgreja);

        assertEquals(1, response.size());
        assertEquals(dto, response.getFirst());
    }

    @Test
    void deveLancarExcecaoQuandoNenhumMinisterioForEncontrado() {
        UUID idMembro = UUID.fromString("11111111-1111-1111-1111-111111111111");
        UUID idIgreja = UUID.fromString("22222222-2222-2222-2222-222222222222");

        when(findMinisteriosMembrosRepository.buscarMinisteriosMembro(idMembro, idIgreja)).thenReturn(List.of());

        ObjectNotFoundException ex = assertThrows(
                ObjectNotFoundException.class,
                () -> useCase.buscarMinisteriosMembro(idMembro, idIgreja)
        );

        assertEquals("Nenhum ministério encontrado para o membro informado.", ex.getMessage());
    }
}

