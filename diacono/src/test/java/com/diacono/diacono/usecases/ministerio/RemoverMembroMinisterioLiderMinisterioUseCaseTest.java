package com.diacono.diacono.usecases.ministerio;

import com.diacono.diacono.applications.dtos.RestResponseMessageDTO;
import com.diacono.diacono.domain.repository.MembroMinisterioRepository;
import com.diacono.diacono.global.error.exceptions.ObjectNotFoundException;
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

@ExtendWith(MockitoExtension.class)
class RemoverMembroMinisterioLiderMinisterioUseCaseTest {

	@Mock
	private MembroMinisterioRepository membroMinisterioRepository;

	@InjectMocks
	private RemoverMembroMinisterioLiderMinisterioUseCase useCase;

	@Test
	void deveRemoverMembroDoMinisterioComSucesso() {
		UUID idMinisterio = UUID.fromString("11111111-1111-1111-1111-111111111111");
		UUID idMembro = UUID.fromString("22222222-2222-2222-2222-222222222222");

		when(membroMinisterioRepository.deleteByMembroIdExternoAndMinisterioIdExterno(idMembro, idMinisterio))
				.thenReturn(1);

		RestResponseMessageDTO response = useCase.execute(idMinisterio, idMembro);

		assertEquals(HttpStatus.OK, response.getStatus());
		assertEquals("Membro removido do ministério com sucesso", response.getMessage());
		verify(membroMinisterioRepository).deleteByMembroIdExternoAndMinisterioIdExterno(idMembro, idMinisterio);
	}

	@Test
	void deveLancarExcecaoQuandoMembroDoMinisterioNaoForEncontradoParaRemocao() {
		UUID idMinisterio = UUID.fromString("11111111-1111-1111-1111-111111111111");
		UUID idMembro = UUID.fromString("22222222-2222-2222-2222-222222222222");

		when(membroMinisterioRepository.deleteByMembroIdExternoAndMinisterioIdExterno(idMembro, idMinisterio))
				.thenReturn(0);

		ObjectNotFoundException ex = assertThrows(
				ObjectNotFoundException.class,
				() -> useCase.execute(idMinisterio, idMembro)
		);

		assertEquals("Membro do ministério não encontrado para remoção.", ex.getMessage());
		verify(membroMinisterioRepository).deleteByMembroIdExternoAndMinisterioIdExterno(idMembro, idMinisterio);
	}
}