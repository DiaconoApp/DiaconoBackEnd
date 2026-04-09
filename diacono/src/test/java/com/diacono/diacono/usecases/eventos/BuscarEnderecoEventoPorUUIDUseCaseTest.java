package com.diacono.diacono.usecases.eventos;

import com.diacono.diacono.domain.entity.EnderecoEvento;
import com.diacono.diacono.domain.repository.EnderecoEventoRepository;
import com.diacono.diacono.global.error.exceptions.ObjectNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

//noinspection unused
@ExtendWith(MockitoExtension.class)
@SuppressWarnings("unused")
class BuscarEnderecoEventoPorUUIDUseCaseTest {

	@Mock
	private EnderecoEventoRepository enderecoEventoRepository;

	@InjectMocks
	private BuscarEnderecoEventoPorUUIDUseCase useCase;

	@Test
	void deveRetornarEnderecoQuandoIdExternoExistir() {
		UUID idEndereco = UUID.fromString("11111111-1111-1111-1111-111111111111");
		EnderecoEvento endereco = EnderecoEvento.builder().cep("12345678").numero("10").build();

		when(enderecoEventoRepository.findByIdExterno(idEndereco)).thenReturn(Optional.of(endereco));

		EnderecoEvento response = useCase.execute(idEndereco);

		assertSame(endereco, response);
	}

	@Test
	void deveLancarExcecaoQuandoEnderecoNaoForEncontrado() {
		UUID idEndereco = UUID.fromString("11111111-1111-1111-1111-111111111111");
		when(enderecoEventoRepository.findByIdExterno(idEndereco)).thenReturn(Optional.empty());

		ObjectNotFoundException ex = assertThrows(ObjectNotFoundException.class, () -> useCase.execute(idEndereco));

		assertEquals("Endereço do evento não encontrado", ex.getMessage());
	}
}