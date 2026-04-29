package com.diacono.diacono.usecases.ministerio;

import com.diacono.diacono.applications.dtos.ministerio.MinisterioSimplificadoDTO;
import com.diacono.diacono.applications.mappers.ministerio.MinisterioMapper;
import com.diacono.diacono.domain.entity.Ministerio;
import com.diacono.diacono.domain.enums.EnumStatusMinisterio;
import com.diacono.diacono.domain.repository.MinisteriosRepository;
import com.diacono.diacono.global.error.exceptions.ObjectNotFoundException;
import com.diacono.diacono.global.util.JwtUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BuscarMinisteriosGeraisUseCaseTest {

	@Mock
	private MinisteriosRepository ministeriosRepository;

	@Mock
	private MinisterioMapper ministerioMapper;

	@Mock
	private JwtUtils jwtUtils;

	@InjectMocks
	private BuscarMinisteriosGeraisUseCase useCase;

	@Test
	void deveBuscarMinisteriosGeraisComSucesso() {
		UUID idIgreja = UUID.fromString("11111111-1111-1111-1111-111111111111");
		Ministerio ministerio = Ministerio.builder().nome("Louvor").build();
		MinisterioSimplificadoDTO dto = new MinisterioSimplificadoDTO(
				UUID.fromString("22222222-2222-2222-2222-222222222222"),
				"Louvor",
				"Samuel",
				EnumStatusMinisterio.ATIVO,
				LocalDate.of(2026, 1, 1)
		);

		when(jwtUtils.getIgrejaId()).thenReturn(idIgreja);
		when(ministeriosRepository.findByIgrejaIdExterno(idIgreja)).thenReturn(List.of(ministerio));
		when(ministerioMapper.paraMinisterioSimplificadoDTO(ministerio)).thenReturn(dto);

		List<MinisterioSimplificadoDTO> response = useCase.execute();

		assertEquals(1, response.size());
		assertEquals(dto, response.getFirst());
		verify(jwtUtils).getIgrejaId();
		verify(ministeriosRepository).findByIgrejaIdExterno(idIgreja);
		verify(ministerioMapper).paraMinisterioSimplificadoDTO(ministerio);
	}

	@Test
	void deveLancarExcecaoQuandoNaoEncontrarMinisterios() {
		UUID idIgreja = UUID.fromString("11111111-1111-1111-1111-111111111111");

		when(jwtUtils.getIgrejaId()).thenReturn(idIgreja);
		when(ministeriosRepository.findByIgrejaIdExterno(idIgreja)).thenReturn(List.of());

		ObjectNotFoundException ex = assertThrows(ObjectNotFoundException.class, () -> useCase.execute());

		assertEquals("Nenhum ministério encontrado", ex.getMessage());
		verify(ministerioMapper, never()).paraMinisterioSimplificadoDTO(org.mockito.ArgumentMatchers.any());
	}
}