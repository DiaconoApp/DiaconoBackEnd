package com.diacono.diacono.usecases.eventos;

import com.diacono.diacono.applications.dtos.evento.EventoCompletoDTO;
import com.diacono.diacono.applications.mappers.evento.EventoMapper;
import com.diacono.diacono.domain.entity.Evento;
import com.diacono.diacono.domain.enums.EnumStatusEvento;
import com.diacono.diacono.domain.repository.EventoRepository;
import com.diacono.diacono.global.error.exceptions.ObjectNotFoundException;
import com.diacono.diacono.usecases.eventos.validation.ValidarIdExternoPreenchido;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

//noinspection unused
@ExtendWith(MockitoExtension.class)
@SuppressWarnings("unused")
class BuscarEventoEspecificoUseCaseTest {

	@Mock
	private EventoRepository eventoRepository;

	@Mock
	private EventoMapper eventoMapper;

	@Mock
	private ValidarIdExternoPreenchido validarIdExternoPreenchido;

	@InjectMocks
	private BuscarEventoEspecificoUseCase useCase;

	@Test
	void deveRetornarEventoCompletoQuandoEventoExistir() {
		UUID idEvento = UUID.fromString("11111111-1111-1111-1111-111111111111");
		UUID igrejaId = UUID.fromString("22222222-2222-2222-2222-222222222222");
		Evento evento = Evento.builder().nome("Culto").dataHoraInicio(LocalDateTime.of(2026, 5, 10, 19, 0)).build();
		EventoCompletoDTO dto = new EventoCompletoDTO("Culto", "Descricao", "JOVENS", null, null, null, null, null, null, null, null, EnumStatusEvento.PENDENTE);

		evento.setIgreja(com.diacono.diacono.domain.entity.Igreja.builder().idExterno(igrejaId).build());
		when(eventoRepository.findByIdExterno(idEvento)).thenReturn(Optional.of(evento));
		when(eventoMapper.paraEventoCompletoDTO(evento)).thenReturn(dto);

		EventoCompletoDTO response = useCase.execute(idEvento, igrejaId);

		assertSame(dto, response);
		assertEquals(EnumStatusEvento.PENDENTE, response.status());
		verify(validarIdExternoPreenchido).validarIdExternoPreenchido(idEvento);
	}

	@Test
	void deveLancarExcecaoQuandoEventoNaoExistir() {
		UUID idEvento = UUID.fromString("11111111-1111-1111-1111-111111111111");
		when(eventoRepository.findByIdExterno(idEvento)).thenReturn(Optional.empty());

		ObjectNotFoundException ex = assertThrows(ObjectNotFoundException.class, () -> useCase.execute(idEvento, null));

		assertEquals("Evento não encontrado", ex.getMessage());
	}
}