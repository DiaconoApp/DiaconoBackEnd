package com.diacono.diacono.usecases.ministerio;

import com.diacono.diacono.applications.dtos.membro.MembroMinisterioInfoMembroDTO;
import com.diacono.diacono.applications.mappers.membro.MembroMinisterioMapper;
import com.diacono.diacono.domain.entity.MembroMinisterio;
import com.diacono.diacono.domain.enums.EnumCargoMembroMinisterio;
import com.diacono.diacono.domain.enums.EnumStatusMembro;
import com.diacono.diacono.domain.repository.MembroMinisterioRepository;
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
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BuscarMembroMinisterioLiderMinisterioComFiltroUseCaseTest {

	@Mock
	private MembroMinisterioRepository membroMinisterioRepository;

	@Mock
	private MembroMinisterioMapper mapper;

	@InjectMocks
	private BuscarMembroMinisterioLiderMinisterioComFiltroUseCase useCase;

	@Test
	void deveBuscarComFiltroEMapearResultado() {
		UUID idMinisterio = UUID.fromString("11111111-1111-1111-1111-111111111111");
		Pageable pageable = PageRequest.of(0, 10);
		EnumStatusMembro status = EnumStatusMembro.ATIVO;

		MembroMinisterio entidade = MembroMinisterio.builder().build();
		Page<MembroMinisterio> page = new PageImpl<>(List.of(entidade));

		MembroMinisterioInfoMembroDTO dto = new MembroMinisterioInfoMembroDTO(
				UUID.fromString("22222222-2222-2222-2222-222222222222"),
				"Samuel",
				"samuel@teste.com",
				"11999999999",
				EnumStatusMembro.ATIVO,
				LocalDate.of(2000, 1, 1),
				EnumCargoMembroMinisterio.MEMBRO_MINISTERIO,
				"Louvor"
		);

		when(membroMinisterioRepository.buscarPorMembroMinisterioComFiltro(pageable, idMinisterio, "%sam%", status))
				.thenReturn(page);
		when(mapper.paraMembroMinisterioInfoMembroDTO(entidade)).thenReturn(dto);

		Page<MembroMinisterioInfoMembroDTO> response = useCase.execute(idMinisterio, pageable, "sam", status);

		assertEquals(1, response.getTotalElements());
		assertEquals(dto, response.getContent().getFirst());
		verify(membroMinisterioRepository)
				.buscarPorMembroMinisterioComFiltro(pageable, idMinisterio, "%sam%", status);
		verify(mapper).paraMembroMinisterioInfoMembroDTO(entidade);
	}

	@Test
	void deveEnviarTextoNuloQuandoFiltroForEmBranco() {
		UUID idMinisterio = UUID.fromString("11111111-1111-1111-1111-111111111111");
		Pageable pageable = PageRequest.of(0, 10);

		MembroMinisterio entidade = MembroMinisterio.builder().build();
		Page<MembroMinisterio> page = new PageImpl<>(List.of(entidade));
		MembroMinisterioInfoMembroDTO dto = new MembroMinisterioInfoMembroDTO(
				UUID.fromString("22222222-2222-2222-2222-222222222222"),
				"Samuel",
				"samuel@teste.com",
				"11999999999",
				EnumStatusMembro.ATIVO,
				LocalDate.of(2000, 1, 1),
				EnumCargoMembroMinisterio.MEMBRO_MINISTERIO,
				"Louvor"
		);

		when(membroMinisterioRepository.buscarPorMembroMinisterioComFiltro(pageable, idMinisterio, null, EnumStatusMembro.ATIVO))
				.thenReturn(page);
		when(mapper.paraMembroMinisterioInfoMembroDTO(any(MembroMinisterio.class))).thenReturn(dto);

		useCase.execute(idMinisterio, pageable, "   ", EnumStatusMembro.ATIVO);

		verify(membroMinisterioRepository)
				.buscarPorMembroMinisterioComFiltro(eq(pageable), eq(idMinisterio), isNull(), eq(EnumStatusMembro.ATIVO));
	}

	@Test
	void deveLancarExcecaoQuandoBuscaRetornarVazia() {
		UUID idMinisterio = UUID.fromString("11111111-1111-1111-1111-111111111111");
		Pageable pageable = PageRequest.of(0, 10);
		Page<MembroMinisterio> pageVazia = Page.empty();

		when(membroMinisterioRepository.buscarPorMembroMinisterioComFiltro(pageable, idMinisterio, "%sam%", EnumStatusMembro.ATIVO))
				.thenReturn(pageVazia);

		ObjectNotFoundException ex = assertThrows(
				ObjectNotFoundException.class,
				() -> useCase.execute(idMinisterio, pageable, "sam", EnumStatusMembro.ATIVO)
		);

		assertEquals("Nenhum membro_ministerio encontrado com os filtros informados.", ex.getMessage());
		verify(mapper, never()).paraMembroMinisterioInfoMembroDTO(any());
	}
}