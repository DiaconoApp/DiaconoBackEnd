package com.diacono.diacono.auth.service;

import com.diacono.diacono.auth.model.CustomMembroOAuth2User;
import com.diacono.diacono.membro.model.entity.Membro;
import com.diacono.diacono.membro.service.MembroService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CustomOidcUserServiceTest {

    @Mock
    private MembroService membroService;

    @Mock
    private OidcUser oidcUser;

    private static class TestableCustomOidcUserService {
        private final MembroService membroService;

        public TestableCustomOidcUserService(MembroService membroService) {
            this.membroService = membroService;
        }

        public OidcUser process(OidcUser oidcUser) {
            String email = oidcUser.getAttribute("email");
            String nome = oidcUser.getAttribute("name");

            Membro membro = membroService.buscarPorEmail(email);

            if (membro == null) {
                Membro novoMembro = new Membro();
                novoMembro.setEmail(email);
                novoMembro.setNome(nome);

                Membro membroSalvo = membroService.salvarMembro(novoMembro);
                return new CustomMembroOAuth2User(membroSalvo, oidcUser);
            }

            return new CustomMembroOAuth2User(membro, oidcUser);
        }
    }

    @Test
    @DisplayName("Deve retornar CustomMembroOAuth2User quando membro já existe")
    void deveRetornarCustomUserQuandoMembroExiste() {
        String email = "existente@email.com";
        String name = "Membro Existente";

        Membro membroExistente = new Membro();
        ReflectionTestUtils.setField(membroExistente, "idExterno", UUID.randomUUID());
        membroExistente.setEmail(email);

        when(oidcUser.getAttribute("email")).thenReturn(email);
        when(oidcUser.getAttribute("name")).thenReturn(name);
        when(membroService.buscarPorEmail(email)).thenReturn(membroExistente);

        TestableCustomOidcUserService sut = new TestableCustomOidcUserService(membroService);

        OidcUser result = sut.process(oidcUser);

        assertNotNull(result);
        assertInstanceOf(CustomMembroOAuth2User.class, result);
        CustomMembroOAuth2User custom = (CustomMembroOAuth2User) result;
        assertEquals(email, custom.getMembro().getEmail());
        assertEquals(membroExistente.getIdExterno(), custom.getMembro().getIdExterno());

        verify(membroService).buscarPorEmail(email);
        verify(membroService, never()).salvarMembro(any());
    }
}