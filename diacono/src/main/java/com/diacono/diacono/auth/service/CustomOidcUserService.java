package com.diacono.diacono.auth.service;

import com.diacono.diacono.auth.model.CustomMembroOAuth2User;
import com.diacono.diacono.membro.model.entity.EnumCargoMembro;
import com.diacono.diacono.membro.model.entity.EnumStatusMembro;
import com.diacono.diacono.membro.model.entity.Membro;
import com.diacono.diacono.membro.service.MembroService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Service;

import java.util.Locale;
import java.util.regex.Pattern;

/**
 * Servico customizado para autenticacao OAuth2/OIDC
 * OWASP A07: Implementa validacoes de seguranca no fluxo de autenticacao externa
 */
@Service
public class CustomOidcUserService extends OidcUserService {

    // OWASP A05: Logging seguro sem expor dados sensiveis em producao
    private static final Logger logger = LoggerFactory.getLogger(CustomOidcUserService.class);

    // OWASP A02/A07: Regex simplificado para validacao basica de formato de email
    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$");

    private static final String AUTH_ERROR_CODE = "invalid_user_info";
    private static final String GENERIC_AUTH_ERROR = "Falha na autenticacao via provedor externo";

    private final MembroService membroService;

    public CustomOidcUserService(MembroService membroService) {
        this.membroService = membroService;
    }

    @Override
    public OidcUser loadUser(OidcUserRequest userRequest) throws OAuth2AuthenticationException {
        // OWASP A07: Delega validacao de ID Token e assinatura ao metodo pai (Spring Security)
        OidcUser oidcUser;
        try {
            oidcUser = super.loadUser(userRequest);
        } catch (OAuth2AuthenticationException e) {
            // OWASP A05: Log interno detalhado, mensagem generica ao usuario
            logger.error("Falha na validacao do OIDC ID Token: {}", e.getMessage());
            throw new OAuth2AuthenticationException(
                new OAuth2Error(AUTH_ERROR_CODE, GENERIC_AUTH_ERROR, null)
            );
        }

        // OWASP A07: Validacao defensiva de claims obrigatorios do OIDC
        String email = oidcUser.getAttribute("email");
        String nome = oidcUser.getAttribute("name");

        if (!isValidEmail(email)) {
            // OWASP A05/A07: Nao expoe detalhes de validacao ao usuario
            logger.warn("Tentativa de autenticacao OIDC com email invalido ou ausente");
            throw new OAuth2AuthenticationException(
                new OAuth2Error(AUTH_ERROR_CODE, GENERIC_AUTH_ERROR, null)
            );
        }

        if (isBlankOrNull(nome)) {
            // OWASP A07: Nome e obrigatorio para criar identidade valida
            logger.warn("Tentativa de autenticacao OIDC sem claim 'name' para email: {}", maskEmail(email));
            throw new OAuth2AuthenticationException(
                new OAuth2Error(AUTH_ERROR_CODE, GENERIC_AUTH_ERROR, null)
            );
        }

        // OWASP A02/A05: Normaliza email para consistencia no banco
        String emailNormalizado = normalizeEmail(email);

        Membro membro = membroService.buscarPorEmail(emailNormalizado);

        if (membro == null) {
            // OWASP A01/A07: Cria novo membro com cargo padrao e status ativo
            logger.info("Criando novo membro via OIDC para dominio: {}", extractDomain(emailNormalizado));

            Membro novoMembro = new Membro();
            novoMembro.setEmail(emailNormalizado);
            novoMembro.setNome(sanitizeName(nome));
            novoMembro.setCargoMembro(EnumCargoMembro.MEMBRO);
            novoMembro.setStatus(EnumStatusMembro.ATIVO);

            try {
                Membro membroSalvo = membroService.salvarMembro(novoMembro);
                logger.info("Membro criado com sucesso via OIDC");
                return new CustomMembroOAuth2User(membroSalvo, oidcUser);
            } catch (Exception e) {
                // OWASP A05: Trata erro de persistencia sem expor stack trace
                logger.error("Erro ao salvar novo membro via OIDC: {}", e.getMessage());
                throw new OAuth2AuthenticationException(
                    new OAuth2Error(AUTH_ERROR_CODE, GENERIC_AUTH_ERROR, null)
                );
            }
        }

        // OWASP A01/A07: Somente membros ativos podem autenticar via OIDC
        if (!EnumStatusMembro.ATIVO.equals(membro.getStatus())) {
            logger.warn("Tentativa de autenticacao OIDC com conta inativa para email: {}", maskEmail(emailNormalizado));
            throw new OAuth2AuthenticationException(
                new OAuth2Error(AUTH_ERROR_CODE, GENERIC_AUTH_ERROR, null)
            );
        }

        logger.info("Autenticacao OIDC bem-sucedida para membro existente");
        return new CustomMembroOAuth2User(membro, oidcUser);
    }

    // OWASP A07: Validacao rigorosa de formato de email
    private boolean isValidEmail(String email) {
        if (email == null || email.isBlank()) {
            return false;
        }
        if (email.length() > 254) {
            return false;
        }
        return EMAIL_PATTERN.matcher(email).matches();
    }

    private boolean isBlankOrNull(String valor) {
        return valor == null || valor.isBlank();
    }

    // OWASP A02/A05: Normaliza email para evitar duplicatas por variacao de caixa
    private String normalizeEmail(String email) {
        return email.trim().toLowerCase(Locale.ROOT);
    }

    // OWASP A02: Sanitiza nome removendo espacos excessivos
    private String sanitizeName(String nome) {
        return nome.trim().replaceAll("\\s+", " ");
    }

    // OWASP A05: Mascara email para logs seguros
    private String maskEmail(String email) {
        if (email == null || !email.contains("@")) {
            return "***";
        }
        String[] parts = email.split("@");
        String localPart = parts[0];
        return localPart.substring(0, 2) + "***@" + parts[1];
    }

    // OWASP A05: Extrai dominio para auditoria sem expor identidade completa
    private String extractDomain(String email) {
        if (email == null || !email.contains("@")) {
            return "unknown";
        }
        return email.substring(email.indexOf("@") + 1);
    }
}