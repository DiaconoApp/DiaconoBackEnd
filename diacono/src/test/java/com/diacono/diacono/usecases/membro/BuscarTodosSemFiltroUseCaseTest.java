package com.diacono.diacono.usecases.membro;

import com.diacono.diacono.applications.dtos.membro.MembroResponseDTO;
import com.diacono.diacono.applications.mappers.membro.MembroMapper;
import com.diacono.diacono.domain.entity.Membro;
import com.diacono.diacono.domain.enums.EnumCargoMembro;
import com.diacono.diacono.domain.enums.EnumStatusMembro;
import com.diacono.diacono.domain.repository.MembroRepository;
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
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BuscarTodosSemFiltroUseCaseTest {

	@Mock
	private MembroRepository membroRepository;

	@Mock
	private MembroMapper membroMapper;

	@InjectMocks
	private BuscarTodosSemFiltroUseCase useCase;

	@Test
	void deveRetornarPaginaMapeadaQuandoExistiremMembros() {
		UUID idIgreja = UUID.fromString("11111111-1111-1111-1111-111111111111");
		Pageable pageable = PageRequest.of(0, 10);

		Membro membro = new Membro();
		MembroResponseDTO dto = new MembroResponseDTO(
				UUID.fromString("22222222-2222-2222-2222-222222222222"),
				"Samuel",
				"samuel@teste.com",
				"11999999999",
				LocalDate.of(2000, 1, 1),
				null,
				EnumStatusMembro.ATIVO,
                EnumCargoMembro.MEMBRO
		);

		Page<Membro> membrosPage = new PageImpl<>(List.of(membro), pageable, 1);

		when(membroRepository.findByIgrejaIdExterno(idIgreja, pageable)).thenReturn(membrosPage);
		when(membroMapper.paraMembroResponseDTO(membro)).thenReturn(dto);

		Page<MembroResponseDTO> response = useCase.execute(pageable, idIgreja);

		assertEquals(1, response.getTotalElements());
		assertEquals(dto, response.getContent().getFirst());
		verify(membroMapper).paraMembroResponseDTO(membro);
	}

	@Test
	void deveLancarExcecaoQuandoPaginaEstiverVazia() {
		UUID idIgreja = UUID.fromString("11111111-1111-1111-1111-111111111111");
		Pageable pageable = PageRequest.of(0, 10);

		when(membroRepository.findByIgrejaIdExterno(idIgreja, pageable)).thenReturn(Page.empty());

		ObjectNotFoundException ex = assertThrows(ObjectNotFoundException.class, () -> useCase.execute(pageable, idIgreja));

		assertEquals("Nenhum membro encontrado", ex.getMessage());
		verify(membroMapper, never()).paraMembroResponseDTO(org.mockito.ArgumentMatchers.any());
	}
}