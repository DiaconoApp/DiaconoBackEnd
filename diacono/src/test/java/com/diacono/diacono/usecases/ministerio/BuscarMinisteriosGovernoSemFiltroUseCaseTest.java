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
class BuscarMinisteriosGovernoSemFiltroUseCaseTest {

	@Mock
	private MinisteriosRepository ministeriosRepository;

	@Mock
	private MinisterioMapper ministerioMapper;

	@Mock
	private JwtUtils jwtUtils;

	@InjectMocks
	private BuscarMinisteriosGovernoSemFiltroUseCase useCase;

	@Test
	void deveBuscarSemFiltroEMapearResultado() {
		UUID idIgreja = UUID.fromString("11111111-1111-1111-1111-111111111111");
		Pageable pageable = PageRequest.of(0, 10);

		Ministerio ministerio = Ministerio.builder().nome("Louvor").build();
		Page<Ministerio> page = new PageImpl<>(List.of(ministerio));
		MinisterioSimplificadoDTO dto = new MinisterioSimplificadoDTO(
				UUID.fromString("22222222-2222-2222-2222-222222222222"),
				"Louvor",
				"Samuel",
				EnumStatusMinisterio.ATIVO,
				LocalDate.of(2026, 1, 1)
		);

		when(jwtUtils.getIgrejaId()).thenReturn(idIgreja);
		when(ministeriosRepository.findByIgrejaIdExterno(idIgreja, pageable)).thenReturn(page);
		when(ministerioMapper.paraMinisterioSimplificadoDTO(ministerio)).thenReturn(dto);

		Page<MinisterioSimplificadoDTO> response = useCase.execute(pageable);

		assertEquals(1, response.getTotalElements());
		assertEquals(dto, response.getContent().getFirst());
		verify(jwtUtils).getIgrejaId();
		verify(ministeriosRepository).findByIgrejaIdExterno(idIgreja, pageable);
		verify(ministerioMapper).paraMinisterioSimplificadoDTO(ministerio);
	}

	@Test
	void deveLancarExcecaoQuandoNenhumMinisterioForEncontrado() {
		UUID idIgreja = UUID.fromString("11111111-1111-1111-1111-111111111111");
		Pageable pageable = PageRequest.of(0, 10);

		when(jwtUtils.getIgrejaId()).thenReturn(idIgreja);
		when(ministeriosRepository.findByIgrejaIdExterno(idIgreja, pageable)).thenReturn(Page.empty());

		ObjectNotFoundException ex = assertThrows(ObjectNotFoundException.class, () -> useCase.execute(pageable));

		assertEquals("Nenhum ministério encontrado", ex.getMessage());
		verify(ministerioMapper, never()).paraMinisterioSimplificadoDTO(any());
	}
}