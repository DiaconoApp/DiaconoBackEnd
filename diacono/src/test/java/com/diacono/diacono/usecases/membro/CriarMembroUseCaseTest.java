package com.diacono.diacono.usecases.membro;

import com.diacono.diacono.applications.dtos.membro.EnderecoMembroDTO;
import com.diacono.diacono.applications.dtos.membro.MembroCreateDTO;
import com.diacono.diacono.applications.mappers.membro.MembroMapper;
import com.diacono.diacono.domain.entity.MembroMinisterio;
import com.diacono.diacono.domain.entity.Ministerio;
import com.diacono.diacono.domain.enums.EnumCargoMembro;
import com.diacono.diacono.domain.enums.EnumGeneroMembro;
import com.diacono.diacono.domain.repository.MembroMinisterioRepository;
import com.diacono.diacono.domain.repository.MembroRepository;
import com.diacono.diacono.domain.repository.MinisteriosRepository;
import com.diacono.diacono.global.error.exceptions.ObjectNotFoundException;
import com.diacono.diacono.global.error.exceptions.ObjectSaveErrorException;
import com.diacono.diacono.usecases.igreja.BuscarIgrejaPorUUIDUseCase;
import com.diacono.diacono.usecases.membro.validation.ValidarCriacaoMembro;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CriarMembroUseCaseTest {

	@Mock
	private MembroRepository membroRepository;

	@Mock
	private MembroMapper membroMapper;

	@Mock
	private ValidarCriacaoMembro validarCriacaoMembro;

	@Mock
	private MinisteriosRepository ministeriosRepository;

	@Mock
	private MembroMinisterioRepository membroMinisterioRepository;

	@Mock
	private BuscarIgrejaPorUUIDUseCase buscarIgrejaPorUUIDUseCase;

	@InjectMocks
	private CriarMembroUseCase useCase;

	@Test
	void deveLancarExcecaoQuandoDadosDoMembroForemNulos() {
		ObjectSaveErrorException ex = assertThrows(ObjectSaveErrorException.class, () -> useCase.execute(null));

		assertEquals("Dados do membro não podem ser nulos.", ex.getMessage());
		verifyNoInteractions(membroRepository, membroMapper, validarCriacaoMembro, buscarIgrejaPorUUIDUseCase);
	}

	@Test
	void deveLancarExcecaoQuandoCargoForLiderSemMinisterioAssociado() {
		MembroCreateDTO dto = criarMembroCreateDTO();

		ObjectSaveErrorException ex = assertThrows(ObjectSaveErrorException.class, () -> useCase.execute(dto));

		assertEquals("Para cadastrar um líder de ministério, é necessário associar um ministério ao membro.", ex.getMessage());
	}

	@Test
	void deveBuscarMinisterioPorUuidComSucesso() {
		UUID idMinisterio = UUID.fromString("11111111-1111-1111-1111-111111111111");
		Ministerio ministerio = Ministerio.builder().nome("Louvor").build();

		when(ministeriosRepository.findByIdExterno(idMinisterio)).thenReturn(Optional.of(ministerio));

		Ministerio response = useCase.buscarPorUUID(idMinisterio);

		assertSame(ministerio, response);
	}

	@Test
	void deveLancarExcecaoQuandoMinisterioNaoForEncontradoNoBuscarPorUuid() {
		UUID idMinisterio = UUID.fromString("11111111-1111-1111-1111-111111111111");

		when(ministeriosRepository.findByIdExterno(idMinisterio)).thenReturn(Optional.empty());

		ObjectNotFoundException ex = assertThrows(ObjectNotFoundException.class, () -> useCase.buscarPorUUID(idMinisterio));

		assertEquals("Ministério não encontrado", ex.getMessage());
	}

	@Test
	void deveLancarExcecaoQuandoSalvarTodosNaoPersistirMembroMinisterio() {
		MembroMinisterio entrada = MembroMinisterio.builder().build();

		when(membroMinisterioRepository.save(any(MembroMinisterio.class))).thenReturn(MembroMinisterio.builder().build());

		ObjectSaveErrorException ex = assertThrows(ObjectSaveErrorException.class, () -> useCase.salvarTodos(entrada));

		assertEquals("Nenhum membro_ministerio foi salvo.", ex.getMessage());
	}

	@Test
	void deveSalvarTodosComSucessoQuandoMembroMinisterioTiverIdInterno() {
		MembroMinisterio entrada = MembroMinisterio.builder().build();

		when(membroMinisterioRepository.save(any(MembroMinisterio.class)))
				.thenReturn(MembroMinisterio.builder().idInterno(10L).build());

		assertDoesNotThrow(() -> useCase.salvarTodos(entrada));
		verify(membroMinisterioRepository).save(entrada);
	}

	private MembroCreateDTO criarMembroCreateDTO() {
		return new MembroCreateDTO(
				UUID.fromString("22222222-2222-2222-2222-222222222222"),
				"Samuel",
				"12345678909",
				LocalDate.of(2000, 1, 1),
				"samuel@teste.com",
				"11999999999",
				"123456",
				(List<UUID>) null,
				EnumCargoMembro.LIDER_MINISTERIO,
				EnumGeneroMembro.MASCULINO,
				new EnderecoMembroDTO("12345678", "SP", "SAO PAULO", "CENTRO", "RUA A", "AP 1", "10")
		);
	}
}