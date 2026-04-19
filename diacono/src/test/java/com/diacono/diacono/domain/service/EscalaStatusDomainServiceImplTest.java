package com.diacono.diacono.domain.service;

import com.diacono.diacono.domain.enums.EnumStatusEvento;
import com.diacono.diacono.domain.repository.EscalaEventoRepository;
import com.diacono.diacono.domain.repository.EscalaMinisterioRepository;
import com.diacono.diacono.domain.repository.EventoRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class EscalaStatusDomainServiceImplTest {

    @Mock
    private EscalaMinisterioRepository escalaMinisterioRepository;

    @Mock
    private EscalaEventoRepository escalaEventoRepository;

    @Mock
    private EventoRepository eventoRepository;

    @InjectMocks
    private EscalaStatusDomainServiceImpl service;

    @Test
    void deveConfirmarEscalaEventoEEventoQuandoTudoConfirmado() {
        UUID escalaEventoId = UUID.fromString("11111111-1111-1111-1111-111111111111");
        UUID eventoId = UUID.fromString("22222222-2222-2222-2222-222222222222");

        when(escalaMinisterioRepository.areAllConfirmadosByEscalaEventoId(escalaEventoId)).thenReturn(true);
        when(escalaEventoRepository.findEventoIdByEscalaEventoId(escalaEventoId)).thenReturn(eventoId);
        when(escalaEventoRepository.areAllConfirmadosByEventoId(eventoId)).thenReturn(true);

        service.recalcularStatusPorEscalaEventoId(escalaEventoId);

        verify(escalaEventoRepository).updateStatusByEscalaEventoId(escalaEventoId, EnumStatusEvento.CONFIRMADO);
        verify(eventoRepository).updateStatusByEventoId(eventoId, EnumStatusEvento.CONFIRMADO);
    }

    @Test
    void deveMarcarComoPendenteQuandoNaoEstiverTudoConfirmado() {
        UUID escalaEventoId = UUID.fromString("11111111-1111-1111-1111-111111111111");
        UUID eventoId = UUID.fromString("22222222-2222-2222-2222-222222222222");

        when(escalaMinisterioRepository.areAllConfirmadosByEscalaEventoId(escalaEventoId)).thenReturn(false);
        when(escalaEventoRepository.findEventoIdByEscalaEventoId(escalaEventoId)).thenReturn(eventoId);
        when(escalaEventoRepository.areAllConfirmadosByEventoId(eventoId)).thenReturn(false);

        service.recalcularStatusPorEscalaEventoId(escalaEventoId);

        verify(escalaEventoRepository).updateStatusByEscalaEventoId(escalaEventoId, EnumStatusEvento.PENDENTE);
        verify(eventoRepository).updateStatusByEventoId(eventoId, EnumStatusEvento.PENDENTE);
    }

    @Test
    void naoDeveRecalcularEventoQuandoEscalaEventoNaoTemEventoAssociado() {
        UUID escalaEventoId = UUID.fromString("11111111-1111-1111-1111-111111111111");

        when(escalaMinisterioRepository.areAllConfirmadosByEscalaEventoId(escalaEventoId)).thenReturn(false);
        when(escalaEventoRepository.findEventoIdByEscalaEventoId(escalaEventoId)).thenReturn(null);

        service.recalcularStatusPorEscalaEventoId(escalaEventoId);

        verify(escalaEventoRepository).updateStatusByEscalaEventoId(escalaEventoId, EnumStatusEvento.PENDENTE);
        verify(eventoRepository, never()).updateStatusByEventoId(org.mockito.ArgumentMatchers.any(), org.mockito.ArgumentMatchers.any());
    }
}

