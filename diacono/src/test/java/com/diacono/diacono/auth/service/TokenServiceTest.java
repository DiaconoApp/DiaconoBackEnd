package com.diacono.diacono.auth.service;

import com.diacono.diacono.Igreja.model.entity.Igreja;
import com.diacono.diacono.membro.model.entity.EnumCargoMembro;
import com.diacono.diacono.membro.model.entity.Membro;
import org.junit.jupiter.api.DisplayName;
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
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TokenServiceTest {

    @Mock
    private JwtEncoder jwtEncoder;

    @Mock
    private Jwt jwt;

    @InjectMocks
    private TokenService tokenService;

    @Test
    @DisplayName("Deve gerar token com claims corretos")
    void deveGerarTokenComClaimsCorretos() {
        var expiresIn = 7200L;
        ReflectionTestUtils.setField(tokenService, "expiresIn", expiresIn);

        Igreja igreja = new Igreja();
        ReflectionTestUtils.setField(igreja, "idExterno", UUID.randomUUID());
        igreja.setNome("Igreja Teste");

        Membro membro = new Membro();
        ReflectionTestUtils.setField(membro, "idExterno", UUID.randomUUID());
        membro.setNome("Nome Teste");
        membro.setDataNascimento(LocalDate.of(1990, 1, 1));
        membro.setIgreja(igreja);
        membro.setCargoMembro(EnumCargoMembro.MEMBRO);

        when(jwt.getTokenValue()).thenReturn("fake-jwt-token");
        ArgumentCaptor<JwtEncoderParameters> captor = ArgumentCaptor.forClass(JwtEncoderParameters.class);
        when(jwtEncoder.encode(captor.capture())).thenReturn(jwt);

        String token = tokenService.generateToken(membro);

        assertNotNull(token);
        assertEquals("fake-jwt-token", token);

        JwtEncoderParameters params = captor.getValue();
        assertNotNull(params);

        JwtClaimsSet claims = params.getClaims();
        assertEquals("diacono-api", claims.getClaim("iss"));
        assertEquals(membro.getIdExterno().toString(), claims.getSubject());
        assertEquals(membro.getNome(), claims.getClaim("nome"));
        assertEquals(membro.getDataNascimento().toString(), claims.getClaim("idade"));
        assertEquals(igreja.getIdExterno(), claims.getClaim("fk_igreja"));
        assertEquals(igreja.getNome(), claims.getClaim("igreja"));

        Instant issuedAt = claims.getIssuedAt();
        Instant expiresAt = claims.getExpiresAt();
        assertNotNull(issuedAt);
        assertNotNull(expiresAt);
        assertEquals(expiresIn, expiresAt.getEpochSecond() - issuedAt.getEpochSecond(), 1);

        verify(jwtEncoder).encode(any());
    }

    @Test
    @DisplayName("Deve lançar NullPointerException quando dados do membro estiverem nulos")
    void deveLancarErroQuandoDadosDoMembroForemNulos() {
        Membro membro = new Membro(); // sem dataNascimento e sem igreja

        assertThrows(NullPointerException.class, () -> tokenService.generateToken(membro));
    }
}