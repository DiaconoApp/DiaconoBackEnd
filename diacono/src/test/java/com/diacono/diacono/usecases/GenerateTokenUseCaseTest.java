package com.diacono.diacono.usecases;

import com.diacono.diacono.domain.entity.Igreja;
import com.diacono.diacono.domain.entity.Membro;
import com.diacono.diacono.domain.enums.EnumCargoMembro;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.Instant;
import java.time.LocalDate;
import java.util.Map;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GenerateTokenUseCaseTest {

	@Mock
	private JwtEncoder jwtEncoder;

	@InjectMocks
	private GenerateTokenUseCase generateTokenUseCase;

	@Test
	void deveGerarTokenComClaimsEsperadas() {
		UUID membroId = UUID.fromString("11111111-1111-1111-1111-111111111111");
		UUID igrejaId = UUID.fromString("22222222-2222-2222-2222-222222222222");

		Igreja igreja = new Igreja();
		igreja.setNome("Igreja Central");
		ReflectionTestUtils.setField(igreja, "idExterno", igrejaId);

		Membro membro = new Membro();
		membro.setNome("Samuel");
		membro.setDataNascimento(LocalDate.of(2000, 1, 15));
		membro.setCargoMembro(EnumCargoMembro.MEMBRO);
		membro.setIgreja(igreja);
		ReflectionTestUtils.setField(membro, "idExterno", membroId);

		ReflectionTestUtils.setField(generateTokenUseCase, "expiresIn", 3600L);

		Jwt jwt = new Jwt(
				"token-gerado",
				Instant.now(),
				Instant.now().plusSeconds(3600),
				Map.of("alg", "none"),
				Map.of("sub", membroId.toString())
		);

		when(jwtEncoder.encode(any(JwtEncoderParameters.class))).thenReturn(jwt);

		String token = generateTokenUseCase.execute(membro);

		assertEquals("token-gerado", token);

		ArgumentCaptor<JwtEncoderParameters> captor = ArgumentCaptor.forClass(JwtEncoderParameters.class);
		verify(jwtEncoder).encode(captor.capture());

		JwtClaimsSet claims = captor.getValue().getClaims();
		assertEquals("diacono-api", claims.getClaimAsString("iss"));
		assertEquals(membroId.toString(), claims.getSubject());
		assertEquals("MEMBRO", claims.getClaim("scope"));
		assertEquals("Samuel", claims.getClaim("nome"));
		assertEquals("2000-01-15", claims.getClaim("idade"));
		assertEquals(igrejaId, claims.getClaim("fk_igreja"));
		assertEquals("Igreja Central", claims.getClaim("igreja"));
		assertEquals(3600L, claims.getExpiresAt().getEpochSecond() - claims.getIssuedAt().getEpochSecond());
		assertFalse(claims.getExpiresAt().isBefore(claims.getIssuedAt()));
	}
}