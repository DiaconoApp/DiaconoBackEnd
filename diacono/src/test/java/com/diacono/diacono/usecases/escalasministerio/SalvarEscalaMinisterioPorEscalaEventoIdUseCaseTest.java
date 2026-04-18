package com.diacono.diacono.usecases.escalasministerio;

import com.diacono.diacono.applications.dtos.RestResponseMessageDTO;
import com.diacono.diacono.applications.dtos.escalaministerio.EscalaMembroMinisterioDTO;
import com.diacono.diacono.applications.dtos.escalaministerio.EscalaMinisterioSalvarDTO;
import com.diacono.diacono.applications.dtos.ministerio.MinisterioSuperSimplificadoDTO;
import com.diacono.diacono.domain.entity.EscalaEvento;
import com.diacono.diacono.domain.enums.EnumStatusEscalaMinisterio;
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
import org.springframework.http.HttpStatus;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SalvarEscalaMinisterioPorEscalaEventoIdUseCaseTest {

    @Mock
    private EscalaMinisterioRepository escalaMinisterioRepository;

    @Mock
    private EscalaEventoRepository escalaEventoRepository;

    @Mock
    private MembroMinisterioRepository membroMinisterioRepository;

    @InjectMocks
    private SalvarEscalaMinisterioPorEscalaEventoIdUseCase useCase;

    @Test
    void deveSalvarListaDeEscaladosComSucesso() {
        UUID igrejaId = UUID.fromString("11111111-1111-1111-1111-111111111111");
        UUID escalaEventoId = UUID.fromString("22222222-2222-2222-2222-222222222222");
        UUID membroId = UUID.fromString("33333333-3333-3333-3333-333333333333");
        UUID ministerioId = UUID.fromString("44444444-4444-4444-4444-444444444444");
        UUID membroMinisterioId = UUID.fromString("55555555-5555-5555-5555-555555555555");

        List<EscalaMinisterioSalvarDTO> request = List.of(
                new EscalaMinisterioSalvarDTO(membroMinisterioId, EnumStatusEscalaMinisterio.CONFIRMADO)
        );

        when(escalaEventoRepository.findMinisterioIdByEscalaEventoId(igrejaId, escalaEventoId))
                .thenReturn(ministerioId);
        when(membroMinisterioRepository.buscarMinisterioLider(membroId, igrejaId))
                .thenReturn(List.of(new MinisterioSuperSimplificadoDTO(ministerioId, "Louvor")));
        when(escalaMinisterioRepository.findEscalaMembroMinisterioByEscalaEventoId(igrejaId, escalaEventoId))
                .thenReturn(List.of(new EscalaMembroMinisterioDTO(membroMinisterioId, "Ana", null, false)));
        when(escalaMinisterioRepository.findMembrosMinisterioOcupadosByEscalaEventoId(igrejaId, escalaEventoId))
                .thenReturn(List.of());
        when(escalaEventoRepository.findEscalaEventoByIdExternoAndIgrejaId(igrejaId, escalaEventoId))
                .thenReturn(new EscalaEvento());

        RestResponseMessageDTO response = useCase.execute(escalaEventoId, igrejaId, membroId, request);

        assertEquals(HttpStatus.OK, response.getStatus());
        assertEquals("Escala de membros do ministério atualizada com sucesso", response.getMessage());
        verify(escalaMinisterioRepository).replaceEscalaMinisterioByEscalaEventoId(eq(igrejaId), eq(escalaEventoId), any(EscalaEvento.class), eq(request));
    }

    @Test
    void deveLancarExcecaoQuandoEscalaEventoNaoPertencerAIgreja() {
        UUID igrejaId = UUID.fromString("11111111-1111-1111-1111-111111111111");
        UUID escalaEventoId = UUID.fromString("22222222-2222-2222-2222-222222222222");
        UUID membroId = UUID.fromString("33333333-3333-3333-3333-333333333333");

        List<EscalaMinisterioSalvarDTO> request = List.of();

        when(escalaEventoRepository.findMinisterioIdByEscalaEventoId(igrejaId, escalaEventoId))
                .thenReturn(null);

        ObjectNotFoundException ex = assertThrows(
                ObjectNotFoundException.class,
                () -> useCase.execute(escalaEventoId, igrejaId, membroId, request)
        );

        assertEquals("Escala evento não encontrada para a igreja informada.", ex.getMessage());
        verify(escalaMinisterioRepository, never()).replaceEscalaMinisterioByEscalaEventoId(eq(igrejaId), eq(escalaEventoId), any(EscalaEvento.class), eq(request));
    }

    @Test
    void deveLancarExcecaoQuandoListaForNula() {
        UUID igrejaId = UUID.fromString("11111111-1111-1111-1111-111111111111");
        UUID escalaEventoId = UUID.fromString("22222222-2222-2222-2222-222222222222");
        UUID membroId = UUID.fromString("33333333-3333-3333-3333-333333333333");

        FieldInvalidException ex = assertThrows(
                FieldInvalidException.class,
                () -> useCase.execute(escalaEventoId, igrejaId, membroId, null)
        );

        assertEquals("Lista de escala do ministério não pode ser nula", ex.getMessage());
        verify(escalaEventoRepository, never()).findMinisterioIdByEscalaEventoId(igrejaId, escalaEventoId);
    }

    @Test
    void deveLancarExcecaoQuandoListaPossuirMembrosDuplicados() {
        UUID igrejaId = UUID.fromString("11111111-1111-1111-1111-111111111111");
        UUID escalaEventoId = UUID.fromString("22222222-2222-2222-2222-222222222222");
        UUID membroId = UUID.fromString("33333333-3333-3333-3333-333333333333");
        UUID membroMinisterioId = UUID.fromString("55555555-5555-5555-5555-555555555555");

        List<EscalaMinisterioSalvarDTO> request = List.of(
                new EscalaMinisterioSalvarDTO(membroMinisterioId, EnumStatusEscalaMinisterio.CONFIRMADO),
                new EscalaMinisterioSalvarDTO(membroMinisterioId, EnumStatusEscalaMinisterio.PENDENTE)
        );

        FieldInvalidException ex = assertThrows(
                FieldInvalidException.class,
                () -> useCase.execute(escalaEventoId, igrejaId, membroId, request)
        );

        assertEquals("A lista de escalados contém membros duplicados", ex.getMessage());
        verify(escalaEventoRepository, never()).findMinisterioIdByEscalaEventoId(igrejaId, escalaEventoId);
    }

    @Test
    void deveLancarExcecaoQuandoListaPossuirMembroForaDoMinisterioDaEscala() {
        UUID igrejaId = UUID.fromString("11111111-1111-1111-1111-111111111111");
        UUID escalaEventoId = UUID.fromString("22222222-2222-2222-2222-222222222222");
        UUID membroId = UUID.fromString("33333333-3333-3333-3333-333333333333");
        UUID ministerioId = UUID.fromString("44444444-4444-4444-4444-444444444444");
        UUID membroValidoId = UUID.fromString("55555555-5555-5555-5555-555555555555");
        UUID membroInvalidoId = UUID.fromString("66666666-6666-6666-6666-666666666666");

        List<EscalaMinisterioSalvarDTO> request = List.of(
                new EscalaMinisterioSalvarDTO(membroInvalidoId, EnumStatusEscalaMinisterio.CONFIRMADO)
        );

        when(escalaEventoRepository.findMinisterioIdByEscalaEventoId(igrejaId, escalaEventoId))
                .thenReturn(ministerioId);
        when(membroMinisterioRepository.buscarMinisterioLider(membroId, igrejaId))
                .thenReturn(List.of(new MinisterioSuperSimplificadoDTO(ministerioId, "Louvor")));
        when(escalaMinisterioRepository.findEscalaMembroMinisterioByEscalaEventoId(igrejaId, escalaEventoId))
                .thenReturn(List.of(new EscalaMembroMinisterioDTO(membroValidoId, "Ana", null, false)));

        FieldInvalidException ex = assertThrows(
                FieldInvalidException.class,
                () -> useCase.execute(escalaEventoId, igrejaId, membroId, request)
        );

        assertEquals("A lista contém membros que não pertencem ao ministério da escala informada", ex.getMessage());
        verify(escalaMinisterioRepository, never()).replaceEscalaMinisterioByEscalaEventoId(eq(igrejaId), eq(escalaEventoId), any(EscalaEvento.class), eq(request));
    }

    @Test
    void deveLancarExcecaoQuandoListaPossuirConflitoDeEscala() {
        UUID igrejaId = UUID.fromString("11111111-1111-1111-1111-111111111111");
        UUID escalaEventoId = UUID.fromString("22222222-2222-2222-2222-222222222222");
        UUID membroId = UUID.fromString("33333333-3333-3333-3333-333333333333");
        UUID ministerioId = UUID.fromString("44444444-4444-4444-4444-444444444444");
        UUID membroMinisterioId = UUID.fromString("55555555-5555-5555-5555-555555555555");

        List<EscalaMinisterioSalvarDTO> request = List.of(
                new EscalaMinisterioSalvarDTO(membroMinisterioId, EnumStatusEscalaMinisterio.CONFIRMADO)
        );

        when(escalaEventoRepository.findMinisterioIdByEscalaEventoId(igrejaId, escalaEventoId))
                .thenReturn(ministerioId);
        when(membroMinisterioRepository.buscarMinisterioLider(membroId, igrejaId))
                .thenReturn(List.of(new MinisterioSuperSimplificadoDTO(ministerioId, "Louvor")));
        when(escalaMinisterioRepository.findEscalaMembroMinisterioByEscalaEventoId(igrejaId, escalaEventoId))
                .thenReturn(List.of(new EscalaMembroMinisterioDTO(membroMinisterioId, "Ana", null, false)));
        when(escalaMinisterioRepository.findMembrosMinisterioOcupadosByEscalaEventoId(igrejaId, escalaEventoId))
                .thenReturn(List.of(membroMinisterioId));

        FieldInvalidException ex = assertThrows(
                FieldInvalidException.class,
                () -> useCase.execute(escalaEventoId, igrejaId, membroId, request)
        );

        assertEquals("A lista contém membros com conflito de escala para este horário", ex.getMessage());
        verify(escalaMinisterioRepository, never()).replaceEscalaMinisterioByEscalaEventoId(eq(igrejaId), eq(escalaEventoId), any(EscalaEvento.class), eq(request));
    }

    @Test
    void deveLancarExcecaoQuandoEscalaEventoNaoForEncontradaAoBuscarEntidade() {
        UUID igrejaId = UUID.fromString("11111111-1111-1111-1111-111111111111");
        UUID escalaEventoId = UUID.fromString("22222222-2222-2222-2222-222222222222");
        UUID membroId = UUID.fromString("33333333-3333-3333-3333-333333333333");
        UUID ministerioId = UUID.fromString("44444444-4444-4444-4444-444444444444");
        UUID membroMinisterioId = UUID.fromString("55555555-5555-5555-5555-555555555555");

        List<EscalaMinisterioSalvarDTO> request = List.of(
                new EscalaMinisterioSalvarDTO(membroMinisterioId, EnumStatusEscalaMinisterio.CONFIRMADO)
        );

        when(escalaEventoRepository.findMinisterioIdByEscalaEventoId(igrejaId, escalaEventoId))
                .thenReturn(ministerioId);
        when(membroMinisterioRepository.buscarMinisterioLider(membroId, igrejaId))
                .thenReturn(List.of(new MinisterioSuperSimplificadoDTO(ministerioId, "Louvor")));
        when(escalaMinisterioRepository.findEscalaMembroMinisterioByEscalaEventoId(igrejaId, escalaEventoId))
                .thenReturn(List.of(new EscalaMembroMinisterioDTO(membroMinisterioId, "Ana", null, false)));
        when(escalaMinisterioRepository.findMembrosMinisterioOcupadosByEscalaEventoId(igrejaId, escalaEventoId))
                .thenReturn(List.of());
        when(escalaEventoRepository.findEscalaEventoByIdExternoAndIgrejaId(igrejaId, escalaEventoId))
                .thenReturn(null);

        ObjectNotFoundException ex = assertThrows(
                ObjectNotFoundException.class,
                () -> useCase.execute(escalaEventoId, igrejaId, membroId, request)
        );

        assertEquals("Escala evento não encontrada para a igreja informada.", ex.getMessage());
        verify(escalaMinisterioRepository, never()).replaceEscalaMinisterioByEscalaEventoId(eq(igrejaId), eq(escalaEventoId), any(EscalaEvento.class), eq(request));
    }
}

