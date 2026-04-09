package com.diacono.diacono.usecases.membro;

import com.diacono.diacono.domain.entity.Membro;
import com.diacono.diacono.domain.repository.MembroRepository;
import com.diacono.diacono.global.error.exceptions.ObjectNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BuscarPorEmaiUseCaseTest {

	@Mock
	private MembroRepository membroRepository;

	@InjectMocks
	private BuscarPorEmaiUseCase useCase;

	@Test
	void deveRetornarMembroQuandoEmailExistir() {
		String email = "samuel@teste.com";
		Membro membro = new Membro();

		when(membroRepository.findByEmail(email)).thenReturn(Optional.of(membro));

		Membro response = useCase.execute(email);

		assertSame(membro, response);
	}

	@Test
	void deveLancarExcecaoQuandoEmailNaoExistir() {
		String email = "naoexiste@teste.com";
		when(membroRepository.findByEmail(email)).thenReturn(Optional.empty());

		ObjectNotFoundException ex = assertThrows(ObjectNotFoundException.class, () -> useCase.execute(email));

		assertEquals("Membro não encontrado com o email fornecido.", ex.getMessage());
	}
}