package com.diacono.diacono.global.config;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class GoogleOAuthPropertiesTest {

    @Test
    void deveUsarTokenUriPadraoQuandoConfiguracaoNaoVierPreenchida() {
        GoogleOAuthProperties properties = new GoogleOAuthProperties(
                "google-client-id",
                "google-client-secret",
                null
        );

        assertEquals("https://oauth2.googleapis.com/token", properties.resolvedTokenUri());
    }

    @Test
    void deveUsarTokenUriConfiguradoQuandoValorExistir() {
        GoogleOAuthProperties properties = new GoogleOAuthProperties(
                "google-client-id",
                "google-client-secret",
                "https://oauth2.custom.example/token"
        );

        assertEquals("https://oauth2.custom.example/token", properties.resolvedTokenUri());
    }
}
