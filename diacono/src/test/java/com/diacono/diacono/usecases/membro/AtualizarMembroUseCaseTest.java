package com.diacono.diacono.usecases.membro;

import com.diacono.diacono.applications.dtos.RestResponseMessageDTO;
import com.diacono.diacono.applications.dtos.membro.MembroUpdateDTO;
import com.diacono.diacono.domain.entity.Igreja;
import com.diacono.diacono.domain.entity.Membro;
import com.diacono.diacono.domain.entity.MembroMinisterio;
import com.diacono.diacono.domain.entity.Ministerio;
import com.diacono.diacono.domain.enums.EnumCargoMembro;
import com.diacono.diacono.domain.enums.EnumCargoMembroMinisterio;
import com.diacono.diacono.domain.enums.EnumGeneroMembro;
import com.diacono.diacono.domain.enums.EnumStatusMembro;
import com.diacono.diacono.domain.repository.EscalaMinisterioRepository;
import com.diacono.diacono.domain.repository.MembroMinisterioRepository;
import com.diacono.diacono.domain.repository.MembroRepository;
import com.diacono.diacono.domain.repository.MinisteriosRepository;
import com.diacono.diacono.global.error.exceptions.ObjectNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AtualizarMembroUseCaseTest {

    @Mock
    private MembroRepository membroRepository;

    @Mock
    private MembroMinisterioRepository membroMinisterioRepository;

    @Mock
    private MinisteriosRepository ministeriosRepository;

    @Mock
    private EscalaMinisterioRepository escalaMinisterioRepository;

    @InjectMocks
    private AtualizarMembroUseCase useCase;

    @Test
    void deveAdicionarNovoMinisterioSemRemoverVinculoExistente() {
        UUID igrejaId = UUID.fromString("11111111-1111-1111-1111-111111111111");
        UUID membroId = UUID.fromString("22222222-2222-2222-2222-222222222222");
        UUID ministerioExistenteId = UUID.fromString("33333333-3333-3333-3333-333333333333");
        UUID ministerioNovoId = UUID.fromString("44444444-4444-4444-4444-444444444444");

        Igreja igreja = Igreja.builder().nome("ICF").build();
        ReflectionTestUtils.setField(igreja, "idExterno", igrejaId);

        Membro membro = Membro.builder()
                .igreja(igreja)
                .nome("Joao")
                .cargoMembro(EnumCargoMembro.MEMBRO)
                .generoMembro(EnumGeneroMembro.MASCULINO)
                .status(EnumStatusMembro.ATIVO)
                .build();
        ReflectionTestUtils.setField(membro, "idExterno", membroId);

        Ministerio ministerioExistente = Ministerio.builder().nome("Louvor").igreja(igreja).build();
        ReflectionTestUtils.setField(ministerioExistente, "idExterno", ministerioExistenteId);

        Ministerio ministerioNovo = Ministerio.builder().nome("Recepcao").igreja(igreja).build();
        ReflectionTestUtils.setField(ministerioNovo, "idExterno", ministerioNovoId);

        MembroMinisterio vinculoExistente = MembroMinisterio.builder()
                .membro(membro)
                .ministerio(ministerioExistente)
                .cargoMembro(EnumCargoMembroMinisterio.MEMBRO_MINISTERIO)
                .nomeMinisterio("Louvor")
                .build();
        ReflectionTestUtils.setField(vinculoExistente, "idExterno", UUID.fromString("aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa"));

        MembroUpdateDTO request = new MembroUpdateDTO(
                null,
                null,
                null,
                null,
                null,
                null,
                List.of(ministerioExistenteId, ministerioNovoId),
                null,
                null,
                null,
                null
        );

        when(membroRepository.findByIdExternoAndIgrejaIdExterno(membroId, igrejaId)).thenReturn(Optional.of(membro));
        when(membroMinisterioRepository.findAllByMembroIdExternoAndIgrejaIdExterno(membroId, igrejaId))
                .thenReturn(List.of(vinculoExistente));
        when(ministeriosRepository.findAllByIdExternoInAndIgrejaId(List.of(ministerioExistenteId, ministerioNovoId), igrejaId))
                .thenReturn(Set.of(ministerioExistente, ministerioNovo));

        RestResponseMessageDTO response = useCase.execute(membroId, request, igrejaId);

        assertEquals(HttpStatus.OK, response.getStatus());
        assertEquals("Membro atualizado com sucesso", response.getMessage());
        verify(membroMinisterioRepository, never()).deleteByMembro(any());
        verify(membroMinisterioRepository, never()).deleteByMembroIdExternoAndMinisterioIdExterno(membroId, ministerioExistenteId);
        verify(escalaMinisterioRepository, never()).deleteByMembroMinisterioIdsAndIgrejaId(any(), any());
        verify(membroMinisterioRepository, times(1)).save(any(MembroMinisterio.class));
        verify(membroRepository).save(membro);
    }

    @Test
    void deveRemoverVinculoLimparEscalasAntesDeExcluirMembroMinisterio() {
        UUID igrejaId = UUID.fromString("11111111-1111-1111-1111-111111111111");
        UUID membroId = UUID.fromString("22222222-2222-2222-2222-222222222222");
        UUID ministerioMantidoId = UUID.fromString("33333333-3333-3333-3333-333333333333");
        UUID ministerioRemovidoId = UUID.fromString("44444444-4444-4444-4444-444444444444");
        UUID vinculoRemovidoId = UUID.fromString("bbbbbbbb-bbbb-bbbb-bbbb-bbbbbbbbbbbb");

        Igreja igreja = Igreja.builder().nome("ICF").build();
        ReflectionTestUtils.setField(igreja, "idExterno", igrejaId);

        Membro membro = Membro.builder()
                .igreja(igreja)
                .nome("Joao")
                .cargoMembro(EnumCargoMembro.MEMBRO)
                .generoMembro(EnumGeneroMembro.MASCULINO)
                .status(EnumStatusMembro.ATIVO)
                .build();
        ReflectionTestUtils.setField(membro, "idExterno", membroId);

        Ministerio ministerioMantido = Ministerio.builder().nome("Louvor").igreja(igreja).build();
        ReflectionTestUtils.setField(ministerioMantido, "idExterno", ministerioMantidoId);

        Ministerio ministerioRemovido = Ministerio.builder().nome("Recepcao").igreja(igreja).build();
        ReflectionTestUtils.setField(ministerioRemovido, "idExterno", ministerioRemovidoId);

        MembroMinisterio vinculoMantido = MembroMinisterio.builder()
                .membro(membro)
                .ministerio(ministerioMantido)
                .cargoMembro(EnumCargoMembroMinisterio.MEMBRO_MINISTERIO)
                .nomeMinisterio("Louvor")
                .build();

        MembroMinisterio vinculoRemovido = MembroMinisterio.builder()
                .membro(membro)
                .ministerio(ministerioRemovido)
                .cargoMembro(EnumCargoMembroMinisterio.MEMBRO_MINISTERIO)
                .nomeMinisterio("Recepcao")
                .build();
        ReflectionTestUtils.setField(vinculoRemovido, "idExterno", vinculoRemovidoId);

        MembroUpdateDTO request = new MembroUpdateDTO(
                null,
                null,
                null,
                null,
                null,
                null,
                List.of(ministerioMantidoId),
                null,
                null,
                null,
                null
        );

        when(membroRepository.findByIdExternoAndIgrejaIdExterno(membroId, igrejaId)).thenReturn(Optional.of(membro));
        when(membroMinisterioRepository.findAllByMembroIdExternoAndIgrejaIdExterno(membroId, igrejaId))
                .thenReturn(List.of(vinculoMantido, vinculoRemovido));
        when(ministeriosRepository.findAllByIdExternoInAndIgrejaId(List.of(ministerioMantidoId), igrejaId))
                .thenReturn(Set.of(ministerioMantido));

        RestResponseMessageDTO response = useCase.execute(membroId, request, igrejaId);

        assertEquals(HttpStatus.OK, response.getStatus());
        verify(escalaMinisterioRepository).deleteByMembroMinisterioIdsAndIgrejaId(igrejaId, List.of(vinculoRemovidoId));
        verify(membroMinisterioRepository).deleteByMembroIdExternoAndMinisterioIdExterno(membroId, ministerioRemovidoId);
        verify(membroMinisterioRepository).flush();
        verify(membroMinisterioRepository, never()).save(any(MembroMinisterio.class));
    }

    @Test
    void deveLancarExcecaoQuandoMinisterioNaoPertencerAIgreja() {
        UUID igrejaId = UUID.fromString("11111111-1111-1111-1111-111111111111");
        UUID membroId = UUID.fromString("22222222-2222-2222-2222-222222222222");
        UUID ministerioId = UUID.fromString("33333333-3333-3333-3333-333333333333");

        Igreja igreja = Igreja.builder().nome("ICF").build();
        ReflectionTestUtils.setField(igreja, "idExterno", igrejaId);

        Membro membro = Membro.builder()
                .igreja(igreja)
                .nome("Joao")
                .cargoMembro(EnumCargoMembro.MEMBRO)
                .generoMembro(EnumGeneroMembro.MASCULINO)
                .status(EnumStatusMembro.ATIVO)
                .build();
        ReflectionTestUtils.setField(membro, "idExterno", membroId);

        MembroUpdateDTO request = new MembroUpdateDTO(
                null,
                null,
                null,
                null,
                null,
                null,
                List.of(ministerioId),
                null,
                null,
                null,
                null
        );

        when(membroRepository.findByIdExternoAndIgrejaIdExterno(membroId, igrejaId)).thenReturn(Optional.of(membro));
        when(membroMinisterioRepository.findAllByMembroIdExternoAndIgrejaIdExterno(membroId, igrejaId))
                .thenReturn(List.of());
        when(ministeriosRepository.findAllByIdExternoInAndIgrejaId(List.of(ministerioId), igrejaId))
                .thenReturn(Set.of());

        ObjectNotFoundException ex = assertThrows(ObjectNotFoundException.class,
                () -> useCase.execute(membroId, request, igrejaId));

        assertEquals("Um ou mais ministérios não foram encontrados", ex.getMessage());
    }
}
