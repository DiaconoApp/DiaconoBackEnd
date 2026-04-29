package com.diacono.diacono.usecases.igreja;

import com.diacono.diacono.domain.entity.Igreja;
import com.diacono.diacono.domain.repository.IgrejaRepository;
import com.diacono.diacono.global.error.exceptions.ObjectNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@SuppressWarnings("unused")
class BuscarIgrejaPorUUIDUseCaseTest {

	@Mock
	private IgrejaRepository igrejaRepository;

	@InjectMocks
	private BuscarIgrejaPorUUIDUseCase useCase;

	@Test
	void deveRetornarIgrejaQuandoIdExternoExistir() {
		UUID idExterno = UUID.fromString("11111111-1111-1111-1111-111111111111");
		Igreja igreja = new Igreja();

		when(igrejaRepository.findByIdExterno(idExterno)).thenReturn(Optional.of(igreja));

		Igreja response = useCase.execute(idExterno);

		assertSame(igreja, response);
	}

	@Test
	void deveLancarExcecaoQuandoIgrejaNaoForEncontrada() {
		UUID idExterno = UUID.fromString("11111111-1111-1111-1111-111111111111");
		when(igrejaRepository.findByIdExterno(idExterno)).thenReturn(Optional.empty());

		ObjectNotFoundException ex = assertThrows(ObjectNotFoundException.class, () -> useCase.execute(idExterno));

		assertEquals("Igreja não encontrada", ex.getMessage());
	}
}