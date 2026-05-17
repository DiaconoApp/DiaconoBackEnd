package com.diacono.diacono.usecases.ministerio;

import com.diacono.diacono.applications.dtos.RestResponseMessageDTO;
import com.diacono.diacono.applications.dtos.ministerio.MinisterioCreateDTO;
import com.diacono.diacono.domain.entity.Igreja;
import com.diacono.diacono.domain.entity.Membro;
import com.diacono.diacono.domain.entity.MembroMinisterio;
import com.diacono.diacono.domain.entity.Ministerio;
import com.diacono.diacono.domain.enums.EnumCargoMembro;
import com.diacono.diacono.domain.enums.EnumCargoMembroMinisterio;
import com.diacono.diacono.domain.enums.EnumStatusMinisterio;
import com.diacono.diacono.domain.repository.MinisteriosRepository;
import com.diacono.diacono.global.error.exceptions.ObjectNotFoundException;
import com.diacono.diacono.infrastructure.persistence.springdata.MembroJpaRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDate;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AdicionarMinisterioUseCaseTest {

	@Mock
	private MinisteriosRepository ministeriosRepository;

	@Mock
	private MembroJpaRepository membroRepository;

	@InjectMocks
	private AdicionarMinisterioUseCase useCase;

	@Test
	void deveCriarMinisterioComSucessoEPromoverLider() {
		UUID idLider = UUID.fromString("11111111-1111-1111-1111-111111111111");
		UUID igrejaId = UUID.fromString("22222222-2222-2222-2222-222222222222");
		MinisterioCreateDTO dto = new MinisterioCreateDTO(idLider, "Louvor");

		Igreja igreja = Igreja.builder()
				.nome("Igreja Central")
				.build();
		// Força o idExterno para simular que pertence à mesma Igreja do token
		ReflectionTestUtils.setField(igreja, "idExterno", igrejaId);

		Membro lider = new Membro();
		lider.setNome("Samuel");
		lider.setIgreja(igreja);
		lider.setCargoMembro(EnumCargoMembro.MEMBRO);

		when(membroRepository.findByIdExterno(idLider)).thenReturn(lider);

		RestResponseMessageDTO response = useCase.execute(dto, igrejaId);

		assertEquals(HttpStatus.CREATED, response.getStatus());
		assertEquals("Ministério criado com sucesso", response.getMessage());
		assertEquals(EnumCargoMembro.LIDER_MINISTERIO, lider.getCargoMembro());

		ArgumentCaptor<Ministerio> captor = ArgumentCaptor.forClass(Ministerio.class);
		verify(ministeriosRepository).save(captor.capture());
		Ministerio salvo = captor.getValue();

		assertEquals("Louvor", salvo.getNome());
		assertEquals("Samuel", salvo.getNomeLider());
		assertEquals(igreja, salvo.getIgreja());
		assertEquals(EnumStatusMinisterio.ATIVO, salvo.getStatus());
		assertEquals(LocalDate.now(), salvo.getDataCriacao());
		assertNotNull(salvo.getMembros());
		assertEquals(1, salvo.getMembros().size());

		MembroMinisterio membroLider = salvo.getMembros().iterator().next();
		assertEquals(lider, membroLider.getMembro());
		assertEquals(salvo, membroLider.getMinisterio());
		assertEquals(EnumCargoMembroMinisterio.LIDER_MINISTERIO, membroLider.getCargoMembro());
		assertEquals("Louvor", membroLider.getNomeMinisterio());
		assertEquals(LocalDate.now(), membroLider.getDataRegistro());
	}

	@Test
	void deveLancarExcecaoQuandoLiderNaoForEncontrado() {
		UUID idLider = UUID.fromString("11111111-1111-1111-1111-111111111111");
		UUID igrejaId = UUID.fromString("22222222-2222-2222-2222-222222222222");
		MinisterioCreateDTO dto = new MinisterioCreateDTO(idLider, "Louvor");

		when(membroRepository.findByIdExterno(idLider)).thenReturn(null);

		ObjectNotFoundException ex = assertThrows(
				ObjectNotFoundException.class,
				() -> useCase.execute(dto, igrejaId)
		);

		assertEquals("Líder do ministério não encontrado", ex.getMessage());
		verify(ministeriosRepository, never()).save(any());
	}

	@Test
	void deveLancarExcecaoQuandoLiderPertenceAOutraIgreja() {
		UUID idLider = UUID.fromString("11111111-1111-1111-1111-111111111111");
		UUID igrejaId = UUID.fromString("22222222-2222-2222-2222-222222222222");
		UUID outkaIgrejaId = UUID.fromString("33333333-3333-3333-3333-333333333333");
		MinisterioCreateDTO dto = new MinisterioCreateDTO(idLider, "Louvor");

		Igreja igrejaOutra = Igreja.builder()
				.nome("Outra Igreja")
				.build();
		// Força o idExterno para simular uma Igreja diferente
		ReflectionTestUtils.setField(igrejaOutra, "idExterno", outkaIgrejaId);

		Membro lider = new Membro();
		lider.setNome("Samuel");
		lider.setIgreja(igrejaOutra);
		lider.setCargoMembro(EnumCargoMembro.MEMBRO);

		when(membroRepository.findByIdExterno(idLider)).thenReturn(lider);

		ObjectNotFoundException ex = assertThrows(
				ObjectNotFoundException.class,
				() -> useCase.execute(dto, igrejaId)
		);

		assertEquals("Líder do ministério não encontrado", ex.getMessage());
		verify(ministeriosRepository, never()).save(any());
	}

	@Test
	void deveLancarExcecaoQuandoIgrejaDoLiderForNula() {
		UUID idLider = UUID.fromString("11111111-1111-1111-1111-111111111111");
		UUID igrejaId = UUID.fromString("22222222-2222-2222-2222-222222222222");
		MinisterioCreateDTO dto = new MinisterioCreateDTO(idLider, "Louvor");

		Membro lider = new Membro();
		lider.setNome("Samuel");
		lider.setIgreja(null);
		lider.setCargoMembro(EnumCargoMembro.MEMBRO);

		when(membroRepository.findByIdExterno(idLider)).thenReturn(lider);

		ObjectNotFoundException ex = assertThrows(
				ObjectNotFoundException.class,
				() -> useCase.execute(dto, igrejaId)
		);

		assertEquals("Líder do ministério não encontrado", ex.getMessage());
		verify(ministeriosRepository, never()).save(any());
	}
}