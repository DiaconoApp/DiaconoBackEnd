package com.diacono.diacono.usecases.ministerio;

import com.diacono.diacono.applications.dtos.RestResponseMessageDTO;
import com.diacono.diacono.applications.dtos.ministerio.MinisterioUpdateDTO;
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
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class EditarMinisterioUseCaseTest {

	@Mock
	private MinisteriosRepository ministeriosRepository;

	@Mock
	private MembroJpaRepository membroRepository;

	@InjectMocks
	private EditarMinisterioUseCase useCase;

	@Test
	void deveAtualizarNomeEStatusComSucessoSemTrocarLider() {
		UUID idMinisterio = UUID.fromString("11111111-1111-1111-1111-111111111111");
		Ministerio ministerioExistente = Ministerio.builder()
				.nome("Nome Antigo")
				.status(EnumStatusMinisterio.ATIVO)
				.build();

		MinisterioUpdateDTO dto = new MinisterioUpdateDTO("Nome Novo", EnumStatusMinisterio.INATIVO, null);

		when(ministeriosRepository.findByIdExterno(idMinisterio)).thenReturn(Optional.of(ministerioExistente));

		RestResponseMessageDTO response = useCase.execute(dto, idMinisterio);

		assertEquals(HttpStatus.OK, response.getStatus());
		assertEquals("Ministério atualizado com sucesso", response.getMessage());
		assertEquals("Nome Novo", ministerioExistente.getNome());
		assertEquals(EnumStatusMinisterio.INATIVO, ministerioExistente.getStatus());
		verify(ministeriosRepository).save(ministerioExistente);
		verify(membroRepository, never()).findByIdExterno(any());
	}

	@Test
	void deveLancarExcecaoQuandoMinisterioNaoForEncontrado() {
		UUID idMinisterio = UUID.fromString("11111111-1111-1111-1111-111111111111");
		MinisterioUpdateDTO dto = new MinisterioUpdateDTO("Nome Novo", EnumStatusMinisterio.ATIVO, null);

		when(ministeriosRepository.findByIdExterno(idMinisterio)).thenReturn(Optional.empty());

		ObjectNotFoundException ex = assertThrows(
				ObjectNotFoundException.class,
				() -> useCase.execute(dto, idMinisterio)
		);

		assertEquals("Ministério não encontrado", ex.getMessage());
		verify(ministeriosRepository, never()).save(any());
	}

	@Test
	void deveLancarExcecaoQuandoNovoLiderNaoForEncontrado() {
		UUID idMinisterio = UUID.fromString("11111111-1111-1111-1111-111111111111");
		UUID idNovoLider = UUID.fromString("22222222-2222-2222-2222-222222222222");

		Ministerio ministerioExistente = Ministerio.builder().nome("Louvor").build();
		ministerioExistente.setMembros(new HashSet<>());

		MinisterioUpdateDTO dto = new MinisterioUpdateDTO(null, null, idNovoLider);

		when(ministeriosRepository.findByIdExterno(idMinisterio)).thenReturn(Optional.of(ministerioExistente));
		when(membroRepository.findByIdExterno(idNovoLider)).thenReturn(null);

		ObjectNotFoundException ex = assertThrows(
				ObjectNotFoundException.class,
				() -> useCase.execute(dto, idMinisterio)
		);

		assertEquals("Novo líder não encontrado", ex.getMessage());
		verify(ministeriosRepository, never()).save(any());
	}

	@Test
	void deveTrocarLiderQuandoNovoLiderJaPertencerAoMinisterio() {
		UUID idMinisterio = UUID.fromString("11111111-1111-1111-1111-111111111111");
		UUID idNovoLider = UUID.fromString("22222222-2222-2222-2222-222222222222");
		UUID idLiderAtual = UUID.fromString("33333333-3333-3333-3333-333333333333");

		Membro liderAtualMembro = new Membro();
		liderAtualMembro.setNome("Lider Antigo");
		liderAtualMembro.setCargoMembro(EnumCargoMembro.LIDER_MINISTERIO);
		ReflectionTestUtils.setField(liderAtualMembro, "idExterno", idLiderAtual);

		Membro novoLiderMembro = new Membro();
		novoLiderMembro.setNome("Lider Novo");
		novoLiderMembro.setCargoMembro(EnumCargoMembro.MEMBRO);
		ReflectionTestUtils.setField(novoLiderMembro, "idExterno", idNovoLider);

		Ministerio ministerioExistente = Ministerio.builder()
				.nome("Louvor")
				.nomeLider("Lider Antigo")
				.build();
		ReflectionTestUtils.setField(ministerioExistente, "idExterno", idMinisterio);

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

		MinisterioUpdateDTO dto = new MinisterioUpdateDTO(null, null, idNovoLider);

		when(ministeriosRepository.findByIdExterno(idMinisterio)).thenReturn(Optional.of(ministerioExistente));
		when(membroRepository.findByIdExterno(idNovoLider)).thenReturn(novoLiderMembro);

		useCase.execute(dto, idMinisterio);

		assertEquals("Lider Novo", ministerioExistente.getNomeLider());
		assertEquals(EnumCargoMembro.LIDER_MINISTERIO, novoLiderMembro.getCargoMembro());
		assertEquals(EnumCargoMembroMinisterio.LIDER_MINISTERIO, novoLiderRelacao.getCargoMembro());
		assertEquals(EnumCargoMembroMinisterio.MEMBRO_MINISTERIO, liderAtualRelacao.getCargoMembro());
		assertEquals(EnumCargoMembro.MEMBRO, liderAtualMembro.getCargoMembro());
		verify(ministeriosRepository).save(ministerioExistente);
	}
}