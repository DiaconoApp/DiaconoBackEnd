package com.diacono.diacono.usecases.eventos;

import com.diacono.diacono.domain.entity.Ministerio;
import com.diacono.diacono.domain.repository.MinisteriosRepository;
import com.diacono.diacono.global.error.exceptions.ObjectNotFoundException;
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

//noinspection unused
@ExtendWith(MockitoExtension.class)
@SuppressWarnings("unused")
class BuscarMinisterioPorUUIDUseCaseTest {

	@Mock
	private MinisteriosRepository ministeriosRepository;

	@InjectMocks
	private BuscarMinisterioPorUUIDUseCase useCase;

	@Test
	void deveRetornarMinisteriosQuandoEncontrados() {
		UUID idMinisterio = UUID.fromString("11111111-1111-1111-1111-111111111111");
		UUID igrejaId = UUID.fromString("22222222-2222-2222-2222-222222222222");
		Ministerio ministerio = Ministerio.builder().nome("Louvor").build();

		// A01: Validar scoping por Igreja
		when(ministeriosRepository.findAllByIdExternoInAndIgrejaId(List.of(idMinisterio), igrejaId)).thenReturn(Set.of(ministerio));

		Set<Ministerio> response = useCase.execute(List.of(idMinisterio), igrejaId);

		assertEquals(1, response.size());
	}

	@Test
	void deveLancarExcecaoQuandoNaoEncontrarMinisterios() {
		UUID idMinisterio = UUID.fromString("11111111-1111-1111-1111-111111111111");
		UUID igrejaId = UUID.fromString("22222222-2222-2222-2222-222222222222");
		when(ministeriosRepository.findAllByIdExternoInAndIgrejaId(List.of(idMinisterio), igrejaId)).thenReturn(Set.of());

		// A01: Validar que ObjectNotFoundException é lançada quando nenhum ministério da Igreja é encontrado
		ObjectNotFoundException ex = assertThrows(ObjectNotFoundException.class, () -> useCase.execute(List.of(idMinisterio), igrejaId));

		assertEquals("Ministérios não encontrados", ex.getMessage());
	}
}