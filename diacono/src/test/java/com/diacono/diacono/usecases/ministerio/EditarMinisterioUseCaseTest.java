package com.diacono.diacono.usecases.ministerio;

import com.diacono.diacono.applications.dtos.RestResponseMessageDTO;
import com.diacono.diacono.applications.dtos.ministerio.MinisterioUpdateDTO;
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
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EditarMinisterioUseCaseTest {

	@Mock
	private MinisteriosRepository ministeriosRepository;

	@Mock
	private MembroJpaRepository membroRepository;

	@InjectMocks
	private EditarMinisterioUseCase useCase;

	private static final UUID ID_MINISTERIO   = UUID.fromString("11111111-1111-1111-1111-111111111111");
	private static final UUID ID_NOVO_LIDER   = UUID.fromString("22222222-2222-2222-2222-222222222222");
	private static final UUID ID_LIDER_ATUAL  = UUID.fromString("33333333-3333-3333-3333-333333333333");
	private static final UUID ID_IGREJA       = UUID.fromString("44444444-4444-4444-4444-444444444444");
	private static final UUID ID_OUTRA_IGREJA = UUID.fromString("55555555-5555-5555-5555-555555555555");

	private Igreja igreja(UUID igrejaId) {
		Igreja igreja = Igreja.builder().nome("Igreja Teste").build();
		ReflectionTestUtils.setField(igreja, "idExterno", igrejaId);
		return igreja;
	}

	private Ministerio ministerioComIgreja(UUID igrejaId) {
		Ministerio ministerio = Ministerio.builder()
				.nome("Nome Antigo")
				.status(EnumStatusMinisterio.ATIVO)
				.build();
		ReflectionTestUtils.setField(ministerio, "idExterno", ID_MINISTERIO);
		ministerio.setIgreja(igreja(igrejaId));
		ministerio.setMembros(new HashSet<>());
		return ministerio;
	}

	@Test
	void deveAtualizarNomeEStatusComSucessoSemTrocarLider() {
		Ministerio ministerioExistente = ministerioComIgreja(ID_IGREJA);
		MinisterioUpdateDTO dto = new MinisterioUpdateDTO("Nome Novo", EnumStatusMinisterio.INATIVO, null);

		when(ministeriosRepository.findByIdExterno(ID_MINISTERIO)).thenReturn(Optional.of(ministerioExistente));

		RestResponseMessageDTO response = useCase.execute(dto, ID_MINISTERIO, ID_IGREJA);

		assertEquals(HttpStatus.OK, response.getStatus());
		assertEquals("Ministério atualizado com sucesso", response.getMessage());
		assertEquals("Nome Novo", ministerioExistente.getNome());
		assertEquals(EnumStatusMinisterio.INATIVO, ministerioExistente.getStatus());
		verify(ministeriosRepository).save(ministerioExistente);
		verify(membroRepository, never()).findByIdExterno(any());
	}

	@Test
	void deveLancarExcecaoQuandoMinisterioNaoForEncontrado() {
		MinisterioUpdateDTO dto = new MinisterioUpdateDTO("Nome Novo", EnumStatusMinisterio.ATIVO, null);

		when(ministeriosRepository.findByIdExterno(ID_MINISTERIO)).thenReturn(Optional.empty());

		ObjectNotFoundException ex = assertThrows(
				ObjectNotFoundException.class,
				() -> useCase.execute(dto, ID_MINISTERIO, ID_IGREJA)
		);

		assertEquals("Ministério não encontrado", ex.getMessage());
		verify(ministeriosRepository, never()).save(any());
	}

	@Test
	void deveLancarExcecaoQuandoMinisterioPertenceAOutraIgreja() {
		Ministerio ministerioOutraIgreja = ministerioComIgreja(ID_OUTRA_IGREJA);
		MinisterioUpdateDTO dto = new MinisterioUpdateDTO("Nome Novo", null, null);

		when(ministeriosRepository.findByIdExterno(ID_MINISTERIO)).thenReturn(Optional.of(ministerioOutraIgreja));

		ObjectNotFoundException ex = assertThrows(
				ObjectNotFoundException.class,
				() -> useCase.execute(dto, ID_MINISTERIO, ID_IGREJA)
		);

		assertEquals("Ministério não encontrado", ex.getMessage());
		verify(ministeriosRepository, never()).save(any());
		verifyNoInteractions(membroRepository);
	}

	@Test
	void deveLancarExcecaoQuandoNovoLiderNaoForEncontrado() {
		Ministerio ministerioExistente = ministerioComIgreja(ID_IGREJA);
		MinisterioUpdateDTO dto = new MinisterioUpdateDTO(null, null, ID_NOVO_LIDER);

		when(ministeriosRepository.findByIdExterno(ID_MINISTERIO)).thenReturn(Optional.of(ministerioExistente));
		when(membroRepository.findByIdExterno(ID_NOVO_LIDER)).thenReturn(null);

		ObjectNotFoundException ex = assertThrows(
				ObjectNotFoundException.class,
				() -> useCase.execute(dto, ID_MINISTERIO, ID_IGREJA)
		);

		assertEquals("Novo líder não encontrado", ex.getMessage());
		verify(ministeriosRepository, never()).save(any());
	}

	@Test
	void deveLancarExcecaoQuandoNovoLiderPertenceAOutraIgreja() {
		Ministerio ministerioExistente = ministerioComIgreja(ID_IGREJA);
		MinisterioUpdateDTO dto = new MinisterioUpdateDTO(null, null, ID_NOVO_LIDER);

		Membro liderOutraIgreja = new Membro();
		liderOutraIgreja.setIgreja(igreja(ID_OUTRA_IGREJA));
		ReflectionTestUtils.setField(liderOutraIgreja, "idExterno", ID_NOVO_LIDER);

		when(ministeriosRepository.findByIdExterno(ID_MINISTERIO)).thenReturn(Optional.of(ministerioExistente));
		when(membroRepository.findByIdExterno(ID_NOVO_LIDER)).thenReturn(liderOutraIgreja);

		ObjectNotFoundException ex = assertThrows(
				ObjectNotFoundException.class,
				() -> useCase.execute(dto, ID_MINISTERIO, ID_IGREJA)
		);

		assertEquals("Novo líder não encontrado", ex.getMessage());
		verify(ministeriosRepository, never()).save(any());
	}

	@Test
	void deveTrocarLiderQuandoNovoLiderJaPertencerAoMinisterio() {
		Membro liderAtualMembro = new Membro();
		liderAtualMembro.setNome("Lider Antigo");
		liderAtualMembro.setCargoMembro(EnumCargoMembro.LIDER_MINISTERIO);
		liderAtualMembro.setIgreja(igreja(ID_IGREJA));
		ReflectionTestUtils.setField(liderAtualMembro, "idExterno", ID_LIDER_ATUAL);

		Membro novoLiderMembro = new Membro();
		novoLiderMembro.setNome("Lider Novo");
		novoLiderMembro.setCargoMembro(EnumCargoMembro.MEMBRO);
		novoLiderMembro.setIgreja(igreja(ID_IGREJA));
		ReflectionTestUtils.setField(novoLiderMembro, "idExterno", ID_NOVO_LIDER);

		Ministerio ministerioExistente = ministerioComIgreja(ID_IGREJA);
		ministerioExistente.setNomeLider("Lider Antigo");

		MembroMinisterio liderAtualRelacao = MembroMinisterio.builder()
				.membro(liderAtualMembro)
				.ministerio(ministerioExistente)
				.cargoMembro(EnumCargoMembroMinisterio.LIDER_MINISTERIO)
				.nomeMinisterio("Louvor")
				.build();

		MembroMinisterio novoLiderRelacao = MembroMinisterio.builder()
				.membro(novoLiderMembro)
				.ministerio(ministerioExistente)
				.cargoMembro(EnumCargoMembroMinisterio.MEMBRO_MINISTERIO)
				.nomeMinisterio("Louvor")
				.build();

		Set<MembroMinisterio> membros = new HashSet<>();
		membros.add(liderAtualRelacao);
		membros.add(novoLiderRelacao);
		ministerioExistente.setMembros(membros);

		MinisterioUpdateDTO dto = new MinisterioUpdateDTO(null, null, ID_NOVO_LIDER);

		when(ministeriosRepository.findByIdExterno(ID_MINISTERIO)).thenReturn(Optional.of(ministerioExistente));
		when(membroRepository.findByIdExterno(ID_NOVO_LIDER)).thenReturn(novoLiderMembro);

		useCase.execute(dto, ID_MINISTERIO, ID_IGREJA);

		assertEquals("Lider Novo", ministerioExistente.getNomeLider());
		assertEquals(EnumCargoMembro.LIDER_MINISTERIO, novoLiderMembro.getCargoMembro());
		assertEquals(EnumCargoMembroMinisterio.LIDER_MINISTERIO, novoLiderRelacao.getCargoMembro());
		assertEquals(EnumCargoMembroMinisterio.MEMBRO_MINISTERIO, liderAtualRelacao.getCargoMembro());
		assertEquals(EnumCargoMembro.MEMBRO, liderAtualMembro.getCargoMembro());
		verify(ministeriosRepository).save(ministerioExistente);
	}
}