package com.diacono.diacono.usecases.ministerio;

import com.diacono.diacono.applications.dtos.ministerio.MinisterioSuperSimplificadoDTO;
import com.diacono.diacono.domain.repository.MinisteriosRepository;
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
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BuscarMinisteriosMembroUseCaseTest {

    @Mock
    private MinisteriosRepository ministeriosRepository;

    @InjectMocks
    private BuscarMinisteriosMembroUseCase useCase;

    private static final UUID ID_MEMBRO = UUID.fromString("11111111-1111-1111-1111-111111111111");
    private static final UUID ID_IGREJA = UUID.fromString("22222222-2222-2222-2222-222222222222");

    @Test
    void deveRetornarMinisteriosQuandoMembroPossuirVinculos() {
        MinisterioSuperSimplificadoDTO dto = new MinisterioSuperSimplificadoDTO(
                UUID.fromString("33333333-3333-3333-3333-333333333333"),
                "Louvor"
        );

        when(ministeriosRepository.buscarMinisteriosMembro(ID_MEMBRO, ID_IGREJA))
                .thenReturn(List.of(dto));

        List<MinisterioSuperSimplificadoDTO> response = useCase.execute(ID_IGREJA, ID_MEMBRO);

        assertEquals(1, response.size());
        assertEquals(dto, response.getFirst());
        verify(ministeriosRepository).buscarMinisteriosMembro(ID_MEMBRO, ID_IGREJA);
    }

    @Test
    void deveRetornarMultiplosMinisteriosQuandoMembroPossuirVariosVinculos() {
        MinisterioSuperSimplificadoDTO dto1 = new MinisterioSuperSimplificadoDTO(
                UUID.fromString("33333333-3333-3333-3333-333333333333"),
                "Louvor"
        );
        MinisterioSuperSimplificadoDTO dto2 = new MinisterioSuperSimplificadoDTO(
                UUID.fromString("44444444-4444-4444-4444-444444444444"),
                "Intercessão"
        );

        when(ministeriosRepository.buscarMinisteriosMembro(ID_MEMBRO, ID_IGREJA))
                .thenReturn(List.of(dto1, dto2));

        List<MinisterioSuperSimplificadoDTO> response = useCase.execute(ID_IGREJA, ID_MEMBRO);

        assertEquals(2, response.size());
        verify(ministeriosRepository).buscarMinisteriosMembro(ID_MEMBRO, ID_IGREJA);
    }

    @Test
    void deveLancarObjectNotFoundExceptionQuandoNenhumMinisterioForEncontrado() {
        when(ministeriosRepository.buscarMinisteriosMembro(ID_MEMBRO, ID_IGREJA))
                .thenReturn(List.of());

        ObjectNotFoundException ex = assertThrows(
                ObjectNotFoundException.class,
                () -> useCase.execute(ID_IGREJA, ID_MEMBRO)
        );

        assertEquals("Nenhum ministério encontrado para o membro informado.", ex.getMessage());
        verify(ministeriosRepository).buscarMinisteriosMembro(ID_MEMBRO, ID_IGREJA);
    }
}