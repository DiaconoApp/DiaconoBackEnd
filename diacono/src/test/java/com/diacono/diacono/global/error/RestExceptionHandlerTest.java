package com.diacono.diacono.global.error;

import com.diacono.diacono.global.error.exceptions.GoogleAuthorizationCodeException;
import com.diacono.diacono.global.error.exceptions.GoogleOAuthIntegrationException;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.assertEquals;

class RestExceptionHandlerTest {

    @Test
    void deveResponder401ParaErroDeAuthorizationCodeGoogle() throws Exception {
        RestExceptionHandler handler = new RestExceptionHandler();

        ResponseEntity<RestErrorMessage> response = invokeExceptionHandler(
                handler,
                "googleAuthorizationCodeHandler",
                GoogleAuthorizationCodeException.class,
                new GoogleAuthorizationCodeException("Authorization code invalido ou expirado")
        );

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertEquals("Authorization code invalido ou expirado", response.getBody().getMessage());
    }

    @Test
    void deveResponder502ParaErroDeIntegracaoComGoogle() throws Exception {
        RestExceptionHandler handler = new RestExceptionHandler();

        ResponseEntity<RestErrorMessage> response = invokeExceptionHandler(
                handler,
                "googleOAuthIntegrationHandler",
                GoogleOAuthIntegrationException.class,
                new GoogleOAuthIntegrationException("Falha de comunicacao com o Google")
        );

        assertEquals(HttpStatus.BAD_GATEWAY, response.getStatusCode());
        assertEquals("Falha de comunicacao com o Google", response.getBody().getMessage());
    }

    @SuppressWarnings("unchecked")
    private <T extends RuntimeException> ResponseEntity<RestErrorMessage> invokeExceptionHandler(
            RestExceptionHandler handler,
            String methodName,
            Class<T> exceptionType,
            T exception
    ) throws Exception {
        Method method = RestExceptionHandler.class.getDeclaredMethod(methodName, exceptionType);
        method.setAccessible(true);
        return (ResponseEntity<RestErrorMessage>) method.invoke(handler, exception);
    }
}
