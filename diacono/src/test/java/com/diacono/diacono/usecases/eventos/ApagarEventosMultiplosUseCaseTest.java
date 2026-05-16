package com.diacono.diacono.usecases.eventos;

import com.diacono.diacono.applications.dtos.RestResponseMessageDTO;
import com.diacono.diacono.domain.entity.Evento;
import com.diacono.diacono.domain.entity.Igreja;
import com.diacono.diacono.domain.entity.Recorrencia;
import com.diacono.diacono.domain.enums.TipoRecorrencia;
import com.diacono.diacono.domain.repository.EventoRepository;
import com.diacono.diacono.global.error.exceptions.ObjectNotFoundException;
import com.diacono.diacono.usecases.eventos.validation.ValidarIdExternoPreenchido;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

//noinspection unused
@ExtendWith(MockitoExtension.class)
@SuppressWarnings("unused")
class ApagarEventosMultiplosUseCaseTest {

	@Mock
	private EventoRepository eventoRepository;

	@Mock
	private ValidarIdExternoPreenchido validarIdExternoPreenchido;

	@InjectMocks
	private ApagarEventosMultiplosUseCase useCase;

	@Test
	void deveApagarEventosMultiplosComSucesso() {
		UUID idEvento = UUID.fromString("11111111-1111-1111-1111-111111111111");
		UUID idIgreja = UUID.fromString("22222222-2222-2222-2222-222222222222");

		Igreja igreja = Igreja.builder().nome("Igreja Central").build();
		ReflectionTestUtils.setField(igreja, "idExterno", idIgreja);

		Evento evento = new Evento();
		evento.setDataHoraInicio(LocalDateTime.of(2026, 5, 10, 19, 0));
		evento.setRecorrencia(Recorrencia.builder().tipoRecorrencia(TipoRecorrencia.SEMANAL).build());
		evento.setIgreja(igreja);

		when(eventoRepository.findByIdExterno(idEvento)).thenReturn(Optional.of(evento));
		when(eventoRepository.findByPeriodoAndRecorrencia(evento.getRecorrencia(), evento.getDataHoraInicio(), idIgreja))
				.thenReturn(new ArrayList<>());

		RestResponseMessageDTO response = useCase.execute(idEvento, idIgreja);

		assertEquals(HttpStatus.OK, response.getStatus());
		assertEquals("Eventos apagados com sucesso", response.getMessage());

		@SuppressWarnings("unchecked")
		ArgumentCaptor<List<Evento>> captor = ArgumentCaptor.forClass(List.class);
		verify(eventoRepository).deleteAll(captor.capture());
		assertEquals(1, captor.getValue().size());
		assertEquals(evento, captor.getValue().getFirst());
	}

	@Test
	void deveLancarExcecaoQuandoEventoBaseNaoForEncontrado() {
		UUID idEvento = UUID.fromString("11111111-1111-1111-1111-111111111111");
		UUID idIgreja = UUID.fromString("22222222-2222-2222-2222-222222222222");
		when(eventoRepository.findByIdExterno(idEvento)).thenReturn(Optional.empty());

		ObjectNotFoundException ex = assertThrows(ObjectNotFoundException.class, () -> useCase.execute(idEvento, idIgreja));

		assertEquals("Não foi possível apagar o evento, verifique se o evento existe", ex.getMessage());
	}

	@Test
	void deveLancarExcecaoQuandoEventoPertenceAOutraIgreja() {
		UUID idEvento = UUID.fromString("11111111-1111-1111-1111-111111111111");
		UUID idIgreja = UUID.fromString("22222222-2222-2222-2222-222222222222");
		UUID outraIgrejaId = UUID.fromString("33333333-3333-3333-3333-333333333333");

		Igreja igreja = Igreja.builder().nome("Outra Igreja").build();
		ReflectionTestUtils.setField(igreja, "idExterno", outraIgrejaId);

		Evento evento = new Evento();
		evento.setDataHoraInicio(LocalDateTime.of(2026, 5, 10, 19, 0));
		evento.setRecorrencia(Recorrencia.builder().tipoRecorrencia(TipoRecorrencia.SEMANAL).build());
		evento.setIgreja(igreja);

		when(eventoRepository.findByIdExterno(idEvento)).thenReturn(Optional.of(evento));

		ObjectNotFoundException ex = assertThrows(ObjectNotFoundException.class, () -> useCase.execute(idEvento, idIgreja));

		assertEquals("Não foi possível apagar o evento, verifique se o evento existe", ex.getMessage());
	}
}