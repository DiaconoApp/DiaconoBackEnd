package com.diacono.diacono.usecases.membro;

import com.diacono.diacono.applications.dtos.CadastroExternoDTO;
import com.diacono.diacono.applications.dtos.membro.EnderecoMembroDTO;
import com.diacono.diacono.applications.mappers.membro.MembroMapper;
import com.diacono.diacono.domain.entity.Membro;
import com.diacono.diacono.domain.enums.EnumGeneroMembro;
import com.diacono.diacono.domain.repository.MembroRepository;
import com.diacono.diacono.global.error.exceptions.ObjectExistsException;
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
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CadastrarMembroUseCaseTest {

	@Mock
	private MembroRepository membroRepository;

	@Mock
	private MembroMapper membroMapper;

	@Mock
	private BuscarIgrejaPorUUIDUseCase buscarIgrejaPorUUIDUseCase;

	@Mock
	private ValidarCriacaoMembro validarCriacaoMembro;

	@InjectMocks
	private CadastrarMembroUseCase useCase;

	@Test
	void deveLancarExcecaoQuandoDadosDoMembroForemNulos() {
		ObjectSaveErrorException ex = assertThrows(ObjectSaveErrorException.class, () -> useCase.execute(null));

		assertEquals("Dados do membro não podem ser nulos.", ex.getMessage());
		verifyNoInteractions(membroMapper, buscarIgrejaPorUUIDUseCase, validarCriacaoMembro);
	}

	@Test
	void deveLancarExcecaoQuandoMembroJaExistir() {
		CadastroExternoDTO dto = criarCadastroDTO();
		Membro existente = new Membro();

		when(membroRepository.findByEmailOrCpf(dto.email(), dto.cpf())).thenReturn(Optional.of(existente));

		ObjectExistsException ex = assertThrows(ObjectExistsException.class, () -> useCase.criarMembroExterno(dto));

		assertEquals("Erro ao se cadastrar", ex.getMessage());
	}

	@Test
	void deveLancarExcecaoQuandoMembroNaoForEncontradoNaBuscaInicial() {
		CadastroExternoDTO dto = criarCadastroDTO();

		when(membroRepository.findByEmailOrCpf(dto.email(), dto.cpf())).thenReturn(Optional.empty());

		ObjectNotFoundException ex = assertThrows(ObjectNotFoundException.class, () -> useCase.criarMembroExterno(dto));

		assertEquals("Membro não encontrado", ex.getMessage());
	}

	private CadastroExternoDTO criarCadastroDTO() {
		return new CadastroExternoDTO(
				UUID.fromString("11111111-1111-1111-1111-111111111111"),
				"Samuel",
				"12345678909",
				LocalDate.of(2000, 1, 1),
				"samuel@teste.com",
				"11999999999",
				"123456",
				EnumGeneroMembro.MASCULINO,
				new EnderecoMembroDTO("12345678", "SP", "SAO PAULO", "CENTRO", "RUA A", "AP 1", "10")
		);
	}
}