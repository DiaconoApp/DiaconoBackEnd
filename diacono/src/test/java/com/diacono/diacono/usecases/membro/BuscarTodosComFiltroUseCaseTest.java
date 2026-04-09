package com.diacono.diacono.usecases.membro;

import com.diacono.diacono.applications.dtos.membro.MembroResponseDTO;
import com.diacono.diacono.applications.mappers.membro.MembroMapper;
import com.diacono.diacono.domain.entity.Membro;
import com.diacono.diacono.domain.entity.MembroMinisterio;
import com.diacono.diacono.domain.entity.Ministerio;
import com.diacono.diacono.domain.enums.EnumStatusMembro;
import com.diacono.diacono.domain.repository.MembroRepository;
import com.diacono.diacono.global.error.exceptions.ObjectNotFoundException;
import com.diacono.diacono.global.util.JwtUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BuscarTodosComFiltroUseCaseTest {

	@Mock
	private MembroRepository membroRepository;

	@Mock
	private MembroMapper membroMapper;

	@Mock
	private JwtUtils jwtUtils;

	@InjectMocks
	private BuscarTodosComFiltroUseCase useCase;

	@Test
	void deveRetornarMembrosFiltradosPorStatusEMinisterio() {
		UUID idIgreja = UUID.fromString("11111111-1111-1111-1111-111111111111");
		UUID idMinisterio = UUID.fromString("22222222-2222-2222-2222-222222222222");
		Pageable pageable = PageRequest.of(0, 10);

		Ministerio ministerio = Ministerio.builder().nome("Louvor").build();
		ReflectionTestUtils.setField(ministerio, "idExterno", idMinisterio);

		Membro membro = new Membro();
		membro.setStatus(EnumStatusMembro.ATIVO);
		MembroMinisterio membroMinisterio = MembroMinisterio.builder().ministerio(ministerio).build();
		Set<MembroMinisterio> ministerios = new HashSet<>();
		ministerios.add(membroMinisterio);
		membro.setMinisterios(ministerios);

		MembroResponseDTO dto = new MembroResponseDTO(
				UUID.fromString("33333333-3333-3333-3333-333333333333"),
				"Samuel",
				"samuel@teste.com",
				"11999999999",
				LocalDate.of(2000, 1, 1),
				null,
				EnumStatusMembro.ATIVO
		);

		when(jwtUtils.getIgrejaId()).thenReturn(idIgreja);
		when(membroRepository.findAllWithFilter("%sam%", idIgreja)).thenReturn(List.of(membro));
		when(membroMapper.paraMembrosResponseDTO(List.of(membro))).thenReturn(List.of(dto));

		Page<MembroResponseDTO> response = useCase.execute(pageable, "sam", EnumStatusMembro.ATIVO, idMinisterio);

		assertEquals(1, response.getTotalElements());
		assertEquals(dto, response.getContent().getFirst());
		verify(membroRepository).findAllWithFilter("%sam%", idIgreja);
		verify(membroMapper).paraMembrosResponseDTO(List.of(membro));
	}

	@Test
	void deveLancarExcecaoQuandoBuscaBrutaNaoRetornarMembros() {
		UUID idIgreja = UUID.fromString("11111111-1111-1111-1111-111111111111");
		Pageable pageable = PageRequest.of(0, 10);

		when(jwtUtils.getIgrejaId()).thenReturn(idIgreja);
		when(membroRepository.findAllWithFilter("%sam%", idIgreja)).thenReturn(List.of());

		ObjectNotFoundException ex = assertThrows(
				ObjectNotFoundException.class,
				() -> useCase.execute(pageable, "sam", null, null)
		);

		assertEquals("Nenhum membro encontrado", ex.getMessage());
		verify(membroMapper, never()).paraMembrosResponseDTO(anyList());
	}

	@Test
	void deveLancarExcecaoQuandoFiltrosEliminaremTodosOsMembros() {
		UUID idIgreja = UUID.fromString("11111111-1111-1111-1111-111111111111");
		Pageable pageable = PageRequest.of(0, 10);

		Membro membro = new Membro();
		membro.setStatus(EnumStatusMembro.INATIVO);

		when(jwtUtils.getIgrejaId()).thenReturn(idIgreja);
		when(membroRepository.findAllWithFilter("%sam%", idIgreja)).thenReturn(List.of(membro));

		ObjectNotFoundException ex = assertThrows(
				ObjectNotFoundException.class,
				() -> useCase.execute(pageable, "sam", EnumStatusMembro.ATIVO, null)
		);

		assertEquals("Nenhum membro encontrado", ex.getMessage());
		verify(membroMapper, never()).paraMembrosResponseDTO(anyList());
	}
}