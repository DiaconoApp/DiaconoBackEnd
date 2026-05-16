package com.diacono.diacono.infrastructure.auth;

import com.diacono.diacono.applications.dtos.googleauth.GoogleAuthorizationCodeRequestDTO;
import com.diacono.diacono.applications.dtos.googleauth.GoogleTokenResponseDTO;
import com.diacono.diacono.global.config.GoogleOAuthProperties;
import com.diacono.diacono.global.error.exceptions.GoogleAuthorizationCodeException;
import com.diacono.diacono.global.error.exceptions.GoogleOAuthIntegrationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestClientResponseException;

@Component
public class GoogleAuthorizationCodeExchangerImpl implements GoogleAuthorizationCodeExchanger {

    private final RestClient restClient;
    private final GoogleOAuthProperties googleOAuthProperties;

    @Autowired
    public GoogleAuthorizationCodeExchangerImpl(GoogleOAuthProperties googleOAuthProperties) {
        this(RestClient.create(), googleOAuthProperties);
    }

    GoogleAuthorizationCodeExchangerImpl(RestClient restClient,
                                         GoogleOAuthProperties googleOAuthProperties) {
        this.restClient = restClient;
        this.googleOAuthProperties = googleOAuthProperties;
    }

    @Override
    public GoogleTokenResponseDTO exchange(GoogleAuthorizationCodeRequestDTO request) {
        validarConfiguracao();

        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
        formData.add("code", request.authorizationCode());
        formData.add("client_id", googleOAuthProperties.clientId());
        formData.add("client_secret", googleOAuthProperties.clientSecret());
        formData.add("redirect_uri", request.redirectUri());
        formData.add("grant_type", "authorization_code");

        if (request.codeVerifier() != null && !request.codeVerifier().isBlank()) {
            formData.add("code_verifier", request.codeVerifier());
        }

        try {
            GoogleTokenResponseDTO tokenResponse = restClient.post()
                    .uri(googleOAuthProperties.resolvedTokenUri())
                    .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                    .body(formData)
                    .retrieve()
                    .body(GoogleTokenResponseDTO.class);

            if (tokenResponse == null || tokenResponse.idToken() == null || tokenResponse.idToken().isBlank()) {
                throw new GoogleOAuthIntegrationException("Resposta do Google sem id_token");
            }

            return tokenResponse;
        } catch (RestClientResponseException exception) {
            throw traduzirErroRespostaGoogle(exception);
        } catch (RestClientException exception) {
            throw new GoogleOAuthIntegrationException("Falha de comunicacao com o Google");
        }
    }

    private void validarConfiguracao() {
        if (googleOAuthProperties.clientId() == null || googleOAuthProperties.clientId().isBlank()
                || googleOAuthProperties.clientSecret() == null || googleOAuthProperties.clientSecret().isBlank()) {
            throw new GoogleOAuthIntegrationException("Configuracao do Google OAuth ausente na aplicacao");
        }
    }

    private RuntimeException traduzirErroRespostaGoogle(RestClientResponseException exception) {
        String responseBody = exception.getResponseBodyAsString();

        if (responseBody != null && responseBody.contains("redirect_uri_mismatch")) {
            return new GoogleAuthorizationCodeException("Redirect URI divergente para autenticacao Google");
        }

        if (responseBody != null && responseBody.contains("invalid_grant")) {
            return new GoogleAuthorizationCodeException("Authorization code invalido ou expirado");
        }

        if (responseBody != null && responseBody.contains("invalid_request")) {
            return new GoogleAuthorizationCodeException("Requisicao invalida para autenticacao Google");
        }

        if (exception.getStatusCode().is4xxClientError()) {
            return new GoogleAuthorizationCodeException("Falha ao validar authorization code com o Google");
        }

        return new GoogleOAuthIntegrationException("Falha de comunicacao com o Google");
    }
}
