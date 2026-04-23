package com.diacono.diacono.usecases.ministerio;

import com.diacono.diacono.applications.dtos.RestResponseMessageDTO;
import com.diacono.diacono.domain.entity.Igreja;
import com.diacono.diacono.domain.entity.Ministerio;
import com.diacono.diacono.domain.repository.MembroMinisterioRepository;
import com.diacono.diacono.domain.repository.MinisteriosRepository;
import com.diacono.diacono.global.error.exceptions.ObjectNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RemoverMembroMinisterioLiderMinisterioUseCaseTest {

	@Mock
	private MembroMinisterioRepository membroMinisterioRepository;

	@Mock
	private MinisteriosRepository ministeriosRepository;

	@InjectMocks
	private RemoverMembroMinisterioLiderMinisterioUseCase useCase;

	private static final UUID ID_MINISTERIO  = UUID.fromString("11111111-1111-1111-1111-111111111111");
	private static final UUID ID_MEMBRO      = UUID.fromString("22222222-2222-2222-2222-222222222222");
	private static final UUID ID_IGREJA      = UUID.fromString("33333333-3333-3333-3333-333333333333");
	private static final UUID ID_OUTRA_IGREJA = UUID.fromString("44444444-4444-4444-4444-444444444444");

	private Ministerio ministerioComIgreja(UUID igrejaId) {
		Igreja igreja = mock(Igreja.class);
		when(igreja.getIdExterno()).thenReturn(igrejaId);

		Ministerio ministerio = new Ministerio();
		ministerio.setIgreja(igreja);
		return ministerio;
	}

	@Test
	void deveRemoverMembroDoMinisterioComSucesso() {
		when(ministeriosRepository.findByIdExterno(ID_MINISTERIO))
				.thenReturn(Optional.of(ministerioComIgreja(ID_IGREJA)));
		when(membroMinisterioRepository.deleteByMembroIdExternoAndMinisterioIdExterno(ID_MEMBRO, ID_MINISTERIO))
				.thenReturn(1);

		RestResponseMessageDTO response = useCase.execute(ID_MINISTERIO, ID_MEMBRO, ID_IGREJA);

		assertEquals(HttpStatus.OK, response.getStatus());
		assertEquals("Membro removido do ministério com sucesso", response.getMessage());
		verify(ministeriosRepository).findByIdExterno(ID_MINISTERIO);
		verify(membroMinisterioRepository).deleteByMembroIdExternoAndMinisterioIdExterno(ID_MEMBRO, ID_MINISTERIO);
	}

	@Test
	void deveLancarExcecaoQuandoMinisterioNaoForEncontrado() {
		when(ministeriosRepository.findByIdExterno(ID_MINISTERIO))
				.thenReturn(Optional.empty());

		ObjectNotFoundException ex = assertThrows(
				ObjectNotFoundException.class,
				() -> useCase.execute(ID_MINISTERIO, ID_MEMBRO, ID_IGREJA)
		);

		assertEquals("Ministério não encontrado", ex.getMessage());
		verify(ministeriosRepository).findByIdExterno(ID_MINISTERIO);
		verifyNoInteractions(membroMinisterioRepository);
	}

	@Test
	void deveLancarExcecaoQuandoMinisterioPertenceAOutraIgreja() {
		when(ministeriosRepository.findByIdExterno(ID_MINISTERIO))
				.thenReturn(Optional.of(ministerioComIgreja(ID_OUTRA_IGREJA)));

		ObjectNotFoundException ex = assertThrows(
				ObjectNotFoundException.class,
				() -> useCase.execute(ID_MINISTERIO, ID_MEMBRO, ID_IGREJA)
		);

		assertEquals("Ministério não encontrado", ex.getMessage());
		verify(ministeriosRepository).findByIdExterno(ID_MINISTERIO);
		verifyNoInteractions(membroMinisterioRepository);
	}

	@Test
	void deveLancarExcecaoQuandoMinisterioSemIgrejaAssociada() {
		Ministerio ministerioSemIgreja = new Ministerio();

		when(ministeriosRepository.findByIdExterno(ID_MINISTERIO))
				.thenReturn(Optional.of(ministerioSemIgreja));

		ObjectNotFoundException ex = assertThrows(
				ObjectNotFoundException.class,
				() -> useCase.execute(ID_MINISTERIO, ID_MEMBRO, ID_IGREJA)
		);

		assertEquals("Ministério não encontrado", ex.getMessage());
		verifyNoInteractions(membroMinisterioRepository);
	}

	@Test
	void deveLancarExcecaoQuandoMembroNaoForEncontradoNoMinisterio() {
		when(ministeriosRepository.findByIdExterno(ID_MINISTERIO))
				.thenReturn(Optional.of(ministerioComIgreja(ID_IGREJA)));
		when(membroMinisterioRepository.deleteByMembroIdExternoAndMinisterioIdExterno(ID_MEMBRO, ID_MINISTERIO))
				.thenReturn(0);

		ObjectNotFoundException ex = assertThrows(
				ObjectNotFoundException.class,
				() -> useCase.execute(ID_MINISTERIO, ID_MEMBRO, ID_IGREJA)
		);

		assertEquals("Membro do ministério não encontrado para remoção.", ex.getMessage());
		verify(membroMinisterioRepository).deleteByMembroIdExternoAndMinisterioIdExterno(ID_MEMBRO, ID_MINISTERIO);
	}
}