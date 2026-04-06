package com.diacono.diacono.usecases;

import com.diacono.diacono.applications.dtos.login.LoginRequestDTO;
import com.diacono.diacono.applications.dtos.login.LoginResponseDTO;
import com.diacono.diacono.domain.entity.Membro;
import com.diacono.diacono.global.error.exceptions.BadCredentialsException;
import com.diacono.diacono.usecases.membro.BuscarPorEmaiUseCase;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class LoginServiceUseCaseTest {

	@Mock
	private BuscarPorEmaiUseCase buscarPorEmaiUseCase;

	@Mock
	private BCryptPasswordEncoder bCryptPasswordEncoder;

	@Mock
	private GenerateTokenUseCase generateTokenUseCase;

	@InjectMocks
	private LoginServiceUseCase loginServiceUseCase;

	@Test
	void deveRetornarTokenQuandoCredenciaisForemValidas() {
		LoginRequestDTO request = new LoginRequestDTO("usuario@teste.com", "123456");
		Membro membro = new Membro();
		membro.setSenha("senha-criptografada");

		when(buscarPorEmaiUseCase.execute(request.email())).thenReturn(membro);
		when(bCryptPasswordEncoder.matches(request.senha(), membro.getSenha())).thenReturn(true);
		when(generateTokenUseCase.execute(membro)).thenReturn("jwt-token");
		when(generateTokenUseCase.getExpiresIn()).thenReturn(3600L);

		LoginResponseDTO response = loginServiceUseCase.execute(request);

		assertEquals("jwt-token", response.acessToken());
		assertEquals(3600L, response.expiresIn());
		verify(generateTokenUseCase).execute(membro);
	}

	@Test
	void deveLancarExcecaoQuandoSenhaForInvalida() {
		LoginRequestDTO request = new LoginRequestDTO("usuario@teste.com", "senha-errada");
		Membro membro = new Membro();
		membro.setSenha("senha-criptografada");

		when(buscarPorEmaiUseCase.execute(request.email())).thenReturn(membro);
		when(bCryptPasswordEncoder.matches(request.senha(), membro.getSenha())).thenReturn(false);

		BadCredentialsException exception = assertThrows(
				BadCredentialsException.class,
				() -> loginServiceUseCase.execute(request)
		);

		assertEquals("Usuário ou senha inválidos", exception.getMessage());
		verify(generateTokenUseCase, never()).execute(any(Membro.class));
	}

	@Test
	void deveLancarExcecaoQuandoMembroNaoForEncontrado() {
		LoginRequestDTO request = new LoginRequestDTO("naoexiste@teste.com", "123456");

		when(buscarPorEmaiUseCase.execute(request.email())).thenReturn(null);

		BadCredentialsException exception = assertThrows(
				BadCredentialsException.class,
				() -> loginServiceUseCase.execute(request)
		);

		assertEquals("Usuário ou senha inválidos", exception.getMessage());
		verify(bCryptPasswordEncoder, never()).matches(any(), any());
		verify(generateTokenUseCase, never()).execute(any(Membro.class));
		verify(generateTokenUseCase, never()).getExpiresIn();
	}
}