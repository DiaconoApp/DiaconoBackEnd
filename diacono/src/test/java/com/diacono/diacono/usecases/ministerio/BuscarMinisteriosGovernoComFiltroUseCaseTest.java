package com.diacono.diacono.usecases.ministerio;

import com.diacono.diacono.applications.dtos.ministerio.MinisterioSimplificadoDTO;
import com.diacono.diacono.applications.mappers.ministerio.MinisterioMapper;
import com.diacono.diacono.domain.entity.Ministerio;
import com.diacono.diacono.domain.enums.EnumStatusMinisterio;
import com.diacono.diacono.domain.repository.MinisteriosRepository;
import com.diacono.diacono.global.error.exceptions.ObjectNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BuscarMinisteriosGovernoComFiltroUseCaseTest {

	@Mock
	private MinisteriosRepository ministeriosRepository;

	@Mock
	private MinisterioMapper mapper;

	@InjectMocks
	private BuscarMinisteriosGovernoComFiltroUseCase useCase;

	@Test
	void deveBuscarComFiltroFormatadoEMapearResultado() {
		UUID idIgreja = UUID.fromString("11111111-1111-1111-1111-111111111111");
		Pageable pageable = PageRequest.of(0, 10);
		String busca = "  louvor  ";
		EnumStatusMinisterio status = EnumStatusMinisterio.ATIVO;

		Ministerio ministerio = Ministerio.builder().nome("LOUVOR").build();
		Page<Ministerio> page = new PageImpl<>(List.of(ministerio));
		MinisterioSimplificadoDTO dto = new MinisterioSimplificadoDTO(
				UUID.fromString("22222222-2222-2222-2222-222222222222"),
				"LOUVOR",
				"Samuel",
				EnumStatusMinisterio.ATIVO,
				LocalDate.of(2026, 1, 1)
		);

		when(ministeriosRepository.buscarComFiltros(pageable, "LOUVOR", status, idIgreja)).thenReturn(page);
		when(mapper.paraMinisterioSimplificadoDTO(ministerio)).thenReturn(dto);

		Page<MinisterioSimplificadoDTO> response = useCase.execute(pageable, busca, status, idIgreja);

		assertEquals(1, response.getTotalElements());
		assertEquals(dto, response.getContent().getFirst());
		verify(ministeriosRepository).buscarComFiltros(pageable, "LOUVOR", status, idIgreja);
		verify(mapper).paraMinisterioSimplificadoDTO(ministerio);
	}

	@Test
	void deveLancarExcecaoQuandoNenhumMinisterioForEncontrado() {
		UUID idIgreja = UUID.fromString("11111111-1111-1111-1111-111111111111");
		Pageable pageable = PageRequest.of(0, 10);

		when(ministeriosRepository.buscarComFiltros(pageable, "LOUVOR", EnumStatusMinisterio.ATIVO, idIgreja))
				.thenReturn(Page.empty());

		ObjectNotFoundException ex = assertThrows(
				ObjectNotFoundException.class,
				() -> useCase.execute(pageable, "louvor", EnumStatusMinisterio.ATIVO, idIgreja)
		);

		assertEquals("Nenhum ministério encontrado", ex.getMessage());
		verify(mapper, never()).paraMinisterioSimplificadoDTO(any());
	}
}