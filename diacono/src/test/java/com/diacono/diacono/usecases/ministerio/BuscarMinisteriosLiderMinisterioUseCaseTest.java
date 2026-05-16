package com.diacono.diacono.usecases.ministerio;

import com.diacono.diacono.applications.dtos.ministerio.MinisterioSuperSimplificadoDTO;
import com.diacono.diacono.domain.repository.MembroMinisterioRepository;
import com.diacono.diacono.global.error.exceptions.ObjectNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BuscarMinisteriosLiderMinisterioUseCaseTest {

	@Mock
	private MembroMinisterioRepository membroMinisterioRepository;

	@InjectMocks
	private BuscarMinisteriosLiderMinisterioUseCase useCase;

	@Test
	void deveBuscarMinisteriosDoLiderComSucesso() {
		UUID idMembro = UUID.fromString("11111111-1111-1111-1111-111111111111");
		UUID idIgreja = UUID.fromString("22222222-2222-2222-2222-222222222222");

		MinisterioSuperSimplificadoDTO dto = new MinisterioSuperSimplificadoDTO(
				UUID.fromString("33333333-3333-3333-3333-333333333333"),
				"Louvor"
		);

		when(membroMinisterioRepository.buscarMinisterioLider(idMembro, idIgreja))
				.thenReturn(List.of(dto));

		List<MinisterioSuperSimplificadoDTO> response = useCase.execute(idIgreja, idMembro);

		assertEquals(1, response.size());
		assertEquals(dto, response.get(0));

		verify(membroMinisterioRepository)
				.buscarMinisterioLider(idMembro, idIgreja);
	}

	@Test
	void deveLancarExcecaoQuandoNenhumMinisterioForEncontrado() {
		UUID idMembro = UUID.fromString("11111111-1111-1111-1111-111111111111");
		UUID idIgreja = UUID.fromString("22222222-2222-2222-2222-222222222222");

		when(membroMinisterioRepository.buscarMinisterioLider(idMembro, idIgreja))
				.thenReturn(List.of());

		ObjectNotFoundException ex = assertThrows(
				ObjectNotFoundException.class,
				() -> useCase.execute(idIgreja, idMembro)
		);

		assertEquals("Nenhum ministério encontrado para o líder informado.", ex.getMessage());
	}
}