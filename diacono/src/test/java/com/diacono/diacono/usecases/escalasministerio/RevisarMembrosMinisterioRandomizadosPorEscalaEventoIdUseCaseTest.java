package com.diacono.diacono.usecases.escalasministerio;

import com.diacono.diacono.applications.dtos.escalaministerio.EscalaMembroMinisterioDTO;
import com.diacono.diacono.applications.dtos.escalaministerio.EscalaMembroMinisterioSimplificadoDTO;
import com.diacono.diacono.applications.dtos.ministerio.MinisterioSuperSimplificadoDTO;
import com.diacono.diacono.domain.repository.EscalaEventoRepository;
import com.diacono.diacono.domain.repository.EscalaMinisterioRepository;
import com.diacono.diacono.domain.repository.MembroMinisterioRepository;
import com.diacono.diacono.global.error.exceptions.FieldInvalidException;
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
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@SuppressWarnings("unused")
class RevisarMembrosMinisterioRandomizadosPorEscalaEventoIdUseCaseTest {

    @Mock
    private EscalaMinisterioRepository escalaMinisterioRepository;

    @Mock
    private EscalaEventoRepository escalaEventoRepository;

    @Mock
    private MembroMinisterioRepository membroMinisterioRepository;

    @InjectMocks
    private RevisarMembrosMinisterioRandomizadosPorEscalaEventoIdUseCase useCase;

    @Test
    void deveSubstituirMembroIndicadoNaMesmaPosicaoDaLista() {
        UUID igrejaId = UUID.fromString("11111111-1111-1111-1111-111111111111");
        UUID escalaEventoId = UUID.fromString("22222222-2222-2222-2222-222222222222");
        UUID membroId = UUID.fromString("33333333-3333-3333-3333-333333333333");
        UUID ministerioId = UUID.fromString("44444444-4444-4444-4444-444444444444");

        UUID membroA = UUID.fromString("55555555-5555-5555-5555-555555555555");
        UUID membroB = UUID.fromString("66666666-6666-6666-6666-666666666666");
        UUID membroC = UUID.fromString("88888888-8888-8888-8888-888888888888");
        UUID membroOcupado = UUID.fromString("77777777-7777-7777-7777-777777777777");
        UUID membroASerTrocado = membroB;

        when(escalaEventoRepository.findMinisterioIdByEscalaEventoId(igrejaId, escalaEventoId))
                .thenReturn(ministerioId);
        when(membroMinisterioRepository.buscarMinisterioLider(membroId, igrejaId))
                .thenReturn(List.of(new MinisterioSuperSimplificadoDTO(ministerioId, "Louvor")));

        when(escalaMinisterioRepository.findEscalaMembroMinisterioByEscalaEventoId(igrejaId, escalaEventoId))
                .thenReturn(List.of(
                        new EscalaMembroMinisterioDTO(membroA, "Ana", null, null),
                        new EscalaMembroMinisterioDTO(membroB, "Bruno", null, null),
                        new EscalaMembroMinisterioDTO(membroC, "Carla", null, null),
                        new EscalaMembroMinisterioDTO(membroOcupado, "Carlos", null, null)
                ));
        when(escalaMinisterioRepository.findMembrosMinisterioOcupadosByEscalaEventoId(igrejaId, escalaEventoId))
                .thenReturn(List.of(membroOcupado));

        List<EscalaMembroMinisterioSimplificadoDTO> selecionados = List.of(
                new EscalaMembroMinisterioSimplificadoDTO(membroB, "Bruno"),
                new EscalaMembroMinisterioSimplificadoDTO(membroC, "Carla")
        );

        List<EscalaMembroMinisterioSimplificadoDTO> response = useCase.execute(
                escalaEventoId,
                igrejaId,
                membroId,
                membroASerTrocado,
                selecionados
        );

        assertEquals(2, response.size());
        assertEquals(membroA, response.get(0).idExternoMembroMinisterio());
        assertEquals(membroC, response.get(1).idExternoMembroMinisterio());
    }

