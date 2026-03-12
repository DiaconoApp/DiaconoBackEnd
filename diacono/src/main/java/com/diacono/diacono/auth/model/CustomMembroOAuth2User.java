package com.diacono.diacono.auth.model;

import com.diacono.diacono.membro.model.entity.Membro;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.core.oidc.OidcIdToken;
import org.springframework.security.oauth2.core.oidc.OidcUserInfo;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.UUID;

/**
 * Usuario customizado para autenticacao OAuth2/OIDC com dados de Membro interno
 * OWASP A01: Implementa principal de autenticacao com validacoes defensivas
 */
public class CustomMembroOAuth2User implements OidcUser {

    private final Membro membro;
    private final OAuth2User oauth2UserDelegate;
    private final OidcUser oidcUserDelegate;

    public CustomMembroOAuth2User(Membro membro, OidcUser oidcUser) {
        // OWASP A01/A07: Valida campos obrigatorios antes de criar principal de autenticacao
        validateMembro(membro);
        if (oidcUser == null) {
            throw new IllegalArgumentException("OidcUser nao pode ser nulo");
        }

        this.membro = membro;
        this.oidcUserDelegate = oidcUser;
        this.oauth2UserDelegate = oidcUser;
    }

    public CustomMembroOAuth2User(Membro membro, OAuth2User oauth2User) {
        // OWASP A01/A07: Valida campos obrigatorios antes de criar principal de autenticacao
        validateMembro(membro);
        if (oauth2User == null) {
            throw new IllegalArgumentException("OAuth2User nao pode ser nulo");
        }

        this.membro = membro;
        this.oidcUserDelegate = null;
        this.oauth2UserDelegate = oauth2User;
    }

    // OWASP A01/A07: Validacao centralizada de campos criticos do Membro
    private void validateMembro(Membro membro) {
        if (membro == null) {
            throw new IllegalArgumentException("Membro nao pode ser nulo");
        }
        if (membro.getIdInterno() == null) {
            throw new IllegalArgumentException("ID interno do membro nao pode ser nulo");
        }
        if (membro.getIdExterno() == null) {
            throw new IllegalArgumentException("ID externo do membro nao pode ser nulo");
        }
        if (membro.getCargoMembro() == null) {
            throw new IllegalArgumentException("Cargo do membro nao pode ser nulo");
        }
        if (membro.getEmail() == null || membro.getEmail().isBlank()) {
            throw new IllegalArgumentException("Email do membro nao pode ser nulo ou vazio");
        }
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // OWASP A01: Authorities baseadas em cargo do membro para RBAC
        String cargoName = membro.getCargoMembro().name();
        return Collections.singletonList(
                new SimpleGrantedAuthority("SCOPE_" + cargoName)
        );
    }

    @Override
    public String getName() {
        // OWASP A05: Retorna ID interno como identificador do principal
        return membro.getIdInterno().toString();
    }

    @Override
    public Map<String, Object> getAttributes() {
        // OWASP A05: Delega ao OAuth2User original preservando claims do provedor
        return oauth2UserDelegate.getAttributes();
    }

    @Override
    public Map<String, Object> getClaims() {
        // OWASP A05: Retorna claims OIDC quando disponivel, senao atributos OAuth2
        return (oidcUserDelegate != null) ? oidcUserDelegate.getClaims() : oauth2UserDelegate.getAttributes();
    }

    @Override
    public OidcUserInfo getUserInfo() {
        // OWASP A05: Retorna UserInfo somente para fluxo OIDC completo
        return (oidcUserDelegate != null) ? oidcUserDelegate.getUserInfo() : null;
    }

    @Override
    public OidcIdToken getIdToken() {
        // OWASP A07: Retorna ID Token somente para fluxo OIDC completo
        return (oidcUserDelegate != null) ? oidcUserDelegate.getIdToken() : null;
    }

    // OWASP A01: Expoe Membro interno para uso em controladores com autorizacao
    public Membro getMembro() {
        return membro;
    }

    // OWASP A01: Expoe ID externo (UUID) para validacoes de propriedade de recurso
    public UUID getMembroId() {
        return membro.getIdExterno();
    }

    // OWASP A02/A05: Expoe email normalizado do membro interno
    public String getEmail() {
        return membro.getEmail();
    }
}