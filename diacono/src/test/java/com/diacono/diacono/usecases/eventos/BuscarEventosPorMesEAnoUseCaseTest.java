package com.diacono.diacono.usecases.eventos;

import com.diacono.diacono.applications.dtos.evento.EventoSimplificadoDTO;
import com.diacono.diacono.applications.mappers.evento.EventoMapper;
import com.diacono.diacono.domain.entity.Evento;
import com.diacono.diacono.domain.repository.EventoRepository;
import com.diacono.diacono.global.error.exceptions.FieldInvalidException;
import com.diacono.diacono.global.error.exceptions.ObjectNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

//noinspection unused
@ExtendWith(MockitoExtension.class)
@SuppressWarnings("unused")
class BuscarEventosPorMesEAnoUseCaseTest {

	@Mock
	private EventoRepository eventoRepository;

	@Mock
	private EventoMapper eventoMapper;

	@InjectMocks
	private BuscarEventosPorMesEAnoUseCase useCase;

	@Test
	void deveRetornarEventosDoMesComSucesso() {
		UUID idIgreja = UUID.fromString("11111111-1111-1111-1111-111111111111");
		Evento evento = Evento.builder().nome("Culto").dataHoraInicio(LocalDateTime.of(2026, 5, 10, 19, 0)).build();
		EventoSimplificadoDTO dto = new EventoSimplificadoDTO(List.of());

		when(eventoRepository.findByPeriodo(
				LocalDateTime.of(2026, 5, 1, 0, 0),
				LocalDateTime.of(2026, 5, 31, 23, 59, 59),
				idIgreja)
		).thenReturn(List.of(evento));
		when(eventoMapper.paraEventoSimplificado(List.of(evento))).thenReturn(dto);

		EventoSimplificadoDTO response = useCase.execute(5, 2026, idIgreja);

		assertSame(dto, response);
	}

	@Test
	void deveLancarExcecaoQuandoMesForInvalido() {
		FieldInvalidException ex = assertThrows(FieldInvalidException.class, () -> useCase.execute(13, 2026, null));

		assertEquals("O mês precisa estar entre 1 e 12", ex.getMessage());
	}

	@Test
	void deveLancarExcecaoQuandoNaoEncontrarEventosNoPeriodo() {
		UUID idIgreja = UUID.fromString("11111111-1111-1111-1111-111111111111");

		when(eventoRepository.findByPeriodo(
				LocalDateTime.of(2026, 5, 1, 0, 0),
				LocalDateTime.of(2026, 5, 31, 23, 59, 59),
				idIgreja)
		).thenReturn(List.of());

		ObjectNotFoundException ex = assertThrows(ObjectNotFoundException.class, () -> useCase.execute(5, 2026, idIgreja));

		assertEquals("Nenhum evento encontrado para o mês e ano informados", ex.getMessage());
	}
}