    @Test
    void deveLancarExcecaoQuandoIdMembroASerTrocadoForNulo() {
        UUID igrejaId = UUID.fromString("11111111-1111-1111-1111-111111111111");
        UUID escalaEventoId = UUID.fromString("22222222-2222-2222-2222-222222222222");
        UUID membroId = UUID.fromString("33333333-3333-3333-3333-333333333333");
        UUID ministerioId = UUID.fromString("44444444-4444-4444-4444-444444444444");
        UUID membroA = UUID.fromString("55555555-5555-5555-5555-555555555555");
        UUID membroASerTrocado = null;

        when(escalaEventoRepository.findMinisterioIdByEscalaEventoId(igrejaId, escalaEventoId))
                .thenReturn(ministerioId);
        when(membroMinisterioRepository.buscarMinisterioLider(membroId, igrejaId))
                .thenReturn(List.of(new MinisterioSuperSimplificadoDTO(ministerioId, "Louvor")));
        FieldInvalidException ex = assertThrows(
                FieldInvalidException.class,
                () -> useCase.execute(
                        escalaEventoId,
                        igrejaId,
                        membroId,
                        membroASerTrocado,
                        null
                )
        );

        assertEquals("O ID do membro a ser trocado deve ser fornecido.", ex.getMessage());
    }

    @Test
    void deveLancarExcecaoQuandoMembroASerTrocadoNaoEstiverNaLista() {
        UUID igrejaId = UUID.fromString("11111111-1111-1111-1111-111111111111");
        UUID escalaEventoId = UUID.fromString("22222222-2222-2222-2222-222222222222");
        UUID membroId = UUID.fromString("33333333-3333-3333-3333-333333333333");
        UUID ministerioId = UUID.fromString("44444444-4444-4444-4444-444444444444");
        UUID membroA = UUID.fromString("55555555-5555-5555-5555-555555555555");
        UUID membroB = UUID.fromString("66666666-6666-6666-6666-666666666666");
        UUID membroASerTrocado = UUID.fromString("99999999-9999-9999-9999-999999999999");

        when(escalaEventoRepository.findMinisterioIdByEscalaEventoId(igrejaId, escalaEventoId))
                .thenReturn(ministerioId);
        when(membroMinisterioRepository.buscarMinisterioLider(membroId, igrejaId))
                .thenReturn(List.of(new MinisterioSuperSimplificadoDTO(ministerioId, "Louvor")));
        List<EscalaMembroMinisterioSimplificadoDTO> selecionados = List.of(
                new EscalaMembroMinisterioSimplificadoDTO(membroB, "Bruno")
        );

        FieldInvalidException ex = assertThrows(
                FieldInvalidException.class,
                () -> useCase.execute(
                        escalaEventoId,
                        igrejaId,
                        membroId,
                        membroASerTrocado,
                        selecionados
                )
        );

        assertEquals("O membro a ser trocado não foi encontrado na lista de membros selecionados.", ex.getMessage());
    }

    @Test
    void deveLancarExcecaoQuandoNaoHouverNovoMembroDisponivel() {
        UUID igrejaId = UUID.fromString("11111111-1111-1111-1111-111111111111");
        UUID escalaEventoId = UUID.fromString("22222222-2222-2222-2222-222222222222");
        UUID membroId = UUID.fromString("33333333-3333-3333-3333-333333333333");
        UUID ministerioId = UUID.fromString("44444444-4444-4444-4444-444444444444");
        UUID membroA = UUID.fromString("55555555-5555-5555-5555-555555555555");
        UUID membroASerTrocado = membroA;

        when(escalaEventoRepository.findMinisterioIdByEscalaEventoId(igrejaId, escalaEventoId))
                .thenReturn(ministerioId);
        when(membroMinisterioRepository.buscarMinisterioLider(membroId, igrejaId))
                .thenReturn(List.of(new MinisterioSuperSimplificadoDTO(ministerioId, "Louvor")));
        when(escalaMinisterioRepository.findEscalaMembroMinisterioByEscalaEventoId(igrejaId, escalaEventoId))
                .thenReturn(List.of(new EscalaMembroMinisterioDTO(membroA, "Ana", null, null)));
        when(escalaMinisterioRepository.findMembrosMinisterioOcupadosByEscalaEventoId(igrejaId, escalaEventoId))
                .thenReturn(List.of());

        FieldInvalidException ex = assertThrows(
                FieldInvalidException.class,
                () -> useCase.execute(
                        escalaEventoId,
                        igrejaId,
                        membroId,
                        membroASerTrocado,
                        List.of(new EscalaMembroMinisterioSimplificadoDTO(membroA, "Ana"))
                )
        );

        assertEquals("A quantidade de membros randomizados não pode ser maior que a quantidade de membros disponíveis. Membros disponíveis: 0", ex.getMessage());
    }
}

