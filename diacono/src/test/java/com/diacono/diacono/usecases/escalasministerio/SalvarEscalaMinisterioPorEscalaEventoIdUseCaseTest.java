package com.diacono.diacono.usecases.escalasministerio;

import com.diacono.diacono.applications.dtos.RestResponseMessageDTO;
import com.diacono.diacono.applications.dtos.escalaministerio.EscalaMembroMinisterioDTO;
import com.diacono.diacono.applications.dtos.escalaministerio.EscalaMinisterioSalvarDTO;
import com.diacono.diacono.applications.dtos.ministerio.MinisterioSuperSimplificadoDTO;
import com.diacono.diacono.domain.entity.EscalaEvento;
import com.diacono.diacono.domain.entity.EscalaMinisterio;
import com.diacono.diacono.domain.entity.MembroMinisterio;
import com.diacono.diacono.domain.entity.Ministerio;
import com.diacono.diacono.domain.enums.EnumStatusEscalaMinisterio;
import com.diacono.diacono.domain.repository.EscalaEventoRepository;
import com.diacono.diacono.domain.repository.EscalaMinisterioRepository;
import com.diacono.diacono.domain.repository.MembroMinisterioRepository;
import com.diacono.diacono.domain.service.EscalaStatusDomainService;
import com.diacono.diacono.global.error.exceptions.FieldInvalidException;
import com.diacono.diacono.global.error.exceptions.ObjectNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
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

    @Mock
    private EscalaStatusDomainService escalaStatusDomainService;

    @InjectMocks
    private SalvarEscalaMinisterioPorEscalaEventoIdUseCase useCase;

    private UUID escalaEventoId;
    private UUID igrejaId;
    private UUID liderId;
    private UUID ministerioId;
    private UUID membroMinisterioId1;
    private UUID membroMinisterioId2;

    @BeforeEach
    void setUp() {
        escalaEventoId = UUID.randomUUID();
        igrejaId = UUID.randomUUID();
        liderId = UUID.randomUUID();
        ministerioId = UUID.randomUUID();
        membroMinisterioId1 = UUID.randomUUID();
        membroMinisterioId2 = UUID.randomUUID();
    }

    @Test
    void deveLancarErroQuandoListaForNula() {
        FieldInvalidException ex = assertThrows(
                FieldInvalidException.class,
                () -> useCase.execute(escalaEventoId, igrejaId, liderId, null)
        );

        assertEquals("Lista de escala do ministério não pode ser nula", ex.getMessage());
        verify(escalaMinisterioRepository, never()).replaceEscalaMinisterioByEscalaEventoId(eq(igrejaId), eq(escalaEventoId), anyList());
    }

    @Test
    void deveLancarErroQuandoListaContiverMembrosDuplicados() {
        List<EscalaMinisterioSalvarDTO> request = List.of(
                new EscalaMinisterioSalvarDTO(membroMinisterioId1, EnumStatusEscalaMinisterio.CONFIRMADO),
                new EscalaMinisterioSalvarDTO(membroMinisterioId1, EnumStatusEscalaMinisterio.PENDENTE)
        );

        FieldInvalidException ex = assertThrows(
                FieldInvalidException.class,
                () -> useCase.execute(escalaEventoId, igrejaId, liderId, request)
        );

        assertEquals("A lista de escalados contém membros duplicados", ex.getMessage());
        verify(escalaMinisterioRepository, never()).replaceEscalaMinisterioByEscalaEventoId(eq(igrejaId), eq(escalaEventoId), anyList());
    }

    @Test
    void deveLancarErroQuandoLiderNaoPossuirVinculoComMinisterioDaEscala() {
        List<EscalaMinisterioSalvarDTO> request = List.of(
                new EscalaMinisterioSalvarDTO(membroMinisterioId1, EnumStatusEscalaMinisterio.CONFIRMADO)
        );

        when(escalaEventoRepository.findMinisterioIdByEscalaEventoId(igrejaId, escalaEventoId)).thenReturn(ministerioId);
        when(membroMinisterioRepository.buscarMinisterioLider(liderId, igrejaId)).thenReturn(List.of());

        ObjectNotFoundException ex = assertThrows(
                ObjectNotFoundException.class,
                () -> useCase.execute(escalaEventoId, igrejaId, liderId, request)
        );

        assertEquals("O líder informado não possui vínculo com o ministério solicitado.", ex.getMessage());
    }

    @Test
    void deveLancarErroQuandoMembroNaoPertencerAoMinisterioDaEscala() {
        List<EscalaMinisterioSalvarDTO> request = List.of(
                new EscalaMinisterioSalvarDTO(membroMinisterioId2, EnumStatusEscalaMinisterio.CONFIRMADO)
        );

        mockLiderComVinculoAoMinisterio();
        when(escalaMinisterioRepository.findEscalaMembroMinisterioByEscalaEventoId(igrejaId, escalaEventoId))
                .thenReturn(List.of(new EscalaMembroMinisterioDTO(membroMinisterioId1, "Membro 1", null, false)));

        FieldInvalidException ex = assertThrows(
                FieldInvalidException.class,
                () -> useCase.execute(escalaEventoId, igrejaId, liderId, request)
        );

        assertEquals("A lista contém membros que não pertencem ao ministério da escala informada", ex.getMessage());
    }

    @Test
    void deveLancarErroQuandoExistirConflitoDeHorario() {
        List<EscalaMinisterioSalvarDTO> request = List.of(
                new EscalaMinisterioSalvarDTO(membroMinisterioId1, EnumStatusEscalaMinisterio.CONFIRMADO)
        );

        mockLiderComVinculoAoMinisterio();
        when(escalaMinisterioRepository.findEscalaMembroMinisterioByEscalaEventoId(igrejaId, escalaEventoId))
                .thenReturn(List.of(new EscalaMembroMinisterioDTO(membroMinisterioId1, "Membro 1", null, true)));
        when(escalaMinisterioRepository.findMembrosMinisterioOcupadosByEscalaEventoId(igrejaId, escalaEventoId))
                .thenReturn(List.of(membroMinisterioId1));

        FieldInvalidException ex = assertThrows(
                FieldInvalidException.class,
                () -> useCase.execute(escalaEventoId, igrejaId, liderId, request)
        );

        assertEquals("A lista contém membros com conflito de escala para este horário", ex.getMessage());
    }

    @Test
    void deveSalvarEscalaComStatusConfirmadoQuandoStatusForNulo() {
        List<EscalaMinisterioSalvarDTO> request = List.of(
                new EscalaMinisterioSalvarDTO(membroMinisterioId1, null)
        );

        mockLiderComVinculoAoMinisterio();
        when(escalaMinisterioRepository.findEscalaMembroMinisterioByEscalaEventoId(igrejaId, escalaEventoId))
                .thenReturn(List.of(new EscalaMembroMinisterioDTO(membroMinisterioId1, "Membro 1", null, false)));
        when(escalaMinisterioRepository.findMembrosMinisterioOcupadosByEscalaEventoId(igrejaId, escalaEventoId))
                .thenReturn(List.of());

        EscalaEvento escalaEvento = EscalaEvento.builder()
                .ministerio(Ministerio.builder().nome("Louvor").build())
                .build();
        when(escalaEventoRepository.findEscalaEventoByIdExternoAndIgrejaId(igrejaId, escalaEventoId))
                .thenReturn(escalaEvento);

        MembroMinisterio membroMinisterio = MembroMinisterio.builder().build();
        when(escalaMinisterioRepository.findMembrosMinisterioByEscalaEventoIdAndIds(igrejaId, escalaEventoId, List.of(membroMinisterioId1)))
                .thenReturn(Map.of(membroMinisterioId1, membroMinisterio));

        RestResponseMessageDTO response = useCase.execute(escalaEventoId, igrejaId, liderId, request);

        assertEquals(HttpStatus.OK, response.getStatus());
        assertEquals("Escala de membros do ministério atualizada com sucesso", response.getMessage());

        ArgumentCaptor<List<EscalaMinisterio>> captor = ArgumentCaptor.forClass(List.class);
        verify(escalaMinisterioRepository, times(1))
                .replaceEscalaMinisterioByEscalaEventoId(eq(igrejaId), eq(escalaEventoId), captor.capture());
        verify(escalaStatusDomainService, times(1)).recalcularStatusPorEscalaEventoId(escalaEventoId);

        List<EscalaMinisterio> escalasSalvas = captor.getValue();
        assertEquals(1, escalasSalvas.size());
        assertEquals(EnumStatusEscalaMinisterio.CONFIRMADO, escalasSalvas.getFirst().getStatusEscalaMinisterio());
        assertTrue(escalasSalvas.getFirst().getEscalaEvento() == escalaEvento);
    }

    private void mockLiderComVinculoAoMinisterio() {
        when(escalaEventoRepository.findMinisterioIdByEscalaEventoId(igrejaId, escalaEventoId)).thenReturn(ministerioId);
        when(membroMinisterioRepository.buscarMinisterioLider(liderId, igrejaId))
                .thenReturn(List.of(new MinisterioSuperSimplificadoDTO(ministerioId, "Louvor")));
    }
}
