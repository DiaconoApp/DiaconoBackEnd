package com.diacono.diacono.usecases.ministerio;

import com.diacono.diacono.applications.dtos.RestResponseMessageDTO;
import com.diacono.diacono.applications.dtos.membro.MembroMinisterioCreateDTO;
import com.diacono.diacono.domain.entity.MembroMinisterio;
import com.diacono.diacono.domain.entity.Ministerio;
import com.diacono.diacono.domain.entity.Membro;
import com.diacono.diacono.applications.dtos.ministerio.MinisterioSuperSimplificadoDTO;
import java.util.List;
import com.diacono.diacono.domain.enums.EnumCargoMembroMinisterio;
import com.diacono.diacono.domain.repository.MembroMinisterioRepository;
import com.diacono.diacono.domain.repository.MinisteriosRepository;
import com.diacono.diacono.global.error.exceptions.FieldInvalidException;
import com.diacono.diacono.global.error.exceptions.ObjectNotFoundException;
import com.diacono.diacono.global.error.exceptions.ObjectSaveErrorException;
import com.diacono.diacono.infrastructure.persistence.springdata.MembroJpaRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AdicionarMembroMinisterioLiderMinisterioUseCaseTest {

	@Mock
	private MinisteriosRepository ministeriosRepository;

	@Mock
	private MembroJpaRepository membroRepository;

	@Mock
	private MembroMinisterioRepository membroMinisterioRepository;

	@InjectMocks
	private AdicionarMembroMinisterioLiderMinisterioUseCase useCase;

	@Test
	void deveAdicionarMembroAoMinisterioComSucesso() {
		UUID idMinisterio = UUID.fromString("11111111-1111-1111-1111-111111111111");
		UUID idMembro = UUID.fromString("22222222-2222-2222-2222-222222222222");
		UUID igrejaId = UUID.fromString("33333333-3333-3333-3333-333333333333");
		UUID membroExecutor = UUID.fromString("44444444-4444-4444-4444-444444444444");
		MembroMinisterioCreateDTO dto = new MembroMinisterioCreateDTO(idMembro);

		when(ministeriosRepository.buscarIdPorUUID(idMinisterio)).thenReturn(Optional.of(10L));
		when(ministeriosRepository.findByIdExternoAndIgrejaId(idMinisterio, igrejaId)).thenReturn(Optional.of(new Ministerio()));

		// preparar membro verificado com igreja correta e idInterno
		Membro membroVerificado = new Membro();
		try {
			java.lang.reflect.Field idField = com.diacono.diacono.global.util.IdEntityUtils.class.getDeclaredField("idInterno");
			idField.setAccessible(true);
			idField.set(membroVerificado, 20L);
			// set igreja idExterno
			com.diacono.diacono.domain.entity.Igreja igreja = new com.diacono.diacono.domain.entity.Igreja();
			java.lang.reflect.Field idExternoField = com.diacono.diacono.global.util.IdEntityUtils.class.getDeclaredField("idExterno");
			idExternoField.setAccessible(true);
			idExternoField.set(igreja, igrejaId);
			membroVerificado.setIgreja(igreja);
		} catch (ReflectiveOperationException e) {
			throw new RuntimeException(e);
		}

		when(membroRepository.findByIdExterno(idMembro)).thenReturn(membroVerificado);

		when(membroMinisterioRepository.buscarMinisterioLider(membroExecutor, igrejaId))
				.thenReturn(List.of(new MinisterioSuperSimplificadoDTO(idMinisterio, "Teste")));

		when(membroMinisterioRepository.save(any(MembroMinisterio.class)))
				.thenReturn(MembroMinisterio.builder().idInterno(99L).build());

		RestResponseMessageDTO response = useCase.execute(idMinisterio, dto, igrejaId, membroExecutor);

		assertEquals(HttpStatus.OK, response.getStatus());
		assertEquals("Membro adicionado ao ministério com sucesso", response.getMessage());

		ArgumentCaptor<MembroMinisterio> captor = ArgumentCaptor.forClass(MembroMinisterio.class);
		verify(membroMinisterioRepository).save(captor.capture());
		MembroMinisterio salvo = captor.getValue();

		assertEquals(10L, salvo.getMinisterio().getIdInterno());
		assertEquals(20L, salvo.getMembro().getIdInterno());
		assertEquals(EnumCargoMembroMinisterio.MEMBRO_MINISTERIO, salvo.getCargoMembro());
		assertNotNull(salvo.getDataRegistro());
	}

	@Test
	void deveLancarExcecaoQuandoDtoForNulo() {
		UUID idMinisterio = UUID.fromString("11111111-1111-1111-1111-111111111111");
		UUID idMembro = UUID.fromString("22222222-2222-2222-2222-222222222222");
		UUID igrejaId = UUID.fromString("33333333-3333-3333-3333-333333333333");

		FieldInvalidException ex = assertThrows(
				FieldInvalidException.class,
				() -> useCase.execute(idMinisterio, null, igrejaId, idMembro)
		);

		assertEquals("Dados do membro do ministério não podem ser nulos", ex.getMessage());
		verify(ministeriosRepository, never()).buscarIdPorUUID(any());
		verify(membroRepository, never()).buscarIdPorUUID(any());
		verify(membroMinisterioRepository, never()).save(any());
	}

	@Test
	void deveLancarExcecaoQuandoMinisterioNaoForEncontrado() {
		UUID idMinisterio = UUID.fromString("11111111-1111-1111-1111-111111111111");
		MembroMinisterioCreateDTO dto = new MembroMinisterioCreateDTO(
				UUID.fromString("22222222-2222-2222-2222-222222222222")
		);
		UUID igrejaId = UUID.fromString("33333333-3333-3333-3333-333333333333");

		when(ministeriosRepository.buscarIdPorUUID(idMinisterio)).thenReturn(Optional.empty());

		ObjectNotFoundException ex = assertThrows(
				ObjectNotFoundException.class,
				() -> useCase.execute(idMinisterio, dto, dto.idExterno(), igrejaId)
		);

		assertEquals("Ministério não encontrado", ex.getMessage());
		verify(membroRepository, never()).buscarIdPorUUID(any());
		verify(membroMinisterioRepository, never()).save(any());
	}

	@Test
	void deveLancarExcecaoQuandoMembroNaoForEncontrado() {
		UUID idMinisterio = UUID.fromString("11111111-1111-1111-1111-111111111111");
		UUID idMembro = UUID.fromString("22222222-2222-2222-2222-222222222222");
		UUID igrejaId = UUID.fromString("33333333-3333-3333-3333-333333333333");
		MembroMinisterioCreateDTO dto = new MembroMinisterioCreateDTO(idMembro);

		when(ministeriosRepository.buscarIdPorUUID(idMinisterio)).thenReturn(Optional.of(10L));
		when(ministeriosRepository.findByIdExternoAndIgrejaId(idMinisterio, igrejaId)).thenReturn(Optional.of(new Ministerio()));
		when(membroMinisterioRepository.buscarMinisterioLider(idMembro, igrejaId)).thenReturn(List.of(new MinisterioSuperSimplificadoDTO(idMinisterio, "Teste")));
		when(membroRepository.findByIdExterno(idMembro)).thenReturn(null);

		ObjectNotFoundException ex = assertThrows(
				ObjectNotFoundException.class,
				() -> useCase.execute(idMinisterio, dto, igrejaId, idMembro)
		);

		assertEquals("Membro não encontrado", ex.getMessage());
		verify(membroMinisterioRepository, never()).save(any());
	}

	@Test
	void deveLancarExcecaoQuandoSalvarMembroMinisterioFalhar() {
		UUID idMinisterio = UUID.fromString("11111111-1111-1111-1111-111111111111");
		UUID idMembro = UUID.fromString("22222222-2222-2222-2222-222222222222");
		UUID igrejaId = UUID.fromString("33333333-3333-3333-3333-333333333333");
		MembroMinisterioCreateDTO dto = new MembroMinisterioCreateDTO(idMembro);

		when(ministeriosRepository.buscarIdPorUUID(idMinisterio)).thenReturn(Optional.of(10L));
		when(ministeriosRepository.findByIdExternoAndIgrejaId(idMinisterio, igrejaId)).thenReturn(Optional.of(new Ministerio()));
		when(membroMinisterioRepository.buscarMinisterioLider(idMembro, igrejaId)).thenReturn(List.of(new MinisterioSuperSimplificadoDTO(idMinisterio, "Teste")));

		Membro membroVerificado = new Membro();
		try {
			java.lang.reflect.Field idField = com.diacono.diacono.global.util.IdEntityUtils.class.getDeclaredField("idInterno");
			idField.setAccessible(true);
			idField.set(membroVerificado, 20L);
			com.diacono.diacono.domain.entity.Igreja igreja = new com.diacono.diacono.domain.entity.Igreja();
			java.lang.reflect.Field idExternoField = com.diacono.diacono.global.util.IdEntityUtils.class.getDeclaredField("idExterno");
			idExternoField.setAccessible(true);
			idExternoField.set(igreja, igrejaId);
			membroVerificado.setIgreja(igreja);
		} catch (ReflectiveOperationException e) {
			throw new RuntimeException(e);
		}

		when(membroRepository.findByIdExterno(idMembro)).thenReturn(membroVerificado);

		when(membroMinisterioRepository.save(any(MembroMinisterio.class)))
				.thenReturn(MembroMinisterio.builder().build());

		ObjectSaveErrorException ex = assertThrows(
				ObjectSaveErrorException.class,
				() -> useCase.execute(idMinisterio, dto, igrejaId, idMembro)
		);

		assertEquals("Membro do ministério não foi salvo.", ex.getMessage());
	}
}