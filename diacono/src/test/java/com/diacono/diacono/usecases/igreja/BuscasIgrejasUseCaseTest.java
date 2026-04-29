package com.diacono.diacono.usecases.igreja;

import com.diacono.diacono.applications.dtos.igreja.IgrejaSemiCompletoDTO;
import com.diacono.diacono.applications.mappers.igreja.IgrejaMapper;
import com.diacono.diacono.domain.entity.Igreja;
import com.diacono.diacono.domain.repository.IgrejaRepository;
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
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BuscasIgrejasUseCaseTest {

	@Mock
	private IgrejaRepository igrejaRepository;

	@Mock
	private IgrejaMapper igrejaMapper;

	@InjectMocks
	private BuscasIgrejasUseCase useCase;

	@Test
	void deveRetornarListaDeIgrejasMapeadaQuandoExistiremIgrejas() {
		Igreja igreja = new Igreja();
		List<Igreja> igrejas = List.of(igreja);
		IgrejaSemiCompletoDTO dto = new IgrejaSemiCompletoDTO(
				UUID.fromString("11111111-1111-1111-1111-111111111111"),
				"Igreja Central"
		);

		when(igrejaRepository.findAll()).thenReturn(igrejas);
		when(igrejaMapper.paraListaIgrejaSemiCompletoDTO(igrejas)).thenReturn(List.of(dto));

		List<IgrejaSemiCompletoDTO> response = useCase.execute();

		assertEquals(1, response.size());
		assertEquals(dto, response.getFirst());
		verify(igrejaMapper).paraListaIgrejaSemiCompletoDTO(igrejas);
	}

	@Test
	void deveLancarExcecaoQuandoListaDeIgrejasEstiverVazia() {
		when(igrejaRepository.findAll()).thenReturn(List.of());

		ObjectNotFoundException ex = assertThrows(ObjectNotFoundException.class, () -> useCase.execute());

		assertEquals("Igrejas não encontradas", ex.getMessage());
		verify(igrejaMapper, never()).paraListaIgrejaSemiCompletoDTO(org.mockito.ArgumentMatchers.anyList());
	}

	@Test
	void deveLancarExcecaoQuandoRepositorioRetornarListaNula() {
		when(igrejaRepository.findAll()).thenReturn(null);

		ObjectNotFoundException ex = assertThrows(ObjectNotFoundException.class, () -> useCase.buscarIgrejas());

		assertEquals("Igrejas não encontradas", ex.getMessage());
		verify(igrejaMapper, never()).paraListaIgrejaSemiCompletoDTO(org.mockito.ArgumentMatchers.anyList());
	}
}