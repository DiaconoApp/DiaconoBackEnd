package com.diacono.diacono.usecases.eventos;

import com.diacono.diacono.applications.dtos.RestResponseMessageDTO;
import com.diacono.diacono.domain.repository.EventoRepository;
import com.diacono.diacono.global.error.exceptions.ObjectNotFoundException;
import com.diacono.diacono.usecases.eventos.validation.ValidarIdExternoPreenchido;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

//noinspection unused
@ExtendWith(MockitoExtension.class)
@SuppressWarnings("unused")
class ApagarEventoUnicoUseCaseTest {

	@Mock
	private EventoRepository eventoRepository;

	@Mock
	private ValidarIdExternoPreenchido validarIdExternoPreenchido;

	@InjectMocks
	private ApagarEventoUnicoUseCase useCase;

	@Test
	void deveApagarEventoUnicoComSucesso() {
		UUID idEvento = UUID.fromString("11111111-1111-1111-1111-111111111111");
		when(eventoRepository.deleteByIdExterno(idEvento)).thenReturn(1L);

		RestResponseMessageDTO response = useCase.execute(idEvento);

		assertEquals(HttpStatus.OK, response.getStatus());
		assertEquals("Evento apagado com sucesso", response.getMessage());
		verify(validarIdExternoPreenchido).validarIdExternoPreenchido(idEvento);
	}

	@Test
	void deveLancarExcecaoQuandoEventoNaoForEncontradoParaApagar() {
		UUID idEvento = UUID.fromString("11111111-1111-1111-1111-111111111111");
		when(eventoRepository.deleteByIdExterno(idEvento)).thenReturn(0L);

		ObjectNotFoundException ex = assertThrows(ObjectNotFoundException.class, () -> useCase.execute(idEvento));

		assertEquals("Não foi possível apagar o evento, verifique se o evento existe", ex.getMessage());
	}
}